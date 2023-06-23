package com.example.appimop.ui.page

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.appimop.checkPermit
import com.example.appimop.ensurePermit
import com.example.appimop.locate
import com.example.appimop.locationManagerBehavior
import com.example.appimop.ui.DesignPreview

@Composable
fun LocationPage() {
    val context = LocalContext.current
    var isGrantCoarse by remember { mutableStateOf(context.checkPermit(Manifest.permission.ACCESS_COARSE_LOCATION)) }
    var isGrantFine by remember { mutableStateOf(context.checkPermit(Manifest.permission.ACCESS_FINE_LOCATION)) }
    val isGrant by remember(isGrantCoarse, isGrantFine) {
        derivedStateOf {
            isGrantCoarse && isGrantFine
        }
    }
    var location: Location? by remember {
        mutableStateOf(null)
    }

    (context as? Activity)?.run {
        ensurePermit(Manifest.permission.ACCESS_COARSE_LOCATION) {
            isGrantCoarse = it
        }
        ensurePermit(Manifest.permission.ACCESS_FINE_LOCATION) {
            isGrantFine = it
        }
    }

    LaunchedEffect(Unit) {
        Log.d("location", "isGrantCoarse: $isGrantCoarse isGrantFine: $isGrantFine")
    }


    if (isGrant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick =
                {
                    location = (context as? Activity)?.locate()
                },
            ) {
                Text("定位")
            }

            location?.let {
                Text(
                    text="${it.latitude}"
                )
                Text(
                    text="${it.longitude}"
                )
            }
        }
    }
}

@Preview
@Composable
fun LocationPagePreview() {
    DesignPreview {
        LocationPage()
    }
}