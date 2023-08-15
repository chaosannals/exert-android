package com.example.bootdemo.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bootdemo.StringOrContent

@Composable
fun ConfirmDialog(
    visible: Boolean,
    title: StringOrContent,
    onConfirm: (Boolean) -> Unit,
) {
    if (visible) {
        Dialog(
            onDismissRequest = { onConfirm(false) },
            ) {
            Column (
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(Color.White, RoundedCornerShape(14.dp))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .heightIn(min = 100.dp),
                    ) {
                    when (title) {
                        is StringOrContent.StringValue -> {
                            Text(text = title.value)
                        }

                        is StringOrContent.ContentValue -> {
                            title.value.invoke()
                        }
                    }
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color.Gray,
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                            )
                        }
                ){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onConfirm(false) },
                        ) {
                        Text(text = "取消")
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onConfirm(true) },
                    ) {
                        Text(text = "确定")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialog(
        visible = true,
        title = StringOrContent.StringValue("提示框内容"),
        onConfirm = {},
    )
}