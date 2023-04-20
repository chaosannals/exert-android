package com.example.appshell.db

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appshell.LocalTotalStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "web_view_conf")
data class WebViewConf(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "start_url") var startUrl: String,
    @ColumnInfo(name = "var_name") var valName: String,
    @ColumnInfo(name = "token") var token: String? = null,
)

@Composable
fun ensureWebViewConf(action: (WebViewConf) -> Unit) {
    val totalStatus = LocalTotalStatus.current

    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            val dao = totalStatus.database.webViewConfDao()
            var info = dao.getInfo()
            if (info == null) {
                info = WebViewConf(
                    id = 1,
                    startUrl = "http://bilibili.com",
//                    startUrl = "http://debugx5.qq.com",
                    valName = "app",
                )
                dao.init(info)
                Log.d("conf-page", "new")
            }
            Log.d("conf-page", "read ${info}")
            action(info)
        }
    }
}