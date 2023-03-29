package com.example.appshell.js

import android.webkit.JavascriptInterface

class KjsObject {
    @JavascriptInterface
    fun say():String { return "Hello" }
}