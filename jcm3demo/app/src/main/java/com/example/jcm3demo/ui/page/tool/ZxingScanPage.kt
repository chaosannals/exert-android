package com.example.jcm3demo.ui.page.tool

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jcm3demo.R
import com.example.jcm3demo.ui.d2dt
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.Executors

fun saveForDebug(context: Context, bmp: Bitmap) {
    var f = File(context.getOutputDirectory(), "deubg-${Date().d2dt}.jpg")
    val fo = FileOutputStream(f)
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fo)
    fo.flush()
    fo.close()
}

@Composable
fun ZxingScanPage() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
//    val emdPath = context.externalMediaDirs.firstOrNull()

    // 检查权限，无则申请。
    var sp = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )
    if (sp != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.CAMERA,
            ),
            11 // 自定标识
        )
    }

    var text by remember {
        mutableStateOf("结果")
    }

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val preview = androidx.camera.core.Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }

    val iconSize = 50.dp
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color.Black),
    ) {
        AndroidView(
            factory = {
                PreviewView(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            preview.setSurfaceProvider(it.surfaceProvider)
        }

        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                        CameraSelector.LENS_FACING_FRONT
                    } else {
                        CameraSelector.LENS_FACING_BACK
                    }
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_flip_camera_ios),
                    contentDescription = "切换相机",
                    modifier= Modifier.size(iconSize),
                    tint = colorResource(id = R.color.white)
                )
            }

            IconButton(
                onClick = {
                    imageCapture.takePicture(
                        Executors.newSingleThreadExecutor(),
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                super.onCaptureSuccess(image)
                                val w = image.width
                                val h = image.height
                                val buffer = image.planes[0].buffer
                                val bytes = ByteArray(buffer.remaining())
                                buffer.get(bytes)
                                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                                text = "获取图像: ${w}x${h}"

//                                val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.tp_debugzx3)

                                // saveForDebug(context, bmp)

                                Log.i("zxingscan", "start")

//                                val buffer = ByteBuffer.allocate(bmp.byteCount)
//                                bmp.copyPixelsToBuffer(buffer)
//                                buffer.rewind()
//                                val bytes = buffer.array()
//                                val ls = PlanarYUVLuminanceSource(
//                                    bytes,
//                                    bmp.width, bmp.height,
//                                    0, 0, bmp.width, bmp.height,
//                                    false
//                                )

                                val ib = IntArray(bmp.width * bmp.height)
                                bmp.getPixels(ib, 0, bmp.width, 0, 0, bmp.width, bmp.height)
                                val ls = RGBLuminanceSource(bmp.width, bmp.height, ib)

                                val bb = BinaryBitmap(HybridBinarizer(ls))

                                Log.i("zxingscan", "to bb.")



                                try {
                                    val mfr = MultiFormatReader()
                                    val hints = hashMapOf(
                                        DecodeHintType.CHARACTER_SET to "UTF-8",
                                        DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE),
                                        // DecodeHintType.TRY_HARDER to true,
                                        //DecodeHintType.PURE_BARCODE to true,
                                    )
                                    var r = mfr.decode(bb, hints)

                                    Log.i("zxingscan", "r: ${r}")
                                    text = r.text
                                } catch (e: NotFoundException) {
                                    text = "无法识别: ${e.message} ${e.stackTraceToString()}"
                                }
                                Log.i("zxingscan", "end")
                            }

                            override fun onError(exception: ImageCaptureException) {
                                super.onError(exception)
                                text = "异常：${exception.message} ${exception.stackTraceToString()}"
                            }
                        }
                    )
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lens),
                    contentDescription = "拍照",
                    modifier= Modifier.size(iconSize),
                    tint = colorResource(id = R.color.white)
                )
            }
        }

        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
        )
    }
}

@Preview()
@Composable
fun ZxingScanPagePreview() {
    ZxingScanPage()
}