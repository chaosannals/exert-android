package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import com.example.jcm3ui.ui.routeTo
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.sf
import com.example.jcm3ui.ui.ssp
import kotlinx.coroutines.flow.MutableStateFlow

enum class FileFilter(
    val title: String,
    val pattern: String?=null,
) {
    All(
        title= "全部",
    ),
    WeiXin(
        title = "微信",
        pattern = "%WeiXin%",
    ),
    Camera(
        title="相册",
        pattern = "%Camera%",
    ),
    ScreenShot(
        title="截屏",
        pattern = "%Screenshot%",
    ),
}

data class FileFilterOption(
    val filter: FileFilter,
    val firstStat: FileStat?,
    val count: Int,
)


enum class FileType(
    val title: String,
) {
    Image("图片"),
    Video("视频"),
}


data class FileStat(
    val id: Long,
    val contentUri: Uri,
    val name: String,
    val path: String,
    val duration: Int?=null,
    val size: Int,
    val type: FileType,
//    val thumbnail: Bitmap?,
)

// 图片
fun Context.loadImagesStats(
    filter: FileFilter,
    isLoadThumbnail: Boolean,
): List<FileStat>? {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.RELATIVE_PATH,
        MediaStore.Images.Media.SIZE
    )
    val selection = filter.pattern?.let {
        "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
    }
    val selectionArgs = filter.pattern?.let {
        arrayOf(it)
    }
    val query = contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        null,
    )

    return query?.use { cursor ->
        val result = mutableListOf<FileStat>()
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val pathColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        if (cursor.moveToFirst().not()) return result
        do {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val path = cursor.getString(pathColumn)
            val size = cursor.getInt(sizeColumn)
            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
//            val thumbnail = if (isLoadThumbnail && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                contentResolver.loadThumbnail(contentUri, Size(256, 256), null)
//            } else {
//                null
//            }

            result.add(
                FileStat(
                    id = id,
                    contentUri = contentUri,
                    name = name,
                    path = path,
                    size = size,
                    type = FileType.Image,
//                    thumbnail = thumbnail
                )
            )
        } while(cursor.moveToNext())
        return result
    }
}

// 视频
fun Context.loadVideosStats(
    filter: FileFilter,
    isLoadThumbnail: Boolean,
): List<FileStat>? {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.RELATIVE_PATH,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE
    )
    val selection = filter.pattern?.let {
        "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
    }
    val selectionArgs = filter.pattern?.let {
        arrayOf(it)
    }
    val query = contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        null,
    )

    return query?.use { cursor ->
        val result = mutableListOf<FileStat>()
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val pathColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

        if (cursor.moveToFirst().not()) return result
        do {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val path = cursor.getString(pathColumn)
            val size = cursor.getInt(sizeColumn)
            val duration = cursor.getInt(durationColumn)
            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id
            )
//            val thumbnail = if (isLoadThumbnail && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                contentResolver.loadThumbnail(contentUri, Size(256, 256), null)
//            } else {
//                null
//            }
            result.add(
                FileStat(
                    id = id,
                    contentUri = contentUri,
                    name = name,
                    path = path,
                    size = size,
                    duration = duration,
                    type = FileType.Video,
//                    thumbnail = thumbnail,
                )
            )
        } while(cursor.moveToNext())
        return result
    }
}

fun Context.loadFileStats(
    permissions: Map<String, Boolean>,
    filter: FileFilter,
    isLoadThumbnail: Boolean,
): List<FileStat> {
    val fileStats = mutableListOf<FileStat>()
    for (it in permissions) {
        if (!it.value) continue
        when (it.key) {
            Manifest.permission.READ_MEDIA_IMAGES -> {
                loadImagesStats(filter, isLoadThumbnail)?.let {
                    fileStats.addAll(it)
                }
            }

            Manifest.permission.READ_MEDIA_VIDEO -> {
                loadVideosStats(filter, isLoadThumbnail)?.let {
                    fileStats.addAll(it)
                }
            }

            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                loadImagesStats(filter, isLoadThumbnail)?.let {
                    fileStats.addAll(it)
                }
                loadVideosStats(filter, isLoadThumbnail)?.let {
                    fileStats.addAll(it)
                }
            }
        }
    }
    return fileStats
}

fun Context.loadByFilterAll(
    permissions: Map<String, Boolean>,
): List<FileFilterOption> {
    return FileFilter.values().map {
        val fileStats = loadFileStats(permissions, it, false)
        FileFilterOption(
            filter = it,
            firstStat = fileStats.firstOrNull(),
            count = fileStats.size,
        )
    }.filter { it.count > 0 }
}

fun Int.padZero(length: Int=2) : String {
    return toString().padStart(length, '0')
}

