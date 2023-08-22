package com.example.bootdemo.ui.page.store

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.defaultDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "default",

    // 此迁移只是一次性的，而且还会清空原 SharedPreferences 的数据
    // 不管需不需要迁移都清空
    // 如果已有数据（比如已经迁移过一次，或者已经写入 DataStore 了）不会迁移，但是会清空 SharedPreferences。
    produceMigrations = {context ->
        listOf(
            SharedPreferencesMigration({
                PreferenceManager.getDefaultSharedPreferences(context)
            })
        )
    }
)
val defaultSharedSetKey = stringSetPreferencesKey("defaultSharedSet")

@Composable
fun DataStoreFromSharedPreferencesPage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val defaultSharedSetFlow = remember {
        context.defaultDataStore.data.map {
            it[defaultSharedSetKey]
        }
    }
    var defaultSharedSet:Set<String>? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        defaultSharedSetFlow.collect {
            defaultSharedSet = it
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = {
            coroutineScope.launch {
                context.defaultDataStore.edit {
                    val old = it[defaultSharedSetKey]
                    val now = old?.plus("${old.size}") ?: setOf()
                    it[defaultSharedSetKey] = now
                }
            }
        }) {
            defaultSharedSet?.run {
                Text("defaultSharedSet: $size")
            }
        }
        defaultSharedSet?.forEach {
            Text(it)
        }
    }
}

@Preview
@Composable
fun DataStoreFromSharedPreferencesPagePreview() {
    DataStoreFromSharedPreferencesPage()
}