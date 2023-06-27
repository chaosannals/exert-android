package com.example.appimop.ui.page

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.ApkKit
import com.example.appimop.ApkKit.isInstalled
import com.example.appimop.locate
import com.example.appimop.rememberPermit
import com.example.appimop.ui.DesignPreview

@OptIn(ExperimentalMaterial3Api::class)
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

    var findTmap by remember {
        mutableStateOf(false)
    }
    var findBdmap by remember {
        mutableStateOf(false)
    }

    var bdText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        Log.d("location", "isGrantCoarse: $isGrantCoarse isGrantFine: $isGrantFine")
        findTmap = context.isInstalled("com.tencent.map")
        findBdmap = context.isInstalled("com.baidu.BaiduMap")
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

            Button(
                onClick =
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("qqmap://map/routeplan?type=drive&from=清华&fromcoord=39.994745,116.247282&to=怡和世家&tocoord=39.867192,116.493187&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"))
                    context.startActivity(intent)
                }
            ) {
                Text("腾讯导航(这个 Key 是官方示例，随时可能失效)")
            }
            Button(
                onClick =
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("qqmap://map/routeplan?type=drive&fromcoord=CurrentLocation&to=怡和世家&tocoord=39.867192,116.493187&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"))
                    context.startActivity(intent)
                }
            ) {
                Text("腾讯导航(当前)")
            }
            Text("腾讯地图安装：$findTmap")

            Button(
                onClick =
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("baidumap://map/navi?query=故宫&src=andr.baidu.openAPIdemo"))
                    context.startActivity(intent)
                }
            ) {
                Text("百度导航（）")
            }

            TextField(
                value = bdText,
                onValueChange = {bdText =it},
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick =
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("baidumap://map/place/search?query=$bdText&radius=1000&src=andr.baidu.openAPIdemo"))
                    context.startActivity(intent)
                }
            ) {
                Text("百度搜索（）")
            }
            Text("百度地图安装：$findBdmap")
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