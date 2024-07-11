package com.example.calendardemo.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendardemo.ui.navigate

@Composable
fun BottomBar() {
    val coroutineScope = rememberCoroutineScope()

    Row(
        horizontalArrangement=Arrangement.SpaceAround,
        verticalAlignment=Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "HOME",
            modifier = Modifier
                .clickable {
                    coroutineScope.navigate("/")
                }
        )
        Text(
            text = "WEB1",
            modifier = Modifier
                .clickable {
                    coroutineScope.navigate("/web")
                }
        )
        Text(
            text = "WEB2",
            modifier = Modifier
                .clickable {
                    coroutineScope.navigate("/web/my")
                }
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar()
}