package com.example.jcm3ui.ui.page.demo

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.ssp
import com.example.jcm3ui.ui.widget.VideoExoPlayerViewer

@Preview
@Composable
fun FileViewPage() {
    val context = LocalContext.current

    val imageLoader by remember(context) {
        derivedStateOf {
            ImageLoader.Builder(context)
                .components {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(VideoFrameDecoder.Factory())
                }.build()
        }
    }
    val pickItems by filePickSelectItemsSubject.collectAsState()

    var currentSelect by remember(pickItems) {
        mutableStateOf(pickItems.getOrNull(0))
    }
    val selects = remember(pickItems) {
        pickItems.map { it.contentUri }.toMutableStateList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // 顶部
        Row(
            horizontalArrangement= Arrangement.SpaceBetween,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .height(44.sdp)
                .fillMaxWidth()
                .padding(horizontal = 15.sdp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "返回",
                modifier = Modifier
                    .size(15.sdp)
            )

            val isSelect by remember(selects, currentSelect) {
                derivedStateOf {
                    selects.contains(currentSelect?.contentUri)
                }
            }

            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(15.sdp)
                    .background(
                        if (isSelect) Color(0xFF04A3FC) else Color.White,
                        RoundedCornerShape(2.sdp),
                    )
                    .border(
                        BorderStroke(0.5.sdp, Color(0xFFEDEDED)),
                        RoundedCornerShape(2.sdp),
                    )
                    .clickable {
                        currentSelect?.let {
                            if (isSelect) {
                                selects.remove(it.contentUri)
                            } else {
                                selects.add(it.contentUri)
                            }
                        }
                    }
            ) {
                if (isSelect) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "选中",
                        tint = Color.White,
                        modifier = Modifier.size(11.sdp),
                    )
                }
            }
        }

        // 视口
        Box(
            contentAlignment=Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0x44444444))
        ) {
            currentSelect?.let {
                when (it.type) {
                    FileType.Image -> {
                        AsyncImage(
                            model = it.contentUri,
                            contentScale = ContentScale.None,
                            contentDescription = "原图",
                            alignment = Alignment.Center,
                            imageLoader=imageLoader,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    FileType.Video -> {
                        VideoExoPlayerViewer(
                            uri = it.contentUri,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
        }

        // 底部
        val scrollState = rememberScrollState()
        Row(
            horizontalArrangement= Arrangement.Start,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.sdp)
                .background(Color(0xE5FFFFFF))
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(8.sdp))
            pickItems.forEach {
                val isSelect by remember {
                    derivedStateOf {
                        currentSelect?.contentUri == it.contentUri
                    }
                }
                AsyncImage(
                    model = it.contentUri,
                    contentDescription = "小图",
                    contentScale=ContentScale.Crop,
                    alignment = Alignment.Center,
                    imageLoader=imageLoader,
                    modifier = Modifier
                        .padding(horizontal = 7.sdp)
                        .border(
                            if (isSelect) 1.sdp else 0.5.sdp,
                            if (isSelect) Color(0xFF04A3FC) else Color(0xFF999999)
                        )
                        .size(36.sdp)
                        .clickable {
                            currentSelect = it
                        }
                )
            }
            Spacer(modifier = Modifier.width(8.sdp))
        }
        Row (
            horizontalArrangement= Arrangement.End,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.sdp)
                .padding(horizontal = 15.sdp)
        ){
            Box(
                contentAlignment=Alignment.Center,
                modifier = Modifier
                    .size(125.sdp, 34.sdp)
                    .background(Color(0xFF04A3FC), RoundedCornerShape(17.sdp))
            ) {
                Text(
                    text = "确定(${selects.size})",
                    color=Color.White,
                    fontSize = 14.ssp,
                )
            }
        }
    }
}