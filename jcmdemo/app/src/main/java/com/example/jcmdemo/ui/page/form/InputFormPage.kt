package com.example.jcmdemo.ui.page.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputFormPage() {
    // 实验性 API 而且 Popup 里面调用 show 没效果。
    val keyboard = LocalSoftwareKeyboardController.current

    var text by remember {
        mutableStateOf("")
    }
    Column (
        verticalArrangement=Arrangement.Top,
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it},
            cursorBrush = Brush.verticalGradient(
                0.00f to Color.Transparent,
                0.35f to Color.Transparent,
                0.35f to Color.Green,
                0.90f to Color.Green,
                0.90f to Color.Transparent,
                1.00f to Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                // 指定键盘类型，不是输入框本身限定符号。复制还是可以搞出奇怪的符号进来。
                keyboardType = KeyboardType.Number,
            )
        ) {
            if (text.isEmpty()) {
                Text(text="占位符")
            }
            it()
        }

        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                var isShowPopup by remember {
                    mutableStateOf(false)
                }

                Button(onClick = { isShowPopup=true }) {
                    Text("Popup")
                }

                if (isShowPopup) {
                    Popup(
                        alignment=Alignment.BottomCenter,
                        onDismissRequest={isShowPopup=false},
                    ) {
                        var popupText by remember {
                            mutableStateOf("")
                        }
                        val focusRequester = remember { FocusRequester() }

                        BasicTextField(
                            value = popupText,
                            onValueChange = {popupText=it},
                            modifier = Modifier
                                .focusRequester(focusRequester),
                        ) {
                            Column(
                                verticalArrangement=Arrangement.Top,
                                horizontalAlignment =Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Cyan),
                                ) {
                                    it()
                                }
                                Button(
                                    onClick = {
                                        keyboard?.show()
                                    },
                                ) {
                                    Text("keyboard: ${keyboard}")
                                }
                            }
                        }
                        LaunchedEffect(focusRequester) {
                            if (isShowPopup) {
                                focusRequester.requestFocus()
                                delay(100)
                                keyboard?.show()
                            }
                        }
                    }
                }
            }
            Box(
                modifier= Modifier
                    .fillMaxWidth()
                    .height(40.sdp)
                    .background(Color.Red),
            )
        }
    }
}

@Preview
@Composable
fun InputFormPagePreview() {
    DesignPreview() {
        InputFormPage()
    }
}