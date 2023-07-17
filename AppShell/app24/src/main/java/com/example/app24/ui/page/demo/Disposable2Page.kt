package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.LocalNavController

@Composable
fun Disposable2Page() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val now by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    val previous by remember(navBackStackEntry) {
        derivedStateOf {
            navController.previousBackStackEntry?.destination?.route
        }
    }

    Log.d("app24", "Disposable2Page Compose")

    DisposableEffect(Unit) {
        Log.d("app24", "Disposable2Page Effect")
        onDispose {
            Log.d("app24", "Disposable2Page onDispose")
        }
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text(text="Now: $now")
        Text(text="Previous: $previous")
        Button(onClick = { navController.navigate("disposable-2-page") }) {
            Text("To Self")
        }
        Button(onClick = { navController.navigate("disposable-page") }) {
            Text("To 1")
        }
    }
}

@Preview
@Composable
fun Disposable2PagePreview() {
    DesignPreview {
        Disposable2Page()
    }
}