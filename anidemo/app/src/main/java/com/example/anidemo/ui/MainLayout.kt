package com.example.anidemo.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.LocalMainScrollerPercentage

@ExperimentalMaterial3Api
@Composable
fun MainLayout() {
    var sd by remember {
        mutableStateOf(0f)
    }

    CompositionLocalProvider(
        LocalMainScrollerPercentage provides ScrollPercentage(0.0f) {
            sd = it
        },
    ) {
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

            modifier = Modifier.fillMaxSize()
        ) {
            Row() {
                //Text(text = scroller.maxValue.toString())
                Text(text = sd.toString())
            }
            NavGraphRoutes(paddingValues = it)
        }
    }
}

@ExperimentalMaterial3Api
@Preview()
@Composable
fun MainLayoutPreview() {
    MainLayout()
}