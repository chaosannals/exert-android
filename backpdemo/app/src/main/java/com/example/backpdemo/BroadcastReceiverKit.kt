package com.example.backpdemo

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

// AndroidManifest 系统级的 可以接收开机启动消息。

// 注册型的只于 activity 声明周期存活。
fun registerSecondBroadcastReceiver(context: Context) {
    val fbr = SecondBroadcastReceiver()
    val filter = IntentFilter(
        ConnectivityManager.CONNECTIVITY_ACTION
    ).apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        addAction(Intent.ACTION_INPUT_METHOD_CHANGED)
    }
    context.registerReceiver(fbr, filter)
}