package com.example.appshell.ui

import android.content.Context
import androidx.preference.PreferenceManager

data class OtherConf(
    var stringValue1: String,
    var intValue2: Int,
    var IntValueNullable3: Int?,
)

fun Context.SaveOtherConf(conf: OtherConf) {
//    this.getSharedPreferences(key, Context.MODE_PRIVATE)
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    with (sharedPref.edit()) {
        putString("stringValue1", conf.stringValue1)
        putInt("intValue2", conf.intValue2)
        if (conf.IntValueNullable3 == null) {
            remove("IntValueNullable3")
        } else {
            putInt("IntValueNullable3", conf.IntValueNullable3!!)
        }
        apply()
    }
}

fun Context.LoadOtherConf(): OtherConf {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    val result = OtherConf(
        stringValue1 = sharedPref.getString("stringValue1", "")!!,
        intValue2 = sharedPref.getInt("intValue2", 0),
        IntValueNullable3 = if (sharedPref.contains("IntValueNullable3")) {
            sharedPref.getInt("IntValueNullable3", 0)
        } else {
            null
        }
    )
    return result
}