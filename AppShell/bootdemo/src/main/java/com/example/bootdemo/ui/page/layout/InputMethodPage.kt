package com.example.bootdemo.ui.page.layout

import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
fun InputMethodPage() {
    var isInputMethodActive by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            InputMethodManager
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {

    }
}

@Preview
@Composable
fun InputMethodPagePreview() {
    InputMethodPage()
}