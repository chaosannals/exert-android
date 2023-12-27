package com.example.jcm3ui.ui.pick

import android.Manifest
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.LruCache
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.DataSource
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.fetch.SourceResult
import coil.intercept.Interceptor
import coil.memory.MemoryCache
import coil.request.ImageResult
import coil.request.Options
import coil.request.SuccessResult
import com.example.jcm3ui.pickRouteTo
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.sf
import com.example.jcm3ui.ui.ssp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

// 参数
val pickPagePickMaxCount = MutableStateFlow(9)
val filePickSelectItemsSubject = MutableStateFlow<List<FileStat>>(listOf())

// 提到全局后性能提升了，状态保留，返回时滚动才会在原地。
private val pickPageGridStart = MutableStateFlow(0)
private val pickPageGridOffset = MutableStateFlow(0)
private val pickPageFileStats = MutableStateFlow<List<FileStat>>(listOf())
private val pickPageFilter = MutableStateFlow(FileFilter.All)
//private val pickPageUpdateAt = MutableStateFlow(0L)
//private val pickPageUpdateEvent = MutableSharedFlow<Long>()
private val pickPageUpdateScope = CoroutineScope(Dispatchers.IO)

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

@Serializable
data class FileStat(
    val id: Long,

    @Contextual
    val contentUri: Uri,
    val thumbPath: String?=null,

    val name: String,
    val path: String,
    val duration: Int?=null,
    val durationStamp: String="",
    val size: Int,
    val type: FileType,
    val mtime: Int,
)

private fun Context.makePhotoThumbFromContentUri(uri: Uri): Bitmap? {
    return contentResolver.openInputStream(uri).use { stream ->
        val bitmap = BitmapFactory.decodeStream(stream)
        return ThumbnailUtils.extractThumbnail(bitmap, 256, 256)
    }
}

private fun Context.makeVideoThumbFromContentUri(uri: Uri): Bitmap? {
    val mmr = MediaMetadataRetriever()
    mmr.setDataSource(this, uri)
    return mmr.getFrameAtTime(1)?.asImageBitmap()?.let {
        ThumbnailUtils.extractThumbnail(it.asAndroidBitmap(), 256, 256)
    }
}

private fun Context.ensureCache(stat: FileStat): Int {
    return File(stat.thumbPath!!).run {
        if (isFile) return 0
        if (stat.type == FileType.Image) {
            makePhotoThumbFromContentUri(stat.contentUri)
        } else {
            makeVideoThumbFromContentUri(stat.contentUri)
        }?.let {bitmap ->
            outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                return 1
            }
        }
        return -1
    }
}

// 图片
fun Context.loadImagesStats(
    filter: FileFilter,
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
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATE_MODIFIED,
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
        val modifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)

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
            val mtime = cursor.getInt(modifiedColumn)

            val thumbName = "${path}-${name}-${mtime}.jpg".replace('/', '-')
            val thumbPath = File(cacheDir, thumbName).absolutePath

            result.add(
                FileStat(
                    id = id,
                    contentUri = contentUri,
                    thumbPath = thumbPath,
                    name = name,
                    path = path,
                    size = size,
                    type = FileType.Image,
                    mtime = mtime,
                )
            )
        } while(cursor.moveToNext())
        return result
    }
}


// 视频
fun Context.loadVideosStats(
    filter: FileFilter,
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
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DATE_MODIFIED,
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
        val mtimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)

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
            val mtime = cursor.getInt(mtimeColumn)
            val thumbName = "${path}-${name}-${mtime}.jpg".replace('/', '-')
            val thumbPath = File(cacheDir, thumbName).absolutePath

            result.add(
                FileStat(
                    id = id,
                    contentUri = contentUri,
                    thumbPath = thumbPath,
                    name = name,
                    path = path,
                    size = size,
                    duration = duration,
                    durationStamp = formatDuration(duration),
                    type = FileType.Video,
                    mtime = mtime,
                )
            )
        } while(cursor.moveToNext())
        return result
    }
}



fun Context.loadFileStats(
    permissions: Map<String, Boolean>,
    filter: FileFilter,
): List<FileStat> {
    val fileStats = mutableListOf<FileStat>()
    for (it in permissions) {
        if (!it.value) continue
        when (it.key) {
            Manifest.permission.READ_MEDIA_IMAGES -> {
                loadImagesStats(filter)?.let {
                    fileStats.addAll(it)
                }
            }

            Manifest.permission.READ_MEDIA_VIDEO -> {
                loadVideosStats(filter)?.let {
                    fileStats.addAll(it)
                }
            }

            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                loadImagesStats(filter)?.let {
                    fileStats.addAll(it)
                }
                loadVideosStats(filter)?.let {
                    fileStats.addAll(it)
                }
            }
        }
    }
    fileStats.sortByDescending {it.mtime }

    return fileStats
}

