package com.example.appshell

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.reactivex.rxjava3.subjects.BehaviorSubject

object VideoKit {
    val exoPlayer: BehaviorSubject<ExoPlayer> = BehaviorSubject.create()
    val currentId: BehaviorSubject<String> = BehaviorSubject.create()

    fun Context.initVideoPlayer() {
        val drf = DefaultRenderersFactory(this)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
        exoPlayer.onNext(ExoPlayer.Builder(this, drf).build().apply {
            playWhenReady = false
        })
    }

    fun ExoPlayer.resetFromUri(uri: Uri?) {
        Log.d("video-box", "resetFromUri $uri")
        stop()
        clearMediaItems()
        uri?.let {
            val mi = MediaItem.fromUri(it)
            setMediaItem(mi)
            prepare()
        }
    }

    fun ExoPlayer.playUnique(uri: Uri?, id: String) {
        Log.d("video-box", "playUnique $uri  ${currentId.value} => $id")
        if (currentId.value != id) {
            currentId.onNext(id)
            resetFromUri(uri)
        }
        play()
    }

    fun ExoPlayer.pauseUnique(uri: Uri?, id: String) {
        Log.d("video-box", "pauseUnique $uri  ${currentId.value} => $id")
        if (currentId.value != id) {
            currentId.onNext(id)
            resetFromUri(uri)
            play()
        } else {
            pause()
        }
    }

    fun StyledPlayerView.init(exoPlayer: ExoPlayer?) {
        player = exoPlayer
        useController = false
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun Context.loadVideoThumb(videoUrl: Uri?):  ImageBitmap? {
        return videoUrl?.let {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(this, videoUrl)
            mmr.getFrameAtTime(1)?.asImageBitmap()
        }
    }
}