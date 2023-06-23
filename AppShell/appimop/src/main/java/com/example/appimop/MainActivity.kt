package com.example.appimop

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.appimop.X5WebMultiViewKit.initX5
import com.example.appimop.ui.LocalNavController
import com.example.appimop.ui.rootGraph
import com.example.appimop.ui.theme.AppShellTheme
import com.example.appimop.ui.webGraph
import com.example.appimop.ui.widget.BottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        locationManager?.let {
            Log.d("location", "init locationManagerBehavior")
            locationManagerBehavior.onNext(it)
        }

        initX5()

        setContent {
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                AppShellTheme {
                    Column(
                        verticalArrangement=Arrangement.Top,
                        horizontalAlignment=Alignment.Start,
                        modifier=Modifier
                            .fillMaxSize()
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "web-1",
                            modifier = Modifier
                                .weight(1f),
                        ) {
                            rootGraph()
                            webGraph()
                        }
                        BottomBar(
                            modifier = Modifier,
                        )
                    }
                }
            }
        }
    }
}