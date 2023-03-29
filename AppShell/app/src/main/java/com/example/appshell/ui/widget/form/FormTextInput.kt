package com.example.appshell.ui.widget.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.sdp
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun FormTextInput(
    title: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    maxLength: Int? = null,
    onValueChange: ((String?) -> Unit)? = null,
    onValidate: ((String?) -> String?)? = null,
    isTrim: Boolean = true,
    isNullable: Boolean = true,
    isRequired: Boolean = false,
    placeholder: String = "请输入",
) {
    var text: String? by remember {
        mutableStateOf(adaptText(value, isTrim, isNullable, maxLength))
    }
    var validateResult: String? by remember {
        mutableStateOf(validateFormText(
            text,
            isRequired,
            onValidate,
        ))
    }

    LaunchedEffect(value) {
        text = adaptText(value, isTrim, isNullable, maxLength)
    }

    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(10.sdp, 5.sdp)
                .fillMaxWidth()
                .height(50.sdp)
                .background(Color.White, RoundedCornerShape(5.sdp))
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(127.5.sdp),
            ) {
                if (isRequired) {
                    Text(
                        text = "*",
                        color = Color(0xFFEB3C3C),
                        fontSize = 15.ssp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 10.sdp)
                    )
                }
                Text(
                    text = title,
                    color = Color(0xFF4F4F4F),
                    fontSize = 15.ssp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 21.5.sdp),
                )
            }
            BasicTextField(
                value = text ?: "",
                onValueChange = {
                    text = adaptText(it, isTrim, isNullable, maxLength)
                    validateResult = validateFormText(
                        text,
                        isRequired,
                        onValidate,
                    )
                    onValueChange?.invoke(text)
                },
                singleLine = false,
                textStyle = TextStyle(
                    color = Color(0xFF999999),
                    fontSize = 15.ssp,
                ),
                modifier = Modifier
                    .padding(end = 15.sdp)
                    .weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    if (text == null || text!!.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFFD2D2D2),
                            fontSize = 15.ssp,
                        )
                    }
                    it()
                }
            }
        }

        validateResult?.let {
            Text(
                text=it,
                color= Color(0xFFFF6565),
                fontSize=12.ssp,
                modifier= Modifier
                    .padding(start = 10.sdp, end=10.sdp, top=2.sdp, bottom=5.sdp)
            )
        }
    }
}

@Preview
@Composable
fun FormTextInputPreview() {
    var text: String? by remember {
        mutableStateOf("1458752654@qq.com")
    }

    DesignPreview() {
        Form {
            FormTextInput(
                title="邮件",
                value=text,
                onValueChange = { text = it},
                onValidate = { validateFormTextInputEmail(it) },
            )
            FormTextInput(
                title="邮件",
                value="",
                onValidate = { validateFormTextInputEmail(it) },
            )
            FormTextInput(
                title="邮件",
                value="",
                isRequired = true,
                onValidate = { validateFormTextInputEmail(it) },
            )
            FormTextInput(
                title="邮件",
                value="",
                isNullable = false,
                isRequired = true,
                onValidate = { validateFormTextInputEmail(it) },
            )

            FormTextInput(
                title="邮件",
                value="1111.bbbb",
                isRequired = true,
                onValidate = { validateFormTextInputEmail(it) },
            )
        }
    }
}