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
import com.example.appshell.ui.page.state.OnlyFlowLv2Page
import com.example.appshell.ui.page.state.OnlyFlowPage
import com.example.appshell.ui.page.state.OnlyLiveDataLv2Page
import com.example.appshell.ui.page.state.OnlyLiveDataPage
import com.example.appshell.ui.page.state.OnlyRememberLv2Page
import com.example.appshell.ui.page.state.OnlyRememberPage
import com.example.appshell.ui.page.state.OnlyViewModelLv2Page
import com.example.appshell.ui.page.state.OnlyViewModelPage
import com.example.appshell.ui.page.state.SaveParcelizeLv2Page
import com.example.appshell.ui.page.state.SaveParcelizePage
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
        mutableStateOf(
            RouteStatus(
                startRoute = "home-page",
            )
        )
    }
    return status
}

fun NavGraphBuilder.routeDemoGraph() {
    navigation("demo-page", "demo") {// startDestination 必须是子 composable
        composable("demo-page") {
            DemoPage()
        }
        composable("animation-page") {
            AnimationPage()
        }
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
        composable("tx-im-login-page") {
            TxIMLoginPage()
        }
        composable("tx-im-page") {
            TxIMPage()
        }
        composable("pdf-view-page") {
            PdfViewPage()
        }
        composable("reflection-page") {
            ReflectionPage()
        }
        composable("regex-page") {
            RegexPage()
        }
        composable("file-page") {
            FilePage()
        }
        composable("file-custom-pick-page") {
            FileCustomPickPage()
        }
        composable("image-pick-page") {
            ImagePickPage()
        }
        composable("video-pick-page") {
            VideoPickPage()
        }
        composable("video-display-page") {
            VideoDisplayPage()
        }
        composable("video-display-2-page") {
            VideoDisplay2Page()
        }
        composable("null-page") {
            NullPage()
        }
    }
}

fun NavGraphBuilder.routeStateGraph() {
    navigation("state-page", "state") { // route 不能和其他composable 重名
        composable("state-page") {
            StatePage()
        }

        navigation("only-remember-page", "only-remember") {
            composable("only-remember-page") {
                OnlyRememberPage()
            }
            composable("only-remember-lv2-page") {
                OnlyRememberLv2Page()
            }
        }

        navigation("save-parcelize-page", "save-parcelize") {
            composable("save-parcelize-page") {
                SaveParcelizePage()
            }
            composable("save-parcelize-lv2-page") {
                SaveParcelizeLv2Page()
            }
        }

        navigation("only-flow-page", "only-flow") {
            composable("only-flow-page") {
                OnlyFlowPage()
            }
            composable("only-flow-lv2-page") {
                OnlyFlowLv2Page()
            }
        }

        navigation("only-live-data-page", "only-live-data") {
            composable("only-live-data-page") {
                OnlyLiveDataPage()
            }
            composable("only-live-data-lv2-page") {
                OnlyLiveDataLv2Page()
            }
        }

        navigation("only-view-model-page", "only-view-model") {
            composable("only-view-model-page") {
                OnlyViewModelPage()
            }
            composable("only-view-model-lv2-page") {
                OnlyViewModelLv2Page()
            }
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