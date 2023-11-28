package com.example.jcm3ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jcm3ui.ui.camera.ShotPage
import com.example.jcm3ui.ui.camera.ViewPage
import com.example.jcm3ui.ui.page.demo.FileType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private val cameraActivityRouteEventSubject = MutableSharedFlow<String>()
private val cameraActivityRouteScope = CoroutineScope(Dispatchers.IO)
val cameraActivityShotContentUriSubject = MutableStateFlow<Uri?>(null)
val cameraActivityShotTypeSubject = MutableStateFlow(FileType.Image)
val cameraActivityShotPhotoEventSubject = MutableSharedFlow<Unit>()
val cameraActivityShotVideoEventSubject = MutableSharedFlow<PressInteraction>()

fun cameraRouteTo(path: String) {
    cameraActivityRouteScope.launch {
        cameraActivityRouteEventSubject.emit(path)
    }
}



class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(navController) {
                cameraActivityRouteEventSubject.collect {
                    if (it == "[back]") {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it)
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "shot"
            ) {
                composable("shot") {
                    ShotPage()
                }
                composable("view") {
                    ViewPage()
                }
            }
        }
    }
}