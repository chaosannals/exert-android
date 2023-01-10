package cn.chaosannals.dirtool.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cn.chaosannals.dirtool.DirtPreview
import cn.chaosannals.dirtool.LocalNavController
import cn.chaosannals.dirtool.sdp
import cn.chaosannals.dirtool.widget.DirtBottomBar
import cn.chaosannals.dirtool.widget.DirtBottomBarButton
import cn.chaosannals.dirtool.widget.DirtTopBar
import cn.chaosannals.dirtool.widget.DirtTopBarButton

class DirtScaffoldContext (
    var title: String = "",
    var titleColor: Color = Color.Black,
    var topBarColor: Color = Color(0xFF4488FF),
    var topBarButtonColor: Color = Color.White,
    var bottomBarColor: Color = Color.White,
    var isFloatingVisible: Boolean = true,
    var floatingRightDp: Dp = 20.sdp,
    var floatingBottomDp: Dp = 144.sdp,
)

val LocalDirtScaffoldContext = staticCompositionLocalOf<DirtScaffoldContext> {
    error("not DirtScaffoldContext")
}

@Composable
fun rememberDirtScaffoldContext() : DirtScaffoldContext {
    val scaffold by remember {
        mutableStateOf(DirtScaffoldContext())
    }
    return scaffold
}

class DirtScaffoldScope(
    var topCompose: (@Composable BoxScope.() -> Unit)?=null,
    var bottomCompose:  (@Composable BoxScope.() -> Unit)?=null,
    var floatingCompose: (@Composable BoxScope.() -> Unit)?=null,
) {
    fun top(action: @Composable BoxScope.() -> Unit) {
        topCompose = action
    }
    fun bottom(action: @Composable BoxScope.() -> Unit) {
        bottomCompose = action
    }
    fun floating(action: @Composable BoxScope.() -> Unit) {
        floatingCompose = action
    }
}

@Composable
fun DirtScaffold(
    modifier: Modifier = Modifier,
    content: @Composable DirtScaffoldScope.() -> Unit,
) {
    val compose = DirtScaffoldScope()
    val scaffold = rememberDirtScaffoldContext()
    CompositionLocalProvider(
        LocalDirtScaffoldContext provides scaffold,
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize(),
        ) {
            val (topRef, bottomRef, mainRef, floatingRef) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(mainRef) {
                        top.linkTo(topRef.bottom, margin=0.dp)
                        bottom.linkTo(bottomRef.top, margin=0.dp)
                        start.linkTo(parent.start, margin=0.dp)
                        end.linkTo(parent.end, margin=0.dp)
                        height = Dimension.fillToConstraints
                    }
            ) {
                compose.content()
            }

            compose.topCompose?.let {
                Box(
                    modifier = Modifier
                        .constrainAs(topRef) {
                            top.linkTo(parent.top, margin=0.dp)
                            start.linkTo(parent.start, margin=0.dp)
                            end.linkTo(parent.end, margin=0.dp)
                        },
                ) { it() }
            }

            compose.bottomCompose?.let {
                Box(
                    modifier=Modifier
                        .constrainAs(bottomRef) {
                            // top.linkTo(mainRef.bottom, margin=0.dp)
                            bottom.linkTo(parent.bottom, margin=0.dp)
                            start.linkTo(parent.start, margin=0.dp)
                            end.linkTo(parent.end, margin=0.dp)
                        },
                ) { it() }
            }
            compose.floatingCompose?.let {
                if (scaffold.isFloatingVisible) {
                    Box(
                        modifier = Modifier
                            .constrainAs(floatingRef) {
                                end.linkTo(parent.end, margin = scaffold.floatingRightDp)
                                bottom.linkTo(parent.bottom, margin = scaffold.floatingBottomDp)
                            }
                    ) { it() }
                }
            }
        }
    }
}

@Preview
@Composable
fun DirtScaffoldPreview() {
    DirtPreview {
        DirtScaffold {
            val scaffold = LocalDirtScaffoldContext.current
            val navController = LocalNavController.current
            scaffold.title = "示例"
            top {
                DirtTopBar(
                    onClickBack = { navController.navigateUp() },
                ) {
                    DirtTopBarButton(Icons.Default.Settings)
                    DirtTopBarButton(Icons.Default.AccountBox)
                }
            }

            // main
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow),
            ) {

            }

            bottom {
                DirtBottomBar() {
                    DirtBottomBarButton(
                        title="文本1",
                        iconImageVector = Icons.Default.Info,
                    )
                    DirtBottomBarButton(
                        title="文本2",
                        iconImageVector = Icons.Default.Home,
                    )
                    DirtBottomBarButton(
                        title="文本3",
                        iconImageVector = Icons.Default.Person,
                    )
                }
            }

            floating {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Green),
                ) {

                }
            }
        }
    }
}