fun Context.loadByFilterAll(
    permissions: Map<String, Boolean>,
): List<FileFilterOption> {
    return FileFilter.entries.map {
        val fileStats = loadFileStats(permissions, it)
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

private val m1 = Modifier
    .aspectRatio(1f)
private val m2 = Modifier
    .aspectRatio(1f)
    .padding(1.5.sdp)
    .background(Color.White)
    .clip(RectangleShape)

@Composable
fun FilePickBox(
    stat: FileStat,
    isSelect: Boolean,
    imageLoader: ImageLoader,
    onClickSelect: () -> Unit,
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier= m1
            .clickable {
                onClickSelect()
            },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = m2,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(4f)
                    .align(Alignment.TopEnd)
                    .padding(top = 7.sdp, end = 7.sdp)
//                    .size(15.sdp)
                    .size(24.sdp)
//                    .background(
//                        if (isSelect) Color(0xFF04A3FC) else Color.White,
//                        RoundedCornerShape(2.sdp)
//                    )
                    .background(
                        if (isSelect) Color(0xFF04A3FC) else Color(0x44444444),
                        CircleShape
                    )
                    .border(
                        1.25.sdp,
                        Color.White,
                        CircleShape,
                    )
//                    .border(
//                        BorderStroke(0.5.sdp, Color(0xFFEDEDED)),
//                        RoundedCornerShape(2.sdp)
//                    )
//                    .clickable {
//                        onClickSelect()
//                    },
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

            val context = LocalContext.current

            var updateAt by rememberSaveable(stat.thumbPath) {
                mutableLongStateOf(0L)
            }

            key(updateAt) {
                AsyncImage(
//                model = stat.contentUri,
                    model = stat.thumbPath,
//                placeholder = rememberAsyncImagePainter(model = stat.contentUri),
                    onError = {
                        pickPageUpdateScope.launch {
                            if (context.ensureCache(stat) > 0) {
//                                val end = System.currentTimeMillis()
//                                pickPageUpdateEvent.emit(end)
                                updateAt = System.currentTimeMillis()
                            }
                        }
                    },
                    imageLoader = imageLoader,
                    contentDescription = "文件",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            if (stat.type == FileType.Video) {
                Row (
                    horizontalArrangement= Arrangement.SpaceBetween,
                    verticalAlignment= Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(25.sdp)
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Transparent,
//                                    Color(0xFF333333),
//                                )
//                            )
//                        )
                        .padding(horizontal = 8.sdp)
                ){
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "视频标记",
                        tint= Color.White,
                        modifier = Modifier
                            .size(24.sdp)
                    )
                    Text(
                        text = stat.durationStamp,
                        color = Color.White,
//                        fontSize = 11.ssp,
                        fontSize = 14.ssp,
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 90)
@Composable
fun FilePickBoxPreview() {
    var isSelect by remember() {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    val imageLoader by remember(context) {
        derivedStateOf {
            ImageLoader.Builder(context)
                .memoryCache {
                    MemoryCache.Builder(context)
                        .maxSizePercent(0.5)
                        .build()
                }
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
            size = 1000,
            duration = 10861000,
            type = FileType.Video,
            mtime = 0,
            path = "",
        ),
        isSelect = isSelect,
        onClickSelect = { isSelect = !isSelect },
        imageLoader = imageLoader,
    )
}

// 没用到
class ThumbInterceptor(
    private val context: Context,
    private val thumbs: LruCache<String, Drawable>,
    private val scope: CoroutineScope,
): Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val stat = chain.request.data as? FileStat
        if (stat != null) {
            val key = stat.contentUri.toString()
            val thumb = thumbs.get(key)
            if (thumb != null) {
                return SuccessResult(
                    drawable = thumb,
                    request = chain.request,
                    dataSource = DataSource.MEMORY_CACHE,
                )
            } else {
                scope.launch {
                    if (stat.type == FileType.Image) {
                        context.makePhotoThumbFromContentUri(stat.contentUri)
                    } else {
                        context.makeVideoThumbFromContentUri(stat.contentUri)
                    }?.toDrawable(context.resources)?.let {
                        thumbs.put(key, it)
                    }
                }
            }
        }
        return chain.proceed(chain.request)
    }
}

//class ThumbDecoder(
//    private val context: Context,
//    private val thumbs: LruCache<String, Drawable>,
//): Decoder {
//    override suspend fun decode(): DecodeResult? {
//
//    }
//
//    class Factory @JvmOverloads constructor(
//
//    ): Decoder.Factory {
//        override fun create(
//            result: SourceResult,
//            options: Options,
//            imageLoader: ImageLoader
//        ): Decoder? {
//            result.source.source()
//        }
//    }
//}

//val deleteQueueFlow = MutableSharedFlow<Uri>()

@Preview()
@Composable
fun PickPage(
    columnCount: Int = 4,
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

    val pickMaxCount by pickPagePickMaxCount.collectAsState()

//    val updateAt by pickPageUpdateAt.collectAsState()
//    LaunchedEffect(updateAt) {
//        pickPageUpdateEvent.collect {
//            val d = it - updateAt
//            if (d > 1000) {
//                pickPageUpdateAt.value = it
//            }
//        }
//    }

    val imageLoader by remember(context) {
        derivedStateOf {
            ImageLoader.Builder(context)
//                .memoryCache {
//                    MemoryCache.Builder(context)
//                        .maxSizePercent(0.5)
//                        .build()
//                }
//                .diskCache {
//                    DiskCache.Builder()
//                        .directory(context.cacheDir.resolve("image_cache"))
//                        .maxSizePercent(0.25)
//                        .build()
//                }
                .components {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(VideoFrameDecoder.Factory())
//                    add(ThumbInterceptor(context, LruCache(1000), scope))
                }.build()
        }
    }

    val fileStats by pickPageFileStats.collectAsState()
    val filter by pickPageFilter.collectAsState()

    val filterOptions = remember(inspectionMode) {
        mutableStateListOf<FileFilterOption>().apply {
            if (inspectionMode) {
                add(
                    FileFilterOption(
                        filter = FileFilter.All,
                        firstStat = null,
                        count = 100
                    )
                )
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
            launch(Dispatchers.IO) {
                val r = context.loadFileStats(permissions, filter)
                pickPageFileStats.value = r
//                r.forEachIndexed { i, it ->
//                    launch {
//                        if (context.ensureCache(it) > 0) {
//                            val end = System.currentTimeMillis()
//                            pickPageUpdateEvent.emit(end)
//                        }
//                    }
//                }
            }
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
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
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

//    var deleteUri: Uri? by remember {
//        mutableStateOf(null)
//    }

    val deleteSenderLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "用户同意删除: ${it.data}", Toast.LENGTH_SHORT).show()
        } else {
            // 真机测试是 Activity.RESULT_CANCELED
            Toast.makeText(context, "用户拒绝删除: ${it.resultCode}", Toast.LENGTH_SHORT).show()
        }
        if (it.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(context, "用户拒绝", Toast.LENGTH_SHORT).show()
        }
    }

//    LaunchedEffect(Unit) {
//        deleteQueueFlow.collect {
//
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 筛选条件
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.sdp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "返回",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 15.sdp)
                    .size(15.sdp)
                    .clickable {
                        (context as? Activity)?.apply {
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }
                    }
            )

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
                                        pickPageFilter.value = it.filter
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
                fileStats.size % columnCount
            }
        }

        val gridStart by pickPageGridStart.collectAsState()
        val gridOffset by pickPageGridOffset.collectAsState()
        val gridState = rememberLazyGridState(
            initialFirstVisibleItemIndex = gridStart,
            initialFirstVisibleItemScrollOffset = gridOffset,
        )
        DisposableEffect(gridState) {
            onDispose {
                pickPageGridStart.value = gridState.firstVisibleItemIndex
                pickPageGridOffset.value = gridState.firstVisibleItemScrollOffset
            }
        }

//        key(updateAt) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(columnCount),
//            contentPadding = PaddingValues(1.5.sdp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
//                    .background(Color(0xFFF8F8F8)),
                    .background(Color(0xFF444444)),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                itemsIndexed(fileStats, key = { i, fs -> fs.id }) { _, stat ->
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
                        imageLoader = imageLoader,
                    )
                }
                items(endPaddingCount) {
                    Spacer(modifier = Modifier)
                }
                item {
                    val text by remember(fileStats) {
                        derivedStateOf {
                            "共有${fileStats.size}张图片"
                        }
                    }
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .height(90.sdp)
                            .offset(142.sdp, 15.sdp)
                    ) {
                        Text(
                            text = text,
                            color = Color(0xFFCCCCCC),
                            fontSize = 12.ssp,
                        )
                    }
                }
            }
