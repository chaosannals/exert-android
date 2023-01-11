package com.example.mockdemo

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class MockServerWorker(appContext: Context, workerParames: WorkerParameters)
    : CoroutineWorker(appContext, workerParames){
    override suspend fun doWork(): Result {
        embeddedServer(Jetty, port=18080) {
            routing {
                get("/") {
                    call.respondText { "Hello" }
                }
            }
        }.start(wait = false)
        return Result.success()
    }

}