package com.example.appshell.ui.page.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview

// Rx onNext 是同步调用，没有被协程分发，只是回调。
@Composable
fun OnlyRxJava3Lv2Page() {

}

@Preview
@Composable
fun OnlyRxJava3Lv2PagePreview() {
    DesignPreview {
        OnlyRxJava3Lv2Page()
    }
}