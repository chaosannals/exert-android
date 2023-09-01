package com.example.bootdemo

import android.app.Service
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore

class ScreenRecordService() : Service() {
    private lateinit var mediaProjection: MediaProjection
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var virtualDisplay: VirtualDisplay
    var width: Int = 0
    var height: Int = 0
    var dpi: Int = 0
    var isRunning: Boolean = false
        private set
    private var savePath: String = ""

    class ScreenRecordBinder(val service: Service) : Binder()

    override fun onBind(intent: Intent?): IBinder {
        return ScreenRecordBinder(this)
    }

    fun startRecord(): Boolean {
        if (isRunning) {
            return false
        }
        initRecorder()
        createVirtualDisplay()
        mediaRecorder.start()
        isRunning = true
        return true
    }

    fun initRecorder() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(savePath)
            setVideoSize(width, height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setVideoEncodingBitRate(2 * width * height)
            setVideoFrameRate(18)
        }

        mediaRecorder.prepare()
    }

    fun createVirtualDisplay() {
        virtualDisplay = mediaProjection.createVirtualDisplay(
            "VirtualScreen",
            width,
            height,
            dpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder.surface,
            null,
            null,
        )
    }

    fun ContentResolver.getMediaPath() {
        val now = System.currentTimeMillis()
        val name = "$now"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.MediaColumns.DATE_MODIFIED, now / 1000)
//            put(MediaStore.MediaColumns.SIZE, data.size)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.MediaColumns.IS_DOWNLOAD, true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/YourDir")
            }
        }

        val uri = insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        // TODO
    }
}