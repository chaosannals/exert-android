package com.example.app24.js

import android.webkit.JavascriptInterface
import kotlinx.serialization.Serializable

class JsSdk (
    val onSyncCall: (String, String) -> Unit,
    val onSyncFail: (String, String) -> Unit,
    val onAsyncCall: (String, String) -> Unit,
    val onAsyncFail: (String, String) -> Unit,
    val onResultCall: (String, String) -> Unit,
    val onResultFail: (String, String) -> Unit,
) {
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