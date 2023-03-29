package com.example.appshell.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.appshell.*
import com.example.appshell.db.WebViewConf
import com.example.appshell.db.ensureWebViewConf
import com.example.appshell.ui.theme.AppShellTheme
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5Scaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainBox() {
    AppShellTheme {
        val navController = rememberNavController()
        val database = rememberAppDatabase(context = LocalContext.current)
        var sd by remember {
            mutableStateOf(0f)
        }

        val sps = rememberMainScrollSubject {
            sd = it
        }

        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalAppDatabase provides database,
            LocalMainScrollSubject provides sps,
        ) {
            X5Scaffold() {
                NavHost(
                    navController = navController,
                    startDestination = "home-page",
                ) {
                    routeRootGraph()
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