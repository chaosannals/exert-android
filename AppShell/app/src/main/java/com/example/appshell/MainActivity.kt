package com.example.appshell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appshell.ui.MainBox
import com.example.appshell.ui.theme.AppShellTheme
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5Scaffold
import com.tencent.smtt.sdk.QbSdk

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController  provided!")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 X5
        QbSdk.initX5Environment(application, object: QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }
        })
        QbSdk.setDownloadWithoutWifi(true)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()

        setContent {
            MainBox()
        }
    }
}