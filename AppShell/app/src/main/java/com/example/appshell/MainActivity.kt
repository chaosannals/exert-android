package com.example.appshell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.appshell.TxIM.initTxIMSDK
import com.example.appshell.ui.MainBox
import com.example.appshell.ui.widget.form.FormContext
import com.example.appshell.ui.widget.initX5WebShell


val LocalFormContext = staticCompositionLocalOf<FormContext> {
    error("No Form context")
}

@Composable
fun rememberFormContext() : FormContext {
    val r by remember {
        mutableStateOf(FormContext())
    }
    return r
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化

        // 初始化 X5
        initX5WebShell()

        // 初始化 TxIM
        initTxIMSDK(1400805216)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()

        setContent {
            MainBox()
        }
    }
}