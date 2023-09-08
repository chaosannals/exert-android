package com.example.bootdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.projection.MediaProjection
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.view.Surface
import java.io.File

// TODO
class ScreenRecord2Service() : Service() {
    private val MIME_TYPE = "video/avc" // H.264 Advanced
    private val FRAME_RATE = 30 // 30 fps
    private val IFRAME_INTERVAL = 10 // 10 seconds between

    private lateinit var virtualDisplay: VirtualDisplay
    private lateinit var mediaCodec: MediaCodec
    private lateinit var surface: Surface
    private lateinit var mediaMuxer: MediaMuxer

    var mediaProjection: MediaProjection? = null
    var width: Int = 0
    var height: Int = 0
    var dpi: Int = 0
    var outDir: String? = null

    override fun onCreate() {
        super.onCreate()
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                "screenrecord2",
                "screenrecrod2",
                NotificationManager.IMPORTANCE_NONE
            )
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(chan)
            Notification.Builder(this, chan.id)
        } else {
            Notification.Builder(this)
        }.apply {
            setContentTitle("前台录屏")
            setContentText("录屏")
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentIntent(pendingIntent)
            setTicker("录屏2")
        }.build()

        startForeground(400, notification)
    }

    override fun onBind(intent: Intent?): IBinder {
        return ScreenRecordService.ScreenRecordBinder(this)
    }

    fun start() {
        createCodec()
        surface = mediaCodec.createInputSurface()
        mediaCodec.start()
        mediaMuxer = MediaMuxer(getSavePath()!!, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        createVirtualDisplay()
    }

    fun createCodec() {
        val format = MediaFormat.createVideoFormat(MIME_TYPE, width, height).apply {
            setInteger(
                MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            )
//        setInteger(MediaFormat.KEY_BIT_RATE, )
            setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL)
        }

        mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE).apply {
            configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        }
    }

    fun createVirtualDisplay() {
        virtualDisplay = mediaProjection!!.createVirtualDisplay(
            "VirtualScreen",
            width,
            height,
            dpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            surface,
            null,
            null,
        )
    }

    fun getSavePath(): String? {
        val dir = File(outDir)
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null
            }
        }
        val fileName = System.currentTimeMillis().toString()
        return "$outDir/$fileName.mp4"
    }
}