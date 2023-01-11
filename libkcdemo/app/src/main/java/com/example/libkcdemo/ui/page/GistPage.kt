package com.example.libkcdemo.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.LocalNavController
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import com.example.libkcdemo.ui.DesignPreview

@Composable
fun GistPage() {
    val navController = LocalNavController.current
    val scaffold = LocalDirtScaffoldContext.current
    scaffold.isTopBarBackVisible = true
    Column() {
        Text(
            text = "Ktor",
            modifier = Modifier.clickable {
                navController.navigate("ktor-server")
            },
        )
    }
}

@Preview
@Composable
fun GistPagePreview() {
    DesignPreview {
        GistPage()
    }
}