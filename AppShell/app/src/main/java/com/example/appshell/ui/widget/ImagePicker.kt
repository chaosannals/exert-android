package com.example.appshell.ui.widget

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.appshell.ui.ensurePermit
import com.example.appshell.ui.sdp
import com.example.appshell.ui.sizeText
import com.example.appshell.ui.ssp

data class ImagePickerItem(
    val id: Long,
    val name: String,
    val size: Int,
    val uri: Uri,
)

// 共享空间图片
private fun SnapshotStateList<ImagePickerItem>.loadImageItems(context: Context) {
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE
    )
    val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
    val query = context.contentResolver.query(
        collection,
        projection,
        "",
        arrayOf(),
        sortOrder,
    )
    query?.use { cursor ->
        clear()
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
        val b = cursor.moveToFirst()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val size = cursor.getInt(sizeColumn)
            val uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )

            add(
                ImagePickerItem(
                    id = id,
                    name = name,
                    size = size,
                    uri = uri,
                )
            )
        }

    }
}

@Composable
fun ImagePicker(
    visible: Boolean,
    modifier: Modifier = Modifier,
    pickCount: Int = Int.MAX_VALUE,
    rowItemCount: Int=4,
    onConfirm: ((Boolean, List<ImagePickerItem>) -> Unit)? = null,
) {
    val context = LocalContext.current
    val itemsList = remember {
        mutableStateListOf<ImagePickerItem>()
    }
    val selectedList = remember(pickCount) {
        mutableStateListOf<ImagePickerItem>()
    }

    var viewItem: ImagePickerItem? by remember {
        mutableStateOf(null)
    }
    val viewUri: Uri? by remember {
        derivedStateOf { viewItem?.uri }
    }

    val update by rememberUpdatedState() {
        itemsList.loadImageItems(context)
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
            launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter= fadeIn(),
        exit= fadeOut(),
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize(),
        ) {
            ImageDialog(
                imageUri = viewUri,
                onClose = {viewItem = null},
                modifier = Modifier
                    .zIndex(10f)
            )
            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .background(Color.Black)
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
                        val tip = when (pickCount) {
                            1 -> "请选择一张图片"
                            Int.MAX_VALUE -> "请选择图片，已选中：${selectedList.size} "
                            else -> "已选中：${selectedList.size}/${pickCount}"
                        }
                        Text(
                            text = tip,
                            color = Color(0xFF4499DD),
                            fontSize = 14.ssp,
                        )
                    }

                    if (pickCount > 1) {
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
                        Log.d("image-picker", "render ${i} ${selectIndex}")
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(1.sdp)
                                .fillMaxSize()
                                .background(Color.Black)
                                .border(1.sdp, Color.White)
                                .clickable {
                                    if (pickCount > 1) {
                                        if (selectIndex >= 0) {
                                            selectedList.remove(item)
                                        } else {
                                            if (selectedList.size < pickCount) {
                                                selectedList.add(item)
                                            }
                                        }
                                    } else {
                                        onConfirm?.invoke(true, listOf(item))
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
                                AsyncImage(
                                    model = item.uri,
                                    contentDescription = "图片",
                                    alignment = Alignment.Center,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                )
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
}

@Preview
@Composable
fun ImagePickerPreview() {
    DesignPreview {
        ImagePicker(true)
    }
}

@Preview
@Composable
fun ImagePickerPreview2() {
    DesignPreview {
        ImagePicker(
            true,
            pickCount = 9
        )
    }
}

@Preview
@Composable
fun ImagePickerPreview3() {
    DesignPreview {
        ImagePicker(
            true,
            pickCount = 1
        )
    }
}