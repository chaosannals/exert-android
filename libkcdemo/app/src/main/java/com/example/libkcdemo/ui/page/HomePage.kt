package com.example.libkcdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.DirtScaffold
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import com.example.libkcdemo.ui.DesignPreview

@Composable
fun HomePage() {
    val scaffold = LocalDirtScaffoldContext.current
    scaffold.title = ""
    scaffold.isTopBarBackVisible = false

    Column() {
        Text("Home")
    }
}

@Preview
@Composable
fun HomePagePreview() {
    DesignPreview {
        HomePage()
    }
}