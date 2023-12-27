package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
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
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
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
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlinx.serialization.encodeToString

// 客户端
private val httpClient = HttpClient(Android) {
//    install(HttpTimeout)
//    engine {
//        connectTimeout = 400000
//    }
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

private fun ContentResolver.getContentUriAndData(
    base: Uri,
    where: String,
    args: Array<String>
): Pair<Uri, String>? {
    val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DATA,
    )
    return query(base, projection, where, args, null)?.use {
        val idIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val dataIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        return if (it.moveToFirst()) {
            val id = it.getLong(idIndex)
            val data = it.getString(dataIndex)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
            )
            Pair(contentUri, data)
        } else null
    }
}

private fun Context.deleteOrExists(
    base: Uri,
    where: String,
    args: Array<String>
) {
    contentResolver.getContentUriAndData(base, where, args)?.let { (contentUri, absPath) ->
        // 删除必须用 contentUri ，加条件只能查到本次 APP 生命周期内操作的文件，其他 APP 或 上次打开 APP 操作的无效。

        try {
            contentResolver.delete(
                contentUri,
                null,
                null,
            )
            File(absPath).delete()
        } catch (se: SecurityException) {
            // TODO
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // 要删除 安卓 10 后 内容文件一般程序是没有权限的，需要发送给系统，让系统删除
//                val rse = se as RecoverableSecurityException
//                val request = IntentSenderRequest.Builder(rse.userAction.actionIntent.intentSender)
//                    .build()
//
//            }
        }
    }
}

//// 因为上面的deleteOrExists 里 ContentResolver.query 带查询条件的只能查到自己本次插入的，以前别人插入或者上次打开 APP 插入的都查不到。
//private fun ContentResolver.findByNameAndFolder(
//    base: Uri,
//    name: String,
//    folder: String,
//) : List<String> {
//    val projection = arrayOf(
//        MediaStore.MediaColumns.DATA,
//        MediaStore.MediaColumns.DISPLAY_NAME,
//        MediaStore.MediaColumns.RELATIVE_PATH,
//    )
//    val result = mutableListOf<String>()
//    query(base, projection, null, null,null)?.use { cursor ->
//        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
//        val relativePathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
//        if (cursor.moveToFirst()) {
//            do {
//                val data = cursor.getString(dataColumn)
//                val displayName = cursor.getString(displayNameColumn)
//                val relativePath = cursor.getString(relativePathColumn)
//                if (displayName.contentEquals(name, ignoreCase = true) && relativePath.contentEquals(folder, ignoreCase = true)) {
//                    result.add(data)
//                }
//            } while (cursor.moveToNext())
//        }
//    }
//    return result
//}
//// 因为上面的deleteOrExists 里 ContentResolver.query 带查询条件的只能查到自己本次插入的，以前别人插入或者上次打开 APP 插入的都查不到。
//private fun ContentResolver.deleteOrExists2(
//    base: Uri,
//    name: String,
//    folder: String,
//) {
//    findByNameAndFolder(base, name, folder).forEach { absPath ->
//        delete(
//            base,
//            "${MediaStore.MediaColumns.DATA} =?", // 这是 SQL 的 where 条件
//            arrayOf(absPath), // where 参数，占位符 ？
//        )
//        File(absPath).delete()
//    }
//}

private fun Context.insertCoverFormCache(
    base: Uri,
    folder: String,
    name: String,
    mime: String,
    cache: String,
    finalize: File.() -> Unit,
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
            // 如果不是本次添加的，不能通过条件查到。
//            "${MediaStore.MediaColumns.DISPLAY_NAME} =? COLLATE NOCASE AND ${MediaStore.MediaColumns.RELATIVE_PATH} =? COLLATE NOCASE", // 这是 SQL 的 where 条件
//            arrayOf(name, folder) // where 参数，占位符 ？
//            "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ? ",
//            arrayOf(name, folder)
            "${MediaStore.MediaColumns.DISPLAY_NAME} = ?",
            arrayOf(name)
        )
//        deleteOrExists2(base, name, folder)

        insert(
            base,
            contentValues
        )?.let {
            openOutputStream(it)?.use { output ->
                File(externalCacheDir, cache).run {
                    inputStream().use { input ->
                        input.copyTo(output)
                    }
                    finalize()
                }
            }
        }
    }
}

