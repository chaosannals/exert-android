package com.example.libkcdemo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import cn.chaosannals.dirtool.DirtPreview
import cn.chaosannals.dirtool.LocalNavController
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.layout.rememberDirtScaffoldContext

@Composable
fun DesignPreview(
    content: @Composable () -> Unit,
) {
    DirtPreview {
        CompositionLocalProvider(
            LocalNavController provides rememberNavController(),
            LocalDirtScaffoldContext provides rememberDirtScaffoldContext(),
        ) {
            content()
        }
    }
}