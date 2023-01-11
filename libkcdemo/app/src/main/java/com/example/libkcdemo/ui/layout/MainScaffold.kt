package com.example.libkcdemo.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.layout.DirtScaffold
import cn.chaosannals.dirtool.layout.DirtScaffoldScope
import cn.chaosannals.dirtool.layout.LocalDirtScaffoldContext
import cn.chaosannals.dirtool.widget.*
import com.example.libkcdemo.ui.DesignPreview
import com.example.libkcdemo.ui.LocalNavController

@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    content: @Composable DirtScaffoldScope.() -> Unit,
) {
    val navController = LocalNavController.current
    DirtScaffold(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
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
                    onClick = { navController.navigate("home")}
                )
                DirtBottomBarButton(
                    title = "概览",
                    iconImageVector = Icons.Default.List,
                    onClick = { navController.navigate("gist")}
                )
                DirtBottomBarButton(
                    title = "我的",
                    iconImageVector = Icons.Default.Person,
                    onClick = { navController.navigate("mine")}
                )
            }
        }

        floating {
            DirtFloatingBall()
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