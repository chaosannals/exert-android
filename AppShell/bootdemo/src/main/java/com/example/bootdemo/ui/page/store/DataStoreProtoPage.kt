package com.example.bootdemo.ui.page.store

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore

import com.example.application.Settings
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

// 这东西明明是个定式,居然不提供默认实现.
object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings
        get() = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}

private val Context.settingsStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer,
)

@Composable
fun DataStoreProtoPage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var settings: Settings? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        context.settingsStore.data.collect {
            settings = it
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    context.settingsStore.updateData {
                        it.toBuilder().setExampleCounter(it.exampleCounter + 1).build()
                    }
                }
            }
        ) {
            settings?.run {
                Text("${exampleCounter}")
            }
        }
    }
}

@Preview
@Composable
fun DataStoreProtoPagePreview() {
    DataStoreProtoPage()
}