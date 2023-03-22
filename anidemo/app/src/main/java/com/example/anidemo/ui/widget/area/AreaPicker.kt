package com.example.anidemo.ui.widget.area

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.anidemo.ui.sdp

@Composable
fun AreaPicker(
    items: List<AreaItem>,
    modifier: Modifier = Modifier,
) {
    var lv1 by remember {
        mutableStateOf(
            AreaItem(
                id = 110000,
                parentId = 0,
                name="北京市",
            )
        )
    }
    var lv2 by remember {
        mutableStateOf(
            AreaItem(
                id = 110100,
                parentId = 110000,
                name="北京市",
            )
        )
    }
    var lv3 by remember {
        mutableStateOf(
            AreaItem(
                id = 110101,
                parentId = 110100,
                name="东城区",
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.sdp)
            .background(Color.White)
            .clip(RectangleShape)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 58.sdp)
                .fillMaxWidth()
                .height(29.sdp)
                .zIndex(1f)
                .background(
                    color=Color(0xFFEEF8FE),
                ),
        )
        Row(
            modifier = Modifier
                .padding(start = 111.sdp)
                .fillMaxHeight()
                .zIndex(10f),
        ) {
            AreaColumn(
                items = items.filter { it.parentId == 0 },
                selected = lv1,
                modifier = Modifier.weight(1f),
            )
            AreaColumn(
                items = items.filter { it.parentId == lv1.id },
                selected = lv2,
                modifier = Modifier.weight(1f),
            )
            AreaColumn(
                items = items.filter { it.parentId == lv2.id },
                selected = lv3,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
fun AreaPickerPreview() {
    AreaPicker(
        items= defaultAreaItems,
    )
}