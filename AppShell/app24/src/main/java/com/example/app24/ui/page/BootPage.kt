package com.example.app24.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.LocalNavController
import com.example.app24.ui.sdp
import kotlinx.coroutines.delay

@Composable
fun BootPage() {
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        delay(400)
        navController.navigate("home-page")
    }

    Column (
        verticalArrangement=Arrangement.Center,
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4499FF),
                        Color.White,
                    )
                )
            )
    ) {
        Image(
            imageVector = Icons.Default.Star,
            contentDescription = "Logo",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(140.sdp)
        )
    }
}

@Preview
@Composable
fun BootPagePreview() {
    DesignPreview {
        BootPage()
    }
}