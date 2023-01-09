package com.example.libkcdemo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import cn.chaosannals.dirtool.DirtPreview

@Composable
fun DesignPreview(
    content: @Composable () -> Unit,
) {
    DirtPreview {
        CompositionLocalProvider(
            LocalNavController provides rememberNavController(),
        ) {
            content()
        }
    }
}