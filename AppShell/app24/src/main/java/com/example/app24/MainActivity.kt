package com.example.app24

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.X5WebViewKit.initX5
import com.example.app24.ui.MainView
import com.example.app24.ui.theme.AppShellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 通过 URL Intent 获取的数据
        val action = intent?.action
        val data = intent?.data

        Log.d("app24", "URL Intent action: $action data url: $data")

        ensurePermit(Manifest.permission.INTERNET)
        ensurePermit(Manifest.permission.ACCESS_NETWORK_STATE)

        initX5()

        setContent {
            AppShellTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}