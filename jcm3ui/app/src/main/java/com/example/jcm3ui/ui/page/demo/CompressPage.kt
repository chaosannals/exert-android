package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File
import java.util.UUID

// 这种方式好像已经很少被使用了。
private fun Context.makeCache(name: String): Uri {
    val dir = File(cacheDir, "caches") // caches 是 file_paths.xml 里 path 值
    val temp = File(dir, name)
    return FileProvider.getUriForFile(this, "com.example.jcm3ui.fileprovider", temp)
}

private fun Context.makePhoto(name: String): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    return contentResolver.insert(contentUri, contentValues)
}

private fun Context.makeVideo(name: String): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }
    }
    val contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    return contentResolver.insert(contentUri, contentValues)
}

@Preview
@Composable
fun CompressPage() {
    val context = LocalContext.current

    var hasPermissions by remember() {
        mutableStateOf(false)
    }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermissions = it.map { it.value }.reduce { a, b -> a && b }
    }

    val videoProcessorPickLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.CaptureVideo()
    ) {
        Toast.makeText(context, "CaptureVideo: ${it}", Toast.LENGTH_SHORT).show()
    }

    val photoProcessorPickLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.TakePicture()
    ) {
        Toast.makeText(context, "TakePicture: ${it}", Toast.LENGTH_SHORT).show()
    }

    val photoPreviewPickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview() // 这个图片太小。
    ) {
        Toast.makeText(context, "TakePicturePreview: ${it?.width} x ${it?.height}", Toast.LENGTH_SHORT).show()
        bitmap = it
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            enabled = hasPermissions,
            onClick = {
//                val uri = context.makeCache("${UUID.randomUUID()}.mp4") // 这个是可以的
                val uri = context.makeVideo(UUID.randomUUID().toString())
                videoProcessorPickLauncher.launch(uri)
            }
        ) {
            Text(
                text = "拍视频"
            )
        }

        Button(
            enabled = hasPermissions,
            onClick = {
//                val uri = context.makeCache("${UUID.randomUUID()}.jpg") // 高版本安卓结果一直是失败
//                val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) // 结果一直是失败, 这个权限很高，是个目录
//                val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI // 是个目录
                val uri = context.makePhoto(UUID.randomUUID().toString()) // 生成文件路径， 上面2个目录只有 EXTERNAL_CONTENT_URI 权限够。
                photoProcessorPickLauncher.launch(uri)
            }
        ) {
            Text(
                text = "拍照片(Result: false)"
            )
        }

        Button(
            enabled = hasPermissions,
            onClick = {
                photoPreviewPickLauncher.launch(null)
            }
        ) {
            Text(
                text = "拍照片（预览 Bitmap）"
            )
        }

        AsyncImage(
            model = bitmap,
            contentDescription = "图片"
        )
    }
}