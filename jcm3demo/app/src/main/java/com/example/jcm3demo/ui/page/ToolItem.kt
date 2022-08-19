package com.example.jcm3demo.ui.page

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import com.example.jcm3demo.R
import com.example.jcm3demo.ui.RouteItem

enum class ToolItem(var route: RouteItem, var iconRid: Int, var title: String) {
    Camera(RouteItem.ToolCamera, R.drawable.ic_camera_alt, "相机"),
    Images(RouteItem.ToolImages, R.drawable.ic_photo_library, "图片集"),
    Videos(RouteItem.ToolVideos, R.drawable.ic_video_library, "视频集"),
    Chart(RouteItem.ToolChart, R.drawable.ic_bar_chart, "图表"),
    TextToSpeech(RouteItem.ToolTextToSpeech, R.drawable.ic_speaker_phone, "TTS"),
    BaiduMap(RouteItem.ToolBaiduMap, R.drawable.ic_map, "百度地图"),
    BaiduLocation(RouteItem.ToolBaiduLocation, R.drawable.ic_map, "百度定位"),
    Offset(RouteItem.ToolOffset, R.drawable.ic_ad_units, "位移"),
    Zxing(RouteItem.ToolZxing, R.drawable.ic_qr_code, "二维码"),
    ZxingScan(RouteItem.ToolZxingScan, R.drawable.ic_qr_code_scanner, "二维码扫描"),
}