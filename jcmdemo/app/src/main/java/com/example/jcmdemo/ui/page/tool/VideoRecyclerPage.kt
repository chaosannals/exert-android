package com.example.jcmdemo.ui.page.tool

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache

//class LocalCacheDataSourceFactory(private val context: Context) : DataSource.Factory {
//
//    private val defaultDataSourceFactory: CacheDataSource.Factory
//    //TODO Needs to be a singleton
//    private val simpleCache: SimpleCache = SimpleCache(
//        File(context.cacheDir, "media"),
//        LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE)
//    )
//    private val cacheDataSink: CacheDataSink = CacheDataSink(simpleCache, MAX_FILE_SIZE)
//    private val fileDataSource: FileDataSource = FileDataSource()
//
//    init {
//        val userAgent = "Demo"
//        val bandwidthMeter = DefaultBandwidthMeter()
//        defaultDataSourceFactory = DefaultDataSourceFactory(
//            this.context,
//            bandwidthMeter,
//            DefaultHttpDataSourceFactory(userAgent)
//        )
//    }
//
//    override fun createDataSource(): DataSource {
//        return CacheDataSource(
//            simpleCache, defaultDataSourceFactory.createDataSource(),
//            fileDataSource, cacheDataSink,
//            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
//        )
//    }
//}

@Composable
fun VideoRecyclerPage () {
    AndroidView(factory = {
        StyledPlayerView(it)
    }) {

    }
}

@Preview(showBackground = true)
@Composable
fun VideoRecyclerPagePreview () {
    VideoRecyclerPage()
}