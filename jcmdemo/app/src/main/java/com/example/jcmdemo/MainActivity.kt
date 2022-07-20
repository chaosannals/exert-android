package com.example.jcmdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.jcmdemo.ui.MainBox

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent @ExperimentalFoundationApi {
            MainBox()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, // 自己的标识，用来区分申请权限的是哪个调用。
        permissions: Array<out String>, // 申请的权限
        grantResults: IntArray // 最后用户通过的权限
    ) {
        // 权限结果的回调
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
