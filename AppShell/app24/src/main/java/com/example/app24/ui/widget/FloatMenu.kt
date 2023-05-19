package com.example.app24.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.example.app24.ScaffoldKit
import com.example.app24.ui.DesignPreview

@Composable
fun FloatMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit = {},
    modifier: Modifier=Modifier,
) {
    val isShowBottomBar by ScaffoldKit.isShowBottomBar.subscribeAsState(true)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
        ),
    ) {
        Column() {
            Text(
                text="底部栏 ${isShowBottomBar}",
                modifier = Modifier
                    .clickable {
                        ScaffoldKit.isShowBottomBar.onNext(!isShowBottomBar)
                    }
            )
            Text(text="aaaa")
            Text(text="aaaa")
            Text(text="aaaa")
        }
    }
}

@Preview
@Composable
fun FloatMenuPreview() {
    DesignPreview(
        modifier=Modifier.fillMaxSize()
    ) {
        Box() {
            FloatMenu(expanded = true)
        }
    }
}