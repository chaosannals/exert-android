package com.example.app24.ui

import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.app24.ui.page.BootPage
import com.example.app24.ui.page.DemoPage
import com.example.app24.ui.page.HomePage
import com.example.app24.ui.page.demo.BackHandlePage
import com.example.app24.ui.page.demo.CaptureViewImage2Page
import com.example.app24.ui.page.demo.CaptureViewImagePage
import com.example.app24.ui.page.demo.CustomShapePage
import com.example.app24.ui.page.demo.Dialog2Page
import com.example.app24.ui.page.demo.Dialog3Page
import com.example.app24.ui.page.demo.Dialog4Page
import com.example.app24.ui.page.demo.DialogPage
import com.example.app24.ui.page.demo.Disposable2Page
import com.example.app24.ui.page.demo.DisposablePage
import com.example.app24.ui.page.demo.JsonPage
import com.example.app24.ui.page.demo.LineGraphPage
import com.example.app24.ui.page.demo.RandomGraph2Page
import com.example.app24.ui.page.demo.RandomGraphPage
import com.example.app24.ui.page.demo.ThreeTenABPPage
import com.example.app24.ui.page.demo.WebFirstPage
import com.example.app24.ui.page.demo.WebInitPage
import com.example.app24.ui.page.demo.WebJsSdkPage
import com.example.app24.ui.page.demo.WebNewFirstPage
import com.example.app24.ui.page.demo.WebNewSecondPage
import com.example.app24.ui.page.demo.WebSecondPage

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("nav controller null!!")
}

fun NavGraphBuilder.rootGraph() {
    composable("boot-page") {
        Log.d("route info", "当前路由信息：${it.destination.route}")
        BootPage()
    }
    composable("demo-page") {
        DemoPage()
    }
    composable("home-page") {
        HomePage()
    }
}

fun NavGraphBuilder.demoGraph() {
    navigation("three-ten-abp-page", "demo") {
        composable("three-ten-abp-page") {
            ThreeTenABPPage()
        }
        composable("web-first-page") {
            WebFirstPage()
        }
        composable("web-second-page") {
            WebSecondPage()
        }
        composable("web-new-first-page") {
            WebNewFirstPage()
        }
        composable("web-new-second-page") {
            WebNewSecondPage()
        }
        composable("web-jssdk-page") {
            WebJsSdkPage()
        }
        composable("web-init-page") {
            WebInitPage()
        }
        composable("random-graph-page") {
            RandomGraphPage()
        }
        composable("random-graph-2-page") {
            RandomGraph2Page()
        }
        composable("line-graph-page") {
            LineGraphPage()
        }
        composable("dialog-page") {
            DialogPage()
        }
        composable("dialog-2-page") {
            Dialog2Page()
        }
        composable("dialog-3-page") {
            Dialog3Page()
        }
        composable("dialog-4-page") {
            Dialog4Page()
        }
        composable("custom-shape-page") {
            CustomShapePage()
        }
        composable("back-handle-page") {
            BackHandlePage()
        }
        composable("json-page") {
            JsonPage()
        }
        composable(
            route="disposable-page?a={a}&b={b}",
            arguments = listOf(
                navArgument("a") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("b") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
        ) {
            DisposablePage()
        }
        composable("disposable-2-page") {
            Disposable2Page()
        }
        composable("capture-view-image-page") {
            CaptureViewImagePage()
        }
        composable("capture-view-image-2-page") {
            CaptureViewImage2Page()
        }
    }
}