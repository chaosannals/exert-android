package com.example.appshell.ui.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.LocalAppDatabase
import com.example.appshell.db.WebViewConf
import com.example.appshell.ui.sdp
import com.example.appshell.ui.shadow2
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.ScrollColumn
import com.example.appshell.ui.widget.form.Form
import com.example.appshell.ui.widget.form.FormTextInput
import com.example.appshell.db.ensureWebViewConf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ConfPage() {
    val db = LocalAppDatabase.current
    val coroutineScope = rememberCoroutineScope()

    var conf: WebViewConf? by remember {
        mutableStateOf(null)
    }

    ensureWebViewConf { conf = it }

    Log.d("conf-page", "load ${conf}")

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        ScrollColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            Form() {
                FormTextInput(
                    title = "初始URL",
                    isRequired = true,
                    isNullable = false,
                    value = conf?.startUrl,
                    onValueChange = {
                        conf?.startUrl = it ?: ""
                    }
                )
                FormTextInput(
                    title = "导出变量",
                    isRequired = true,
                    isNullable = false,
                    value = conf?.valName,
                    onValueChange = {
                        conf?.valName =  it ?: ""
                    }
                )
                FormTextInput(
                    title = "Token",
                    isRequired = true,
                    value = conf?.token,
                    onValueChange = {
                        conf?.token =  it
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow2(
                    color = Color.Black,
                    alpha = 0.4f,
                    cornersRadius = 0.sdp,
                    shadowBlurRadius = 0.4.sdp,
                    offsetY = (-0.4).sdp,
                )
                .background(Color.White)

                .padding(14.sdp, 4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Cyan, RoundedCornerShape(24.sdp))
                    .padding(14.sdp, 4.sdp)
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {
                            conf?.let {
                                val dao = db.webViewConfDao()
                                dao.save(it)
                            }
                        }
                    }
                ,
            ) {
                Text(
                    text = "保存",
                    color = Color.White,
                    fontSize = 24.ssp,
                )
            }
        }
    }
}

@Preview
@Composable
fun ConfPagePreview() {
    DesignPreview {
        ConfPage()
    }
}