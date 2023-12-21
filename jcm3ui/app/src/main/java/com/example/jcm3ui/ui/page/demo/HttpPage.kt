package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.contentLength
import io.ktor.server.response.respondOutputStream
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import io.ktor.util.InternalAPI
import io.ktor.utils.io.jvm.nio.copyTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.RandomAccessFile
import java.util.UUID
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlinx.serialization.encodeToString

// 客户端
private val httpClient = HttpClient(CIO) {
    install(HttpTimeout)
}

private val httpClientScope = CoroutineScope(Dispatchers.IO)

private fun ContentResolver.getContentSize(
    uri: Uri
): Int? {
    val projection = arrayOf(
        MediaStore.MediaColumns.SIZE,
    )
    return query(uri, projection, null, null, null)?.use {
        val dataIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
        return if (it.moveToFirst()) it.getInt(dataIndex) else null
    }
}

private fun ContentResolver.getContentData(
    base: Uri,
    where: String,
    args: Array<String>
): String? {
    val projection = arrayOf(
        MediaStore.MediaColumns.DATA,
    )
    return query(base, projection, where, args, null)?.use {
        val dataIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        return if (it.moveToFirst()) it.getString(dataIndex) else null
    }
}

private fun ContentResolver.deleteOrExists(
    base: Uri,
    where: String,
    args: Array<String>
) {
    getContentData(base, where, args)?.let {absPath ->
        delete(
            base,
            "${MediaStore.MediaColumns.DATA} =?", // 这是 SQL 的 where 条件
            arrayOf(absPath), // where 参数，占位符 ？
        )
        File(absPath).delete()
    }
}

private fun Context.insertCoverFormCache(
    base: Uri,
    folder: String,
    name: String,
    mime: String,
    cache: String,
) {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, mime)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            put(MediaStore.MediaColumns.IS_DOWNLOAD, true)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, folder)// 名字只能是 [DCIM, Movies, Pictures] 开头
//        }
    }

    contentResolver.run {
        deleteOrExists(
            base,
            // ${MediaStore.MediaColumns.RELATIVE_PATH} =? 居然匹配不到，要改用 LIKE %%
            "${MediaStore.MediaColumns.DISPLAY_NAME} =? AND ${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?", // 这是 SQL 的 where 条件
            arrayOf(name, "%${folder}%") // where 参数，占位符 ？
        )

        insert(
            base,
            contentValues
        )?.let {
            openOutputStream(it)?.use { output ->
                File(externalCacheDir, cache).run {
                    inputStream().use { input ->
                        input.copyTo(output)
                    }
                    delete()
                }
            }
        }
    }
}


@OptIn(InternalAPI::class)
private suspend fun Context.downloadToCache(url: String, onProcess: (Long) -> Unit): Pair<String, String> {
    val response = httpClient.get(url) {
        onDownload { b, c ->
            onProcess((b * 99).floorDiv(c))
        }
    }
    val mime = response.contentType().toString()
    val cache = UUID.randomUUID().toString()
    RandomAccessFile(File(externalCacheDir, cache), "rws").channel.use {
        response.content.copyTo(it)
    }
    onProcess(100)
    return Pair(cache, mime)
}

