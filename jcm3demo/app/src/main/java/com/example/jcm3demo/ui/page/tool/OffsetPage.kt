package com.example.jcm3demo.ui.page.tool

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.*

fun logOffset(it: LayoutCoordinates, label: String) {
    val p = it.positionInRoot()
    val r = it.boundsInRoot()
    Log.i("offsetpage", "[${label}] p: ${p.x}  ${p.y}")
    Log.i("offsetpage", "[${label}] r: (${r.left}, ${r.top}) (${r.right}, ${r.bottom}) ${r.width} ${r.height}")
}

fun debounceSuspend(
    delayMs: Long = 1000L,
    coroutineScope: CoroutineScope,
    job: Job?,
    f: suspend CoroutineScope.() -> Unit
) : Job {
    job?.cancel()
    return coroutineScope.launch {
        delay(delayMs)
        f()
    }
}

@Composable
fun OffsetPage() {
    var p1 by remember {
        mutableStateOf(Offset.Zero)
    }
    var p2 by remember {
        mutableStateOf(Offset.Zero)
    }
    var p3 by remember {
        mutableStateOf(Offset.Zero)
    }

    val pcs1 = rememberCoroutineScope()
    var pj1 : Job? = null
    var pj2 : Job? = null
    var pj3 : Job? = null

    var isShowP1 by remember {
        mutableStateOf(false)
    }
    var isShowP2 by remember {
        mutableStateOf(false)
    }
    var isShowP3 by remember {
        mutableStateOf(false)
    }

    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(500.dp)
                .onGloballyPositioned {
                    pj1 = debounceSuspend(1000L, pcs1, pj1) {
                        logOffset(it, "1 500.dp")
                        p1 = it.positionInRoot()
                        // pj1 = null
                    }
                }
                .fillMaxWidth()
                .clickable {
                    isShowP1 = true
                }
                .background(Color.Red),
        ) {
            Text("offset: ${p1.x} ${p1.y}")
            if (isShowP1) {
                Popup(
                    alignment = Alignment.TopStart,
                    onDismissRequest = {
                        isShowP1 = false
                    },
                    offset = IntOffset.Zero
                ) {
                    Text(
                        text = "P1 Popup",
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(1000.dp)
                .onGloballyPositioned {
                    pj2 = debounceSuspend(1000L, pcs1, pj2) {
                        logOffset(it, "2 1000.dp")
                        p2 = it.positionInRoot()
                    }
                }
                .fillMaxWidth()
                .background(Color.Blue)
                .clickable {
                    isShowP2 = true
                },
        ) {
            Text("offset: ${p2.x} ${p2.y}")
            if (isShowP2) {
                Popup(
                    alignment = Alignment.TopStart,
                    onDismissRequest = {
                        isShowP2 = false
                    },
                    offset = IntOffset.Zero
                ) {
                    Text(
                        text = "P2 Popup",
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(1500.dp)
                .onGloballyPositioned {
                    pj3 = debounceSuspend(1000L, pcs1, pj3) {
                        logOffset(it, "3 1500.dp")
                        p3 = it.positionInRoot()
                    }
                }
                .fillMaxWidth()
                .background(Color.Cyan)
                .clickable {
                    isShowP3 = true
                },
        ) {
            Text("offset: ${p3.x} ${p3.y}")
            if (isShowP3) {
                Popup(
                    alignment = Alignment.TopStart,
                    onDismissRequest = {
                        isShowP3 = false
                    },
                    offset = IntOffset.Zero
                ) {
                    Text(
                        text = "P3 Popup",
                    )
                }
            }
        }
    }
}

@Preview(widthDp=375)
@Composable
fun OffsetPagePreview() {
    OffsetPage()
}