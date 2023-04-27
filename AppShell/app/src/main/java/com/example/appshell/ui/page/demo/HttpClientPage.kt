package com.example.appshell.ui.page.demo

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.HttpApiClient
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.LocalTipQueue
import com.example.appshell.ui.ensurePermit
import com.example.appshell.ui.tip
import com.example.appshell.ui.toHex
import com.example.appshell.ui.widget.DesignPreview
import io.ktor.client.statement.readBytes
import io.ktor.http.contentType
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.utf8Size
import java.io.File
import java.lang.Integer.min
import kotlin.random.Random

fun Context.saveDownload(
    data: ByteArray,
    mime: String,
) : Uri? {
    val now = System.currentTimeMillis()
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS")
        .format(now)
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, mime)
        put(MediaStore.MediaColumns.DATE_MODIFIED, now / 1000)
        put(MediaStore.MediaColumns.SIZE, data.size)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            put(MediaStore.MediaColumns.IS_DOWNLOAD, true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AAAAAAA");
        }
    }
    Log.d("http-client-page", "mime: $mime")

    val furl = contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )

    return furl?.apply {
        contentResolver.openOutputStream(this)?.use {
            it.write(data)
        }
    }
}

val MIME_HEAD = listOf(
    byteArrayOf(0xff.toByte(), 0xd8.toByte()) to "image/jpeg",
    byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4e.toByte(), 0x47.toByte(), 0x0D.toByte(), 0x0A.toByte(), 0x1A.toByte(), 0x0A.toByte()) to "image/png",
    byteArrayOf(0x47.toByte(), 0x49.toByte(), 0x46.toByte(), 0x38.toByte()) to "image/gif",
)

fun compare(one: ByteArray, two: ByteArray, length: Int) : Boolean {
    val minLength = min(one.size, two.size)
    if (length > minLength) return false
    for (i in 0 until length) {
        if (one[i] != two[i]) {
            return false
        }
    }
    return true
}

fun getMime(data: ByteArray) : String? {
    for (m in MIME_HEAD) {
        val head = data.copyOfRange(0, m.first.size)
        Log.d("http-client-page", "compare ${head.toHex()} ${m.first.toHex()}")
        if (compare(m.first, head, m.first.size)) {
            Log.d("http-client-page", "match mime: ${m.second}")
            return m.second
        }
    }
    return null
}

suspend fun Context.saveBase64(
    data: String,
) : Uri? {
    val imageData = Base64.decode(data, Base64.DEFAULT)
    val mime = getMime(imageData) ?: "image/jpeg"
    Log.d("http-client-page", "saveBase64 dataSize: ${imageData.size} mime: ${mime}")
    return saveDownload(imageData, mime)
}

suspend fun Context.downloadPictureBase64(
    purl: String,
): String? {
    val response = HttpApiClient.get(purl)
    if (response != null && response.status.value in 200..299) {
        val imgData = response.readBytes()
        val imgBase64 = Base64.encodeToString(imgData, Base64.DEFAULT)
        Log.d("http-client-page", "base64 dataSize: ${imgData.size} base64Length: ${imgBase64.length}")
        return saveBase64(imgBase64)?.toString()
    }
    return null
}

suspend fun Context.downloadPicture(
    purl: String,
) : String? {
    val response = HttpApiClient.get(purl)
    if (response != null && response.status.value in 200..299) {
        val imgData = response.readBytes()
        val mime = response.contentType()?.toString() ?: "image/jpeg"
        return saveDownload(imgData, mime)?.toString()
    }
    return null
}

@OptIn(InternalAPI::class)
@Composable
fun HttpClientPage() {
    val context = LocalContext.current
    val totalStatus = LocalTotalStatus.current
    val tipQueue = LocalTipQueue.current
    val coroutineScope = rememberCoroutineScope()

    var url by remember {
        mutableStateOf("")
    }
    var httpContent by remember {
        mutableStateOf("")
    }
    var httpError by remember {
        mutableStateOf("")
    }

    var toastTip by remember {
        mutableStateOf("")
    }
    var statusCode by remember {
        mutableStateOf(0)
    }

    (context as? Activity)?.let {
        ensurePermit(it, WRITE_EXTERNAL_STORAGE)
    }

    LaunchedEffect(toastTip) {
        if (toastTip.isNotEmpty()) {
            Toast.makeText(context, toastTip, Toast.LENGTH_SHORT).show()
            toastTip = ""
        }
    }

    val defaultUrls = listOf(
        "https://baidu.com",
        "http://127.0.0.1:8080",
        "https://127.0.0.1:8080",
        "http://127.0.0.1:12345",
    )

    Column (
        verticalArrangement= Arrangement.Center,
        horizontalAlignment= Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn() {
            itemsIndexed(defaultUrls) {_, it ->
                Text(
                    text = it,
                    modifier = Modifier
                        .clickable { url = it },
                )
            }
        }
        OutlinedTextField(
            value = url,
            onValueChange = {url = it},
            label = {
                Text(text="url")
            }
        )
        Text(text = "code: ${statusCode}")

        Row(
            horizontalArrangement=Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick =
                {
                    coroutineScope.launch(Dispatchers.IO) {
                        httpError = ""
                        val r = HttpApiClient.get(
                            urlString = url,
                            onExcept =
                            {
                                val et = it.javaClass.name
                                httpError = "${et}: ${it.message} ${it.stackTrace}"
                                toastTip = httpError
                            },
                        )
                        r?.run {
                            statusCode = status.value
                            if (status.value in 200..299) {

                            }

                            content.read {
                                httpContent = it.decodeString()
                                toastTip = httpContent
                            }
                        }
                    }
                },
            ) {
                Text(text = "发送")
            }

            Button(
                onClick = {
                    val pid = Random.nextInt(100, 400)
                    // 只支持 jpg webp 格式生成
                    val purl = "https://picsum.photos/id/$pid/400/400.jpg"

                    coroutineScope.launch(Dispatchers.IO) {
                        val fpath = context.downloadPicture(purl)
                        if (fpath != null) {
                            tipQueue.tip("下载完成: $purl => $fpath")
                        } else {
                            tipQueue.tip("下载失败: $purl", Color.Red)
                        }
                    }
                },
            ) {
                Text(text="下载")
            }

            Button(
                onClick = {
                    val pid = Random.nextInt(100, 400)
                    // 只支持 jpg webp 格式生成
                    val purl = "https://picsum.photos/id/$pid/400/400.jpg"

                    coroutineScope.launch(Dispatchers.IO) {
                        val fpath = context.downloadPictureBase64(purl)
                        if (fpath != null) {
                            tipQueue.tip("下载 Base64 完成: $purl => $fpath")
                        } else {
                            tipQueue.tip("下载 Base64 失败: $purl", Color.Red)
                        }
                    }
                },
            ) {
                Text(text="下载（Base64）")
            }
        }

        TextField(
            label = {
                    Text(text = "内容")
            },
            value = httpContent,
            onValueChange =
            {
                httpContent = it
            },
            minLines = 4,
            maxLines = 10,
        )

        TextField(
            label = {
                Text(text = "异常")
            },
            value = httpError,
            onValueChange =
            {
                httpError = it
            },
            minLines = 4,
            maxLines = 10,
        )
    }
}

@Preview
@Composable
fun HttpClientPagePreview() {
    DesignPreview {
        HttpClientPage()
    }
}