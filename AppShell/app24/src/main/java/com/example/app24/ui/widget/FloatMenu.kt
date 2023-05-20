package com.example.app24.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.example.app24.ScaffoldKit
import com.example.app24.X5WebViewKit
import com.example.app24.X5WebViewKit.reloadUrl
import com.example.app24.ui.DesignPreview

@Composable
fun FloatMenu(
    expanded: Boolean,
    modifier: Modifier=Modifier,
    onDismissRequest: () -> Unit = {},
) {
    val context = LocalContext.current
    val isShowBottomBar by ScaffoldKit.isShowBottomBar.subscribeAsState(true)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
        ),
        modifier = modifier,
    ) {
        Column() {
            Text(
                text="底部栏 ${isShowBottomBar}",
                modifier = Modifier
                    .clickable {
                        ScaffoldKit.isShowBottomBar.onNext(!isShowBottomBar)
                    }
            )
            Text(
                text="重定向到 baidu",
                modifier = Modifier
                    .clickable {
                        X5WebViewKit.loadUrl("https://m.baidu.com")
                    }
            )
            Text(
                text="重定向到（清理历史） baidu",
                modifier = Modifier
                    .clickable {
                        X5WebViewKit.loadUrlWithClear("https://m.baidu.com")
                    }
            )
            Text(
                text="重创建到 baidu",
                modifier = Modifier
                    .clickable {
                        context.reloadUrl("https://m.baidu.com")
                    }
            )
            Text(
                text="重定向到 bilibili",
                modifier = Modifier
                    .clickable {
                        X5WebViewKit.loadUrl("https://m.bilibili.com")
                    }
            )
            Text(
                text="重定向到（清理历史） bilibili",
                modifier = Modifier
                    .clickable {
                        X5WebViewKit.loadUrlWithClear("https://m.bilibili.com")
                    }
            )
            Text(
                text="重创建到 bilibili",
                modifier = Modifier
                    .clickable {
                        context.reloadUrl("https://m.bilibili.com")
                    }
            )

            Text(
                text="重定向到 mdn 图片",
                modifier = Modifier
                    .clickable {
                        X5WebViewKit.loadUrl("https://developer.mozilla.org/en-US/docs/Web/HTML/Element/img")
                    }
            )

            Text(
                text="重定向到 mdn 视频",
                modifier = Modifier
                    .clickable {
                        X5WebViewKit.loadUrl("https://developer.mozilla.org/en-US/docs/Web/HTML/Element/video")
                    }
            )
        }
    }
}

@Preview
@Composable
fun FloatMenuPreview() {
    DesignPreview(
        modifier=Modifier.fillMaxSize()
    ) {
        Box() {
            FloatMenu(expanded = true)
        }
    }
}