package com.example.bootdemo

import android.app.Activity
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.bootdemo.ui.Bootstrap
import com.example.bootdemo.ui.theme.AppShellTheme

class BootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()
        // 无效
//        window.enterTransition = Fade()
//        window.exitTransition = Fade()
        setContent {
            Bootstrap()
        }
    }
}
