package com.example.jcm3demo.ui.page.tool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ReverseColumnPage(
    content: (@Composable () -> Unit),
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Layout(
            modifier = Modifier,
            content = content,
        ) { ms, cs ->
            layout(cs.maxWidth, cs.maxHeight) {
                var sumh = cs.maxHeight
                for (m in ms) {
                    val c = cs.copy(maxHeight = sumh)
                    val p = m.measure(c)
                    val x = (cs.maxWidth - p.width) / 2
                    sumh -= p.height
                    p.place(x, sumh)
                }
            }
        }
    }
}

@Preview(widthDp = 375, heightDp = 667)
@Composable
fun ReverseColumnPagePreview() {
    ReverseColumnPage() {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Red)
        ) {

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Yellow)
        ) {

        }
        Column (
            modifier = Modifier
                .fillMaxSize()
//                .size(4.dp)
                .background(Color.Cyan),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.White)
            ) {

            }
        }
    }
}