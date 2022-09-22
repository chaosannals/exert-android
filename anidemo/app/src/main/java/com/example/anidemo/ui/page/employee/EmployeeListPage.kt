package com.example.anidemo.ui.page.employee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.LocalNavController

@Composable
fun EmployeeListPage() {
    val nc = LocalNavController.current

    Column (
        modifier = Modifier.fillMaxWidth(),
    ) {
        for (i in 1..100) {
            Button(
                onClick = {
                    nc.navigate("employee/info/${i}")
                },
            ) {
                Text(text = i.toString())
            }
        }
    }
}

@Preview(widthDp = 375)
@Composable
fun EmployeeListPagePreview() {
    EmployeeListPage()
}