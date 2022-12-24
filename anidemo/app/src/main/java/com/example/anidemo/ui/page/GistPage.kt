package com.example.anidemo.ui.page

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.anidemo.LocalMainScroller
import com.example.anidemo.LocalNavController
import com.example.anidemo.ShortcutUtil
import com.example.anidemo.SystemInfoUtil
import com.example.anidemo.ui.widget.PullDownBox

@Composable
fun GistPage() {
    val nc = LocalNavController.current
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Text(
            text = "PullDown",
            modifier = Modifier
                .clickable {
                    nc.navigate("pulldown")
                }
        )
        Text(
            text = "PullDown2",
            modifier = Modifier
                .clickable {
                    nc.navigate("pulldown2")
                }
        )
        Text(
            text = "PullDownPushUp",
            modifier = Modifier
                .clickable {
                    nc.navigate("pulldownpushup")
                }
        )
        Text(
            text = "StickyBoxColumn",
            modifier = Modifier
                .clickable {
                    nc.navigate("stickyboxcolumn")
                }
        )
    }
}

@Preview
@Composable
fun GistPagePreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalMainScroller provides rememberScrollState(),
    ) {
        GistPage()
    }
}