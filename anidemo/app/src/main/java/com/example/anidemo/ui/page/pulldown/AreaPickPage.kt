package com.example.anidemo.ui.page.pulldown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.DesignPreview
import com.example.anidemo.ui.design
import com.example.anidemo.ui.sdp
import com.example.anidemo.ui.widget.area.AreaPicker
import com.example.anidemo.ui.widget.area.LoadAreaItems
import com.example.anidemo.ui.widget.select.ScrollSelect
import com.example.anidemo.ui.widget.select.defaultScrollOptions

@Composable
fun AreaPickPage() {
    Column() {
        ScrollSelect(items = defaultScrollOptions)
        Spacer(modifier = Modifier.height(10.sdp))
        AreaPicker(items = LoadAreaItems(LocalContext.current))
    }
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