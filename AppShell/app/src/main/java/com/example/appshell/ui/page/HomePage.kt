package com.example.appshell.ui.page


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.db.WebViewConf
import com.example.appshell.db.ensureWebViewConf
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5WebShell


@Composable
fun HomePage() {
    val context = LocalContext.current
    var conf: WebViewConf? by remember {
        mutableStateOf(null)
    }

    ensureWebViewConf { conf = it }

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        conf?.let {
            X5WebShell(it.startUrl)
        }
    }
//    BackHandler(enabled = false) {
//        Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
//    }
}

@Preview
@Composable
fun HomePagePreview() {
    DesignPreview {
        HomePage()
    }
}