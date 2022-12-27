package com.example.jcmdemo.ui.page.tool

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
//import com.codeforvictory.android.superimageview.SuperImageView
import com.example.jcmdemo.R
import com.isseiaoki.simplecropview.CropImageView

@SuppressWarnings("ResourceType")
private fun loadResDraw(context: Context): Bitmap {
    // context.resources.getDrawable(R.drawable.will_crop) // 5.0 以前
//        val d = ContextCompat.getDrawable(context, R.drawable.will_crop)

    val r = context.resources.openRawResource(R.drawable.will_crop)
    return BitmapFactory.decodeStream(r)
}

@Composable
fun ImageCropper2Page() {
    val context = LocalContext.current

    val view2 by remember {
        val it = CropImageView(context)
        mutableStateOf(it)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        // 一加载就卡死。
                        Log.d("crop", "start load res .")
                        val b = loadResDraw(context)
                        Log.d("crop", "end load res ${b}.")

                        Log.d("crop", "end set bitmp .")
                    },
            ) {
                Text(
                    text = "加载图片",
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {

                        Log.d("crop", "rotateImage .")
                    },
            ) {
                Text(
                    text = "翻转",
                )
            }

        }

        AndroidView(
            factory = {view2},
        ) {
            it.imageBitmap = loadResDraw(context)
        }
    }
}