fun formatDuration(duration: Int): String {
    val seconds = (duration / 1000) % 60
    val minutes = (duration / 1000 / 60) % 60
    val hours = (duration / 1000 / 60 / 60) % 24
    if (hours > 0) {
        return "${hours.padZero()}:${minutes.padZero()}:${seconds.padZero()}"
    }
    return "${minutes.padZero()}:${seconds.padZero()}"
}

@Composable
fun FilePickBox(
    stat: FileStat,
    isSelect: Boolean,
    imageLoader: ImageLoader,
    thumbnail: Bitmap?,
    onClickSelect: () -> Unit,
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier= Modifier
            .aspectRatio(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.sdp)
                .background(Color.White)
                .clip(RectangleShape)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(4f)
                    .align(Alignment.TopEnd)
                    .padding(top = 7.sdp, end = 7.sdp)
                    .size(15.sdp)
                    .background(
                        if (isSelect) Color(0xFF04A3FC) else Color.White,
                        RoundedCornerShape(2.sdp)
                    )
                    .border(
                        BorderStroke(0.5.sdp, Color(0xFFEDEDED)),
                        RoundedCornerShape(2.sdp)
                    )
                    .clickable {
                        onClickSelect()
                    },
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

            AsyncImage(
                model = thumbnail ?: stat.contentUri,
                imageLoader = imageLoader,
                contentDescription = "文件",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            )

            if (stat.type == FileType.Video) {
                val durationText by remember(stat.duration) {
                    derivedStateOf {
                        stat.duration?.let {
                            formatDuration(it)
                        } ?: ""
                    }
                }
                Row (
                    horizontalArrangement= Arrangement.SpaceBetween,
                    verticalAlignment= Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(25.sdp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF333333),
                                )
                            )
                        )
                        .padding(horizontal = 8.sdp)
                ){
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "视频标记",
                        tint=Color.White,
                    )
                    Text(
                        text = durationText,
                        color = Color.White,
                        fontSize = 11.ssp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FilePickBoxPreview() {
    var isSelect by remember() {
        mutableStateOf(true)
    }
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

    FilePickBox(
        stat = FileStat(
            1,
            Uri.parse(""),
            "测试",
            "WeiXin",
            size=1000,
            duration=10861000,
            type = FileType.Video,
        ),
        isSelect = isSelect,
        onClickSelect = { isSelect = !isSelect },
        imageLoader=imageLoader,
        thumbnail = null,
    )
}

val filePickSelectItemsSubject = MutableStateFlow<List<FileStat>>(listOf())

@Preview()
@Composable
fun FilePickPage() {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

//    val thumbnailMap = remember() {
//        mutableStateMapOf<Uri, Bitmap>()
//    }

    val imageLoader by remember(context) {
        derivedStateOf {
            ImageLoader.Builder(context)
                .components {
                    add(ImageDecoderDecoder.Factory())
                    add(VideoFrameDecoder.Factory())
                }.build()
        }
    }

    val pickMaxCount = 9

    val fileStats = remember() {
        mutableStateListOf<FileStat>()
    }
    var filter by remember() {
        mutableStateOf(FileFilter.All)
    }
    val filterOptions = remember(inspectionMode) {
        mutableStateListOf<FileFilterOption>().apply {
            if (inspectionMode) {
                add(FileFilterOption(
                    filter = FileFilter.All,
                    firstStat = null,
                    count = 100
                ))
            }
        }
    }
    val permissions = remember() {
        mutableStateMapOf<String, Boolean>()
    }
    val selectItems by filePickSelectItemsSubject.collectAsState()

    val selectIds by remember(selectItems) {
        derivedStateOf {
            selectItems.map{ it.id }.toSet()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        permissions.clear()
        permissions.putAll(it)
    }

    // permissions 居然不触发 compose 需要 permissions.size ?
    LaunchedEffect(permissions.size, filter) {
        if (permissions.isNotEmpty()) {
            val r = context.loadFileStats(permissions, filter, true)
            fileStats.clear()
            fileStats.addAll(r)
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
//                Manifest.permission.READ_MEDIA_AUDIO,
                )
            )
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    var showPopup by remember(inspectionMode) {
//        mutableStateOf(false)
        mutableStateOf(inspectionMode)
    }

    LaunchedEffect(permissions.size, showPopup) {
        if (permissions.isNotEmpty()) {
            val f = context.loadByFilterAll(permissions)
            filterOptions.clear()
            filterOptions.addAll(f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 筛选条件
        Row (
            horizontalArrangement= Arrangement.Center,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.sdp)
        ) {
            Row(
                horizontalArrangement= Arrangement.Center,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier
                    .size(110.sdp, 31.sdp)
                    .background(Color(0xFFF8F8F8), RoundedCornerShape(17.sdp))
                    .clickable {
                        showPopup = true
                    },
            ) {
                Text(text = filter.title)
                Icon(
                    imageVector = if (showPopup) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = "箭头",
                    tint=Color(0xFF666666),
                    modifier = Modifier
                        .padding(5.sdp)
                        .size(9.sdp)
                )
            }
            if (showPopup) {
                Popup(
                    onDismissRequest = { showPopup = false },
                    popupPositionProvider = object : PopupPositionProvider {
                        override fun calculatePosition(
                            anchorBounds: IntRect,
                            windowSize: IntSize,
                            layoutDirection: LayoutDirection,
                            popupContentSize: IntSize
                        ): IntOffset {
                            val x = 0
                            val y = anchorBounds.bottom
                            return IntOffset(x, y)
                        }
                    },
                    properties = PopupProperties(
                        focusable = true, // 允许获得焦点
                        clippingEnabled = false, // 允许超出屏幕
                        excludeFromSystemGesture = false, // 排除系统手势
                    ),
                ) {
                    val shape = remember() {
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 15.sdp,
                            bottomEnd = 15.sdp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape)
                            .clip(shape)
                            .padding(horizontal = 15.sdp),
                    ) {
                        filterOptions.forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.sdp)
                                    .drawBehind {
                                        drawLine(
                                            Color(0xFFEDEDED),
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, size.height),
                                            strokeWidth = 0.5f.sf,
                                        )
                                    }
                                    .clickable {
                                        showPopup = false
                                        filter = it.filter
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 10.sdp)
                                        .size(36.sdp)
                                ) {
                                    it.firstStat?.let {
                                        AsyncImage(
                                            model = it.contentUri,
                                            contentDescription = "第一个图片",
                                            imageLoader=imageLoader,
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                    }
                                }
                                Text(
                                    text = it.filter.title,
                                    color = Color(0xFF4F4F4F),
                                    fontSize = 13.ssp,
                                )
                                Text(
                                    text="(${it.count})",
                                    color=Color(0xFF999999),
                                    fontSize=12.ssp,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "箭头",
                                    tint=Color(0xFF999999),
                                    modifier = Modifier.size(10.sdp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 列表
        val endPaddingCount by remember(fileStats) {
            derivedStateOf {
                fileStats.size % 4
            }
        }
        LazyVerticalGrid(
            columns =  GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFF8F8F8)),
            verticalArrangement= Arrangement.SpaceBetween,
            horizontalArrangement=Arrangement.SpaceBetween,
        ) {
            itemsIndexed(fileStats) { _, stat ->
                val isSelect by remember(selectIds, stat) {
                    derivedStateOf {
                        selectIds.contains(stat.id)
                    }
                }
                FilePickBox(
                    stat = stat,
                    isSelect = isSelect,
                    onClickSelect = {
                        if (isSelect) {
                            filePickSelectItemsSubject.value = selectItems.filter {
                                it.id != stat.id
                            }
                        } else {
                            if (selectItems.size < pickMaxCount) {
                                filePickSelectItemsSubject.value = selectItems + stat
                            }
                        }
                    },
                    imageLoader=imageLoader,
                    thumbnail = remember(stat) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            context.contentResolver.loadThumbnail(
                                stat.contentUri,
                                Size(256, 256),
                                null
                            )
                        } else {
                            null
                        }
                    }
                )
            }
            items(endPaddingCount) {
                Spacer(modifier=Modifier)
            }
            item {
                val text by remember(fileStats) {
                    derivedStateOf {
                        "共有${fileStats.size}张图片"
                    }
                }
                Box(
                    contentAlignment= Alignment.TopCenter,
                    modifier = Modifier
                        .height(90.sdp)
                        .offset(142.sdp, 15.sdp)
                ) {
                    Text(
                        text=text,
                        color=Color(0xFFCCCCCC),
                        fontSize=12.ssp,
                    )
                }
            }
        }

        // 底部
        Row (
            horizontalArrangement=Arrangement.SpaceBetween,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.sdp)
                .padding(horizontal = 15.sdp)
        ){
            Text(
                text = "预览",
                color=Color(0xFF04A3FC),
                fontSize=14.ssp,
                modifier = Modifier.clickable {
                    routeTo("demo/file-view")
                }
            )
            Box(
                contentAlignment=Alignment.Center,
                modifier = Modifier
                    .size(125.sdp, 34.sdp)
                    .background(Color(0xFF04A3FC), RoundedCornerShape(17.sdp))
            ) {
                Text(
                    text="确认(${selectItems.size})",
                    color=Color.White,
                    fontSize=14.ssp,
                )
            }
        }
    }
}