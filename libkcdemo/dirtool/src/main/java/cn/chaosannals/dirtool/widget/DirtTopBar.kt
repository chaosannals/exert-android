package cn.chaosannals.dirtool.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.layout.rememberDirtScaffoldContext
import cn.chaosannals.dirtool.sdp
import cn.chaosannals.dirtool.ssp

@Composable
fun DirtTopBarButton(
    painter: Painter,
    onClick: (() -> Unit)? = null,
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.sdp)
    ) {
        Icon(
            painter = painter,
            contentDescription = "icon",
            tint = Color.White,
            modifier = Modifier
                .size(34.sdp)
        )
    }
}

@Composable
fun DirtTopBarButton(
    imageVector: ImageVector,
    onClick: (() -> Unit)? = null,
) {
    val scaffold = LocalDirtScaffoldContext.current

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.sdp)
            .clickable { onClick?.invoke() }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "icon",
            tint = scaffold.topBarButtonColor,
            modifier = Modifier
                .size(24.sdp)
        )
    }
}

@Composable
fun DirtTopBar(
    modifier: Modifier = Modifier,
    onClickBack: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val scaffold = LocalDirtScaffoldContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(40.sdp)
            .background(scaffold.topBarColor),
    ) {
        onClickBack?.let {
            if (scaffold.isTopBarBackVisible) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.sdp)
                        .clickable { it() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = scaffold.topBarButtonColor,
                        modifier = Modifier
                            .size(24.sdp)
                    )
                }
            }
        }
        Text(
            text = scaffold.title,
            textAlign = TextAlign.Center,
            color=scaffold.titleColor,
            fontSize=18.ssp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f),
        )
        content?.let {
            Row(
                horizontalArrangement=Arrangement.End,
                verticalAlignment=Alignment.CenterVertically,
                modifier = Modifier,
            ) {
                it()
            }
        }
    }
}

@Preview
@Composable
fun DirtTopBarPreview() {
    DirtPreview {
        CompositionLocalProvider(
            LocalDirtScaffoldContext provides rememberDirtScaffoldContext(),
        ) {
            val scaffold = LocalDirtScaffoldContext.current
            scaffold.title = "顶部状态栏"

            Column(
                verticalArrangement=Arrangement.Top,
                horizontalAlignment=Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                DirtTopBar(
                    modifier = Modifier.background(Color.White),
                )
                DirtTopBar(
                    onClickBack = {}
                )

                DirtTopBar {
                    DirtTopBarButton(Icons.Default.Settings)
                    DirtTopBarButton(Icons.Default.Add)
                    DirtTopBarButton(Icons.Default.AccountBox)
                }
                DirtTopBar (
                    onClickBack = {}
                ) {
                    DirtTopBarButton(Icons.Default.Settings)
                    DirtTopBarButton(Icons.Default.Add)
                    DirtTopBarButton(Icons.Default.AccountBox)
                }
            }
        }
    }
}