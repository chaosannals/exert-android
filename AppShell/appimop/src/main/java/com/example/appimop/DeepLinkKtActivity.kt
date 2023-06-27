package com.example.appimop

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

class DeepLinkKtActivity : Activity() {

    // deeplink 没有触发此方法
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val action: String? = intent.action
        val data: Uri? = intent.data

        Log.d("deeplink", "k oncreate: action: $action  data: $data")
        finish()
    }

    // 调用此方法
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val action: String? = intent.action
        val data: Uri? = intent.data // app4://www.example4.com/p/ath2?a=123&b=432432 参数 b 没有被传递

        Log.d("deeplink", "k oncreate: action: $action  data: $data")
//        finish() // 调用此处将不调用 onStart
    }

    // 调用 onCreate 后调用 onStart
    override fun onStart() {
        super.onStart()
        val action: String? = intent.action
        val data: Uri? = intent.data // app4://www.example4.com/p/ath2?a=123&b=432432  参数 b没有被传递

        Log.d("deeplink", "k onstart: action: $action  data: $data")
        finish()
    }
}