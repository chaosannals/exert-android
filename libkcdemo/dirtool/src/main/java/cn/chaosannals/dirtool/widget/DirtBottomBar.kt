package cn.chaosannals.dirtool.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.layout.rememberDirtScaffoldContext
import cn.chaosannals.dirtool.sdp
import cn.chaosannals.dirtool.ssp

@Composable
fun RowScope.DirtBottomBarButton(
    title: String? = null,
    icon: Painter? = null,
    iconImageVector: ImageVector? = null,
    iconColor: Color? = null,
) {
    Box(
        modifier = Modifier
            .padding(top=4.sdp)
            .weight(1f)
            .height(44.sdp),
    ) {
        if (icon != null) {
            if (iconColor == null) {
                Image(
                    painter = icon,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(24.sdp)
                )
            } else {
                Icon(
                    painter = icon,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(24.sdp)
                )
            }
        } else {
            iconImageVector?.let {
                if (iconColor == null) {
                    Image(
                        imageVector = it,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(24.sdp)
                    )
                } else {
                    Icon(
                        imageVector = it,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(24.sdp)
                    )
                }
            }
        }

        title?.let {
            Text(
                text = it,
                fontSize=14.ssp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun DirtBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val scaffold = LocalDirtScaffoldContext.current
    Row (
        horizontalArrangement=Arrangement.SpaceAround,
        verticalAlignment=Alignment.CenterVertically,
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .background(scaffold.bottomBarColor),
    ) {
        content()
    }
}

@Preview
@Composable
fun DirtButtonBarPreview() {
    DirtPreview {
        CompositionLocalProvider(
            LocalDirtScaffoldContext provides rememberDirtScaffoldContext(),
        ) {
            Column (
                verticalArrangement=Arrangement.Top,
                horizontalAlignment=Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                DirtBottomBar() {
                    DirtBottomBarButton(
                        title="文本1"
                    )
                    DirtBottomBarButton(
                        title="文本2"
                    )
                    DirtBottomBarButton(
                        title="文本3"
                    )
                }

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

                DirtBottomBar() {
                    DirtBottomBarButton(
                        iconImageVector = Icons.Default.Info,
                    )
                    DirtBottomBarButton(
                        iconImageVector = Icons.Default.Home,
                    )
                    DirtBottomBarButton(
                        iconImageVector = Icons.Default.Person,
                    )
                }
            }
        }
    }
}