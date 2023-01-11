package com.example.mockdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import cn.chaosannals.dirtool.Dirt
import com.example.mockdemo.ui.RouteHostBox
import com.example.mockdemo.ui.theme.LibkcdemoTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val mockServerWorkRequest: WorkRequest =
//            PeriodicWorkRequestBuilder<MockServerWorker>(1, TimeUnit.HOURS)
//                .build()
        Dirt.designWidthDp = 375.dp
        Dirt.designHeightDp = 667.dp
        WindowCompat.setDecorFitsSystemWindows(window,false)
        setContent { RouteHostBox() }
    }
}