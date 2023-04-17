package com.example.appshell

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*

object HttpApiClient {
    val cioClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {

            }
        }
    }

    val androidClient by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    suspend fun get(
        urlString: String,
        onExcept: ((Throwable) -> Unit)?=null,
    ) : HttpResponse? {
        try {
            val r = androidClient.get(
                urlString = urlString,
            ) {

            }
            return r
        } catch (e: Throwable) {
            onExcept?.invoke(e)
//            Toast.makeText(context, "throwable catch", Toast.LENGTH_SHORT).show()
        }
        return null
    }
}
