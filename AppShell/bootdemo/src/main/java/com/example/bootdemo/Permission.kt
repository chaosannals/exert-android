package com.example.bootdemo

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat

fun Context.checkPermit(permission: String): Boolean {
    val sp = ContextCompat.checkSelfPermission(
        this,
        permission
    )
    return sp == PackageManager.PERMISSION_GRANTED
}

@Composable
fun Context.requestPermissionForResult(permission: String): State<Boolean> {
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

@Composable
fun Context.requestPermissionsForResult(permissions: Array<String>): State<Map<String, Boolean>> {
    val result = remember(permissions) {
        mutableStateOf(mapOf<String,Boolean>())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult =
        {
            result.value = it
        },
    )

    LaunchedEffect(launcher, permissions) {
        launcher.launch(permissions)
    }
    return result
}