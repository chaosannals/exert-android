package com.example.libkcdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import com.example.libkcdemo.ui.DesignPreview

@Composable
fun MinePage() {
    val scaffold = LocalDirtScaffoldContext.current
    scaffold.isTopBarBackVisible = true
    Column() {
        Text("Mine")
    }
}

@Preview
@Composable
fun MinePagePreview() {
    DesignPreview {
        MinePage()
    }
}