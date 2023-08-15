package com.example.bootdemo.ui.widget

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.example.bootdemo.StringOrContent

val finishDialogVisible: MutableLiveData<Boolean> = MutableLiveData()

@Composable
fun FinishDialog() {
    val context = LocalContext.current
    val visible by finishDialogVisible.observeAsState(initial = false)

    ConfirmDialog(
        visible = visible,
        title = StringOrContent.StringValue("是否退出？"),
    ) {
        if (it) {
            (context as Activity).finish()
        }
        finishDialogVisible.value = false
    }
}

@Preview
@Composable
fun FinishDialogPreview() {
    FinishDialog()
}