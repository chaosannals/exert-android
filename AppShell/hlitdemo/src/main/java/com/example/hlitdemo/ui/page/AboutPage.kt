package com.example.hlitdemo.ui.page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import java.io.File

fun Context.installByFile(file: File) {
    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(this, "$packageName.file_provider", file)
    } else {
        Uri.fromFile(file)
    }
    return installByUrl(uri)
}

fun Context.installByUrl(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    startActivity(intent)
}

// 需要权限 <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
@Composable
fun AboutPage() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Button(onClick = { },) {
            Text("安装")
        }
        Button(onClick = { },) {
            Text("更新")
        }
    }
}

@Preview
@Composable
fun AboutPagePreview() {
    AboutPage()
}