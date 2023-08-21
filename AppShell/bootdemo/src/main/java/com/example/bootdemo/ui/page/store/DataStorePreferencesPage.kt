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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val EXAMPLE_COUNTER = intPreferencesKey("example_counter")

// 预览（Preview）没有效果，原因非常简单，预览无法预览带 网络 IO 的操作。而这个存储属于 IO 操作。
@Composable
fun DataStorePreferencesPage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val exampleCounter = remember {
        context.dataStore.data.map { 
            it[EXAMPLE_COUNTER] ?: 0
        }
    }
    var counter by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(exampleCounter) {
        exampleCounter.collect {
            counter = it
        }
    }
    
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    context.dataStore.edit {
                        val now = it[EXAMPLE_COUNTER] ?: 0
                        it[EXAMPLE_COUNTER] = now + 1
                    }
                }
            }
        ) {
            Text(text = "${counter}")
        }
    }
}

@Preview
@Composable
fun DataStorePreferencesPagePreview() {
    DataStorePreferencesPage()
}