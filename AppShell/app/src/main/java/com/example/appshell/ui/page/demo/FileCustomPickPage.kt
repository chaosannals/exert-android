package com.example.appshell.ui.page.demo

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun FileCustomPickPage() {
    val fileList = remember {
        mutableStateListOf<String>()
    }
}

@Preview
@Composable
fun FileCustomPickPagePreview() {
    DesignPreview {
        FileCustomPickPage()
    }
}