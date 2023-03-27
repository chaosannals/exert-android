package com.example.anidemo.ui.widget.area

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.sdp
import com.example.anidemo.ui.sf
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Composable
fun AreaColumn(
    items: List<AreaItem>,
    modifier: Modifier = Modifier,
    selected: AreaItem? = null,
    onSelected: ((AreaItem) -> Unit)? = null,
) {
    val selectedIndex = max(items.indexOf(selected), 0)
    val lineHeight = 29f.sf
    val startIndent = lineHeight * 2

    var lineCurrent by remember {
        mutableStateOf(selectedIndex)
    }

    var contentHeight by remember {
        mutableStateOf(0)
    }
    var scrollBottom by remember {
        mutableStateOf(0)
    }

    var scrollAll by remember {
        val start = if (selectedIndex >= 0) -lineHeight * selectedIndex else 0f
        mutableStateOf(start + startIndent)
    }
    val scrollState = rememberScrollableState {
        //Log.d("areapicker", "scrolling: ${scrollAll}  ${startIndent} ${contentHeight} ${scrollBottom}")

        val sv = when {
            scrollAll > startIndent -> -(scrollAll - startIndent + 1f)
            scrollAll < scrollBottom -> -(scrollAll - scrollBottom) + 1f
            else -> it
        }

        scrollAll += sv
        lineCurrent = min(items.size - 1, floor(-(scrollAll - startIndent - 0.5 * lineHeight) / lineHeight).toInt())
        Log.d("areapicker", "line: ${lineCurrent} scrolling: ${scrollAll}  ${startIndent} ${contentHeight} ${scrollBottom}")
        sv
    }

    LaunchedEffect(selectedIndex) {
        Log.d("areapicker", "launched line: ${selectedIndex}")
        lineCurrent = selectedIndex
        scrollAll = startIndent - (lineCurrent * lineHeight)
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (!scrollState.isScrollInProgress) {
            scrollAll = startIndent - (lineCurrent * lineHeight)

            if (selectedIndex != lineCurrent) {
                val index = max(min(lineCurrent, items.size - 1), 0)
                onSelected?.invoke(items[index])
            }
        }
    }

    Layout(
        modifier = modifier
            .scrollable(
                state = scrollState,
                enabled = true,
                orientation = Orientation.Vertical,
            ),
        content = {
            items.forEachIndexed { i, it ->
                var color = Color(0xFFC1C1C1)
                selected?.let {s ->
                    if (i == lineCurrent) {
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
            scrollBottom = - contentHeight + startIndent.toInt() + lineHeight.toInt()
            Log.d("areapicker", "maxHeight: ${cs.maxHeight}")
        }
    }
}

@Preview
@Composable
fun AreaColumnPreview() {
     val areaItems = LoadAreaItems(LocalContext.current)
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
            items = areaItems.filter { it.parentId == 0 },
            selected = lv1,
            modifier = Modifier.weight(1f),
        )
        AreaColumn(
            items = areaItems.filter { it.parentId == lv1.id },
            selected = lv2,
            modifier = Modifier.weight(1f),
        )
        AreaColumn(
            items = areaItems.filter { it.parentId == lv2.id },
            selected = lv3,
            modifier = Modifier.weight(1f),
        )
    }
}