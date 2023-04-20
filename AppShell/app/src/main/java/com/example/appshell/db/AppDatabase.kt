package com.example.appshell.db

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 2,
    entities = [WebViewConf::class],
    //改表名之类的使用。
//    autoMigrations = [
//        AutoMigration (
//            from = 1,
//            to = 2,
//            spec = AppDatabase.From1To2Migration::class
//        )
//    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun webViewConfDao(): WebViewConfDao

    //class From1To2Migration: AutoMigrationSpec
}

val MIGRATION_1_TO_2 = object: Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE web_view_conf ADD COLUMN token TEXT DEFAULT ''")
    }
}

@Composable
fun rememberAppDatabase(): AppDatabase {
    val context = LocalContext.current
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