package com.example.appshell.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WebViewConf::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun webViewConfDao(): WebViewConfDao
}