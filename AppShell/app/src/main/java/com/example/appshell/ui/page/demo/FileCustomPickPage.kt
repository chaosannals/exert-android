package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.FileCustomPicker

@Composable
fun FileCustomPickPage() {
    Column (
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        FileCustomPicker()
    }
}

@Preview
@Composable
fun FileCustomPickPagePreview() {
    DesignPreview {
        FileCustomPickPage()
    }
}