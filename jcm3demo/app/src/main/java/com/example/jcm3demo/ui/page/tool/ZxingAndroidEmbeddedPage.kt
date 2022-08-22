package com.example.jcm3demo.ui.page.tool

import android.app.Activity
import android.content.Intent.getIntent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.jcm3demo.MainActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

@ExperimentalMaterial3Api
@Composable
fun ZxingAndroidEmbeddedPage() {
    val context = LocalContext.current as MainActivity
//    var capture: CaptureManager? by remember {
//        mutableStateOf(null)
//    }
//    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
//
//    DisposableEffect(lifecycleOwner) {
//        val lifecycle =lifecycleOwner.lifecycle
//        val observer = LifecycleEventObserver { owner, event ->
//            when (event) {
//                Lifecycle.Event.ON_RESUME -> {
//                    bdm?.let { it.onResume() }
//                }
//                Lifecycle.Event.ON_PAUSE -> {
//                    bdm?.let { it.onPause() }
//                }
//                Lifecycle.Event.ON_DESTROY -> {
//                    bdm?.let { it.onDestroy() }
//                }
//                else -> {}
//            }
//        }
//        lifecycle.addObserver(observer)
//        onDispose {
//            lifecycle.removeObserver(observer)
//        }
//    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { context.launchScan() },
        ) {
            Text("扫码")
        }
        Button(
            onClick = { context.launchQrScan() },
        ) {
            Text("扫码（自定义Activity)")
        }
//        AndroidView(
//            factory = {
//                val dbv = DecoratedBarcodeView(it)
//                capture = CaptureManager(it as Activity, dbv)
//                capture.initializeFromIntent(getIntent(), )
//                dbv
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f),
//        ) {
//        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ZxingAndroidEmbeddedPagePreview() {
    ZxingAndroidEmbeddedPage()
}