package com.example.appshell.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
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