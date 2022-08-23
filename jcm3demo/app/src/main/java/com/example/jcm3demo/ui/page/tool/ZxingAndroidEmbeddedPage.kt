package com.example.jcm3demo.ui.page.tool

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView


@ExperimentalMaterial3Api
@Composable
fun ZxingAndroidEmbeddedPage() {
    val context = LocalContext.current as MainActivity
    val barcodeView = DecoratedBarcodeView(context)
    val beepManager = BeepManager(context)

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner) {
        val lifecycle =lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    barcodeView.resume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    barcodeView.pause()
                }
                Lifecycle.Event.ON_DESTROY -> {

                }
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
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
                Text("扫码（Custom)")
            }

            Button(
                onClick = { context.launchQrScanKeep() },
            ) {
                Text("扫码（Keep)")
            }
        }

        AndroidView(
            factory = {
                barcodeView
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    Toast.makeText(context, "重新扫描", Toast.LENGTH_LONG).show()
                    barcodeView.decodeSingle {
                        beepManager.playBeepSoundAndVibrate()
                        barcodeView.setStatusText(it.text)
                        Toast.makeText(context, "Scanned:" + it.text, Toast.LENGTH_LONG).show()
                    }
                },
        ) {
            it.initializeFromIntent(context.intent)
            it.decodeSingle {
                beepManager.playBeepSoundAndVibrate()
                barcodeView.setStatusText(it.text)
                Toast.makeText(context, "Scanned:" + it.text, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ZxingAndroidEmbeddedPagePreview() {
    ZxingAndroidEmbeddedPage()
}