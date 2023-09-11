package com.example.layoutdemo.ui.page

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.layoutdemo.ui.widget.DrawerButton

@Composable
fun DrawerButtonPage() {
    val m = remember {
        Modifier
            .fillMaxWidth()
            .height(140.dp)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            DrawerButton(
                "a",
                modifier = m,
            ) {
                Text("2")
            }
        }
        items(100) {
            DrawerButton(
                "$it",
                modifier = m,
                sideContent = {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                    ) {
                        Text("[$it]")
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Black)
                ) {
                    Text("$it")
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerButtonPagePreview() {
    DrawerButtonPage()
}