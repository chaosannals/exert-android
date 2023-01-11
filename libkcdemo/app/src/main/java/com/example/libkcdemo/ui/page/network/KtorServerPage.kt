package com.example.libkcdemo.ui.page.network

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.ensurePermission
import com.example.libkcdemo.R
import com.example.libkcdemo.ui.DesignPreview
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.KeyStore

@Composable
fun KtorServerPage() {
    val context = LocalContext.current
    context.ensurePermission(Manifest.permission.INTERNET, 10)

    // Server
    LaunchedEffect(Unit) {
//        val keyStore = KeyStore.getInstance("BKS")
//        withContext(Dispatchers.IO) {
//            val f = context.resources.openRawResource(R.raw.demo) // res/raw/demo.jks
//            keyStore.load(f, "123456".toCharArray())
//        }
        val environment = applicationEngineEnvironment {
            connector {
                port = 8080
            }
            // 安卓上 javax.naming.ldap.LdapName 被剔除了。
//            sslConnector(
//                keyStore = keyStore,
//                keyAlias = "sampleAlias",
//                keyStorePassword = { "123456".toCharArray() },
//                privateKeyPassword = { "".toCharArray() },
//            ) {
//                port = 8443
//            }
            module {
                routing {
                    get("/") {
                        call.respondText { "Hello" }
                    }
                }
            }
        }
        embeddedServer(Jetty, environment).start(wait = false)
    }


    // Client
    var text by remember {
        mutableStateOf("Nothing")
    }
    val coroutineScop = rememberCoroutineScope()
    val client by remember {
        mutableStateOf(HttpClient())
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                coroutineScop.launch {
                    // 非 HTTPS 必须打开，正式使用 HTTPS 不用。
                    // android:usesCleartextTraffic="true"
                    val r = client.get("http://127.0.0.1:8080")
                    text = r.bodyAsText()
                }
                      },
        ) {
            Text(
                text="Send",
            )
        }

        Text(
            text=text,
        )
    }
}

@Preview
@Composable
fun KtorServerPagePreview() {
    DesignPreview {
        KtorServerPage()
    }
}