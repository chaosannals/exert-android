package com.example.appshell.ui.page.demo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    // 无法设置个数，钉钉，QQ，微信，都是自定义的选择界面。
    val mutliList = remember {
        mutableStateListOf<String>()
    }

    val mutliFileLoader = rememberLauncherForActivityResult(
        contract = object : ActivityResultContracts.GetMultipleContents()
        {
            // 可以通过这个重载配置，不过没有必要。有强制要求 super 调用。
            override fun createIntent(context: Context, input: String): Intent {
                return super.createIntent(context, input)
//                return Intent(Intent.ACTION_GET_CONTENT)
//                    .setType(input)
//                    .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        },
    ) {
        mutliList.clear()
        it.forEach { 
            // 必须落盘才能有文件路径，不然是 content://.... 的 uri
            context.contentResolver.openInputStream(it)?.use {input ->
                val data = input.readBytes()
                val cachePath = "${context.cacheDir}/${UUID.randomUUID()}.${data.guessSuffix}"
                val file = File(cachePath)
                context.contentResolver.openOutputStream(Uri.fromFile(file))?.use {
                    it.write(data)
                    mutliList.add(cachePath)
                    file.deleteRecursively()
                }
            }
        }
    }

    val multiOpenDocsLoader  = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
    ) {
        mutliList.clear()
        it.forEach {
            // 必须落盘才能有文件路径，不然是 content://.... 的 uri
            context.contentResolver.openInputStream(it)?.use {input ->
                val data = input.readBytes()
                val cachePath = "${context.cacheDir}/${UUID.randomUUID()}.${data.guessSuffix}"
                val file = File(cachePath)
                context.contentResolver.openOutputStream(Uri.fromFile(file))?.use {
                    it.write(data)
                    mutliList.add(cachePath)
                    file.deleteRecursively()
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(pickPath)
        Button(
            onClick = {
                fileLoader.launch("*/*")
            },
        ) {
            Text("选择")
        }
        
        Button(
            onClick =
            {
                mutliFileLoader.launch("*/*") // GetMultipleContents
            },
        ) {
            Text("多选")
        }

        Button(
            onClick =
            {
                multiOpenDocsLoader.launch(arrayOf("*/*", "*/*")) // OpenMultipleDocuments
            },
        ) {
            Text("多选文档")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            itemsIndexed(mutliList) {i, it ->
                Text(text = "${i} $it")
            }
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