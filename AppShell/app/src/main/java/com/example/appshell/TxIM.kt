package com.example.appshell

import android.content.Context
import android.util.Log
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMImageElem
import com.tencent.imsdk.v2.V2TIMLogListener
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMSDKConfig
import com.tencent.imsdk.v2.V2TIMSDKListener
import com.tencent.imsdk.v2.V2TIMSendCallback
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import com.tencent.imsdk.v2.V2TIMValueCallback
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

data class TxIMLog (
    val logLevel: Int,
    val logContent: String?,
)

object TxIM {
    val logQueue by lazy {
        PublishSubject.create<TxIMLog>()
    }

    fun PublishSubject<TxIMLog>.log(logContent: String?, logLevel: Int=0) {
        onNext(TxIMLog(logLevel, logContent))
    }

    fun Context.initTxIMSDK(appId: Int) {
        val config = V2TIMSDKConfig()
        config.logLevel = V2TIMSDKConfig.V2TIM_LOG_INFO
        config.logListener = object : V2TIMLogListener() {
            override fun onLog(logLevel: Int, logContent: String?) {
                // 日志太多了。
//                logQueue.onNext(TxIMLog(logLevel, logContent))
            }
        }

        val listener = object: V2TIMSDKListener() {
            override fun onConnecting() {
                logQueue.log("开始连接", 999)
            }

            override fun onConnectSuccess() {
                logQueue.log( "连接成功", 998)
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

    fun login(userId: String, userSignature: String, onSuccess: (() -> Unit)?=null) {
        V2TIMManager.getInstance().login(
            userId,
            userSignature,
            object : V2TIMCallback {
                override fun onSuccess() {
                    logQueue.log("登录成功")
                    onSuccess?.invoke()
                }

                override fun onError(p0: Int, p1: String?) {
                    logQueue.log("登录失败 ${p0}: ${p1}", 444)
                }
            }
        )
    }

    val loginUser: String? get() = run {
        V2TIMManager.getInstance().loginUser
    }

    val loginStatus: Int get() = run {
        V2TIMManager.getInstance().loginStatus
    }

    fun logout(onSuccess: (() -> Unit)?=null) {
        V2TIMManager.getInstance().logout(object: V2TIMCallback {
            override fun onSuccess() {
                logQueue.log("注销成功")
                onSuccess?.invoke()
            }

            override fun onError(p0: Int, p1: String?) {
                logQueue.log("注销失败 ${p0}: ${p1}", 444)
            }
        })
    }

    fun sendC2C(
        content: String,
        toUserId: String,
        onSuccess: ((V2TIMMessage) -> Unit)?=null,
    ) {
        V2TIMManager.getInstance().sendC2CTextMessage(
            content,
            toUserId,
            object : V2TIMValueCallback<V2TIMMessage> {
                override fun onSuccess(message: V2TIMMessage) {
                    logQueue.log(message.sender)
                    onSuccess?.invoke(message)
                }

                override fun onError(p0: Int, p1: String?) {
                    logQueue.log("发送失败：${p0}: ${p1}", 444)
                }
            },
        )
    }

    fun sendC2C(
        content: V2TIMMessage,
        toUserId: String,
        onSuccess: (V2TIMMessage) -> Unit= { },
    ) {
        V2TIMManager.getMessageManager().sendMessage(
            content,
            toUserId,
            null,
            V2TIMMessage.V2TIM_PRIORITY_NORMAL,
            false,
            null,
            object : V2TIMSendCallback<V2TIMMessage> {
                override fun onSuccess(p0: V2TIMMessage?) {
                    logQueue.log("发送成功：${p0?.sender}")
                    p0?.let { onSuccess(it) }
                }

                override fun onError(p0: Int, p1: String?) {
                    logQueue.log("发送失败：${p0} ${p1}", 444)
                }

                override fun onProgress(p0: Int) {
                    logQueue.log("发送进度：$p0")
                }

            }
        )
    }

    fun V2TIMMessage.send(
        toUserId: String,
        onSuccess: (V2TIMMessage) -> Unit={},
    ) {
        sendC2C(this, toUserId, onSuccess)
    }

    fun newText(content: String): V2TIMMessage {
        return V2TIMManager
            .getMessageManager()
            .createTextMessage(content).apply {
                isNeedReadReceipt = true
            }
    }

    fun newImage(path: String): V2TIMMessage {
        Log.d("tx-im", "path: $path")
        return V2TIMManager
            .getMessageManager()
            .createImageMessage(path).apply {
                isNeedReadReceipt = true
            }
    }

    inline fun <reified T> newCustomJson(content: T): V2TIMMessage {
        val bytes = Json.encodeToString(content).toByteArray()
        return V2TIMManager
            .getMessageManager()
            .createCustomMessage(bytes)
            .apply {
                isNeedReadReceipt = true
            }
    }

    fun newSound(path: String, duration: Int): V2TIMMessage {
        return V2TIMManager
            .getMessageManager()
            .createSoundMessage(path, duration)
            .apply {
                isNeedReadReceipt = true
            }
    }

    fun newVideo(
        path: String,
        type: String,
        duration: Int,
        snapshotPath: String,
    ) : V2TIMMessage {
        return V2TIMManager
            .getMessageManager()
            .createVideoMessage(path, type, duration, snapshotPath)
            .apply {
                isNeedReadReceipt = true
            }
    }

    fun newFile(
        path: String,
        name: String,
    ) : V2TIMMessage {
        return V2TIMManager
            .getMessageManager()
            .createFileMessage(path, name)
            .apply {
                isNeedReadReceipt = true
            }
    }

    fun newLocation(
        info: String,
        latitude: Double,
        longitude: Double,
    ) : V2TIMMessage {
        return V2TIMManager
            .getMessageManager()
            .createLocationMessage(info, latitude, longitude)
            .apply {
                isNeedReadReceipt = true
            }
    }

    fun newFace(
        code: Int,
        data: ByteArray,
    ) : V2TIMMessage {
        return V2TIMManager
            .getMessageManager()
            .createFaceMessage(code, data)
            .apply {
                isNeedReadReceipt = true
            }
    }

    fun listen(listener: V2TIMAdvancedMsgListener) {
        V2TIMManager
            .getMessageManager()
            .addAdvancedMsgListener(listener)
    }

    fun remove(listener: V2TIMAdvancedMsgListener) {
        V2TIMManager
            .getMessageManager()
            .removeAdvancedMsgListener(listener)
    }
}