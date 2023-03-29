package com.example.appshell.db

import androidx.room.*

@Dao
interface WebViewConfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun init(conf: WebViewConf)

    @Update
    fun save(conf: WebViewConf)

    @Query("SELECT * FROM web_view_conf WHERE id=1 LIMIT 1")
    fun getInfo(): WebViewConf?

    @Query("UPDATE web_view_conf SET start_url = :url WHERE id = 1")
    fun setStartUrl(url: String)
}