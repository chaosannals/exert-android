package com.example.anidemo.ui.page.pulldown

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anidemo.ui.widget.PullDownPushUpColumn

@Composable
fun PullDownPushUpPage() {
    val context = LocalContext.current
    var boxCount by remember {
        mutableStateOf(0)
    }
    var pullDownPx by remember {
        mutableStateOf(0f)
    }

    PullDownPushUpColumn(
        modifier = Modifier,
        onPullDownFinal = {
            Toast.makeText(context, "下拉", Toast.LENGTH_SHORT).show()
        },
        onPullDown = { pullDownPx = it },
        topContent = {
             Box(
                 contentAlignment = Alignment.Center,
                 modifier = Modifier
                    .fillMaxSize(),
             ){
                 Box(
                     modifier = Modifier
                         .size(10.dp)
                         .rotate(it)
//                         .rotate(pullDownPx)
                         .background(Color.White)
                 )
             }
        },
        onPushUpFinal = {
            Toast.makeText(context, "上推", Toast.LENGTH_SHORT).show()
        },
        bottomContent = {

        },
    ) {
        Text(
            text ="添加一些内容",
            modifier = Modifier
                .border(1.dp, Color.Cyan)
                .padding(10.dp)
                .clickable {
                    if (boxCount < 10) {
                        Toast
                            .makeText(
                                context,
                                "添加了",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        ++boxCount
                    } else {
                        Toast
                            .makeText(
                                context,
                                "不能再添加了",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
        )
        Text(
            text="转到锚点",
            modifier = Modifier
                .clickable {
                    Log.d("myanchor", "on click")
                    toAnchor("myanchor")
                }
        )
        for (i in 0..boxCount) {
            Box(
                modifier = Modifier
                    .background(Color.Green)
                    .border(1.dp, Color.Cyan)
                    .size((i * 10).dp, (i * 100).dp),
            ) {
                Text(text = i.toString())
            }
        }
        Text(
            text = "锚点",
            modifier = Modifier
                .anchorTag("myanchor")
        )
        Text(
            text = "减少一些内容",
            modifier = Modifier
                .border(1.dp, Color.Cyan)
                .padding(10.dp)
                .clickable {
                    if (boxCount > 0) {
                        Toast
                            .makeText(
                                context,
                                "减少了",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        --boxCount
                    } else {
                        Toast
                            .makeText(
                                context,
                                "不能再减少了",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
        )
    }
}

@Preview
@Composable
fun PullDownPushUpPagePreview() {
    PullDownPushUpPage()
}