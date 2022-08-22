package com.example.jcm3demo

import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.jcm3demo.ui.theme.Jcm3demoTheme
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView


class QrScanActivity : ComponentActivity(), DecoratedBarcodeView.TorchListener  {
    lateinit var capture: CaptureManager
    lateinit var barcodeScannerView: DecoratedBarcodeView
//    lateinit var switchFlashlightButton: Button
//    lateinit var viewfinderView: ViewfinderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        barcodeScannerView = DecoratedBarcodeView(this)
        barcodeScannerView.setTorchListener(this)
        capture = CaptureManager(this, barcodeScannerView)

        setContent {
            Jcm3demoTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color= colorResource(id = R.color.gray),
                ) {
                    Column {
                        AndroidView(
                            factory = {
                                barcodeScannerView
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                        ) {
                            capture.initializeFromIntent(intent, savedInstanceState)
                            capture.setShowMissingCameraPermissionDialog(false)
                            capture.decode()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onTorchOn() {
        //TODO("Not yet implemented")
    }

    override fun onTorchOff() {
        //TODO("Not yet implemented")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}