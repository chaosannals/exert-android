package com.example.bootdemo.ui

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bootdemo.ui.theme.AppShellTheme
import com.example.bootdemo.ui.widget.BottomBar
import com.example.bootdemo.ui.widget.FinishDialog
import com.example.bootdemo.ui.widget.FloatBall
import com.example.bootdemo.ui.widget.finishDialogVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScaffold() {
    val context = LocalContext.current
    val router = rememberNavController()
    val navEntry = router.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    FinishDialog()

    BackHandler() {
        Log.d("bootdemo", "MainScaffold BackHandler ${router.backQueue.size}")
        if (router.backQueue.isEmpty() || router.backQueue.size == 2) {
            finishDialogVisible.value = true
        } else {
            coroutineScope.launch(Dispatchers.Main) {
                router.navigateUp()
            }
        }
    }

    CompositionLocalProvider(
        LocalRouter provides router,
    ) {
        AppShellTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    ) {
                    Column (
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(y = (-100).dp),
                    ) {
                        FloatBall(Icons.Default.Info) {
                            router.run {
                                Log.d("bootdemo", "FloatBall router:")
                                backQueue.forEach {
                                    Log.d("bootdemo", "FloatBall backQueue route: ${it.destination.route}")
                                }
                            }
                        }
                        FloatBall(Icons.Default.Delete) {
                            router.run {
                                // 1. 全清
                                router.backQueue.clear()
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxSize()
                    ) {
                        NavHost(
                            router,
                            startDestination = "index",
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            rootGraph()
                            canBackGraph()
                        }
                        BottomBar()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScaffoldPreview() {
    MainScaffold()
}