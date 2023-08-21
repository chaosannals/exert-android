package com.example.hlitdemo.ui.page

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.codelab.android.datastore.UserPreferences
import com.codelab.android.datastore.UserPreferences.SortOrder
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

// UserPreferences 由 src/main/proto/user_prefs.proto 通过 protoc 生成源码
// 所以必须先构建一遍触发 protoc 生成源码才能找到类
object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer
)

@Composable
fun DataStoreProtoPage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var userPreferences: UserPreferences? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        context.userPreferencesStore.data.collect {
            userPreferences = it
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Button(onClick = {
            coroutineScope.launch {
                context.userPreferencesStore.updateData {
                    it.toBuilder().setShowCompleted(!it.showCompleted).build()
                }
            }
        }) {
            userPreferences?.run {
                Text("showCompleted: ${showCompleted}")
            }
        }
        Button(onClick = {
            coroutineScope.launch {
                context.userPreferencesStore.updateData {
                    it.toBuilder().setCounter(it.counter + 1).build()
                }
            }
        }) {
            userPreferences?.run {
                Text("counter: ${counter}")
            }
        }
        val sortOrderAll = remember {
            // UNRECOGNIZED 再 proto 没有定义，却出现在末尾，应该是 proto 默认行为。
            // 此值要排除掉，getNumber 是未定义的，调用即报错。
            SortOrder.values().filter { it.name != "UNRECOGNIZED" }
        }
        sortOrderAll.forEach { so ->
            Text(
                text = so.name,
                color = if(so.name == userPreferences?.sortOrder?.name) Color.Cyan else Color.Black,
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            context.userPreferencesStore.updateData {
                                it.toBuilder().setSortOrder(so).build()
                            }
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun DataStoreProtoPagePreview() {
    DataStoreProtoPage()
}