//        }

        // 底部
        Row (
            horizontalArrangement=Arrangement.SpaceBetween,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.sdp)
                .padding(horizontal = 15.sdp)
        ) {
            Text(
                text = "预览",
                color = Color(0xFF04A3FC),
                fontSize = 14.ssp,
                modifier = Modifier.clickable {
                    pickRouteTo("view")
                }
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.sdp, 34.sdp)
                    .background(Color(0xFFFF4411), RoundedCornerShape(17.sdp))
                    .clickable {
                        (context as? Activity)?.apply {
                            selectItems.forEach { fs ->
                                try {
                                    contentResolver.delete(fs.contentUri, null, null)
                                } catch (se: SecurityException) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        val rse = se as RecoverableSecurityException
                                        val request = IntentSenderRequest
                                            .Builder(rse.userAction.actionIntent.intentSender)
                                            .build()

                                        // TODO 同步化， 这里无法传参，导致用户确认后回调不知道哪个 同意 哪个 拒绝。
                                        deleteSenderLauncher.launch(request)
                                    }
                                }
                            }
                            filePickSelectItemsSubject.value = listOf()
                        }
                    }
            ) {
                Text(
                    text="删除(${selectItems.size})",
                    color = Color.White,
                    fontSize = 14.ssp,
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(125.sdp, 34.sdp)
                    .background(Color(0xFF04A3FC), RoundedCornerShape(17.sdp))
                    .clickable {
                        (context as? Activity)?.apply {
                            val data = Intent()
                            data.putExtra("result", pickResultJson.encodeToString(selectItems))
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
            ) {
                Text(
                    text = "确认(${selectItems.size})",
                    color = Color.White,
                    fontSize = 14.ssp,
                )
            }
        }
    }
}