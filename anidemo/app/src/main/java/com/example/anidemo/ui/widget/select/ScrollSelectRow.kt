package com.example.anidemo.ui.widget.select

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.DesignPreview
import com.example.anidemo.ui.sdp
import com.example.anidemo.ui.ssp

val scrollOptionLineHeight = 29f

@Composable
fun ScrollSelectRow(
    text: String,
    color: Color = Color(0xFFC1C1C1),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(scrollOptionLineHeight.sdp),
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
fun ScrollSelectRowPreview() {
    DesignPreview {
        Column(
            modifier = Modifier.width(100.sdp)
        ) {
            ScrollSelectRow("北京")
            ScrollSelectRow("上海")
        }
    }
}