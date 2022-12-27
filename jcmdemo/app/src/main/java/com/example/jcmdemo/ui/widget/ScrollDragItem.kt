package com.example.jcmdemo.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import com.example.jcmdemo.ui.ssp

@Composable
fun ScrollDragItem(
    color: Color,
    title: String? = null,
    titleColor: Color = Color.Black,
    titleSize: TextUnit = 20.ssp,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color),
    ) {
        title?.let {
            Text(
                text = it,
                color = titleColor,
                fontSize = titleSize,
            )
        }
    }
}