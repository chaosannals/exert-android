package com.example.appshell.ui


import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import com.example.appshell.*
import com.example.appshell.ui.theme.AppShellTheme
import com.example.appshell.ui.widget.*
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun MainBox() {
    AppShellTheme {
        val status = rememberTotalStatus()
        val routeStatus = rememberRouteStatus()
        val scaffoldStatus = rememberX5ScaffoldStatus()
        val tipQueue = rememberTipQueue()
        var sd by remember {
            mutableStateOf(0f)
        }

        DisposableEffect(Unit) {
            val txIMDisposable = TxIM.logQueue.subscribe {
                tipQueue.onNext(
                    TipItem(
                        content = "[${it.logLevel}] ${it.logContent}",
                        color = when (it.logLevel) {
                            0 -> Color.Red
                            1 -> Color.Yellow
                            in 900..999 -> Color.Blue
                            else -> Color.Green
                        },
                        1.4.toDuration(DurationUnit.SECONDS)
                    )
                )
            }

            onDispose {
                txIMDisposable.dispose()
            }
        }

        DisposableEffect(status) {
            val scrollOffsetDisposable = status.scrollOffset
                .throttleLast(400, TimeUnit.MILLISECONDS)
                .subscribe { sd = it }

            val exceptionQueueDisposable = status.exceptionQueue
                .subscribe {
                    tipQueue.onNext(
                        TipItem(
                            "${it.message}",
                            Color.Red,
                            1.4.toDuration(DurationUnit.SECONDS),
                        )
                    )
                }

            onDispose {
                scrollOffsetDisposable.dispose()
                exceptionQueueDisposable.dispose()
            }
        }

        CompositionLocalProvider(
            LocalTotalStatus provides status,
            LocalRouteStatus provides routeStatus,
            LocalX5ScaffoldStatus provides scaffoldStatus,
            LocalTipQueue provides tipQueue,
            LocalLoadingPaneSubject provides rememberLoadingPaneSubject(),
        ) {
            LoadingPaneBox() {
                TipMessageBox {
                    X5Scaffold(
                        modifier = Modifier
                            .navigationBarsPadding()
                    ) {
                        NavHost(
                            navController = status.router,
//                    startDestination = routeStatus.startRoute,
                            startDestination = "home-page",
                        ) {
                            routeRootGraph()
                            routeDemoGraph()
                        }
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
fun MainBoxPreview() {
    DesignPreview {
        MainBox()
    }
}