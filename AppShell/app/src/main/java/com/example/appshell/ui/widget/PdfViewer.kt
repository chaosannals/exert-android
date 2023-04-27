package com.example.appshell.ui.widget

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toFile
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.appshell.ui.si
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

@Composable
fun PdfViewer(
    file: File?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val mutex = remember { Mutex() }
    val renderer = remember(file) {
        file?.let {
            val input = ParcelFileDescriptor.open(it, ParcelFileDescriptor.MODE_READ_ONLY)
            PdfRenderer(input)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val width = 375.si
        val height = 669.si
        Log.d("pdf-viewer", "column start $width $height")
        renderer?.run {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                Log.d("pdf-viewer", "LazyColumn start")
                items(pageCount, key = { it }) { index ->
                    Log.d("pdf-viewer", "LazyColumn items start")
                    val cacheKey = MemoryCache.Key("$file-$index")
                    val bitmap = remember(file, index) {
                        val cachedBitmap = context.imageLoader.memoryCache?.get(cacheKey)

                        Log.d("pdf-viewer", "cache bitmap: $cachedBitmap")
                        cachedBitmap ?: {
                            val bitmap = Bitmap.createBitmap(
                                width,
                                height,
                                Bitmap.Config.ARGB_8888,
                            )
                            coroutineScope.launch(Dispatchers.IO) {
                                mutex.withLock {
                                    Log.d("pdf-viewer", "loading $file - $index")
                                    openPage(index).use {
                                        it.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                    }
                                }
                            }
                            bitmap
                        }()
                    }

                    Log.d("pdf-viewer", "bitmap: $bitmap")

                    val request = ImageRequest.Builder(context)
                        .size(width, height)
                        .memoryCacheKey(cacheKey)
                        .data(bitmap)
                        .build()
                    Image(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        painter = rememberAsyncImagePainter(model = request),
                        contentScale = ContentScale.Fit,
                        contentDescription = "pdf 页",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PdfViewerPreview() {
    val context = LocalContext.current
    val cache = File("${context.cacheDir}/ffffff")
    context.assets.open("xl2409.pdf").use {input ->
        context.contentResolver.openOutputStream(Uri.fromFile(cache))?.use {
            it.write(input.readBytes())
        }
    }
    DesignPreview {
        PdfViewer(
            cache
        )
    }
}