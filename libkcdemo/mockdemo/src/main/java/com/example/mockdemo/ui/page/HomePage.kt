package com.example.mockdemo.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContextSubject
import cn.chaosannals.dirtool.layout.reset
import com.example.mockdemo.ui.DesignPreview

@Composable
fun HomePage() {
    val scaffold = LocalDirtScaffoldContextSubject.current
    scaffold.reset {
        title = ""
        isTopBarBackVisible = false
    }
}

@Preview
@Composable
fun HomePagePreview() {
    DesignPreview {
        HomePage()
    }
}