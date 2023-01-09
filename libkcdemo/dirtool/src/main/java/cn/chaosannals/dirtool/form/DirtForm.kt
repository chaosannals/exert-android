package cn.chaosannals.dirtool.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview

class DirtFormContext () {

}

val LocalDirtFormContext = staticCompositionLocalOf<DirtFormContext> {
    error("no dirtool form context")
}

@Composable
fun rememberDirtFormContext(): DirtFormContext {
    return DirtFormContext()
}

@Composable
fun DirtForm(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDirtFormContext provides rememberDirtFormContext(),
    ) {
        content()
    }
}

@Preview
@Composable
fun DirtFormPreview() {
    DirtPreview() {
        DirtForm {
            DirtTextInput()
        }
    }
}