package com.example.appshell.ui.widget

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.example.appshell.ui.LocalTipQueue
import com.example.appshell.ui.tip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// Environment.DIRECTORY_DOWNLOADS
// 程序安装目录下的私有空间
fun SnapshotStateList<Uri>.load(context: Context, tag: String) : String? {
    val dir = context.getExternalFilesDir(tag)
    val files = dir?.listFiles() ?: arrayOf()
    clear()
    addAll(files.map { it.toUri() })
    Log.d("file-custom-picker", "dir: $dir")
    return dir?.toString()
}

// 共享空间图片
fun SnapshotStateList<Uri>.loadPicture(context: Context) {
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
    val selection = ""
    val selectionArgs = arrayOf<String>(
//        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
    )
    val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
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
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val size = cursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )

            Log.d("file-custom-picker", "loadVideo:: id: ${id}, name: ${name}, size: ${size}")

            add(contentUri)
        }

    }
}

// 共享空间
fun SnapshotStateList<Uri>.loadVideo(context: Context) {
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
    val selection = "${MediaStore.Video.Media.DURATION} <= ?"
    val selectionArgs = arrayOf<String>(
        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
    )
    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
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

            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id
            )

            Log.d("file-custom-picker", "loadVideo:: id: ${id}, name: ${name}, duration: ${duration}, size: ${size}")

            add(contentUri)
        }

    }
}

@Composable
fun FileCustomPicker() {
    val tipQueue = LocalTipQueue.current
    val context = LocalContext.current
    val fileList = remember {
        mutableStateListOf<Uri>()
    }
    val coroutineScope = rememberCoroutineScope()

    var dirPath: String? by remember {
        mutableStateOf("")
    }

    Text(text=dirPath ?: "")

    LazyRow() {
        item {
            Box(
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {
                            dirPath = fileList.load(context, Environment.DIRECTORY_DOWNLOADS)
                            tipQueue.tip("加载 下载 完成")
                        }
                    }
            ) {
                Text(text="下载")
            }
        }
        item {
            Box(
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {
                            dirPath = fileList.load(context, Environment.DIRECTORY_PICTURES)
                            tipQueue.tip("加载 图片 完成")
                        }
                    }
            ) {
                Text("图片")
            }
        }

        item {
            Box(
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {
                            fileList.loadVideo(context)
                            tipQueue.tip("加载 视频(共享) 完成")
                        }
                    }
            ) {
                Text("视频(共享)")
            }
        }

        item {
            Box(
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {
                            fileList.loadPicture(context)
                            tipQueue.tip("加载 图片(共享) 完成")
                        }
                    }
            ) {
                Text("图片(共享)")
            }
        }
    }

    LazyColumn {
        itemsIndexed(fileList) {i, uri ->
            Row () {
                Text(
                    text="$i => $uri",
                )
            }
        }
    }
}

@Preview
@Composable
fun FileCustomPickerPreview() {
    DesignPreview {
        FileCustomPicker()
    }
}