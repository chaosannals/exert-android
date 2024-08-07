package com.example.app24.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.LocalNavController

@Composable
fun HomePage() {
    val navController = LocalNavController.current

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Text(text="home")
        Text(
            text="demo",
            modifier = Modifier
                .clickable
                {
                    navController.navigate("demo-page")
                }
        )
    }
}

@Preview
@Composable
fun HomePagePreview() {
    DesignPreview {
        HomePage()
    }
}