package com.example.anidemo.ui.page.pulldown

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anidemo.ui.widget.PullDownBox

@Composable
fun PullDownPage() {
    val context = LocalContext.current
    var isPullDown by remember {
        mutableStateOf(false)
    }
    val contentScrollState = rememberScrollState()

    PullDownBox(
        enabled = contentScrollState.value == 0,
        modifier = Modifier
            .fillMaxSize(),
        scrollListener = {
            isPullDown = it > 0f
            Log.d("anidemo", "s isPull ${isPullDown} ${it}")
        },
        finishedListener = {
            isPullDown = false
            Log.d("anidemo", "f isPull ${isPullDown} ${it}")
            Toast.makeText(
                context,
                if (it > 50) "开始刷新" else "取消",
                Toast.LENGTH_SHORT
            ).show()
        },
        topContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Red)
                    .fillMaxSize(),
            ) {
                Text(if (it > 50f) "释放刷新" else "再往下拉")
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(contentScrollState, !isPullDown)
                .fillMaxSize(),
        ) {
            Text("一些内容")
            for (i in 1..10) {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .size((i * 10).dp, (i * 100).dp),
                )
            }
            Text("底下一些内容")
        }
    }
}

@Preview
@Composable
fun PullDownPagePreview() {
    PullDownPage()
}