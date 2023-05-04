package com.example.appshell.ui.page.demo

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.appshell.VideoKit.loadVideoThumb

@Composable
fun NullPage() {
    val context = LocalContext.current

    val thumb1 = remember {
        context.loadVideoThumb(null)
    }

    val thumb2 = remember {
        context.loadVideoThumb(Uri.parse(""))
    }

    Column {
        if (thumb1 != null) {
            Image(
                bitmap = thumb1,
                contentDescription = "thumb1"
            )
        }

        if (thumb2 != null) {
            Image(
                bitmap = thumb2,
                contentDescription = "thumb2"
            )
        }
    }
}