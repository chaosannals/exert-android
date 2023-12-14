package com.example.jcm3ui.ui.pick

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import com.example.jcm3ui.pickRouteTo
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.ssp
import com.example.jcm3ui.ui.widget.VideoExoPlayerViewer
import com.example.jcm3ui.ui.widget.rememberExoPlayer
import com.example.jcm3ui.ui.widget.replay
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object AndroidUriAsStringSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Uri {
        val text = decoder.decodeString()
        return Uri.parse(text)
    }

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString(value.toString())
    }

}

val pickResultJson = Json {
    serializersModule = SerializersModule {
        contextual(AndroidUriAsStringSerializer)
    }
}

@Preview
@Composable
fun ViewPage() {
    val context = LocalContext.current

    val exoPlayer = rememberExoPlayer()

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
        pickItems.map { Pair(it.contentUri, it) }.toMutableStateMap()
    }

    // remember(currentSelect) 被优化导致逻辑上错误，在下面被改变的时候不触发 compose。
    var isPlay by remember() {
        Log.d("remember-launched", "remember")
        mutableStateOf(false)
    }
    LaunchedEffect(currentSelect) {
        Log.d("remember-launched", "launched")
        isPlay = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // 顶部
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
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
                    .clickable {
                        pickRouteTo("[back]")
                    }
            )

            val isSelect by remember(selects, currentSelect) {
                derivedStateOf {
                    selects.containsKey(currentSelect?.contentUri)
                }
            }

            Box(
                contentAlignment = Alignment.Center,
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
                                selects.put(it.contentUri, it)
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
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0x44444444))
                .clickable {
                    currentSelect?.let {
                        exoPlayer?.replay(it.contentUri)
                    }
                }
        ) {
            val isStop by remember(isPlay, currentSelect) {
                derivedStateOf {
                    !isPlay && currentSelect?.type == FileType.Video
                }
            }
            if (isStop) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .zIndex(4f)
                        .align(Alignment.Center)
                        .size(60.sdp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(34.sdp)
                    )
                }
            }

            currentSelect?.let {
                when (it.type) {
                    FileType.Image -> {
                        AsyncImage(
                            model = it.contentUri,
                            contentScale = ContentScale.None,
                            contentDescription = "原图",
                            alignment = Alignment.Center,
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    FileType.Video -> {
                        exoPlayer?.apply {
                            VideoExoPlayerViewer(
                                uri = it.contentUri,
                                exoPlayer = this,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                isPlay = it
                                Log.d("remember-launched", "isPlay ${it}")
                            }
                        }
                    }
                }
            }
        }

        // 底部
        val scrollState = rememberScrollState()
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
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
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    imageLoader = imageLoader,
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
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.sdp)
                .padding(horizontal = 15.sdp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(125.sdp, 34.sdp)
                    .background(Color(0xFF04A3FC), RoundedCornerShape(17.sdp))
                    .clickable {
                        (context as? Activity)?.apply {
                            val data = Intent()
                            data.putExtra("result", pickResultJson.encodeToString(selects.values))
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
            ) {
                Text(
                    text = "确定(${selects.size})",
                    color = Color.White,
                    fontSize = 14.ssp,
                )
            }
        }
    }
}