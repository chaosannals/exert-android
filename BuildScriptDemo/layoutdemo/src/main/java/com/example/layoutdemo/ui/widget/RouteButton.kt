package com.example.layoutdemo.ui.widget

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.layoutdemo.ui.LocalNavController

data class RouteButtonConf(
    val text: String,
    val route: String,
)

@Composable
fun RouteButton(
    conf: RouteButtonConf,
) {
    val navController = LocalNavController.current
    
    Button(
        onClick = {
            navController?.navigate(conf.route)
        }
    ) {
        Text(text = conf.text)
    }
}

@Preview
@Composable
fun RouteButtonPreview() {
    RouteButton(
        RouteButtonConf("home", "home")
    )
}