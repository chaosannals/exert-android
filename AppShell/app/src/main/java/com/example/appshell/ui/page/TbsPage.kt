package com.example.appshell.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.db.WebViewConf
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5WebShell

@Composable
fun TbsPage() {
    Column (
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        X5WebShell(WebViewConf(
            id = 1,
            startUrl = "http://debugx5.qq.com",
            valName = "app"
        ))
    }
}

@Preview
@Composable
fun TbsPagePreview() {
    DesignPreview {
        TbsPage()
    }
}