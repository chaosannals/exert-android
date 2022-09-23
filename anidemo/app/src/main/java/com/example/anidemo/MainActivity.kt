package com.example.anidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.anidemo.ui.MainLayout
import com.example.anidemo.ui.ScrollPercentage
import com.example.anidemo.ui.theme.AniDemoTheme

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController  provided!")
}

val LocalMainScroller = staticCompositionLocalOf<ScrollState> {
    error("No Main Scroller  provided!")
}

val LocalMainScrollerPercentage = compositionLocalOf<ScrollPercentage> {
    error("No Main Scroller percentage provided!")
}

class MainActivity : ComponentActivity() {

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniDemoTheme {
                val navController = rememberNavController()
                val scroller = rememberScrollState()
                val sp by remember {
                    mutableStateOf(ScrollPercentage(0f))
                }

                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalMainScroller provides scroller,
                    LocalMainScrollerPercentage provides sp,
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainLayout()
                    }
                }
            }
        }
    }
}
