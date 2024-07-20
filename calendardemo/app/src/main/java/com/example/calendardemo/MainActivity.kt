package com.example.calendardemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.example.calendardemo.ui.MainBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leakcanary.EventListener
import leakcanary.LeakCanary

class MainActivity : ComponentActivity() {
    // TODO 。
    private fun initLeakCanary() {
        val analysisUploadListener = EventListener { event ->
            when (event) {
                is EventListener.Event.HeapAnalysisDone.HeapAnalysisSucceeded -> {
                    Toast.makeText(this, "HeapAnalysisSucceeded", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "event", Toast.LENGTH_SHORT).show()
                }
            }
        }

        LeakCanary.config = LeakCanary.config.run {
            copy(eventListeners = eventListeners + analysisUploadListener)
        }

        LeakCanary.showLeakDisplayActivityLauncherIcon(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLeakCanary()
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    while (true) {
                        delay(10000)
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "tick", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            MainBox()
        }
    }
}
