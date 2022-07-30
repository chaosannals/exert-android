package com.example.jcm3demo.ui.page.tool

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.jcm3demo.R

@Composable
fun VideoPauseBox(
    onClick: ((Boolean) -> Unit)? = null,
) {
    var isPause by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .zIndex(2.0f)
            .clickable {
                isPause = !isPause
                onClick?.invoke(isPause)
            }
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isPause) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play_circle_outline),
                contentDescription = "播放（暂停）",
                tint = colorResource(id = R.color.light_sky_blue),
                modifier = Modifier.fillMaxSize(0.6f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPauseBoxPreview() {
    VideoPauseBox()
}