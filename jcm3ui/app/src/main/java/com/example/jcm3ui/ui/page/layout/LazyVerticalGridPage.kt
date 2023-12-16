package com.example.jcm3ui.ui.page.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.ssp

// LazyVerticalGrid 这个布局组件性能只能说够用，每行 4列就已经略显卡顿。
// 每行 2 列 就比较流畅。

val colorStep = 0x00FFFFFF / 200

@Preview
@Composable
fun LazyVerticalGridPage() {
    val scrollState = rememberLazyGridState()

    val colors = remember {
        mutableStateListOf<Color>().apply {
            for (i in 0.. 1000) {
                val c = 0xFF000000 + (0x00FFFFFF and (i * colorStep))
                add(Color(c))
            }
        }
    }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = scrollState,
            columns = GridCells.Fixed(4)
        ) {
            itemsIndexed(colors) {i, c ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(2.sdp)
                        .aspectRatio(1f)
                        .background(c, RoundedCornerShape(4.sdp))
                ) {
                    Text(
                        text = "$i",
                        fontSize = 24.ssp,
                    )
                }
            }
        }
    }
}