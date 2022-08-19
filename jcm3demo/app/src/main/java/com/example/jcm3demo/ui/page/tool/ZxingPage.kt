package com.example.jcm3demo.ui.page.tool

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

@Composable
fun ZxingPage() {
    var text by remember {
        mutableStateOf("123456sfsdfsdfasdfsdfsdf")
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val hints = hashMapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
            EncodeHintType.CHARACTER_SET to "utf-8",
            EncodeHintType.MARGIN to 1,
        )
        val mfw = MultiFormatWriter()
        val bm = mfw.encode(
            text,
            BarcodeFormat.QR_CODE,
            600,
            600,
            hints
        )
        val w = bm.width
        val h = bm.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        for (x in 0 until w) {
            for (y in 0 until h) {
                bmp.setPixel(x, y, if (bm.get(x, y)) { Color.BLACK } else { Color.WHITE })
            }
        }

        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "二维码",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f),
            )
        }

        TextField(
            value = text,
            onValueChange = {text = it},
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Preview(widthDp=375)
@Composable
fun ZxingPagePreview() {
    ZxingPage()
}