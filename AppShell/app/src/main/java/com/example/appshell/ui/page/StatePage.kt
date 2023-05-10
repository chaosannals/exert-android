package com.example.appshell.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun StateRouteButton(
    text: String,
    path: String,
) {
    val totalStatus = LocalTotalStatus.current

    Text(
        text = text,
        fontSize = 14.ssp,
        modifier = Modifier
            .clickable {
                totalStatus.router.navigate(path)
            }
    )
}

@Composable
fun StatePage() {

    Column {
        Text(
            text="状态页"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                StateRouteButton("Only Flow Page", "only-flow-page")
                StateRouteButton("Only LiveData Page", "only-live-data-page")
                StateRouteButton("Only ViewModel Page", "only-view-model-page")
                StateRouteButton("Save Parcelize Page", "save-parcelize-page")
            }
        }
    }
}

@Preview
@Composable
fun StatePagePreview() {
    DesignPreview {
        StatePage()
    }
}