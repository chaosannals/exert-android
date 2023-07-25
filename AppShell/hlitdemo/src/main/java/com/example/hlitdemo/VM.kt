package com.example.hlitdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
public inline fun <reified VM : ViewModel> hiltActivityViewModel(): VM {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val defaultStoreOwner = LocalViewModelStoreOwner.current
    val vmStoreOwner = remember(inspectionMode, context) {
        if (inspectionMode) {
            checkNotNull(defaultStoreOwner) {
                "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
            }
        } else {
            context as MainActivity
        }
    }

    return hiltViewModel(vmStoreOwner)
}