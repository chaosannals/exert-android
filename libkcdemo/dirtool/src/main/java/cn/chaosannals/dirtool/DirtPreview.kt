package cn.chaosannals.dirtool

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

@Composable
fun DirtPreview(
    modifier : Modifier = Modifier,
    isLimit: Boolean = true,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController()
    ) {
        Box(
            modifier = modifier.run {
                if (isLimit) {
                    sizeIn(
                        minWidth = 0.dp,
                        maxWidth = Dirt.designWidthDp.value.sdp,
                        minHeight = 0.dp,
                        maxHeight = Dirt.designHeightDp.value.sdp,
                    )
                } else {
                    width(Dirt.designWidthDp.value.sdp)
                }
            },
        ) {
            content()
        }
    }
}