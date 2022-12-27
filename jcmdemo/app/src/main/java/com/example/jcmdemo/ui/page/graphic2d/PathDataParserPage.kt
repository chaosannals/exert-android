package com.example.jcmdemo.ui.page.graphic2d

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.widget.PathDataParser

@Composable
fun PathDataParserPage() {
    PathDataParser(
        "M0,0 l100,0 L100,100 h100 v-100 h100 v200 H0 z"
    )
}

@Preview
@Composable
fun PathDataParserPagePreview() {
    DesignPreview() {
        PathDataParserPage()
    }
}