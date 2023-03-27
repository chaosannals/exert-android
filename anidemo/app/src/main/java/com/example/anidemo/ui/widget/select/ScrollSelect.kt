package com.example.anidemo.ui.widget.select

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.ui.DesignPreview
import com.example.anidemo.ui.sdp
import com.example.anidemo.ui.sf
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Composable
fun ScrollSelect(
    items: List<ScrollOption>,
    modifier: Modifier = Modifier,
    selected: ScrollOption? = null,
    onSelected: ((ScrollOption) -> Unit)? = null,
) {
    val selectedIndex = max(items.indexOf(selected), 0)
    val lineHeight = scrollOptionLineHeight.sf
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

    Box (
        modifier = modifier
            .fillMaxWidth()
            .height(400.sdp)
            .background(Color.White)
            .clip(RectangleShape)
    ) {
        Layout(
            modifier = Modifier
                .scrollable(
                    state = scrollState,
                    enabled = true,
                    orientation = Orientation.Vertical,
                ),
            content = {
                items.forEachIndexed { i, it ->
                    var color = Color(0xFFC1C1C1)
                    if (i == lineCurrent) {
                        color = Color(0xFF333333)
                    }
                    ScrollSelectRow(it.text, color)
                }
            }
        ) { ms, cs ->
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
}

@Preview
@Composable
fun ScrollSelectColumnPreview() {
    val items = defaultScrollOptions
    var selected: ScrollOption? by remember {
        mutableStateOf(null)
    }
    DesignPreview() {
        Box(
            modifier = Modifier
                .width(200.sdp)
                .height(600.sdp)
        ) {
            ScrollSelect(
                items = items,
                selected = selected,
                onSelected = { selected = it },
            )
        }
    }
}