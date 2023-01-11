package com.example.libkcdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.DirtScaffold
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContextSubject
import cn.chaosannals.dirtool.layout.reset
import com.example.libkcdemo.ui.DesignPreview

@Composable
fun HomePage() {
    val scaffold = LocalDirtScaffoldContextSubject.current
    scaffold.reset {
        title = ""
        isTopBarBackVisible = false
    }

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