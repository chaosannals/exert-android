package com.example.jcm3ui.ui.page.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

// 注：缓存文件不应该转 contentUri，contentUri 是被 contentResolver 管理的资源
// 注册 contentUri 就会被写入 数据库，被系统认为是 媒体资源，然后被其他程序检索到。（此时相册就花花一片缓存文件）

// contentUri 必须是本地文件，路径里有 data user ${applicationId} （小米上文件浏览器路径没找到）
private fun Context.copyToCache(contentUri: Uri): String {
    return File(cacheDir, "cache.jpg").run {
        outputStream().use { output ->
            contentResolver.openInputStream(contentUri)?.use { input ->
                output.write(input.readBytes())
            }
        }
        absolutePath
    }
}

private fun readFromAbsPath(path: String): Bitmap {
    return File(path).inputStream().use {
        return BitmapFactory.decodeStream(it)
    }
}

// contentUri 必须是本地文件，路径里有 data ${applicationId}
private fun Context.copyToExCache(contentUri: Uri): String {
    return File(externalCacheDir, "cache-ex.jpg").run {
        outputStream().use { output ->
            contentResolver.openInputStream(contentUri)?.use { input ->
                output.write(input.readBytes())
            }
        }
        absolutePath
    }
}

@Composable
fun CachePage() {
    val context = LocalContext.current

    var copyToCachePath by remember {
        mutableStateOf("")
    }
    var copyToCacheContentUri by remember {
        mutableStateOf("")
    }
    var cache by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val copyToCacheLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) {
        it?.let {
            copyToCacheContentUri = it.toString()
            copyToCachePath = context.copyToCache(it)
            cache = readFromAbsPath(copyToCachePath)
        }
    }

    var copyToExCachePath by remember {
        mutableStateOf("")
    }
    var exCache by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val copyToExCacheLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) {
        it?.let {
            copyToExCachePath = context.copyToExCache(it)
            exCache = readFromAbsPath(copyToExCachePath)
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(text = "cache: ${copyToCachePath}")
        Text(text = "cache contentUri: ${copyToCacheContentUri}")
        AsyncImage(
            model = cache,
            contentDescription = "cache",
            modifier = Modifier
                .aspectRatio(1f)
                .border(1.dp, Color.Red)
        )
        AsyncImage(
            model = copyToCachePath,
            contentDescription = "cache path",
            modifier = Modifier
                .aspectRatio(1f)
                .border(1.dp, Color.Green)
        )
        AsyncImage(
            model = copyToCacheContentUri,
            contentDescription = "cache contentUri",
            modifier = Modifier
                .aspectRatio(1f)
                .border(1.dp, Color.Blue)
        )
        Button(
            onClick = {
                copyToCacheLauncher.launch("image/jpeg")
            }
        ) {
            Text(text = "copyToCache")
        }

        Text(text="exCache: ${copyToExCachePath}")
        AsyncImage(
            model = exCache,
            contentDescription = "exCache",
            modifier = Modifier
                .aspectRatio(1f)
                .border(1.dp, Color.Green)
        )
        Button(
            onClick = {
                copyToExCacheLauncher.launch("image/jpeg")
            }
        ) {
            Text(text = "copyToExCache")
        }
    }
}