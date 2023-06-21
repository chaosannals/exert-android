package com.example.appimop.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.LocalNavController

@Composable
fun BottomBarItem(
    text: String,
    modifier:Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
    )
}

@Composable
fun BottomBar(
    modifier:Modifier = Modifier,
) {
    val navController = LocalNavController.current

    Row(
        horizontalArrangement=Arrangement.SpaceAround,
        verticalAlignment=Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        BottomBarItem(
            text = "Web1",
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate("web-1")
                }
        )
        BottomBarItem(
            text = "Web2",
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate("web-2")
                }
        )
        BottomBarItem(
            text = "Web3",
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate("web-3")
                }
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    DesignPreview {
        BottomBar()
    }
}