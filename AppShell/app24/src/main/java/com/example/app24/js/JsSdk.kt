package com.example.app24.js

import android.os.Parcelable
import android.util.Log
import android.webkit.JavascriptInterface
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class JsSdkObject(
    val keyA: String,
    val keyB: Boolean,
) : Parcelable

/// @JavascriptInterface 的返回值只能是基础类型 int String
///
class JsSdk (
    val onSyncCall: (String, String) -> Unit,
    val onSyncFail: (String, String) -> Unit,
    val onAsyncCall: (String, String) -> Unit,
    val onAsyncFail: (String, String) -> Unit,
    val onResultCall: (String, String) -> Unit,
    val onResultFail: (String, String) -> Unit,
) {
    // 不可返回 Map,前端拿到是空对象 {}
    @JavascriptInterface
    fun getMap() : Map<String, Boolean> {
        val result = mutableMapOf<String, Boolean>()
        for (c in 'a'..'z') {
            result.put("$c key", true)
        }
        Log.d("app24", "jssdk getMap $result")
        return result
    }

    // 不可返回自定义,前端拿到是空对象 {}
    @JavascriptInterface
    fun getObject(): JsSdkObject {
        return JsSdkObject(
            keyA = "12345",
            keyB = true,
        )
    }

    @JavascriptInterface
    fun getString(): String {
        return "string result"
    }

    @JavascriptInterface
    fun getInt(): Int {
        return 123456
    }

    @JavascriptInterface
    fun launchDispatch(uuid: String, name: String, param: String) {
        when(name) {
            "syncCall" -> onSyncCall(uuid, param)
            "asyncCall" -> onAsyncCall(uuid, param)
            "resultCall" -> onResultCall(uuid, param)
            "syncFail" -> onSyncFail(uuid, param)
            "asyncFail" -> onAsyncFail(uuid, param)
            "resultFail" -> onResultFail(uuid, param)
        }
    }
}