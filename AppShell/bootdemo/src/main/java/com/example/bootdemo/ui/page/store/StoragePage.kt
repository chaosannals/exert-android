package com.example.bootdemo.ui.page.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.bootdemo.ui.LocalRouter

@Composable
fun StorageButton(
    text: String,
    route: String,
) {
    val mode = LocalInspectionMode.current
    val router = if (mode) null else LocalRouter.current

    Button(
        onClick = { router?.navigate(route) },
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun StoragePage() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        StorageButton("Shared Preferences", "shared-preferences")
        StorageButton("Data Store Preferences", "data-store-preferences")
        StorageButton("Data Store Proto", "data-store-proto")
    }
}

@Preview
@Composable
fun StoragePagePreview() {
    StoragePage()
}