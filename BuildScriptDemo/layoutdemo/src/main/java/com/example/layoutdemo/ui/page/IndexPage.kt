package com.example.layoutdemo.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.layoutdemo.ui.widget.RouteButton
import com.example.layoutdemo.ui.widget.RouteButtonConf

@Composable
fun IndexPage() {
    val routes = remember {
        mutableStateListOf(
            RouteButtonConf(text="Drawer Button", route="drawer-button")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(routes) {_, it ->
            RouteButton(it)
        }
    }
}

@Preview
@Composable
fun IndexPagePreview() {
    IndexPage()
}