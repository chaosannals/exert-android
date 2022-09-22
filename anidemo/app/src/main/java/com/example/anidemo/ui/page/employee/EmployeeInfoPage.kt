package com.example.anidemo.ui.page.employee

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmployeeInfoPage(
    id: Long?,
) {
    Text(
        text = id.toString(),
    )
}

@Preview(widthDp = 375)
@Composable
fun EmployeeInfoPagePreview() {
    EmployeeInfoPage(123456)
}