package com.example.appshell.ui.widget

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.appshell.VideoKit.loadVideoThumb
import com.example.appshell.ui.sdp
import com.example.appshell.ui.sizeText
import com.example.appshell.ui.ssp
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class VideoPickerItem(
    val id: Long,
    val name: String,
    val size: Int,
    val duration: Duration,
    val uri: Uri,
    val thumb: ImageBitmap?,
)


// 共享空间
private fun SnapshotStateList<VideoPickerItem>.loadVideoItems(context: Context) {
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE
    )
    val selection = ""
    val selectionArgs = arrayOf<String>()
    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} DESC"
    val query = context.contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        sortOrder,
    )
    query?.use { cursor ->
        clear()

        // 获取信息
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val duration = cursor.getInt(durationColumn)
            val size = cursor.getInt(sizeColumn)

            val uri: Uri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id
            )

            add(
                VideoPickerItem(
                    id=id,
                    name=name,
                    duration=duration.toDuration(DurationUnit.SECONDS),
                    size=size,
                    uri=uri,
                    thumb=context.loadVideoThumb(uri),
                )
            )
        }

    }
}

@Composable
fun VideoPicker(
    visible: Boolean,
    modifier: Modifier = Modifier,
    pickCount: Int = Int.MAX_VALUE,
    rowItemCount: Int=4,
    onConfirm: ((Boolean, List<VideoPickerItem>) -> Unit)? = null,
) {
    val context = LocalContext.current
    val itemsList = remember {
        mutableStateListOf<VideoPickerItem>()
    }
    val selectedList = remember(pickCount) {
        mutableStateListOf<VideoPickerItem>()
    }

    val update by rememberUpdatedState() {
        itemsList.loadVideoItems(context)
    }

    LaunchedEffect(visible) {
        update()
    }

    AnimatedVisibility(
        visible = visible,
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement= Arrangement.Start,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement= Arrangement.Start,
                    verticalAlignment= Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    val tip = when (pickCount) {
                        1 -> "请选择一个视频"
                        Int.MAX_VALUE -> "请选择视频，已选中：${selectedList.size} "
                        else -> "已选中：${selectedList.size}/${pickCount}"
                    }
                    Text(
                        text = tip,
                        color = Color(0xFF4499DD),
                        fontSize = 14.ssp,
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(4.sdp)
                        .background(Color.Cyan)
                        .clickable
                        {
                            onConfirm?.invoke(true, selectedList)
                            selectedList.clear()
                        },
                ) {
                    Text(
                        text = "确定",
                        color= Color.White,
                        fontSize = 14.ssp,
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(4.sdp)
                        .background(Color.Gray)
                        .clickable
                        {
                            onConfirm?.invoke(false, listOf())
                            selectedList.clear()
                        },
                ) {
                    Text(
                        text = "取消",
                        color= Color.White,
                        fontSize = 14.ssp,
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(rowItemCount),
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                itemsIndexed(itemsList) { i, item ->
                    val selectIndex: Int = selectedList.indexOf(item)
                    Log.d("video-picker", "render ${i} ${selectIndex}")
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(1.sdp)
                            .fillMaxSize()
                            .background(Color.Black)
                            .border(1.sdp, Color.White)
                            .clickable {
                                if (selectIndex >= 0) {
                                    selectedList.remove(item)
                                } else {
                                    if (selectedList.size < pickCount) {
                                        selectedList.add(item)
                                    }
                                }
                            }
                    ) {
                        if (selectIndex >= 0) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .zIndex(10f)
                                    .align(Alignment.TopEnd)
                                    .size(24.sdp, 14.sdp)
                                    .background(
                                        Color.Cyan,
                                        RoundedCornerShape(bottomStart = 10.sdp)
                                    )
                            ) {
                                Text(
                                    text = "${selectIndex + 1}",
                                    color = Color.White,
                                    fontSize = 12.ssp,
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .zIndex(1f)
                                .fillMaxSize()
                        ) {
                            if (item.thumb != null) {
                                Image(
                                    bitmap = item.thumb,
                                    contentDescription = "视频",
                                    alignment = Alignment.Center,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                )
                            } else {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .background(Color.Gray)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "无缩略图",
                                    )
                                }
                            }
                            Text(
                                text = item.name,
                                color = Color.White,
                                fontSize = 12.ssp,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = false,
                            )
                            Text(
                                text = item.size.sizeText(),
                                color = Color.White,
                                fontSize = 12.ssp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun VideoPickerPreview() {
    DesignPreview {
        VideoPicker(
            true
        )
    }
}