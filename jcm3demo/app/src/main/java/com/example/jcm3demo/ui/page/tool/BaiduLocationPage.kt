package com.example.jcm3demo.ui.page.tool

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jcm3demo.BDL

val PLIST = listOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    //Manifest.permission.ACCESS_NETWORK_STATE,
)

@ExperimentalMaterial3Api
@Composable
fun BaiduLocationPage() {
    val context = LocalContext.current

    val nps = PLIST.filter {
        val sp = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        sp != PackageManager.PERMISSION_GRANTED
    }.toTypedArray()
    if (nps.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            context as Activity,
            nps,
            6 // 自定标识
        )
        BDL.restart()
    }

    var addr by remember {
        mutableStateOf("")
    }
    var altitude by remember {
        mutableStateOf(0.0)
    }
    var longitude by remember {
        mutableStateOf(0.0)
    }

    DisposableEffect(Unit) {
        val uuid = BDL.attach {
            writeLog(context, "on dbl listen")
            if (it.address.address != null) {
                addr = it.address.address
            }
            altitude = it.altitude
            longitude = it.longitude
        }
        BDL.start()
        writeLog(context,"start bdl")
        onDispose {
            BDL.stop()
            writeLog(context,"stop bdl")
            BDL.detach(uuid)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text("地址：")
        Text(text = addr)
        Text(text = altitude.toString())
        Text(text = longitude.toString())
    }
}

@ExperimentalMaterial3Api
@Preview(widthDp = 375)
@Composable
fun BaiduLocationPagePreview() {
    BaiduLocationPage()
}