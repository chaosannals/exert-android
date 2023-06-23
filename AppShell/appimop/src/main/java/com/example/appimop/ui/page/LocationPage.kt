package com.example.appimop.ui.page

import android.Manifest
import android.app.Activity
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.locate
import com.example.appimop.rememberPermit
import com.example.appimop.ui.DesignPreview

@Composable
fun LocationPage() {
    val context = LocalContext.current
    val isGrantCoarse by context.rememberPermit(Manifest.permission.ACCESS_COARSE_LOCATION)
    val isGrantFine by context.rememberPermit(Manifest.permission.ACCESS_FINE_LOCATION)
    val isGrant by remember(isGrantCoarse, isGrantFine) {
        derivedStateOf {
            isGrantCoarse && isGrantFine
        }
    }
    var location: Location? by remember {
        mutableStateOf(null)
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