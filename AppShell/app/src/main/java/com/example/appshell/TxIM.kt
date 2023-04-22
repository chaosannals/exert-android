package com.example.appshell

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import com.tencent.imsdk.v2.V2TIMLogListener
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMSDKConfig
import com.tencent.imsdk.v2.V2TIMSDKListener
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import io.reactivex.rxjava3.subjects.PublishSubject

data class TxIMLog (
    val logLevel: Int,
    val logContent: String?,
)

object TxIM {
    val logQueue by lazy {
        PublishSubject.create<TxIMLog>()
    }

    fun Context.initTxIMSDK(appId: Int) {
        val config = V2TIMSDKConfig()
        config.logLevel = V2TIMSDKConfig.V2TIM_LOG_INFO
        config.logListener = object : V2TIMLogListener() {
            override fun onLog(logLevel: Int, logContent: String?) {
                logQueue.onNext(TxIMLog(logLevel, logContent))
            }
        }

        val listener = object: V2TIMSDKListener() {
            override fun onConnecting() {
                logQueue.onNext(TxIMLog(999, "开始连接"))
            }

            override fun onConnectSuccess() {
                logQueue.onNext(TxIMLog(998, "连接成功"))
            }

            override fun onConnectFailed(code: Int, error: String?) {
                logQueue.onNext(TxIMLog(997, "连接失败"))
            }

            override fun onKickedOffline() {
                logQueue.onNext(TxIMLog(989, "当前用户被踢下线"))
            }

            override fun onUserSigExpired() {
                logQueue.onNext(TxIMLog(988, "登录票据已经过期"))
            }

            override fun onSelfInfoUpdated(info: V2TIMUserFullInfo?) {
                logQueue.onNext(TxIMLog(987, "当前用户的资料发生了更新 ${info?.selfSignature}"))
            }
        }
        V2TIMManager.getInstance().addIMSDKListener(listener)
        V2TIMManager.getInstance().initSDK(this, appId, config)
    }

    fun unInitTxIMSDK() {
        V2TIMManager.getInstance().unInitSDK()
    }
}