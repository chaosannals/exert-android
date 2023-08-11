package com.example.hlitdemo.ui.page

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.ProxyConfig
import androidx.webkit.ProxyController
import androidx.webkit.WebViewFeature

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RequiresFeature")
@Composable
fun XWebViewProxyPage() {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current
    val isSupportedProxy by remember(isInPreview) {
        derivedStateOf {
            if (isInPreview) {
                false
            } else {
                WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)
            }
        }
    }
    var isOpenProxy by remember {
        mutableStateOf(false)
    }
    var hasDirect by remember {
        mutableStateOf(false)
    }
    val (proxyRule, setProxyRule) = remember {
        mutableStateOf("")
    }
    val (bypassRule, setBypassRule) = remember {
        mutableStateOf("")
    }
    val proxyRules = remember {
        mutableStateListOf<String>()
    }
    val bypassRules = remember {
        mutableStateListOf<String>()
    }
    var isReverseBypass by remember {
        mutableStateOf(false)
    }
    val proxyConfig by remember(hasDirect, proxyRules, bypassRules) {
        derivedStateOf {
            if (isInPreview) {
                null
            } else {
                if (isSupportedProxy) {
                    val builder = ProxyConfig.Builder()
                    proxyRules.forEach {
                        builder.addProxyRule(it)
                    }
                    if (hasDirect) {
                        builder.addDirect()
                    }
                    bypassRules.forEach {
                        builder.addBypassRule(it)
                    }
                    if (isReverseBypass) {
                        builder.setReverseBypassEnabled(isReverseBypass)
                    }
                    builder.build()
                } else {
                    null
                }
            }
        }
    }

    val webView: WebView? by remember(context) {
        derivedStateOf {
            if (isInPreview) {
                null
            } else {
                WebView(context)
            }
        }
    }


    DisposableEffect(isOpenProxy) {
        proxyConfig?.let {
            ProxyController
                .getInstance()
                .setProxyOverride(it, {}, {})
        }
        onDispose {
            ProxyController
                .getInstance()
                .clearProxyOverride({}, {})
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("支持代理：$isSupportedProxy")
            Button(onClick = { hasDirect = !hasDirect }) {
                Text("开启直连：$hasDirect")
            }
            Button(onClick = { isOpenProxy = !isOpenProxy }) {
                Text("开启代理：$isOpenProxy")
            }
            Button(onClick = { isReverseBypass = !isReverseBypass }) {
                Text("旁路白名单：$isReverseBypass")
            }
            proxyRules.forEach {
                Text("代理规则：$it")
            }
            TextField(
                label = { Text("代理") },
                value = proxyRule,
                onValueChange = setProxyRule,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick =
                {
                    proxyRules.add(proxyRule)
                    setProxyRule("")
                },
            ) {
                Text("添加代理")
            }
            bypassRules.forEach {
                Text("旁路规则：$it")
            }
            TextField(
                label = { Text("旁路") },
                value = bypassRule,
                onValueChange = setBypassRule,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick =
                {
                    bypassRules.add(bypassRule)
                    setBypassRule("")
                },
            ) {
                Text("添加旁路")
            }
        }
        if (!isInPreview) {
            AndroidView(
                factory = {
                    webView!!
                },
            ) {

            }
        }
    }
}

@Preview
@Composable
fun XWebViewProxyPagePreview() {
    XWebViewProxyPage()
}