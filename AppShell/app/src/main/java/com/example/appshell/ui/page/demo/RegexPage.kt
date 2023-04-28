package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview

val placeholderPattern = Regex("\\{\\w+\\}")

@Composable
fun RegexPage() {
    Column {
        val matches = placeholderPattern.findAll("{aaa}{bbb}sadfasd{ccc}sdf{asdf}")
        for (m in matches) {
            Text(text = m.value)
        }
    }
}

@Preview
@Composable
fun RegexPagePreview() {
    DesignPreview {
        RegexPage()
    }
}