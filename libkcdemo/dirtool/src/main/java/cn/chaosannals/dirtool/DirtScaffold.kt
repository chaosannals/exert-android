package cn.chaosannals.dirtool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

class DistScaffoldScope(
    var topCompose: (@Composable (Modifier) -> Unit)?=null,
    var bottomCompose:  (@Composable (Modifier) -> Unit)?=null,
    var floatingCompose: (@Composable (Modifier) -> Unit)?=null,
    var mainCompose: (@Composable (Modifier) -> Unit)?=null,
) {
    fun top(action: @Composable (Modifier) -> Unit) {
        topCompose = action
    }

    fun bottom(action: @Composable (Modifier) -> Unit) {
        bottomCompose = action
    }
    fun floating(action: @Composable (Modifier) -> Unit) {
        floatingCompose = action
    }
    fun main(action: @Composable (Modifier) -> Unit) {
        mainCompose = action
    }
}

@Composable
fun DirtScaffold(
    content: @Composable DistScaffoldScope.() -> Unit,
) {
    val compose = DistScaffoldScope()
    compose.content()
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (topRef, bottomRef, mainRef, floatingRef) = createRefs()
        compose.topCompose?.invoke(
            Modifier
                .constrainAs(topRef) {
                    top.linkTo(parent.top, margin=0.dp)
                    // bottom.linkTo(mainRef.top, margin=0.dp)
                    start.linkTo(parent.start, margin=0.dp)
                    end.linkTo(parent.end, margin=0.dp)
                }
        )
        compose.mainCompose?.invoke(
            Modifier
                .fillMaxWidth()
                .constrainAs(mainRef) {
                    top.linkTo(topRef.bottom, margin=0.dp)
                    bottom.linkTo(bottomRef.top, margin=0.dp)
                    start.linkTo(parent.start, margin=0.dp)
                    end.linkTo(parent.end, margin=0.dp)
                }
        )
        compose.bottomCompose?.invoke(
            Modifier
                .constrainAs(bottomRef) {
                    // top.linkTo(mainRef.bottom, margin=0.dp)
                    bottom.linkTo(parent.bottom, margin=0.dp)
                    start.linkTo(parent.start, margin=0.dp)
                    end.linkTo(parent.end, margin=0.dp)
                }
        )
        compose.floatingCompose?.invoke(
            Modifier
                .constrainAs(floatingRef) {
                    end.linkTo(parent.end, margin = 20.sdp)
                    bottom.linkTo(parent.bottom, margin = 64.sdp)
                }
        )
    }
}

@Preview
@Composable
fun DirtScaffoldPreview() {
    DirtPreview {
        DirtScaffold {
            top {
                Box(
                    modifier = it
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Red),
                ) {

                }
            }

            main {
                Box(
                    modifier = it
                        .fillMaxWidth()
                        .background(Color.Yellow),
                ) {

                }
            }

            bottom {
                Box(
                    modifier = it
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Blue),
                ) {

                }
            }

            floating {
                Box(
                    modifier = it
                        .size(40.dp)
                        .background(Color.Green),
                ) {

                }
            }
        }
    }
}