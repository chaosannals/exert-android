package com.example.appshell.ui

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.appshell.ui.page.*
import com.example.appshell.ui.page.demo.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteStatus(
    var startRoute: String,
) : Parcelable

val LocalRouteStatus = staticCompositionLocalOf<RouteStatus>{
    error("No Route Status")
}

@Composable
fun rememberRouteStatus(): RouteStatus {
    val status by rememberSaveable {
        mutableStateOf(RouteStatus(
            startRoute = "home-page",
        ))
    }
    return status
}

fun NavGraphBuilder.routeDemoGraph() {
    navigation("lazy-page", "demo") {
        composable("coil-photo-page") {
            CoilPhotoPage()
        }
        composable("http-client-page") {
            HttpClientPage()
        }
        composable("lazy-page") {
            LazyPage()
        }
        composable("lazy-drag-page") {
            LazyDragPage()
        }
        composable("lazy-nested-pre-column-page") {
            LazyNestedPreColumnPage()
        }
        composable("lazy-nested-column-page") {
            LazyNestedColumnPage()
        }
        composable("loading-pane-page") {
            LoadingPanePage()
        }
        composable("nested-post-scroll-lazy-column-page") {
            NestedPostScrollLazyColumnPage()
        }
        composable("nested-pre-scroll-lazy-column-page") {
            NestedPreScrollLazyColumnPage()
        }
        composable("pull-refresh-page") {
            PullRefreshPage()
        }
        composable("swipe-refresh-page") {
            SwipeRefreshPage()
        }
        composable("tip-message-page") {
            TipMessagePage()
        }
    }
}

fun NavGraphBuilder.routeRootGraph() {
    composable("home-page") {
        HomePage()
    }
    composable("tbs-page") {
        TbsPage()
    }
    composable("conf-page") {
        ConfPage()
    }
    composable("demo-page") {
        DemoPage()
    }
}

fun NavController.routeTop(
    path: String
) {
    backQueue.clear()
    navigate(path) {
        // graph 是路由图，用来注册路由，并不是路由栈（路由历史）。
        // 遍历，清理等操作都是路由图。
        // 路由图内部管理着路由栈（路由历史）
//        graph.forEachIndexed {i, n ->
//            Log.d("navroute", "${i} => ${n.route}")
//        }

        // startDestinationRoute 是路由栈的东西，指的是路由栈底
        // 路由历史类似返回类似弹出栈。
//        Log.d("navroute", "${graph.startDestinationRoute}")
//        graph.startDestinationRoute?.let {
//            // 弹出路由栈到 it ，这个过程栈中多个 it 也只到第一个遇到的 it 就停了。
//            popUpTo(it) {
//                inclusive = true // 弹出包括该 ID 的路由
//                saveState = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
//        graph.setStartDestination(path)
    }
}