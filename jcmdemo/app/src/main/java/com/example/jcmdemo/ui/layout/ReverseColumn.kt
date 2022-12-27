package com.example.jcmdemo.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcmdemo.ui.DesignPreview

@Composable
fun ReverseColumn(
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

@Preview
@Composable
fun ReverseColumnPreview() {
    DesignPreview() {
        ReverseColumn {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Red)
            ) {
                Text("1")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Yellow)
            ) {
                Text("2")
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.White)
                ) {
                    Text("3")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Green)
                ) {
                    Text("4")
                }
            }
        }
    }
}