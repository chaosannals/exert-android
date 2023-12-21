package com.example.jcm3ui.ui.page.demo

import android.content.ComponentName
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun IntentPage() {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        val context = LocalContext.current

        // Intent.ACTION_VIEW 系统明确知道是选择 打开方式
        val openLauncher = rememberLauncherForActivityResult(
            contract =  ActivityResultContracts.GetContent()
        ) {
            it?.let {
                val mime = context.contentResolver.getType(it)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    // 只传 URI ，需要赋权查看，图片浏览器可以在列。
                    setDataAndType(it, mime)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)// 赋权可以查看图片。

                    // component = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity") // 指定QQ
                    // 这是直接把内容传过去，不需要赋权，但是系统的图片浏览器不在选择列中。
//                    type = mime
//                    putExtra(Intent.EXTRA_STREAM, it)
                }
                context.startActivity(intent)
            }
        }

        Button(
            onClick = {
                openLauncher.launch("*/*")
            }
        ) {
            Text(text = "打开")
        }


        // Intent.ACTION_SEND 系统不清楚是要打开，只是做传递，但本质上是一样的事，即意图传递给 Activity
        val sendLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) {
            it?.let {
                val mime = context.contentResolver.getType(it)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = mime
                    component = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
                    putExtra(Intent.EXTRA_STREAM, it)
                }
                context.startActivity(Intent.createChooser(intent, "分享文件"))
            }
        }
        Button(
            onClick = {
                sendLauncher.launch("*/*")
            }
        ) {
            Text("发送到QQ")
        }
    }
}