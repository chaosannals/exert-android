package cn.chaosannals.dirtool.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview

@Composable
fun DirtTextInput(
    value: String?=null,
    onValueChange: ((String?) -> Unit)? = null,
) {
    var text by remember {
        mutableStateOf(value)
    }

    BasicTextField(
        value = text ?: "",
        onValueChange = {
            text = it.ifEmpty { null }
            onValueChange?.invoke(text)
        }
    ) {
        Box() {
            it()
        }
    }
}

@Preview
@Composable
fun DirtTextInputPreview() {
    DirtPreview {
        DirtForm {
            DirtTextInput()
        }
    }
}