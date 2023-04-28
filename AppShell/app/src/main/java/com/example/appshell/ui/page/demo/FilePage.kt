package com.example.appshell.ui.page.demo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.guessSuffix
import com.example.appshell.ui.widget.DesignPreview
import java.io.File
import java.util.UUID

@Composable
fun FilePage() {
    val context = LocalContext.current

    var pickPath by remember {
        mutableStateOf("")
    }
    var pickSuffix by remember {
        mutableStateOf("")
    }

    val fileLoader = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) {
        it?.let {

            // 必须落盘才能有文件路径，不然是 content://.... 的 uri
            context.contentResolver.openInputStream(it)?.use {input ->
                val data = input.readBytes()
                val cachePath = "${context.cacheDir}/${UUID.randomUUID()}.${data.guessSuffix}"
                val file = File(cachePath)
                pickSuffix = data.guessSuffix ?: ""
                context.contentResolver.openOutputStream(Uri.fromFile(file))?.use {
                    it.write(data)
                    pickPath = cachePath
                    file.deleteRecursively()
                }
            }
        }
    }

    Column {
        Text(pickPath)
        Button(
            onClick = {
                fileLoader.launch("*/*")
            },
        ) {
            Text("选择")
        }
    }
}

@Preview
@Composable
fun FilePagePreview() {
    DesignPreview {
        FilePage()
    }
}