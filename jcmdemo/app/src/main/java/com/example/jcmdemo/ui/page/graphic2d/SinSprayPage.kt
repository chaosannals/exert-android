package com.example.jcmdemo.ui.page.graphic2d

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.widget.SinSpray

@Composable
fun SinSprayPage() {
    SinSpray()
}

@Preview
@Composable
fun SinSprayPagePreview() {
    DesignPreview() {
        SinSprayPage()
    }
}