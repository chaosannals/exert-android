package com.example.appshell.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("web_view_var")
data class WebViewVar(
    @PrimaryKey val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("value_type") val valueType: String,
    @ColumnInfo("value") val value: String,
)
