package com.example.bootdemo.ui.page.ssh

import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.util.Properties
import java.util.concurrent.ConcurrentLinkedQueue

class SshController(
    val alive: MutableLiveData<Boolean> = MutableLiveData(false),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : DisposableHandle {
    private val session: MutableLiveData<Session> = MutableLiveData()
    private val exceptions: ConcurrentLinkedQueue<Throwable> = ConcurrentLinkedQueue()

    fun openSession(authInfo: SshAuthInfo) {
        try {
            val jsch = JSch()
            authInfo.privateKey?.let { key ->
                jsch.addIdentity(key)
            }
            session.value = jsch.getSession(
                authInfo.host,
                authInfo.user,
                authInfo.port,
            ).apply {
                val properties = Properties()
                properties.setProperty("StrictHostKeyChecking", "no")
                setConfig(properties)
                connect()
                scope.launch(Dispatchers.IO) {
                    try {
                        alive.value = true
                        while (true) {
                            try {
                                Thread.sleep(2000)
                                if (session.value?.isConnected == true) {
                                    //
                                } else {
                                    break
                                }
                            } catch (e: InterruptedException) {}
                        }
                    }
                    finally {
                        alive.value = false
                    }
                }
            }
        }
        catch (any: Throwable) {
            exceptions.add(any)
        }
    }

    fun openConsole(
        controller: SshConsoleController?=null,
    ) {
        session.value?.run {
            val channel = openChannel("ConsoleChannel")
            channel.connect()

            // 读
            scope.launch(Dispatchers.IO) {
                BufferedReader(InputStreamReader(channel.inputStream)).use { input ->
                    while (true) {
                        val line = input.readLine()
                        controller?.run {
                            line?.let { onReadLine(it) }
                        }
                    }
                }
            }

            // 写
            val output = DataOutputStream(channel.outputStream)
            scope.launch(Dispatchers.IO) {
                controller?.run {
                    writer.collect {
                        withContext(Dispatchers.IO) {
                            output.writeBytes(it)
                            output.flush()
                        }
                    }
                }
            }
        }
    }

    override fun dispose() {
        scope.cancel()
    }
}

@Composable
fun rememberSshController(): SshController {
    val controller = remember {
        SshController()
    }
    DisposableEffect(controller) {
        onDispose {
            controller.dispose()
        }
    }
    return controller
}