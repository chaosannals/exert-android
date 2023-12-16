package com.example.jcm3ui.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jcm3ui.ui.page.HomePage
import com.example.jcm3ui.ui.page.demo.CachePage
import com.example.jcm3ui.ui.page.demo.CameraShotPage
import com.example.jcm3ui.ui.page.demo.CameraViewPage
import com.example.jcm3ui.ui.page.demo.CompressPage
import com.example.jcm3ui.ui.page.demo.FilePickPage
import com.example.jcm3ui.ui.page.demo.FileViewPage
import com.example.jcm3ui.ui.page.demo.PopupMakeDialogPage
import com.example.jcm3ui.ui.page.layout.CustomDrawLazyVerticalGridPage
import com.example.jcm3ui.ui.page.layout.LazyVerticalGridPage
import com.example.jcm3ui.ui.page.layout.LazyVerticalStaggeredGridPage

fun NavGraphBuilder.buildRootGraph() {
    composable("home") {
        HomePage()
    }
}

fun NavGraphBuilder.buildDemoGraph() {
    navigation("demo/file-pick", "demo") {
        composable("demo/file-pick") {
            FilePickPage()
        }
        composable("demo/file-view") {
            FileViewPage()
        }
        composable("demo/camera-shot") {
            CameraShotPage()
        }
        composable("demo/camera-view") {
            CameraViewPage()
        }
        composable("demo/popup-make-dialog") {
            PopupMakeDialogPage()
        }
        composable("demo/compress") {
            CompressPage()
        }
        composable("demo/cache") {
            CachePage()
        }
    }
}

fun NavGraphBuilder.buildLayoutGraph() {
    navigation("layout/lazy-vertical-grid", "layout") {
        composable("layout/lazy-vertical-grid") {
            LazyVerticalGridPage()
        }
        composable("layout/lazy-vertical-staggered-grid") {
            LazyVerticalStaggeredGridPage()
        }
        composable("layout/custom-draw-lazy-vertical-grid") {
            CustomDrawLazyVerticalGridPage()
        }
    }
}