package com.example.app24.ui.page.demo

import android.util.Base64
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun Json2Page() {
    val d = remember {
        mapOf(
            "type" to 2,
        )
    }
    val j by remember(d) {
        derivedStateOf {
            Json.encodeToString(d)
        }
    }
    val e by remember(j) {
        derivedStateOf {
            Base64.encodeToString(j.toByteArray(), Base64.DEFAULT)
        }
    }
    val jd by remember(e) {
        derivedStateOf {
            Base64.decode(e, Base64.DEFAULT).decodeToString()
        }
    }

    Column (
        modifier = Modifier.safeContentPadding()
    ){
        Text("j: $j")
        Text("e: $e")
        Text("jd: $jd")
    }
}

@Preview
@Composable
fun Json2PagePreview() {
    DesignPreview {
        Json2Page()
    }
}