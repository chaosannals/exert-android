package cn.chaosannals.dirtool.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview

@Composable
fun DirtColumn(
    modifier : Modifier = Modifier,
    content: @Composable () -> Unit,
) {

}

@Preview
@Composable
fun DirtColumnPreview() {
    DirtPreview {
        DirtColumn {

        }
    }
}