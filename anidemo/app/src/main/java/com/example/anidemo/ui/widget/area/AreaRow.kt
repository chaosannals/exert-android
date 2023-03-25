package com.example.anidemo.ui.widget.area

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.sdp
import com.example.anidemo.ui.ssp

@Composable
fun AreaRow(
    text: String,
    color: Color = Color(0xFFC1C1C1),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(29.sdp),
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 13.ssp,
            modifier = Modifier
                .padding(vertical = 5.sdp),
        )
    }
}

@Preview
@Composable
fun AreaRowPreview() {
    Column(
        modifier = Modifier.width(100.sdp)
    ) {
        AreaRow("北京")
        AreaRow("上海")
    }
}