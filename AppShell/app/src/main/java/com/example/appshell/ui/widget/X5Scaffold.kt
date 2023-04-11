package com.example.appshell.ui.widget

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.example.appshell.ui.LocalNavController
import com.example.appshell.ui.routeTop
import com.example.appshell.ui.sdp
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Parcelize
data class X5ScaffoldStatus(
    val isShowFloatingBall: Boolean = true,
    val isShowNavbar: Boolean = true,
    val isShowLoadingPane: Boolean = false,
) : Parcelable

//val X5ScaffoldStatusSaver = run {
//    val isShowFloatingBallKey = "isShowFloatingBall"
//    val isShowNavbarKey = "isShowNavbar"
//    mapSaver(
//        save = { mapOf(isShowFloatingBallKey to it.isShowFloatingBall, isShowNavbarKey to it.isShowNavbar)},
//        restore = { X5ScaffoldStatus(it[isShowFloatingBallKey] as Boolean, it[isShowNavbarKey] as Boolean)}
//    )
//}

// 有时能联动有时不能。此方案不稳定
val LocalX5ScaffoldStatus = staticCompositionLocalOf<X5ScaffoldStatus> {
    error("No X5Scaffold Status !")
}

val LocalX5ScaffoldRxSubject = staticCompositionLocalOf<BehaviorSubject<X5ScaffoldStatus>> {
    error("No X5Scaffold rx subject")
}

@Composable
fun rememberX5ScaffoldStatus(): X5ScaffoldStatus {
//    val status by rememberSaveable(stateSaver = X5ScaffoldStatusSaver) {
    val status by rememberSaveable() {
        mutableStateOf(X5ScaffoldStatus())
    }
    return status
}

@SuppressLint("CheckResult")
@Composable
fun rememberX5ScaffoldRxSubject(): BehaviorSubject<X5ScaffoldStatus> {
    val subject by remember {
        val ps = BehaviorSubject.create<X5ScaffoldStatus>()
        ps.onNext(X5ScaffoldStatus())
        mutableStateOf(ps)
    }
    return subject
}

@Composable
fun X5Scaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val navController = LocalNavController.current
    var status = LocalX5ScaffoldStatus.current
    val coroutineScope = rememberCoroutineScope()

    var isShowDebugger by remember {
        mutableStateOf(false)
    }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var isShowLoadingPane by remember { mutableStateOf(false) }
    val subject = rememberX5ScaffoldRxSubject()

    DisposableEffect(subject) {
        val s = subject.subscribe {
            isShowLoadingPane = it.isShowLoadingPane
        }
        onDispose {
            s.dispose()
        }
    }

    CompositionLocalProvider(
        LocalX5ScaffoldRxSubject provides subject,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    content()
                }
                if (status.isShowNavbar) {
                    Navbar(
                        modifier = Modifier
                            .zIndex(10f)
                    ) {
                        NavbarButton(
                            imageVector = Icons.Default.Home,
                            onClicked =
                            {
                                navController.routeTop("home-page")
                            },
                        )
                        NavbarButton(
                            imageVector = Icons.Default.Build,
                            onClicked =
                            {
                                navController.routeTop("tbs-page")
                            },
                        )
                        NavbarButton(
                            imageVector = Icons.Default.Settings,
                            onClicked =
                            {
                                navController.routeTop("conf-page")
                            },
                        )
                        NavbarButton(
                            imageVector = Icons.Default.List,
                            onClicked =
                            {
                                navController.routeTop("demo-page")
                            },
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .zIndex(10f)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 100.sdp, end = 10.sdp),
            ) {
                FloatingBall(
                    modifier = Modifier
                        .size(44.sdp)
                        .offset {
                            IntOffset(
                                offsetX.roundToInt(),
                                offsetY.roundToInt(),
                            )
                        }
                        // 这种只能单轴
//                    .draggable(
//                        orientation = Orientation.Vertical,
//                        state = rememberDraggableState() {
//                            offsetY += it
//                        }
//                    )
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                        },
                    onClick = {
                        // 这个却可以
                        status = status.copy(
                            isShowLoadingPane = true
                        )

                        subject.onNext(
                            subject.value!!.copy(
                                isShowLoadingPane = true
                            )
                        )

                        // 不联动
//                        coroutineScope.launch {
//                            delay(1000)
//                            status.isShowLoadingPane = false
//                        }
                        isShowDebugger = true
                    },
                )
            }

            // 不联动
//            if (status.isShowLoadingPane) {
//                LoadingPane(
//                    onClicked = {
//                        status.isShowLoadingPane = false
//                    }
//                )
//            }
            if (isShowLoadingPane) {
                LoadingPane(
                    onClicked = {
                        subject.onNext(
                            X5ScaffoldStatus(
                                isShowLoadingPane = false
                            )
                        )
                    }
                )
            }

//        if (isShowDebugger) {
            //   Popup 不接收输入事件
//            Popup(
//                alignment = Alignment.BottomCenter,
//                onDismissRequest = { isShowDebugger = false }
//            ) {
//                DebugView()
//            }
//        }
            if (isShowDebugger) {
                Box(
                    modifier = Modifier
                        .zIndex(100f)
                        .fillMaxSize()
                        .background(Color(0x1A000000))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                            isShowDebugger = false
                        },
                ) {
                    DebugView(
                        modifier = Modifier
                            .zIndex(100f)
                            .align(Alignment.BottomCenter)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) {},
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun X5ScaffoldPreview() {
    DesignPreview {
        X5Scaffold {

        }
    }
}