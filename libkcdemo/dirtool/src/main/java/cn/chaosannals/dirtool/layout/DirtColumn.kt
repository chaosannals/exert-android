package cn.chaosannals.dirtool.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview

@Composable
fun DirtColumn(
    modifier : Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = Modifier,
    ) { ms, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {

        }
    }
}

@Preview
@Composable
fun DirtColumnPreview() {
    DirtPreview {
        DirtColumn {

        }
    }
}