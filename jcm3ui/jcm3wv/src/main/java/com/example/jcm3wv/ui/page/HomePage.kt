package com.example.jcm3wv.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3wv.ui.NavTarget
import com.example.jcm3wv.ui.nav




@Preview
@Composable
fun HomePage() {
    val routes by remember {
        derivedStateOf {
            NavTarget.entries.filter {
                it.path.indexOf('/') >= 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        routes.forEach {
            Button(
                onClick = {
                    it.nav()
                }
            ) {
                Text(it.path)
            }
        }
    }
}
