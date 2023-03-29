package com.example.appshell.ui.widget.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview

class FormContext(

) {

}

@Composable
fun rememberFormContext(): FormContext {
    val context by remember {
        mutableStateOf(FormContext())
    }
    return context
}

@Composable
fun Form(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        content()
    }
}

@Preview
@Composable
fun FormPreview() {
    DesignPreview {
        Form {
            FormTextInput(
                title="邮件",
                value="1111.bbbb",
                isRequired = true,
                onValidate = { validateFormTextInputEmail(it) },
            )
        }
    }
}

fun limitText(text: String?, maxLength: Int?): String? {
    return text?.let { t ->
        maxLength?.let { ml ->
            if (t.length > ml) t.substring(0, ml) else t
        } ?: t
    }
}

fun adaptText(
    text: String?,
    isTrim: Boolean,
    isNullable: Boolean,
    maxLength: Int?=null,
): String? {
    var v = if (isTrim) text?.trim() else text
    v = if (isNullable && (v != null && v.isEmpty())) null else v
    return limitText(v, maxLength)
}

fun validateFormText(
    text: String?,
    isRequired: Boolean,
    onValidate: ((String?) -> String?)? = null,
):String? {
    return if (isRequired && text == null) {
        "必填的"
    } else {
        onValidate?.invoke(text)
    }
}

fun validateFormTextInputEmail(
    text: String?
): String? {
    return text?.let {
        val m = """\w+@\w+\.(com|net|org)""".toRegex().matchEntire(it)
        if (m == null) "您输入格式错误，请重新输入" else null
    }
}