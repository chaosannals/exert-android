package com.example.jcm3demo

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.jcm3demo.ui.theme.Jcm3demoTheme
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class QrScanKeepActivity : ComponentActivity(), DecoratedBarcodeView.TorchListener  {
    lateinit var capture: CaptureManager
    lateinit var barcodeScannerView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val self = this
        barcodeScannerView = DecoratedBarcodeView(this)
        barcodeScannerView.setTorchListener(this)
        capture = CaptureManager(this, barcodeScannerView)

        setContent {
            Jcm3demoTheme {
                var text : String? by remember {
                    mutableStateOf(null)
                }

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
                            it.decodeSingle {
                                Toast.makeText(self, "Scanned:" + it.text, Toast.LENGTH_LONG).show()
                                text = it.text
                            }
                        }
                        Text(text = text ?: "")
                        Button(
                            enabled = text != null,
                            onClick = {
                                text = null
                                barcodeScannerView.decodeSingle {
                                    Toast.makeText(self, "Scanned:" + it.text, Toast.LENGTH_LONG).show()
                                    text = it.text
                                }
                            },
                        ) {
                            Text(text = "重新扫描")
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