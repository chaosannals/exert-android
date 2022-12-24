package com.example.anidemo.ui.page.pulldown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anidemo.ui.widget.StickyBoxColumn

@Composable
fun StickyBoxColumnPage() {
    var boxCount by remember {
        mutableStateOf(10)
    }

    StickyBoxColumn() {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Red)
        ) {

        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .sticky()
        ) {
            Text(
                text="添加一个",
                modifier = Modifier
                    .clickable {
                        boxCount += if (boxCount > 20) 0 else 1
                    }
                    .weight(1f, fill = false)
            )
            Text(
                text="减少一个",
                modifier = Modifier
                    .clickable {
                        boxCount -= if (boxCount <= 0) 0 else 1
                    }
                    .weight(1f, fill = false)
            )
        }
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (i in 0 until boxCount) {
                val w = (100 + (i % 2) * 100).dp
                Box(
                    modifier = Modifier
                        .size(w, 200.dp)
                        .background(Color.Cyan),
                )
            }
        }
    }
}

@Preview(widthDp = 375)
@Composable
fun StickyBoxColumnPagePreview() {
    StickyBoxColumnPage()
}