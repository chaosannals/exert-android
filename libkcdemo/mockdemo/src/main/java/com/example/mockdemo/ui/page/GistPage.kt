package com.example.mockdemo.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContextSubject
import cn.chaosannals.dirtool.layout.reset
import com.example.mockdemo.ui.DesignPreview
import com.example.mockdemo.ui.LocalNavController

@Composable
fun GistPage() {
    val navController = LocalNavController.current
    val scaffold = LocalDirtScaffoldContextSubject.current
    scaffold.reset {
        title = "概览"
        isTopBarBackVisible = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text="ktor",
            modifier = Modifier
                .clickable { navController.navigate("ktor-client") }
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