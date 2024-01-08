package com.example.jcm3wv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.jcm3wv.ui.page.HomePage
import com.example.jcm3wv.ui.page.demo.WebViewPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

enum class NavTarget(
    val path: String,
) {
    Back("[back]"),
    Home("home"),
    DemoWebView("demo/web-view"),
}

fun NavTarget.tryTo():Boolean {
    return navEventFlow.tryEmit(this)
}

fun NavTarget.nav() {
    navScope.launch {
        navTo()
    }
}

suspend fun NavTarget.navTo() {
    navEventFlow.emit(this)
}

private val navEventFlow = MutableSharedFlow<NavTarget>()
private val navScope = CoroutineScope(Dispatchers.IO)

@Composable
fun rememberNavEventController(): NavHostController {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        navEventFlow.collect {
            if (it == NavTarget.Back) {
                navController.navigateUp()
            } else {
                navController.navigate(it.path)
            }
        }
    }

    return navController
}

fun NavGraphBuilder.buildRootGraph() {
    composable(NavTarget.Home.path) {
        HomePage()
    }
}

fun NavGraphBuilder.buildDemoGraph() {
    navigation(NavTarget.DemoWebView.path, "demo") {
        composable(NavTarget.DemoWebView.path) {
            WebViewPage()
        }
    }
}