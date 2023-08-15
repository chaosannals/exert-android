package com.example.bootdemo.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.example.bootdemo.ui.LocalRouter

@Composable
fun BottomBarButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick() }
        ,
        ) {
        Icon(imageVector = icon, contentDescription = "icon",)
        Text(text = text)
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
) {
    val router = if (LocalInspectionMode.current) null else LocalRouter.current

    Row (
        horizontalArrangement=Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        BottomBarButton("首页", Icons.Default.Home, modifier = Modifier.weight(1f),) {
            router?.run {
                backQueue.clear()
                navigate("index")
            }
        }
        BottomBarButton("返回页", Icons.Default.Share, modifier = Modifier.weight(1f),) {
            router?.run {
                backQueue.clear()
                navigate("can-back")
            }
        }
        BottomBarButton("返回页 Lv1", Icons.Default.Share, modifier = Modifier.weight(1f),) {
            router?.run {
                backQueue.clear()
                navigate("can-back-lv1")
            }
        }
        BottomBarButton("返回2页 Lv1", Icons.Default.Share, modifier = Modifier.weight(1f),) {
            router?.run {
                backQueue.clear()
                navigate("can-back-2-lv1")
            }
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar()
}