private fun Context.moveToDownloadFolder(
    name: String,
    cache: String,
) {
    val dir = File(dataDir, "Download").run {
        if (exists().not()) {
            mkdirs()
        }
        File(this, "Jcm3ui").apply {
            if (exists().not()) {
                mkdirs()
            }
        }
    }
    File(dir, name).outputStream().use {output->
        File(cache).run{
            inputStream().use {input ->
                input.copyTo(output)
            }
            delete()
        }
    }
}


@OptIn(InternalAPI::class)
private suspend fun Context.downloadToCache(url: String, onProcess: (Long) -> Unit): Pair<String, String> {
    val cache = UUID.randomUUID().toString()
    val mime = httpClient.prepareGet(url).execute {
        val total = it.contentLength()?: 1
        var count = 0
        File(externalCacheDir, cache).outputStream().use { output ->
            it.bodyAsChannel().let {channel ->
                while(channel.isClosedForRead.not()) {
                    val packet = channel.readRemaining(1024)
                    while (packet.isNotEmpty) {
                        val bytes = packet.readBytes()
                        count += bytes.size
                        output.write(bytes)
                        onProcess((count * 99).floorDiv(total))
                    }
                }
            }
        }
        it.contentType().toString()
    }

    // 随机访问导致全读
//    RandomAccessFile(File(externalCacheDir, cache), "rws").channel.use {
//        response.content.copyTo(it)
//    }

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
            Pair(
                "Pictures/Jcm3Ui",
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            )
        }
        mime.contains("video", ignoreCase = true) -> {
            Pair(
                "Movies/Jcm3Ui",
                // 高版本如果不适用此API ，那么每次打开 APP 都是隔离的。
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
            )
        }
        else -> {
            val base = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                MediaStore.Downloads.EXTERNAL_CONTENT_URI
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else null
            Pair("Download/Jcm3Ui", base)
        }
    }.let {(folder, base) ->
        if (base == null) {
            moveToDownloadFolder(path.name, cache)
        } else {
            insertCoverFormCache(base,folder,path.name,mime,cache){
                delete()
            }
        }
    }
}

private suspend fun Context.uploadContent(target: String, uri: Uri, onProcess: (Long) -> Unit): String?  {
    return contentResolver.run {
        //val fs = openFileDescriptor(uri, "rs")?.use { it.statSize } ?: 1 // 这个需要 MANAGE_DOCUMENTS 权限
        val fs = getContentSize(uri)
        openInputStream(uri)?.use {input ->
            val r = httpClient.put(target) {
//                timeout {
//                    requestTimeoutMillis = 100000
//                }
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
                                        length // Content-Length
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
                        get("/test.zip") {
                            try {
                                call.run {
                                    val path = "attachments/test.zip"
                                    // assets 被压缩的无法使用 openFd ，详见 build.gradle androidResources noCompress, 修改后要卸载重装。
                                    val length = context.assets.openFd(path).use { it.length }
                                    response.headers.run {
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            ContentDisposition.Attachment.withParameter(
                                                ContentDisposition.Parameters.FileName,
                                                "test.zip"
//                                            ).withParameter( // zip 不行，响应头部不带
//                                                ContentDisposition.Parameters.Size,
//                                                "$length"
                                            ).toString()
                                        )
//                                        append( // zip 不行， 响应头部不带
//                                            HttpHeaders.ContentLength,
//                                            "$length"
//                                        )
                                    }
                                    context.assets.open(path).use { input ->
                                        respondOutputStream(
                                            ContentType.parse("application/zip"),
                                            HttpStatusCode.OK,
                                            contentLength = length // png 可以，zip 不知道为什么前端拿不到，响应头部不带。
                                        ) {
                                            val buffer = ByteArray(512)
                                            while (true) {
                                                val i = input.read(buffer)
                                                if (i == -1) {
                                                    break
                                                }
                                                write(buffer, 0, i)
                                                //delay(1) // 延迟，让客户端显示进度条
                                            }
                                        }
                                    }
                                }
                            } catch (t: Throwable) {
                                Log.d("error", t.message?: "null")
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                )
            } else {
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
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
        Button(
            onClick = {
                httpClientScope.launch {
                    context.downloadContent("http://127.0.0.1:8080/test.zip") {
                        downloadProcess = it
                    }
                }
            }
        ) {
            Text("下载 zip")
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