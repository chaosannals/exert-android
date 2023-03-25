package com.example.anidemo.ui.page.pulldown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.DesignPreview
import com.example.anidemo.ui.design
import com.example.anidemo.ui.widget.area.AreaPicker
import com.example.anidemo.ui.widget.area.LoadAreaItems

@Composable
fun AreaPickPage() {
    AreaPicker(items = LoadAreaItems(LocalContext.current))
}

@Preview
@Composable
fun AreaPickPagePreview() {
    DesignPreview(
        modifier= Modifier.design(),
    ) {
        AreaPickPage()
    }
}