package com.example.jcm3demo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.common.BaiduMapSDKException
import com.example.jcm3demo.ui.MainLayout
import com.example.jcm3demo.ui.U
import com.example.jcm3demo.ui.page.tool.writeLog
import com.example.jcm3demo.ui.sdp
import com.example.jcm3demo.ui.theme.Jcm3demoTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {


    val barcodeLauncher = registerForActivityResult(ScanContract()) {
        if (it.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Scanned:" + it.contents, Toast.LENGTH_LONG).show()
        }
    }

    fun launchScan() {
        val opts = ScanOptions()
        opts.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        opts.setOrientationLocked(false)
        barcodeLauncher.launch(opts)
    }

    fun launchQrScan() {
        val options = ScanOptions()
            .setOrientationLocked(false)
            .setCaptureActivity(QrScanActivity::class.java)
        barcodeLauncher.launch(options)
    }

    fun launchQrScanKeep() {
        val options = ScanOptions()
            .setOrientationLocked(false)
            .setCaptureActivity(QrScanKeepActivity::class.java)
        barcodeLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 7.4.0 没有这些接口，7.5.0 有并需要同意才能使用。
        //SDKInitializer.setAgreePrivacy(application, true)
        //  v9.2.9版本起加入
        //LocationClient.setAgreePrivacy(true)
        try {
            SDKInitializer.initialize(application)
            SDKInitializer.setHttpsEnable(true)
            SDKInitializer.setCoordType(CoordType.BD09LL)
            BDL.init(application, this)
        } catch (e : BaiduMapSDKException) {
            writeLog(this,"bdm error : ${e.message} ${e.stackTraceToString()}")
        }
        writeLog(this,"displayDp : ${U.displayDp}  375 -> ${375.sdp}")
        setContent {
            Jcm3demoTheme {
                Surface(
                    modifier = Modifier
                        .onGloballyPositioned {
                            var r = it.boundsInRoot()
                            Log.i(
                                "mainactivity",
                                "(${r.left}, ${r.top}) (${r.right}, ${r.bottom}) ${r.width} ${r.height}"
                            )
                        }
                        .fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
                color= colorResource(id = R.color.gray),
                ) {
                    MainLayout()
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Jcm3demoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.gray),
        ) {
            MainLayout()
        }
    }
}