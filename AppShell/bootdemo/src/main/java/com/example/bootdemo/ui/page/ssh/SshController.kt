package com.example.bootdemo.ui.page.ssh

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.util.Properties
import java.util.concurrent.ConcurrentLinkedQueue

class SshController(

) {
    private val thread: MutableLiveData<Thread> = MutableLiveData()
    private val isThreadAlive: MutableLiveData<Boolean> = MutableLiveData(false)
    private val session: MutableLiveData<Session> = MutableLiveData()
    private val exceptions: ConcurrentLinkedQueue<Throwable> = ConcurrentLinkedQueue()

    @Composable
    fun isAliveAsState(): State<Boolean> {
        val isThreadAlive by isThreadAlive.observeAsState(false)
        val result = remember(isThreadAlive) {
            mutableStateOf(isThreadAlive)
        }
        return result
    }

    fun openSession(authInfo: SshAuthInfo) {
        val jsch = JSch()
        try {
            authInfo.privateKey?.let { key ->
                jsch.addIdentity(key)
            }
            val session = jsch.getSession(
                authInfo.host,
                authInfo.user,
                authInfo.port,
            )
            val properties = Properties()
            properties.setProperty("StrictHostKeyChecking", "no")
            session.setConfig(properties)
            session.connect()

            while (true) {
                try {
                    Thread.sleep(2000)
                    if (session.isConnected) {

                    } else {

                    }
                }
                catch (e: InterruptedException) {

                }
            }
        }
        catch (any: Throwable) {
            exceptions.add(any)
        }
    }
}