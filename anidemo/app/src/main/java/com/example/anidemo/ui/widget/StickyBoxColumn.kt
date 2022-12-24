package com.example.anidemo.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

interface StickyBoxColumnScope {
    @Stable
    fun Modifier.sticky(): Modifier
}

internal class LayoutStickyImpl() : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return "sticky"
    }
}

@Composable
fun StickyBoxColumn(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier,
    content: @Composable  StickyBoxColumnScope.() -> Unit,
) {
    val scope = object: StickyBoxColumnScope {
        override fun Modifier.sticky(): Modifier {
            return this.then(LayoutStickyImpl())
        }
    }

    var contentHeight by remember {
        mutableStateOf(0)
    }
    var contentSurplusHeight by remember {
        mutableStateOf(0)
    }
    var scrollAll by remember {
        mutableStateOf(0f)
    }
    val scrollState = rememberScrollableState {
        val nowSv = scrollAll + it
        val sv = when {
            nowSv > 0 -> - scrollAll
            nowSv < contentSurplusHeight -> contentSurplusHeight - scrollAll
            else -> it
        }
        scrollAll += sv
        sv
    }

    Layout(
        modifier = modifier
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical,
            ),
        content = { scope.content() },
    ) { ms, cs ->
        val c = cs.copy(maxHeight = Int.MAX_VALUE)
        val ps = ms.map { it.measure(c) }
        contentHeight = ps.map { it.height }.reduce{ a, b -> a + b }
        contentSurplusHeight = if (cs.maxHeight > contentHeight) 0 else cs.maxHeight - contentHeight

        layout(cs.maxWidth, cs.maxHeight) {
            var sumh = 0
            val sa = scrollAll.toInt()
            for (p in ps) {
                val x = when (horizontalAlignment) {
                    Alignment.CenterHorizontally -> (cs.maxWidth - p.width) / 2
                    else -> 0
                }
                val z = when (p.parentData) {
                    "sticky" -> 1f
                    else -> 0f
                }
                val y = when (p.parentData) {
                    "sticky" -> if (sa > -sumh) sa + sumh else 0
                    else -> sa + sumh
                }
                sumh += p.height
                p.place(x, y, z)
            }
        }
    }
}

@Preview(widthDp = 375, heightDp = 667)
@Composable
fun StickyBoxColumnPreview() {
    var boxCount by remember {
        mutableStateOf(10)
    }
    Box() {
        StickyBoxColumn() {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.Red)
            ) {

            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .sticky()
                    .border(0.5.dp, Color.Green)
            ) {
                Text(
                    text="添加一个",
                    modifier = Modifier
                        .clickable {
                            boxCount += if (boxCount > 20) 0 else 1
                        }
                        .weight(1f, fill = false)
                )
                Text(
                    text="减少一个",
                    modifier = Modifier
                        .clickable {
                            boxCount -= if (boxCount <= 0) 0 else 1
                        }
                        .weight(1f, fill = false)
                )
            }
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                for (i in 0 until boxCount) {
                    val w = (100 + (i % 2) * 100).dp
                    Box(
                        modifier = Modifier
                            .size(w, 200.dp)
                            .background(Color.Cyan),
                    )
                }
            }
        }
    }
}