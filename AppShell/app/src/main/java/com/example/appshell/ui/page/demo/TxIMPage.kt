package com.example.appshell.ui.page.demo

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.appshell.LocalTotalStatus
import com.example.appshell.TxIM
import com.example.appshell.TxIM.log
import com.example.appshell.TxIM.send
import com.example.appshell.ui.LocalTipQueue
import com.example.appshell.ui.tip
import com.example.appshell.ui.widget.DesignPreview
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener
import com.tencent.imsdk.v2.V2TIMDownloadCallback
import com.tencent.imsdk.v2.V2TIMElem
import com.tencent.imsdk.v2.V2TIMMessage
import io.ktor.http.decodeURLPart
import java.io.File

@Composable
fun TxIMPage() {
    val context = LocalContext.current
    val totalStatus = LocalTotalStatus.current
    val tipQueue = LocalTipQueue.current

    var toUserId by remember {
        mutableStateOf("test-2")
    }
    var text by remember {
        mutableStateOf("")
    }
    var imageUrl: Uri? by remember {
        mutableStateOf(null)
    }
    var imagePaths = remember {
        mutableStateListOf<String>()
    }

    DisposableEffect(Unit) {
        val listener = object: V2TIMAdvancedMsgListener() {
            override fun onRecvNewMessage(msg: V2TIMMessage?) {
                when (msg?.elemType) {
                    null -> TxIM.logQueue.log("空消息", 444)
                    V2TIMMessage.V2TIM_ELEM_TYPE_TEXT -> { // 文本消息

                    }
                    V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM -> { // 自定义消息

                    }
                    V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE -> { // 图片消息
                        val imgElem = msg.imageElem
                        for (img in imgElem.imageList) {
                            val imgPath = "${context.cacheDir}/im/image/${TxIM.loginUser}/${img.uuid}"
                            val imgFile = File(imgPath)
                            if (!imgFile.exists()) {
                                img.downloadImage(imgPath,object: V2TIMDownloadCallback {
                                    override fun onSuccess() {
                                        tipQueue.tip("download: ${imgPath}")
                                        imagePaths.add(imgPath)
                                    }

                                    override fun onError(p0: Int, p1: String?) {
                                        tipQueue.tip("download error: ${imgPath} ${p0} ${p1}", Color.Red)
                                    }

                                    override fun onProgress(p0: V2TIMElem.V2ProgressInfo?) {
                                        tipQueue.tip("downloading: ${imgPath} ${p0?.currentSize} / ${p0?.totalSize}")
                                    }
                                })
                            }
                        }
                    }
                    V2TIMMessage.V2TIM_ELEM_TYPE_SOUND -> { // 语音消息

                    }
                    V2TIMMessage.V2TIM_ELEM_TYPE_FACE -> {// 表情消息

                    }
                    V2TIMMessage.V2TIM_ELEM_TYPE_FILE -> { // 文件消息

                    }
                    V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION -> { // 定位消息

                    }
                    else -> TxIM.logQueue.log("消息类型: ${msg?.elemType}")
                }
            }
        }
        TxIM.listen(listener)
        onDispose {
            TxIM.remove(listener)
        }
    }

    val imageTmp = "${context.cacheDir}/image_tmp"
    val fileOpener = rememberLauncherForActivityResult(
        contract =   ActivityResultContracts.GetContent(),
        onResult =
        {
            imageUrl = it
            it?.let {
                context.contentResolver.openInputStream(it)?.run {
                    context.contentResolver.openOutputStream(Uri.fromFile(File(imageTmp)))?.let {
                        it.write(this.readBytes())
                        it.close()
                    }
                    this.close()
                }

            }
        },
    )

    Column (
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = "用户ID: ${TxIM.loginUser}",
        )
        Text(
            text = "用户状态：${TxIM.loginStatus}"
        )
        TextField(
            label = { Text("发送到：") },
            modifier = Modifier.fillMaxWidth(),
            value = toUserId,
            onValueChange = {toUserId = it},
        )
        TextField(
            label = { Text("消息：") },
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {text = it},
        )
        Box(
            contentAlignment=Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable
                {
                    fileOpener.launch("image/*")
                },
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "图片",
                )
            } else {
                Text("选择图片")
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick =
            {
                if (imageUrl != null) {
                    // 不接受 content:// 这种 Uri 的格式，只能是文件路径，所以必须写入存储。
//                    TxIM.newImage(imageUrl!!.toString().decodeURLPart())
                    TxIM.newImage(imageTmp)
                } else {
                    TxIM.newFace(1, "tt00".toByteArray())
                }.send(toUserId)
            },
        ) {
            Text(text="发送")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick =
            {
                TxIM.logout() {
                    totalStatus.routeTo("tx-im-login-page")
                }
            },
        ) {
            Text(text="注销")
        }

//        AsyncImage(
//            modifier = Modifier
//                .fillMaxWidth(),
//            model = "/data/user/0/com.example.appshell.debug/cache/im/image/test-2/1400805216_test-1_f10ccd233e5565c46a6d97dd23a40ba2",
//            contentDescription = "image",
//        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(imagePaths) {_, it ->
                AsyncImage(
                    model = it,
                    contentDescription = "图片",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun TxIMPagePreview() {
    DesignPreview {
        TxIMPage()
    }
}