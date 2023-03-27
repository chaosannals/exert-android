package com.example.anidemo.ui.widget.area

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

@Serializable
data class AreaItem(
    val id: Int,
    val parentId: Int,
    val name: String,
)

fun LoadAreaItems(context: Context): List<AreaItem> {
    val stream = context.assets.open("area.json")
    val writer = StringWriter()
    val buffer = CharArray(1024)
    try {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    } finally {
        stream.close()
    }

    val jsonString = writer.toString()
    return Json.decodeFromString(jsonString)
}