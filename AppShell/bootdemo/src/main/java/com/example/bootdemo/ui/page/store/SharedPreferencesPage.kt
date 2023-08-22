package com.example.bootdemo.ui.page.store

import android.app.Activity
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import okio.utf8Size

private val SP_KEY = "shared key"

// 因为 dataStore 的出现,谷歌并没有给 SharedPreferences 提供 Jetpack Compose 的封装
// 需要自行使用 StateFlow LiveData 或者 Rx 进行封装
@Composable
fun SharedPreferencesPage() {
    val context = LocalContext.current

    val modePrivate = remember {
        (context as? Activity)?.getPreferences(Context.MODE_PRIVATE)
    }
    val modePrivateIntData: MutableLiveData<Int> = remember {
        MutableLiveData(modePrivate?.getInt("modePrivateInt", 0))
    }
    val modePrivateInt by modePrivateIntData.observeAsState(0)

    val shared = remember {
        (context as? Activity)?.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
    }
    val sharedStringData = remember {
        MutableLiveData(shared?.getString("sharedString", ""))
    }
    val sharedString by sharedStringData.observeAsState("")

    val defaultShared = remember {
        PreferenceManager.getDefaultSharedPreferences(context)
    }
    val defaultSharedSetData = remember {
        MutableLiveData(defaultShared.getStringSet("defaultSharedSet", setOf()))
    }
    val defaultSharedSet by defaultSharedSetData.observeAsState(setOf())

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = {
            modePrivate?.edit {
                val v = modePrivateInt + 1
                modePrivateIntData.value = v
                putInt("modePrivateInt", v)
                commit()
            }
        }) {
            Text(text = "modePrivateInt: ${modePrivateInt}")
        }

        Button(onClick = {
            shared?.edit {
                val v = (sharedString ?: "") + (sharedString?.utf8Size() ?: 0).toString()
                sharedStringData.value = v
                putString("sharedString", v)
                commit()
            }
        }) {
            Text("sharedString: $sharedString")
        }

        Button(onClick = {
            defaultShared.edit {
                val item = defaultSharedSet.size.toString()
                val v = defaultSharedSetData.value?.plus(item)
                defaultSharedSetData.value = v
                putStringSet("defaultSharedSet", v)
                commit()
            }
        }) {
            Text("defaultSharedSet: ${defaultSharedSet.size}")
        }
        defaultSharedSet.forEach {
            Text(it)
        }
    }
}

@Preview
@Composable
fun SharedPreferencesPagePreview() {
    SharedPreferencesPage()
}