package com.example.layoutdemo.ui.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 1.5.0 之后的修改器定义方式，性能更好。

// 定义节点，就是利用这个节点替代 composed 的 remember 提高性能
class My150CustomModifierNode(
    var color: Color, // 必须是可变的 var
): Modifier.Node(), DrawModifierNode {
    override fun ContentDrawScope.draw() {
        drawRect(color=color)
        drawContent()
    }
}

private class My150CustomElement(
    private val color: Color,
    private val inspectorInfo: InspectorInfo.() -> Unit
) : ModifierNodeElement<My150CustomModifierNode>() {
    override fun create(): My150CustomModifierNode = My150CustomModifierNode(color)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is My150CustomElement) return false
        if (other.color != color) return false
        return true
    }

    override fun hashCode(): Int = color.hashCode()

    override fun update(node: My150CustomModifierNode) {
        node.color = color
    }

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }
}

fun Modifier.my150Custom(
    color: Color,
) = this then My150CustomElement(color, debugInspectorInfo {
    name = "my150Custom"
    value = color
})

@Composable
fun Modifier150Page() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .my150Custom(Color.Green)
        ) {
            Text(text = "12345676853245342fdggsdfgsfdg")
        }
    }
}

@Preview
@Composable
fun Modifier150PagePreview() {
    Modifier150Page()
}