package com.example.jcm3ui.ui.page.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.DataSource
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.ImageSource
import coil.decode.SvgDecoder
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.request.Options
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.ceil

private data class ContentUriInfo(
    val mimeType: String,
) {

}

private fun Context.getContentUriInfo(uri: Uri): ContentUriInfo? {
    val projection = arrayOf(
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.MIME_TYPE,
    )
    return contentResolver.query(uri, projection, null, null, null)?.use {
        val dataIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val mimeIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
        it.moveToFirst()
        val data = it.getString(dataIndex)
        val mime = it.getString(mimeIndex)
        return ContentUriInfo(
            mimeType = mime,
        )
    }
}

private fun Context.limitPhotoThumbnail(source: Bitmap, limit: Int): Bitmap {
    val ratio = source.width.toFloat() / source.height.toFloat()
    val (w, h) = if (ratio > 1.0f) {
        Pair(limit, ceil(limit.toFloat() / ratio).toInt())
    } else {
        Pair(ceil(limit.toFloat() * ratio).toInt(), limit)
    }
    val target = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val paint = Paint()
    val canvas = Canvas(target)
    canvas.drawBitmap(
        source, Rect(0,0,source.width, source.height),
        Rect(0, 0, w, h), paint
    )
    return target
}

private fun Context.makePhotoThumbnail(uri: Uri, limit: Int): Bitmap {
    contentResolver.openInputStream(uri).use {input ->
        val source = BitmapFactory.decodeStream(input)
        return limitPhotoThumbnail(source, limit)
    }
}

private fun Context.makeVideoThumbnail(uri: Uri, limit: Int): Bitmap? {
    val mmr = MediaMetadataRetriever()
    mmr.setDataSource(this, uri)
    return mmr.getFrameAtTime(1)?.let {
        limitPhotoThumbnail(it, limit)
    }
}

private fun Context.makeThumbnail(uri: Uri, limit: Int): Bitmap? {
    return getContentUriInfo(uri)?.let {info ->
        if (info.mimeType.contains("image", ignoreCase = true)) {
            makePhotoThumbnail(uri, limit)
        } else {
            makeVideoThumbnail(uri, limit)
        }
    }
}

private fun Context.makeThumbAsDataUrl(contentUri: Uri, limit: Int): String? {
    return makeThumbnail(contentUri, limit)?.let { thumb ->
        ByteArrayOutputStream().use { output ->
            thumb.compress(Bitmap.CompressFormat.PNG, 90, output)
//            val data = Base64.encode(output.toByteArray(), Base64.DEFAULT)
            val data = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT or Base64.NO_WRAP)
            "data:image/png;base64,$data"
//            val data = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT) // 这个 Base64 居然会搞出换行符
//            "data:image/png;base64,$data".filter { it != '\n' && it != '\r' }
        }
    }
}

private val tempDataUrl = "data:image/png;base64,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"

//class DataUrlDecoder @JvmOverloads constructor(
//
//): Decoder {
//    class Factory @JvmOverloads constructor(
//
//    ) : Decoder.Factory {
//        override fun create(
//            result: SourceResult,
//            options: Options,
//            imageLoader: ImageLoader
//        ): Decoder? {
//
//        }
//    }
//
//    override suspend fun decode(): DecodeResult? {
//        TODO("Not yet implemented")
//    }
//}


class DataUrlBase64Fetcher  @JvmOverloads constructor(
    val match: MatchResult,
    val context: Context,
): Fetcher {
    override suspend fun fetch(): FetchResult? {
        return match.groups[3]?.value?.let {data ->
            val bytes = Base64.decode(data, Base64.DEFAULT)
            return SourceResult(
                source = ImageSource(ByteArrayInputStream(bytes).source().buffer(), context),
                mimeType = match.groups[2]?.value,
                dataSource = DataSource.MEMORY
            )
        }
    }

    class Factory @JvmOverloads constructor(
        val context: Context,
    ): Fetcher.Factory<String> {
        // 完全正则匹配
        private val DATA_URL_PATTERN = Regex("data:(.+?)(;base64)?,(.+)", RegexOption.DOT_MATCHES_ALL)

        override fun create(data: String, options: Options, imageLoader: ImageLoader): Fetcher? {
            return DATA_URL_PATTERN.matchEntire(data)?.let {
                DataUrlBase64Fetcher(it, context)
            }
        }
    }
}

class DataUrlBase64Interceptor(
    private val context: Context,
) : Interceptor {
    private val DATA_URL_PATTERN = Regex("data:(.+?)(;base64)?,(.+)", RegexOption.DOT_MATCHES_ALL)

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val uri = chain.request.data as? String
        return uri?.let { uri ->
            DATA_URL_PATTERN.matchEntire(uri)?.let {match ->
                match.groups[3]?.value?.let {data ->
                    val bytes = Base64.decode(data, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    SuccessResult(
                        drawable = bitmap.toDrawable(context.resources),
                        request = chain.request,
                        dataSource = DataSource.MEMORY_CACHE,
                    )
                }
            }
        }?: chain.proceed(chain.request)
    }
}

@Preview
@Composable
fun ThumbnailPage() {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {

        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val imageLoader = remember(context) {
            ImageLoader.Builder(context)
                .components {
                    add(DataUrlBase64Interceptor(context))
//                    add(DataUrlBase64Fetcher.Factory(context))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(SvgDecoder.Factory())
                }
                .build()
        }
        var thumb: String? by remember {
            mutableStateOf(null)
        }
        val pickLauncher = rememberLauncherForActivityResult(
            contract =  ActivityResultContracts.GetContent()
        ) {
            it?.let {
                coroutineScope.launch(Dispatchers.IO) {
                    thumb = context.makeThumbAsDataUrl(it, 512)
                }
            }
        }

        Button(
            onClick = {
                // pickLauncher.launch("*") // 这样会直接报不存在 GetContent
                pickLauncher.launch("*/*")
            }
        ) {
            Text(text = "选择")
        }

        AsyncImage(
            model = thumb,
            imageLoader=imageLoader,
            contentScale = ContentScale.Inside,
            contentDescription = ""
        )
        AsyncImage(
            model = tempDataUrl,
            imageLoader=imageLoader,
            contentScale = ContentScale.Inside,
            contentDescription = "示例"
        )
    }
}