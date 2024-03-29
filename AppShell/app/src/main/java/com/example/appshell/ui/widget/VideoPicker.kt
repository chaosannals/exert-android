package com.example.appshell.ui.widget

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.*
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
import com.example.appshell.VideoKit.loadVideoThumb
import com.example.appshell.ui.sdp
import com.example.appshell.ui.sizeText
import com.example.appshell.ui.ssp
import java.util.concurrent.TimeUnit
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
    val selection = "${MediaStore.Video.Media.DURATION} >= ?"
    val selectionArgs = arrayOf(
        TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS).toString()
    )
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
                    duration=duration.toDuration(DurationUnit.MILLISECONDS),
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
    videoMaxDuration: Duration? = null,
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

    val tip by remember(pickCount, videoMaxDuration, selectedList) {
        derivedStateOf {
            "请选择视频" + when (pickCount) {
                1 -> ""
                Int.MAX_VALUE -> "，已选中：${selectedList.size} "
                else -> "，已选中：${selectedList.size}/${pickCount}"
            } + when (videoMaxDuration) {
                null -> ""
                else -> "，最大时长：${videoMaxDuration}"
            }
        }
    }

    var viewItem: VideoPickerItem? by remember {
        mutableStateOf(null)
    }
    val viewUri: Uri? by remember(viewItem) {
        derivedStateOf { viewItem?.uri }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            update()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter=fadeIn(),
        exit=fadeOut(),
    ) {
        VideoDialog(
            videoUrl = viewUri,
            onClose = { viewItem = null },
            modifier = Modifier
                .zIndex(999f)
        )

        Box(
            modifier = Modifier
                .zIndex(1f)
                .background(Color.Black)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                    ) {
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
                            color = Color.White,
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
                            color = Color.White,
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
                        val isLessMaxDuration =
                            if (videoMaxDuration != null) videoMaxDuration >= item.duration else true
                        Log.d("video-picker", "render ${i} ${selectIndex}")
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(1.sdp)
                                .fillMaxSize()
                                .background(Color.Black)
                                .border(1.sdp, Color.White)
                                .clickable(enabled = isLessMaxDuration) {
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

                            Box(
                                contentAlignment=Alignment.Center,
                                modifier = Modifier
                                    .zIndex(10f)
                                    .align(Alignment.BottomEnd)
                                    .size(34.sdp, 24.sdp)
                                    .background(Color(0x44FFFFFF))
                                    .clickable {
                                               viewItem = item
                                    },
                            ) {
                                Text(
                                    text = "查看",
                                    color = Color.White,
                                    fontSize = 14.ssp,
                                )
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
                                            tint = Color.White,
                                            contentDescription = "无缩略图",
                                            modifier = Modifier
                                                .size(24.sdp)
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
                                Text(
                                    text = "${item.duration}",
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