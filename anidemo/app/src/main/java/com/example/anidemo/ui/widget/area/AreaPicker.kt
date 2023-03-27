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
    val lv1items: List<AreaItem> by remember {
        mutableStateOf(
            items.filter { it.parentId == 0 }
        )
    }
    var lv1: AreaItem? by remember {
        mutableStateOf(
            lv1items[0]
        )
    }
    var lv2items: List<AreaItem> by remember {
        mutableStateOf(
            items.filter { if (lv1 != null) it.parentId == lv1!!.id else false }
        )
    }
    var lv2: AreaItem? by remember {
        mutableStateOf(
            lv2items[0]
        )
    }
    var lv3items: List<AreaItem> by remember {
        mutableStateOf(
            items.filter { if (lv2 != null) it.parentId == lv2!!.id else false }
        )
    }
    var lv3: AreaItem? by remember {
        mutableStateOf(
            lv3items[0]
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
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
                items = lv1items,
                selected = lv1,
                onSelected = {
                    lv1 = it
                    lv2items = items.filter { if (lv1 != null) it.parentId == lv1!!.id else false }
                    lv2 = lv2items[0]
                    lv3items = items.filter { if (lv2 != null) it.parentId == lv2!!.id else false }
                    lv3 = lv3items[0]
                             },
                modifier = Modifier.weight(1f),
            )
            AreaColumn(
                items = lv2items,
                selected = lv2,
                onSelected = {
                    lv2 = it
                    lv3items = items.filter { if (lv2 != null) it.parentId == lv2!!.id else false }
                    lv3 = lv2items[0]
                             },
                modifier = Modifier.weight(1f),
            )
            AreaColumn(
                items = lv3items,
                selected = lv3,
                onSelected = { lv3 = it },
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