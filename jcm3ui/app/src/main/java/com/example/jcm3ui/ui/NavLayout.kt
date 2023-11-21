package com.example.jcm3ui.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private val routeEventSubject = MutableSharedFlow<String>()
private val routeScope = CoroutineScope(Dispatchers.IO)

fun routeTo(path: String) {
    routeScope.launch {
        routeEventSubject.emit(path)
    }
}

suspend fun routeWait(path: String) {
    routeEventSubject.emit(path)
}

@Preview()
@Composable
fun NavLayout() {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        routeEventSubject.collect {
            if (it == "[back]") {
                navController.popBackStack()
            } else {
                navController.navigate(it)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier
            .fillMaxSize(),
    ) {
        buildRootGraph()
        buildDemoGraph()
    }
}