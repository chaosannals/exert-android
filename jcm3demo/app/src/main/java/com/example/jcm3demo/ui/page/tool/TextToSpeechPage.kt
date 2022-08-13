package com.example.jcm3demo.ui.page.tool

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.util.*

class TextToSpeechHandler(
    val context: Context,
    val onInited: ((Boolean) -> Unit)? = null,
) : TextToSpeech.OnInitListener {
    var tts : TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        writeLog(context, "tts init s: ${status}")

        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.CHINESE)
            writeLog(context, "tts init r: ${result}")
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                onInited?.invoke(false)
            } else {
                onInited?.invoke(true)
            }
        } else {
            onInited?.invoke(false)
        }
    }

    fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun dispose() {
        tts.stop()
        tts.shutdown()
    }
}

@Composable
fun TextToSpeechPage() {
    val context = LocalContext.current
    var content by remember {
        mutableStateOf("")
    }
    var enabled by remember {
        mutableStateOf(false)
    }

    val tts by remember {
        mutableStateOf(
            TextToSpeechHandler(context, onInited = {enabled = it})
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            writeLog(context, "tts dispose.")
            tts.dispose()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = content,
            onValueChange = { content = it },
        )
        Button(
            enabled = enabled,
            onClick = {
                tts.speak(content)
            }
        ) {
            Text(text = "开始")
        }
    }
}

@Preview(widthDp = 375)
@Composable
fun TextToSpeechPagePreview() {
    TextToSpeechPage()
}