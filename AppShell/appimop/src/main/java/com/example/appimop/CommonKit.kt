package com.example.appimop

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.subjects.BehaviorSubject

val locationManagerBehavior: BehaviorSubject<LocationManager> = BehaviorSubject.create()

//WGS84
@SuppressLint("MissingPermission")
fun Activity.locate(): Location? {
    var result: Location? = null
    if (checkPermit(Manifest.permission.ACCESS_FINE_LOCATION) &&
        checkPermit(Manifest.permission.ACCESS_COARSE_LOCATION)
    ) {
        locationManagerBehavior.value?.run {
            Log.d("location", "get")
            val providers = getProviders(true)
            for (p in providers) {
                val r = getLastKnownLocation(p) ?: continue
                if (result == null || r.accuracy < result!!.accuracy) {
                    result = r
                }
            }
        }
    }
    return result
}

fun Context.checkPermit(permission: String): Boolean {
    val sp = ContextCompat.checkSelfPermission(
        this,
        permission
    )
    return sp == PackageManager.PERMISSION_GRANTED
}

@Composable
fun Context.EnsurePermit(permission: String, onResult: (Boolean) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = onResult,
    )

    LaunchedEffect(launcher, permission) {
        if (!checkPermit(permission)) {
            launcher.launch(permission)
        }
    }
}

@Composable
fun Context.rememberPermit(permission: String): MutableState<Boolean> {
    val result = remember(permission) {
        mutableStateOf(checkPermit(permission))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult =
        {
            result.value = it
        },
    )

    LaunchedEffect(launcher, permission) {
        if (!checkPermit(permission)) {
            launcher.launch(permission)
        }
    }
    return result
}