private suspend fun Context.downloadContent(url: String, onProcess: (Long) -> Unit) {
    val (cache, mime) = downloadToCache(url, onProcess)

    // 插入到 内容数据库 并删除缓存
    val uri = Uri.parse(url)
    val path = Path(uri.path!!)
    when {
        mime.contains("image", ignoreCase = true) -> {
            Pair("Pictures/Jcm3Ui", MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
        mime.contains("video", ignoreCase = true) -> {
            Pair("Movies/Jcm3Ui", MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        }
        else -> {
            val base = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Downloads.EXTERNAL_CONTENT_URI else null
            Pair("Downloads/Jcm3Ui", base)
        }
    }.let {(folder, base) ->
        if (base == null) {
            val dir = File(dataDir, "Download").apply {
                if (exists().not()) {
                    mkdirs()
                }
            }
            File(dir, path.name).outputStream().use {output->
                File(cache).run{
                    inputStream().use {input ->
                        input.copyTo(output)
                    }
                    delete()
                }
            }
        } else {
            insertCoverFormCache(
                base,
                folder,
                path.name,
                mime,
                cache
            )
        }
    }
}

private suspend fun Context.uploadContent(target: String, uri: Uri, onProcess: (Long) -> Unit): String?  {
    return contentResolver.run {
        //val fs = openFileDescriptor(uri, "rs")?.use { it.statSize } ?: 1 // 这个需要 MANAGE_DOCUMENTS 权限
        val fs = getContentSize(uri)
        openInputStream(uri)?.use {input ->
            val r = httpClient.put(target) {
                timeout {
                    requestTimeoutMillis = 100000
                }
                headers.run {
                    fs?.let {
                        append(HttpHeaders.ContentLength, "$it")
                    }
                }
                onUpload { b, c ->
                    onProcess((b * 99).floorDiv(c))
                }
                setBody(input)
            }
            onProcess(100)
            r.bodyAsText()
        }
    }
}

@Composable
fun HttpPage() {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.values.reduce { acc, b -> acc && b }) {
            // 服务器
            val environment = applicationEngineEnvironment {
                connector {
                    host = "0.0.0.0"
                    port = 8080

                }
                module {
                    install(CORS)
                    install(Compression)
                    routing {
                        get("/") {
                            call.respondText { "Hello" }
                        }
                        get("/test.png") {
                            call.run {
                                val path = "attachments/test.png"
                                response.headers.run {
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        ContentDisposition.Attachment.withParameter(
                                            ContentDisposition.Parameters.FileName,
                                            "test.png"
                                        ).toString()
                                    )
                                }
                                val length = context.assets.openFd(path).use { it.length }
                                context.assets.open(path).use { input ->
                                    respondOutputStream(
                                        ContentType.parse("image/png"),
                                        HttpStatusCode.OK,
                                        length
                                    ) {
                                        val buffer = ByteArray(512)
                                        while (true) {
                                            val i = input.read(buffer)
                                            if (i == -1) {
//                                                write(-1)
                                                break
                                            }
                                            write(buffer, 0, i)
                                            //delay(1) // 延迟，让客户端显示进度条
                                        }
                                    }
                                }
                            }
                        }

                        put("upload") {

                            call.run {
                                respondText(
                                    ContentType.parse("application/json"),
                                    HttpStatusCode.OK,
                                ) {
                                    Json.encodeToString(mapOf(
                                        "length" to request.contentLength()
                                    ))
                                }
                            }
                        }
                    }
                }
            }
            embeddedServer(Jetty, environment) {
                connectionGroupSize = 2
                workerGroupSize = 4
                callGroupSize = 10
                shutdownTimeout = 40000
            }.start(wait = false)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.INTERNET,
            )
        )
    }

    var result by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                httpClientScope.launch {
                    val response = httpClient.get("http://127.0.0.1:8080/") {}
                    result = response.bodyAsText()
                }
            }
        ) {
            Text(text = "请求")
        }
        Text(text=result)

        var downloadProcess by remember {
            mutableLongStateOf(0L)
        }
        Button(
            onClick = {
                httpClientScope.launch {
                    context.downloadContent("http://127.0.0.1:8080/test.png") {
                        downloadProcess = it
                    }
                }
            }
        ) {
            Text("下载 png")
        }
        Text("download: $downloadProcess %")

        var uploadProcess by remember {
            mutableLongStateOf(0L)
        }
        var uploadResult by remember {
            mutableStateOf<String?>("")
        }
        val uploadLauncher = rememberLauncherForActivityResult(
            contract =  ActivityResultContracts.GetContent()
        ) {
            it?.let {
                httpClientScope.launch {
                    uploadResult = context.uploadContent("http://127.0.0.1:8080/upload", it) {
                        uploadProcess = it
                    }
                }
            }
        }
        Button(
            onClick = {
                uploadLauncher.launch("*/*")
            }
        ) {
            Text("上传")
        }
        Text("upload: $uploadProcess %")
        Text("result: $uploadResult")
    }
}