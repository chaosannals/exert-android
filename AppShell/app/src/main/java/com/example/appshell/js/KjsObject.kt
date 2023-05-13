package com.example.appshell.js

import android.webkit.JavascriptInterface
import android.widget.Toast

class KjsObject(
    val OnToast: (String) -> Unit = {},
    val OnToggleTabbarVisible: ((Boolean) -> Unit)? = null,
    val OnInvalidToken: (() -> Unit)? = null,
) {
    // js 调用时 方法名 与 kotlin 大小写必须一致。
    @JavascriptInterface
    fun Say():String { return "Hello" }

    // 这种写法无效，只能暴露方法，get不行。
    @get:JavascriptInterface
    val aaa: String get() = run { "123456" }

    @JavascriptInterface
    fun Toast(text: String): Unit {
        OnToast(text)
    }

    @JavascriptInterface
    fun toggleTabbarVisibile(visible: Boolean) {
        OnToggleTabbarVisible?.invoke(visible)
    }

    @JavascriptInterface
    fun invalidToken() {
        OnInvalidToken?.invoke()
    }
}