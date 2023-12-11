package com.example.jcm3ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jcm3ui.ui.pick.PickPage
import com.example.jcm3ui.ui.pick.ViewPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private val pickActivityRouteEventSubject = MutableSharedFlow<String>()
private val pickActivityRouteScope = CoroutineScope(Dispatchers.IO)

fun pickRouteTo(path: String) {
    pickActivityRouteScope.launch {
        pickActivityRouteEventSubject.emit(path)
    }
}

class PickActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(navController) {
                pickActivityRouteEventSubject.collect {
                    if (it == "[back]") {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it)
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "pick"
            ) {
                composable("pick") {
                    PickPage()
                }
                composable("view") {
                    ViewPage()
                }
            }
        }
    }
}