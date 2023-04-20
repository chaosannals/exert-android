package com.example.appshell.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.appshell.*
import com.example.appshell.ui.theme.AppShellTheme
import com.example.appshell.ui.widget.*
import java.util.concurrent.TimeUnit

@Composable
fun MainBox() {
    AppShellTheme {
        val status = rememberTotalStatus()
        val routeStatus = rememberRouteStatus()
        val scaffoldStatus = rememberX5ScaffoldStatus()
        var sd by remember {
            mutableStateOf(0f)
        }

        DisposableEffect(status) {
            val s = status.scrollOffset
                .throttleLast(400, TimeUnit.MILLISECONDS)
                .subscribe { sd = it }
            onDispose {
                s.dispose()
            }
        }

        CompositionLocalProvider(
            LocalTotalStatus provides status,
            LocalRouteStatus provides routeStatus,
            LocalX5ScaffoldStatus provides scaffoldStatus,
            LocalTipQueue provides rememberTipQueue(),
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