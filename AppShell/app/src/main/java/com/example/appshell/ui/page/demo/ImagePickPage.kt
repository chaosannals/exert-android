package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.ImagePicker
import com.example.appshell.ui.widget.ImagePickerItem

private val INT_PATTERN = Regex("[0-9]+")

@Composable
fun ImagePickPage() {
    var isShowPicker by remember {
        mutableStateOf(false)
    }

    val images = remember {
        mutableStateListOf<ImagePickerItem>()
    }

    var pickField by remember {
        mutableStateOf(TextFieldValue())
    }
    var pickCount by remember {
        mutableStateOf(Int.MAX_VALUE)
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement=Arrangement.Top,
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TextField(
                value = pickField,
                onValueChange =
                {
                    pickField = it
                    if (it.text.isEmpty()) {
                        pickCount = Int.MAX_VALUE
                    } else {
                        if (pickField.text.matches(INT_PATTERN)) {
                            pickCount = pickField.text.toInt()
                        }
                    }
                },
            )
            Button(
                onClick = { isShowPicker = true },
            ) {
                Text(
                    text = "浏览",
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(images) {i, image ->
                    AsyncImage(
                        model = image.uri,
                        contentDescription = "图片 $i",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }
            }
        }
        ImagePicker(
            visible = isShowPicker,
            pickCount = pickCount,
            modifier = Modifier
                .zIndex(100f),
            onConfirm = { yes, items ->
                isShowPicker = false
                if (yes) {
                    images.clear()
                    images.addAll(items)
                }
            }
        )
    }
}

@Preview
@Composable
fun ImagePickPagePreview() {
    DesignPreview {
        ImagePickPage()
    }
}