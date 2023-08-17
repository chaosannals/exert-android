package com.example.bootdemo.ui.page.filesystem

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PickVisualMediaPage() {
    var photoOne: Any? by remember {
        mutableStateOf(null)
    }
    val pickerOne = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        photoOne = it
        Log.d("选图", "$photoOne")
    }
    val photoMulti = remember {
        mutableStateListOf<Any?>()
    }
    val pickerMulti = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(9)
    ) {
        photoMulti.clear()
        photoMulti.addAll(it)
        Log.d("选图", "${photoMulti.size}")
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(
            onClick =
            {
                pickerOne.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
        ) {
            Text(text = "单图片选取")
        }
        Log.d("选图", "$photoOne")
        AsyncImage(
            model = photoOne,
            contentDescription = "单图片",
            modifier = Modifier
                .size(100.dp)
        )
        Button(
            onClick =
            {
                pickerMulti.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
        ) {
            Text(text = "多图片选取")
        }
        Log.d("选图", "多 ${photoMulti.size}")
        photoMulti.forEachIndexed { i, it ->
            Log.d("选图", "$i => $it")
            AsyncImage(
                model = it,
                contentDescription = "多图片 $i",
                modifier = Modifier
                    .size(100.dp)
            )
        }
    }
}

@Preview
@Composable
fun PickVisualMediaPagePreview() {
    PickVisualMediaPage()
}