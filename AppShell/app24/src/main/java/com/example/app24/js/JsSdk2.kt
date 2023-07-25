package com.example.app24.js

import android.webkit.JavascriptInterface

class JsSdk2(
    val onInit: () -> Unit,
) {
    @JavascriptInterface
    fun init() {
        onInit()
    }

    @JavascriptInterface
    fun getConfig(): String {
        return """
            {
                "aaa": 1234,
                "bbb": "123123"
            }
        """.trimIndent()
    }
}