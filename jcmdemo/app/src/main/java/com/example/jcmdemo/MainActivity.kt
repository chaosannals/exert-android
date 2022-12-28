package com.example.jcmdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.ui.MainBox
import com.tencent.smtt.sdk.QbSdk

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController  provided!")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QbSdk.initX5Environment(application, object: QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }
        })
        QbSdk.setDownloadWithoutWifi(true)

        // 窗口外框可绘制，需要手写padding，扩展的 Padding 在 com.google.accompanist:accompanist-insets 扩展库
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent @ExperimentalFoundationApi {
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalNavController provides navController,
            ) {
                MainBox()
            }
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
