package com.example.jcmdemo.ui.page

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.edmodo.cropper.CropImageView
import com.example.jcmdemo.R

@SuppressWarnings("ResourceType")
private fun loadResDraw(context: Context): Bitmap {
    // context.resources.getDrawable(R.drawable.will_crop) // 5.0 以前
//        val d = ContextCompat.getDrawable(context, R.drawable.will_crop)

    val r = context.resources.openRawResource(R.drawable.will_crop)
    return BitmapFactory.decodeStream(r)
}

@Composable
fun ImageCropperPage() {
    val context = LocalContext.current

    var ciView: CropImageView? by remember {
        mutableStateOf(null)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row (
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
                        ciView?.setImageBitmap(b)
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
                        ciView?.rotateImage(90)
                        Log.d("crop", "rotateImage .")
                    },
            ) {
                Text(
                    text = "翻转",
                )
            }

        }
        AndroidView(
            factory = {
                val civ = CropImageView(it)
                civ.setAspectRatio(5, 10)
                civ.setFixedAspectRatio(true)
                civ.setGuidelines(1)

                // 一加载就卡死。

//                Log.d("crop", "start load res .")
//                val b = loadResDraw(it)
//                Log.d("crop", "end load res ${b}.")
//                // civ.setImageBitmap(b)
//                Log.d("crop", "end set bitmp .")

                civ
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
        ) {
//            it.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )

            ciView = it
        }
    }
}

@Preview
@Composable
fun ImageCropperPagePreview() {
    ImageCropperPage()
}