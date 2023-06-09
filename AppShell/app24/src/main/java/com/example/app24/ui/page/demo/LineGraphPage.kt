package com.example.app24.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.dt
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.widget.LineGraph
import com.example.app24.ui.widget.LineGraphPoint
import com.example.app24.ui.widget.LineGraphView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineGraphPage () {
    var text by remember {
        mutableStateOf("")
    }

    Column () {
        TextField(value = text, onValueChange = { text = it })
        LineGraphView(
            lines = listOf(
                LineGraph(
                    color = Color.Yellow,
                    points = listOf(
                        LineGraphPoint(10.1f, "2022-02-01 00:00:00".dt),
                        LineGraphPoint(354.5f, "2022-02-02 00:00:00".dt),
                        LineGraphPoint(256.6f, "2022-02-03 00:00:00".dt),
                        LineGraphPoint(50.5f, "2022-02-04 00:00:00".dt),
                        LineGraphPoint(205.4f, "2022-02-05 00:00:00".dt),
                        LineGraphPoint(401.3f, "2022-02-06 00:00:00".dt),
                        LineGraphPoint(411.3f, "2022-02-07 00:00:00".dt),
                        LineGraphPoint(491.3f, "2022-02-08 00:00:00".dt),
                        LineGraphPoint(205.4f, "2022-02-09 00:00:00".dt),
                        LineGraphPoint(401.3f, "2022-02-10 00:00:00".dt),
                        LineGraphPoint(411.3f, "2022-02-11 00:00:00".dt),
                        LineGraphPoint(491.3f, "2022-02-12 00:00:00".dt),
                    )
                ),
                LineGraph(
                    color = Color.Blue,
                    points = listOf(
                        LineGraphPoint(20.2f, "2022-02-01 00:00:00".dt),
                        LineGraphPoint(353.3f, "2022-02-02 00:00:00".dt),
                        LineGraphPoint(20.1f, "2022-02-03 00:00:00".dt),
                        LineGraphPoint(100.5f, "2022-02-04 00:00:00".dt),
                        LineGraphPoint(10.6f, "2022-02-05 00:00:00".dt),
                        LineGraphPoint(666.5f, "2022-02-06 00:00:00".dt),
                    )
                ),
            ),
        )
    }
}

@Preview
@Composable
fun LineGraphPagePreview() {
    DesignPreview {
        LineGraphPage()
    }
}