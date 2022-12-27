package com.example.jcmdemo.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import kotlin.math.max

@Composable
fun ReverseRow(
    modifier: Modifier = Modifier,
    verticalAlignment:  Alignment.Vertical = Alignment.CenterVertically,
    content: (@Composable () -> Unit),
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = verticalAlignment,
        modifier = modifier,
    ) {
        Layout(
            modifier = Modifier,
            content = content,
        ) { ms, cs ->
            val ps = mutableListOf<Placeable>()
            var maxH = 0
            var sumw = cs.maxWidth
            for (m in ms) {
                val c = cs.copy(maxWidth = sumw)
                val p = m.measure(c)
                sumw -= p.width
                maxH = max(p.height, maxH)
                ps.add(p)
            }
            layout(cs.maxWidth, maxH) {
                sumw = cs.maxWidth
                for (p in ps) {
                    val y = when(verticalAlignment) {
                        Alignment.CenterVertically -> (maxH - p.height) / 2
                        Alignment.Bottom -> maxH
                        else -> 0
                    }
                    sumw -= p.width
                    p.place(sumw, y)
                }
            }
        }
    }
}

@Preview
@Composable
fun ReverseRowPreview() {
    DesignPreview() {
        ReverseRow() {
            Text("AAA")
            Text("BBB")
            Text("CCC")
        }
    }
}