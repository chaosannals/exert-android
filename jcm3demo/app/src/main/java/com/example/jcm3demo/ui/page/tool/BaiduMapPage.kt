package com.example.jcm3demo.ui.page.tool

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.baidu.mapapi.map.MapView
import com.example.jcm3demo.ui.sdp

@Composable
fun BaiduMapPage() {
    var bdm : MapView? by remember {
        mutableStateOf(null)
    }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner) {
        val lifecycle =lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    bdm?.let { it.onResume() }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    bdm?.let { it.onPause() }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    bdm?.let { it.onDestroy() }
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
        modifier = Modifier.fillMaxWidth(),
    ){
        AndroidView(
            factory = { MapView(it) },
            modifier = Modifier
                .height(500.sdp)
                .fillMaxWidth()
        ) {
            bdm = it
        }
    }
}

@Preview(widthDp=375)
@Composable
fun BaiduMapPagePreview(){
    BaiduMapPage()
}