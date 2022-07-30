package com.example.jcm3demo.ui.page.tool

import android.content.Context
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

interface VideoPlayerListener : Player.Listener {
    fun getId() : String
    fun onBeReplaced()
    fun onEnsured() {}
}

object VideoPlayer {
    private var exoPlayer: ExoPlayer? = null
    private var exoListener: VideoPlayerListener? = null

    fun ensure(context: Context, listener: VideoPlayerListener): ExoPlayer {
        writeLog(context, "player ensure start")
        if (exoPlayer == null) {
            writeLog(context, "player ensure: null start")
            val drf = DefaultRenderersFactory(context)
                .setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)
            exoPlayer = ExoPlayer.Builder(context, drf).build().apply {
                addListener(listener)
                playWhenReady = false
            }
            exoListener = listener
            writeLog(context, "player ensure: null end")
        } else {
            writeLog(context, "player ensure: else start")
            exoPlayer!!.stop()
            exoPlayer!!.clearMediaItems()
            exoListener?.let {
                exoPlayer!!.removeListener(it)
                if (listener.getId() != it.getId()) {
                    it.onBeReplaced()
                }
            }
            exoPlayer!!.addListener(listener)
            exoListener = listener
            writeLog(context, "player ensure: else end")
        }

        listener.onEnsured()
        writeLog(context, "player ensure end")
        return exoPlayer!!
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun play() {
        exoPlayer?.play()
    }
}

fun ExoPlayer.playFromUri(uri: String) {
    val mi = MediaItem.fromUri(uri)
    this.clearMediaItems()
    this.setMediaItem(mi)
    this.prepare()
    this.play()
}