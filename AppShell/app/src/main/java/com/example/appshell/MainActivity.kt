package com.example.appshell

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.room.Room
import com.example.appshell.db.AppDatabase
import com.example.appshell.db.MIGRATION_1_TO_2
import com.example.appshell.ui.MainBox
import com.example.appshell.ui.widget.form.FormContext
import com.example.appshell.ui.widget.initX5WebShell
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

val LocalAppDatabase = staticCompositionLocalOf<AppDatabase> {
    error("No App database!")
}

val LocalMainScrollSubject = staticCompositionLocalOf<PublishSubject<Float>> {
    error("No Main Scroll subject provided!")
}

val LocalFormContext = staticCompositionLocalOf<FormContext> {
    error("No Form context")
}

@Composable
fun rememberAppDatabase(context: Context): AppDatabase {
    val database by remember {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appshell",
        )
            .addMigrations(MIGRATION_1_TO_2)
            .build()
        mutableStateOf(db)
    }
    return database
}

@SuppressLint("CheckResult")
@Composable
fun rememberMainScrollSubject() : PublishSubject<Float> {
    val sps by remember {
        val r = PublishSubject.create<Float>()
        mutableStateOf(r)
    }
    return sps
}

@Composable
fun rememberFormContext() : FormContext {
    val r by remember {
        mutableStateOf(FormContext())
    }
    return r
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化

        // 初始化 X5
        initX5WebShell()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()

        setContent {
            MainBox()
        }
    }
}