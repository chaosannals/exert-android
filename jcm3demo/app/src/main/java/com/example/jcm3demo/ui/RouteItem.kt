package com.example.jcm3demo.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.jcm3demo.ui.page.*
import com.example.jcm3demo.ui.page.tool.*

// 路由配置
enum class RouteItem(var route: String, var page: @Composable (NavController) -> Unit) {
    Conf("conf", { ConfPage() }),
    Home("home", { HomePage() }),
    Tool("tool", { nc -> ToolPage(nc) }),
    ToolCamera("tool-camera", { CameraPage() }),
    ToolChart("tool-chart", { ChartPage() }),
    ToolImages("tool-images", { ImagesPage() }),
    ToolVideos("tool-videos", { VideosPage() }),
    ToolTextToSpeech("tool-text-to-speech", { TextToSpeechPage() }),
    ToolBaiduMap("tool-baidu-map", { BaiduMapPage() }),
    ToolBaiduLocation("tool-baidu-location", @ExperimentalMaterial3Api { BaiduLocationPage() }),
    ToolOffset("tool-offset", { OffsetPage() }),
    ToolZxing("tool-zxing", { ZxingPage() }),
    ToolZxingScan("tool-zxing-scan", { ZxingScanPage() }),
}

// 路由到
fun NavController.routeTo(route: RouteItem) {
    navigate(route.route) {
        graph.startDestinationRoute?.let {
            popUpTo(it) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
