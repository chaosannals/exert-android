package com.example.appshell.ui.page.demo

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.HttpApiClient
import com.example.appshell.ui.widget.DesignPreview
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(InternalAPI::class)
@Composable
fun HttpClientPage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var url by remember {
        mutableStateOf("")
    }
    var httpContent by remember {
        mutableStateOf("")
    }
    var httpError by remember {
        mutableStateOf("")
    }

    var toastTip by remember {
        mutableStateOf("")
    }
    var statusCode by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(toastTip) {
        if (toastTip.isNotEmpty()) {
            Toast.makeText(context, toastTip, Toast.LENGTH_SHORT).show()
            toastTip = ""
        }
    }

    val defaultUrls = listOf(
        "https://baidu.com",
        "http://127.0.0.1:8080",
        "https://127.0.0.1:8080",
        "http://127.0.0.1:12345",
    )

    Column (
        verticalArrangement= Arrangement.Center,
        horizontalAlignment= Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn() {
            itemsIndexed(defaultUrls) {_, it ->
                Text(
                    text = it,
                    modifier = Modifier
                        .clickable { url = it },
                )
            }
        }
        OutlinedTextField(
            value = url,
            onValueChange = {url = it},
            label = {
                Text(text="url")
            }
        )
        Text(text = "code: ${statusCode}")
        Button(
            onClick =
            {
                coroutineScope.launch(Dispatchers.IO) {
                    httpError = ""
                    val r = HttpApiClient.get(
                        urlString = url,
                        onExcept =
                        {
                            val et = it.javaClass.name
                            httpError = "${et}: ${it.message} ${it.stackTrace}"
                            toastTip = httpError
                        },
                    )
                    r?.run {
                        statusCode = status.value
                        if (status.value in 200..299) {

                        }

                        content.read {
                            httpContent = it.decodeString()
                            toastTip = httpContent
                        }
                    }
                }
            },
        ) {
            Text(text = "发送")
        }

        TextField(
            label = {
                    Text(text = "内容")
            },
            value = httpContent,
            onValueChange =
            {
                httpContent = it
            },
            minLines = 4,
            maxLines = 10,
        )

        TextField(
            label = {
                Text(text = "异常")
            },
            value = httpError,
            onValueChange =
            {
                httpError = it
            },
            minLines = 4,
            maxLines = 10,
        )
    }
}

@Preview
@Composable
fun HttpClientPagePreview() {
    DesignPreview {
        HttpClientPage()
    }
}