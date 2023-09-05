package com.example.bootdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ScreenRecord2Service() : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return ScreenRecordService.ScreenRecordBinder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultCode = intent?.getIntExtra("code", -1)
        val resultData = intent?.getParcelableExtra<Intent>("data")
//        mediaProjection = mediaProjectManager?.getMediaProjection(resultCode!!, resultData!!)
        return super.onStartCommand(intent, flags, startId)
    }
}