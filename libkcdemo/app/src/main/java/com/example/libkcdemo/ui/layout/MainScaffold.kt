package com.example.libkcdemo.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import cn.chaosannals.dirtool.LocalNavController
import cn.chaosannals.dirtool.layout.DirtScaffold
import cn.chaosannals.dirtool.layout.DirtScaffoldScope
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.widget.DirtBottomBar
import cn.chaosannals.dirtool.widget.DirtBottomBarButton
import cn.chaosannals.dirtool.widget.DirtTopBar
import cn.chaosannals.dirtool.widget.DirtTopBarButton
import com.example.libkcdemo.ui.DesignPreview

@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    content: @Composable DirtScaffoldScope.() -> Unit,
) {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {
        DirtScaffold(
            modifier = modifier
                .background(
                    brush = Brush.verticalGradient(
                        listOf (
                            Color(0xFF4499FF),
                            Color.White,
                            Color(0xFFF4F4F4),
                            Color(0xFFF0F0F0),
                        ),
                    ),
                )
                .fillMaxSize()
        ) {
            val scaffold = LocalDirtScaffoldContext.current
            scaffold.topBarColor = Color.Transparent

            content()

            top {
                DirtTopBar(
                    onClickBack = { navController.navigateUp() },
                ) {
                    DirtTopBarButton(Icons.Default.ShoppingCart)
                    DirtTopBarButton(Icons.Default.Settings)
                }
            }

            bottom {
                DirtBottomBar(
                    modifier = Modifier
                ) {
                    DirtBottomBarButton(
                        title = "首页",
                        iconImageVector = Icons.Default.Home,
                    )
                    DirtBottomBarButton(
                        title = "列表",
                        iconImageVector = Icons.Default.List,
                    )
                    DirtBottomBarButton(
                        title = "我的",
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

@Preview
@Composable
fun MainScaffoldPreview() {
    DesignPreview {
        MainScaffold {

        }
    }
}