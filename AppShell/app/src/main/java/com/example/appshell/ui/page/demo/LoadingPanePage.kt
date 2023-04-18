package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.LocalLoadingPaneSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun LoadingPanePage() {
    val pane = LocalLoadingPaneSubject.current
    var text by remember { mutableStateOf("1") }
    val nr = Regex("\\d{0,9}\\.?\\d{0,2}")

    Column (
        verticalArrangement=Arrangement.Center,
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ){
        val coroutineScope = rememberCoroutineScope()
        TextField(
            value = text,
            onValueChange =
            {
                if (nr.matches(it)) {
                    text = it
                }
            },
        )
        Button(
            onClick =
            {
                coroutineScope.launch(Dispatchers.IO) {
                    pane.onNext(true)
                    val v = text.trim()
                    val d = if (v.isEmpty()) 1.0 else v.toDouble()
                    delay(d.toDuration(DurationUnit.SECONDS))
                    pane.onNext(false)
                }
            },
        ) {
            Text("Loading")
        }
    }
}

@Preview
@Composable
fun LoadingPanePagePreview() {
    DesignPreview {
        LoadingPanePage()
    }
}