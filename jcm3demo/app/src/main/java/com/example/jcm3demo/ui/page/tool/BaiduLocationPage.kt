package com.example.jcm3demo.ui.page.tool

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3demo.BDL

@Composable
fun BaiduLocationPage() {
    var addr by remember {
        mutableStateOf("")
    }

    DisposableEffect(Unit) {
        val uuid = BDL.attach {
            addr = it.address.address
        }
        onDispose {
            BDL.detach(uuid)
        }
    }
    Text("地址：")
    Text(text = addr)
}

@Preview(widthDp = 375)
@Composable
fun BaiduLocationPagePreview() {
    BaiduLocationPage()
}