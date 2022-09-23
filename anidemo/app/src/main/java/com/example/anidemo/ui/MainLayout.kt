package com.example.anidemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.tooling.preview.Preview

@ExperimentalMaterial3Api
@Composable
fun MainLayout() {
    val scroller = rememberScrollState()
    val sd = (scroller.value.toFloat() / (scroller.maxValue + 1))

    Scaffold(
        topBar = {},
        bottomBar = {
            MainBottomBar()
        },
        floatingActionButton={
            if (sd < 0.99) {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = { },
                ) {

                }
            }
        },

        modifier = Modifier
    ) {
        Row() {
            Text(text = scroller.maxValue.toString())
            Text(text = sd.toString())
        }
        NavGraphRoutes(scroller = scroller, paddingValues = it)
    }
}

@ExperimentalMaterial3Api
@Preview()
@Composable
fun MainLayoutPreview() {
    MainLayout()
}