package com.example.appimop.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appimop.X5WebMultiViewKit.ensureWebView
import com.example.appimop.X5WebMultiViewKit.rememberWebView
import com.example.appimop.ui.DesignPreview
import com.tencent.smtt.sdk.WebView
import io.reactivex.rxjava3.subjects.BehaviorSubject

val isShowWebDialog: BehaviorSubject<Boolean> = BehaviorSubject.create()

@Composable
fun WebDialog() {
    val visible by isShowWebDialog.subscribeAsState(initial = true)

    if (visible) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            onDismissRequest = { isShowWebDialog.onNext(false) },
        ) {
            X5WebMultiViewBox(
                key = "web-dialog",
            )
        }
    }
}

@Preview
@Composable
fun WebDialogPreview() {
    DesignPreview {
        WebDialog()
    }
}