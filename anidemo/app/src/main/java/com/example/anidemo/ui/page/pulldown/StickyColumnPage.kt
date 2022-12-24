package com.example.anidemo.ui.page.pulldown

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anidemo.ui.widget.StickyColumn

@Composable
fun StickyColumnPage() {
    val context = LocalContext.current
    var boxCount by remember {
        mutableStateOf(10)
    }

    StickyColumn(
        onStickyChanged = { tag, state ->
            Toast.makeText(context, "${tag} => ${state}", Toast.LENGTH_SHORT).show()
        }
    ) {
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
                .border(0.5.dp, Color.Green)
                .sticky("bar")
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
        Box(
            modifier = Modifier
                .size(44.dp, 100.dp)
                .background(Color.Cyan),
        )
        Box(
            modifier = Modifier
                .size(200.dp, 44.dp)
                .background(Color.Yellow)
                .sticky("foo"),
        )
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

@Preview
@Composable
fun StickyColumnPagePreview() {
    StickyColumnPage()
}