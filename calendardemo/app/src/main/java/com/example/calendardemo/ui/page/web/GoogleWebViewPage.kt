package com.example.calendardemo.ui.page.web

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendardemo.ui.widget.GoogleWebView
import com.example.calendardemo.ui.widget.rememberSaveableWebViewState

@Composable
fun GoogleWebViewPage() {
    val state = rememberSaveableWebViewState()
    GoogleWebView(
        state = state,
        modifier = Modifier
            .fillMaxSize()
    )
}

@Preview
@Composable
fun GoogleWebViewPagePreview() {

}