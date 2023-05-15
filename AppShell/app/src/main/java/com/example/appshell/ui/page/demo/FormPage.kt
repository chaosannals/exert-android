package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun FormPage() {
    Column (
        modifier = Modifier
            .imePadding() // 这个修改器，只在高版本安卓有效。会顶起输入法的 padding
    ) {

    }
}

@Preview
@Composable
fun FormPagePreview() {
    DesignPreview {
        FormPage()
    }
}