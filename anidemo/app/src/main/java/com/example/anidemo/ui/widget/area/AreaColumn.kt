package com.example.anidemo.ui.widget.area

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.sdp
import com.example.anidemo.ui.sf
import com.example.anidemo.ui.ssp

@Composable
fun AreaRow(
    text: String,
    color: Color = Color(0xFFC1C1C1),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(29.sdp),
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 13.ssp,
            modifier = Modifier
                .padding(vertical = 5.sdp),
        )
    }
}

@Composable
fun AreaColumn(
    items: List<AreaItem>,
    modifier: Modifier = Modifier,
    selected: AreaItem? = null,
    onSelected: ((AreaItem) -> Unit)? = null,
) {
    var contentHeight by remember {
        mutableStateOf(0)
    }
    var contentSurplusHeight by remember {
        mutableStateOf(0)
    }
    var scrollAll by remember {
        val i = items.indexOf(selected)
        val start = if (i >= 0) -29f.sf * i else 0f
        mutableStateOf(start + 58f.sf)
    }
    var selectAnimating by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollableState {
        Log.d("areapicker", "scrolling: ${scrollAll}  ${58f.sf} ${contentHeight} ${contentSurplusHeight}")

        val start = 58f.sf
        //val end = if (contentSurplusHeight == 0) - 58f.sf else contentSurplusHeight + 58f.sf

        val sv = when {
            selectAnimating -> it
            scrollAll > start -> -(scrollAll - start + 1f)
            (contentSurplusHeight == 0 && scrollAll < 58f.sf) -> (scrollAll - 58f.sf) + 1f
            scrollAll < (contentSurplusHeight - 58f.sf) -> -(scrollAll - contentSurplusHeight + 58f.sf) + 1f
            else -> it
        }

        scrollAll += sv
        sv
    }

    Layout(
        modifier = modifier
            .scrollable(
                state = scrollState,
                enabled = true,
                orientation = Orientation.Vertical,
            ),
        content = {
            items.forEach {
                var color = Color(0xFFC1C1C1)
                selected?.let {s ->
                    if (it.id == s.id) {
                        color = Color(0xFF333333)
                    }
                }
                AreaRow(it.name, color)
            }
        }
    ) {ms, cs ->
        val sai = scrollAll.toInt()
        layout(cs.maxWidth, cs.maxHeight) {
            var heightSum = 0
            for (m in ms) {
                val p = m.measure(cs)
                val x = 0
                val y = heightSum + sai
                heightSum += p.height
                p.place(x, y)
            }

            contentHeight = heightSum
            contentSurplusHeight = if (cs.maxHeight > contentHeight) 0 else cs.maxHeight - contentHeight
        }
    }
}

@Preview
@Composable
fun AreaColumnPreview() {
    // val areaItems = LoadAreaItems(LocalContext.current)
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
    Row (
        modifier= Modifier
            .fillMaxWidth()
            .height(150.sdp),
    ) {
        AreaColumn(
            items = defaultAreaItems.filter { it.parentId == 0 },
            selected = lv1,
            modifier = Modifier.weight(1f),
        )
        AreaColumn(
            items = defaultAreaItems.filter { it.parentId == lv1.id },
            selected = lv2,
            modifier = Modifier.weight(1f),
        )
        AreaColumn(
            items = defaultAreaItems.filter { it.parentId == lv2.id },
            selected = lv3,
            modifier = Modifier.weight(1f),
        )
    }
}