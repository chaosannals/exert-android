package com.example.app24.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.LocalNavController
import com.example.app24.ui.sdp
import com.example.app24.ui.ssp

@Composable
fun BottomBarItem(
    icon: ImageVector,
    text: String,
    route: String,
) {
    val navController = LocalNavController.current

    Column (
        modifier = Modifier
            .clickable
            {
                navController.navigate(route)
            },
    ) {
        Image(
            imageVector = icon,
            contentDescription = "icon",
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .size(24.sdp)
        )
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.ssp,
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier=Modifier,
) {
    Row(
        horizontalArrangement=Arrangement.SpaceAround,
        verticalAlignment=Alignment.CenterVertically,
        modifier=modifier
            .background(Color.White),
    ) {
        BottomBarItem(Icons.Default.Home, "首页", "home-page")
        BottomBarItem(Icons.Default.Star, "启动", "boot-page")
        BottomBarItem(Icons.Default.Share, "示例", "demo-page")
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    DesignPreview {
        BottomBar()
    }
}