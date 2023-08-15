package com.example.bootdemo

import androidx.compose.runtime.Composable

sealed class StringOrContent {
    class StringValue(val value: String): StringOrContent()
    class ContentValue(val value: @Composable () -> Unit): StringOrContent()
}
