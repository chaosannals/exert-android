package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import kotlinx.serialization.SerialName
import java.net.URLEncoder
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

data class UrlQueryStringDemoA (
    val a: Int,
    val bbb: String,
    val dddd: Double,
)

data class UrlQueryStringDemoB (
    val c: Int,
    val xxx: Float,

    @SerialName("fff_aaa")
    val fffAAA: Float,

    @SerialName("sss_bbb")
    val sssBBB: String,
)

fun <T> T.makeQueryString(): String {
    val result = mutableListOf<String>()
    if (this == null) {
        return ""
    }
    for (dp in this!!::class.declaredMemberProperties) {
        val v = dp.getter.call(this)
        if (v != null) {
            val n = dp.findAnnotation<SerialName>()
            val name = if (n != null) {
                n.value
            } else {
                dp.name
            }
            val value = URLEncoder.encode(v.toString(), "utf-8")
            result.add("${name}=$value")
        }
    }
    return result.joinToString("&")
}

@Composable
fun UrlQueryStringMakePage() {
    LazyColumn(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        item {
            val a = UrlQueryStringDemoA(123, "靠zi++++zzz", 234.343)
            SelectionContainer{
                Text(
                    text = a.makeQueryString(),
                )
            }
        }

        item {
            val b = UrlQueryStringDemoB(3443, 2342.234f, 32434.2343f, "asdfsdf++中文")
            SelectionContainer {
                Text(text = b.makeQueryString())
            }
        }
    }
}

@Preview
@Composable
fun UrlQueryStringMakePagePreview() {
    DesignPreview {
        UrlQueryStringMakePage()
    }
}