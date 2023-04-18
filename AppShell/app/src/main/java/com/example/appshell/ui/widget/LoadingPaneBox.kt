package com.example.appshell.ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.appshell.R
import com.example.appshell.ui.sdp
import io.reactivex.rxjava3.subjects.BehaviorSubject

val LocalLoadingPaneSubject = staticCompositionLocalOf<BehaviorSubject<Boolean>> {
    error("No Loading pane subject")
}

@Composable
fun rememberLoadingPaneSubject() : BehaviorSubject<Boolean> {
    val r by remember {
        val bs = BehaviorSubject.create<Boolean>()
        bs.onNext(false)
        mutableStateOf(bs)
    }
    return r
}

@Composable
fun LoadingPaneBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val pane = LocalLoadingPaneSubject.current
    val infiniteTransition = rememberInfiniteTransition()
    val degrees by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = 1444,
                easing = LinearEasing,
            )
        ),
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = 1444,
                easing = LinearEasing,
            )
        ),
    )

    var isShow by remember { mutableStateOf(false) }

    DisposableEffect(pane) {
        val subject = pane.subscribe {
            isShow = it
        }
        onDispose {
            subject.dispose()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        if (isShow) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(99999f)
                    .matchParentSize()
                    .background(Color(0x14000000))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {},
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loading),
                    contentDescription = "Loading",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .rotate(degrees)
                        .alpha(alpha)
                        .size(140.sdp),
                )
            }
        }
        content()
    }
}

@Preview
@Composable
fun LoadingPaneBoxPreview() {
    DesignPreview {
        LoadingPaneBox() {

        }
    }
}