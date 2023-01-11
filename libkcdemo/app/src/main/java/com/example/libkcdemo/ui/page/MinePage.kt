package com.example.libkcdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContextSubject
import cn.chaosannals.dirtool.layout.reset
import com.example.libkcdemo.ui.DesignPreview

@Composable
fun MinePage() {
    val scaffold = LocalDirtScaffoldContextSubject.current
    scaffold.reset {
        title="我的"
        isTopBarBackVisible = true
    }
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