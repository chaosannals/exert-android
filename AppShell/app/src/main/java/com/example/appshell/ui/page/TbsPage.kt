package com.example.appshell.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5WebShell

@Composable
fun TbsPage() {
    Column (
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        X5WebShell("http://debugx5.qq.com")
    }
}

@Preview
@Composable
fun TbsPagePreview() {
    DesignPreview {
        TbsPage()
    }
}