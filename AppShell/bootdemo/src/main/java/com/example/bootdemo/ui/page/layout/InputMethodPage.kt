package com.example.bootdemo.ui.page.layout

import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.bootdemo.inputMethodManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMethodPage() {
    val context = LocalContext.current
    val imm by inputMethodManager.subscribeAsState(initial = null)

    var isInputMethodActive by remember {
        mutableStateOf(false)
    }
    var isSoftInputVisible by remember {
        mutableStateOf(false)
    }
    var outHeight by remember {
        mutableStateOf(0)
    }
    var inHeight by remember {
        mutableStateOf(0)
    }
    var isInputVisible by remember {
        mutableStateOf(false)
    }
    var isPopupVisible by remember {
        mutableStateOf(false)
    }
    var isDialogVisible by remember {
        mutableStateOf(false)
    }

    if (isPopupVisible) {
        Popup(
            onDismissRequest = { isPopupVisible = false },
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    val x = 0
                    val y = anchorBounds.bottom
                    return IntOffset(x, y)
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .border(1.dp, Color.Black)
                    .background(Color.White),
            )
        }
    }
    
    if (isDialogVisible) {
        Dialog(
            onDismissRequest = { isDialogVisible = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .border(1.dp, Color.Black)
                    .background(Color.White),
            )
        }
    }

    DisposableEffect(Unit) {
        val listener = (context as? Activity)?.run {
            val result = ViewTreeObserver.OnPreDrawListener {
                outHeight = window.decorView.height
                val rect = Rect()
                window.decorView.getWindowVisibleDisplayFrame(rect)
                inHeight = rect.height()
                val percentHeight = inHeight.toFloat() / outHeight.toFloat()
                isInputVisible = percentHeight < 0.75f
                true
            }
            window.decorView.viewTreeObserver.addOnPreDrawListener(result)
            result
        }
        onDispose {
            listener?.let{
                (context as? Activity)?.window?.decorView?.viewTreeObserver?.removeOnPreDrawListener(it)
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            isInputMethodActive = imm?.isActive ?: false
            (context as? Activity)?.run {
                isSoftInputVisible = window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
            }
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text("input isActive: $isInputMethodActive")
        Text("window soft input visible: $isInputMethodActive")
        Text("window out Height: $outHeight")
        Text("window in Height: $inHeight")
        Text("input visible: $isInputVisible")
        Button(onClick = {
            (context as? Activity)?.run {
                imm?.showSoftInput(window.decorView, 0)
            }
        }) {
            Text("显示软键盘")
        }
        Button(onClick = {
            (context as? Activity)?.run {
                imm?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            }
        }) {
            Text("隐藏软键盘")
        }
        // 已废弃的方法
//        Button(onClick = {
//            (context as? Activity)?.run {
//                imm?.toggleSoftInput(..)
//            }
//        }) {
//            Text("切换软键盘")
//        }
        Button(onClick = { isPopupVisible = true }) {
            Text("Popup")
        }
        Button(onClick = {
            isDialogVisible = true
        }) {
            Text("Dialog")
        }

        var text by remember {
            mutableStateOf("")
        }
        TextField(
            value = text,
            onValueChange = {text = it}
        )
    }
}

@Preview
@Composable
fun InputMethodPagePreview() {
    InputMethodPage()
}