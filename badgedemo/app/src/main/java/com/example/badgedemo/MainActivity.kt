package com.example.badgedemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.badgedemo.ui.page.HomePage
import com.example.badgedemo.ui.theme.BadgedemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        WorkManager.getInstance(this)
//            .beginUniqueWork(
//                "BadgerNotificationWorker",
//                ExistingWorkPolicy.KEEP,
//                OneTimeWorkRequest.from(BadgerNotificationWorker::class.java)
//            ).enqueue()
//            .state.observe(this) {
//                Toast.makeText(this, "BadgerNotificationWorker $it", Toast.LENGTH_SHORT).show()
//            }
        val r = OneTimeWorkRequestBuilder<BadgerNotificationWorker>()
            .build()
        WorkManager.getInstance(this)


        setContent {
            BadgedemoTheme {
                HomePage()
            }
        }
    }
}
