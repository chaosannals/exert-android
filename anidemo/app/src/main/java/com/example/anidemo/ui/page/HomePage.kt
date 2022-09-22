package com.example.anidemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.LocalNavController

@Composable
fun HomePage() {
    val nc = LocalNavController.current

    Column () {
        Button(onClick = {
            nc.navigate("employee")
        }) {
            Text("employee")
        }

        Button(onClick = {
            nc.navigate("employee/listing")
        }) {
            Text("employee listing")
        }
    }
}

@Preview(widthDp = 375)
@Composable
fun HomePagePreview() {
    HomePage()
}