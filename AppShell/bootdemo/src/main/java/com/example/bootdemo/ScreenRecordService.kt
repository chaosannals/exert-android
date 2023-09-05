package com.example.bootdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File

val screenRecordTip = PublishSubject.create<String>()

class ScreenRecordService() : Service() {
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var virtualDisplay: VirtualDisplay

    var mediaProjection: MediaProjection? = null
    var width: Int = 0
    var height: Int = 0
    var dpi: Int = 0
    var outDir: String? = null

    var isRunning: Boolean = false
        private set
    private var savePath: String = ""

    class ScreenRecordBinder(val service: Service) : Binder()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val chan = NotificationChannel("screenrecord", "screenrecrod", NotificationManager.IMPORTANCE_NONE)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(chan)

        val notification: Notification = Notification.Builder(this, chan.id)
            .setContentTitle("前台录屏")
            .setContentText("录屏")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setTicker("录屏2")
            .build()

        startForeground(444, notification)
    }

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

    fun stopRecord(): Boolean {
        if (!isRunning) {
            return false
        }
        isRunning = false

        try {
            mediaRecorder.stop()
            mediaRecorder.reset()
            virtualDisplay.release()
        } catch (t: Throwable) {
            screenRecordTip.onNext(t.message ?: "")
        }
        return false
    }

    fun initRecorder() {
        savePath = getSavePath()!!
        // 这个被谷歌废弃，格式需要选定机器支持的。
        // TODO 获取设备支持的格式，并进行设置。
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(savePath)
//            setVideoSize(width, height)
            setVideoSize(800, 600) // 太大的话,设备不支持导致 prepare 报错，没有提出。
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD)
//            setVideoEncodingBitRate(2 * width * height)
//            setVideoEncodingBitRate(1080 * 1960)
//            setVideoFrameRate(18)
        }

        mediaRecorder.prepare()
    }

    fun createVirtualDisplay() {
        virtualDisplay = mediaProjection!!.createVirtualDisplay(
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

    fun getSavePath(): String? {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            val rootDir = Environment.getExternalStorageDirectory().absolutePath + "/video/"
            val rootDir = outDir
            val dir = File(rootDir)
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null
                }
            }
            val fileName = System.currentTimeMillis().toString()
            return "$rootDir/$fileName.mp4"
        }
        return null
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