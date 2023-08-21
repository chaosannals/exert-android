package com.example.bootdemo.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bootdemo.ui.page.IndexPage
import com.example.bootdemo.ui.page.canback.CanBack2Lv1Page
import com.example.bootdemo.ui.page.canback.CanBack2Lv2N1Page
import com.example.bootdemo.ui.page.canback.CanBack2Lv2N2Page
import com.example.bootdemo.ui.page.canback.CanBack3Lv1Page
import com.example.bootdemo.ui.page.canback.CanBackLv1Page
import com.example.bootdemo.ui.page.canback.CanBackPage
import com.example.bootdemo.ui.page.filesystem.FileSystemPage
import com.example.bootdemo.ui.page.filesystem.PickVisualMediaPage
import com.example.bootdemo.ui.page.lock.CoroutinePage
import com.example.bootdemo.ui.page.lock.LockPage
import com.example.bootdemo.ui.page.lock.LooperPage
import com.example.bootdemo.ui.page.lock.MutexPage
import com.example.bootdemo.ui.page.side.DisposeLv1Page
import com.example.bootdemo.ui.page.side.DisposeLv2N2Page
import com.example.bootdemo.ui.page.side.DisposeLv2Page
import com.example.bootdemo.ui.page.side.EffectPage
import com.example.bootdemo.ui.page.store.DataStorePreferencesPage
import com.example.bootdemo.ui.page.store.DataStoreProtoPage
import com.example.bootdemo.ui.page.store.SharedPreferencesPage
import com.example.bootdemo.ui.page.store.StoragePage
import com.example.bootdemo.ui.page.web.WebViewPage

val LocalRouter = staticCompositionLocalOf<NavHostController> {
    error("not found router.")
}

fun NavGraphBuilder.rootGraph() {
    composable("index") {
        IndexPage()
    }
}

fun NavGraphBuilder.canBackGraph() {
    composable("can-back") {
        CanBackPage()
    }
    composable("can-back-lv1") {
        CanBackLv1Page()
    }

    // 带有 navigation 多级
    navigation(
        startDestination = "can-back-2-lv1",
        route = "can-back-2"
    ) {
        composable("can-back-2-lv1") {
            CanBack2Lv1Page()
        }
        composable("can-back-2-lv2-n1") {
            CanBack2Lv2N1Page()
        }
        composable("can-back-2-lv2-n2") {
            CanBack2Lv2N2Page()
        }
    }

    composable("can-back-3-lv1") {
        CanBack3Lv1Page()
    }
}

fun NavGraphBuilder.webGraph() {
    composable("web-view") {
        WebViewPage()
    }
}

fun NavGraphBuilder.filesystemGraph() {
    navigation(
        startDestination = "file-system",
        route = "file"
    ) {
        composable("file-system") {
            FileSystemPage()
        }
        composable("pick-visual-media") {
            PickVisualMediaPage()
        }
    }
}

fun NavGraphBuilder.lockGraph() {
    navigation("lock", "lock-demo") {
        composable("lock") {
            LockPage()
        }
        composable("mutex") {
            MutexPage()
        }
        composable("looper") {
            LooperPage()
        }
        composable("coroutine") {
            CoroutinePage()
        }
    }
}

fun NavGraphBuilder.sideGraph() {
    navigation("effect", "side") {
        composable("effect") {
            EffectPage()
        }
        composable("dispose-lv1") {
            DisposeLv1Page()
        }
        composable("dispose-lv2") {
            DisposeLv2Page()
        }
        composable("dispose-lv2-n2") {
            DisposeLv2N2Page()
        }
    }
}

fun NavGraphBuilder.storeGraph() {
    navigation("storage", "store") {
        composable("storage") {
            StoragePage()
        }
        composable("shared-preferences") {
            SharedPreferencesPage()
        }
        composable("data-store-preferences") {
            DataStorePreferencesPage()
        }
        composable("data-store-proto") {
            DataStoreProtoPage()
        }
    }
}