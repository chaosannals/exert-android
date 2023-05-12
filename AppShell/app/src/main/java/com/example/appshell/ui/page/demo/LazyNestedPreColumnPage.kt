package com.example.appshell.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.LazyNestedPreColumn

private val INT_PATTERN = Regex("[0-9]+")

@Composable
fun LazyNestedPreColumnPage() {
    var countField by remember {
        mutableStateOf(TextFieldValue(text="4444"))
    }
    val count by remember(countField) {
        derivedStateOf {
            if (countField.text.matches(INT_PATTERN)) {
                countField.text.toInt()
            } else {
                0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = countField,
            onValueChange =
            {
                countField = it
            },
        )
        LazyNestedPreColumn() {
            item {
                Box(modifier = Modifier
                    .size(240.sdp)
                    .background(Color.Red))
            }
            item {
                Box(modifier = Modifier
                    .size(140.sdp)
                    .background(Color.Green))
            }
            item {
                Box(modifier = Modifier
                    .size(24.sdp)
                    .background(Color.Blue))
            }
            item {
                Box(modifier = Modifier
                    .size(40.sdp)
                    .background(Color.Cyan))
            }
            items(count) { index ->
                val t = "I'm item $index"
                key (t) {
                    Text(
                        t,
                        modifier = Modifier
//                        .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LazyNestedPreColumnPagePreview() {
    DesignPreview {
        LazyNestedPreColumnPage()
    }
}