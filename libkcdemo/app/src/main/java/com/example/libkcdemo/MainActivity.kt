package com.example.libkcdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import cn.chaosannals.dirtool.Dirt
import com.example.libkcdemo.ui.RouteHostBox

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dirt.designWidthDp = 375.dp
        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = Color.Transparent.toArgb()
        setContent { RouteHostBox() }
    }
}
