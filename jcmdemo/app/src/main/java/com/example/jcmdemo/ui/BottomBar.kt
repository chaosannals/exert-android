package com.example.jcmdemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.R

sealed class BottomItem(var route: String, var icon: Int, var title: String) {
    object Home :BottomItem("home", R.drawable.ic_home, "首页")
    object Gist :BottomItem("gist", R.drawable.ic_gist, "概览")
    object Conf :BottomItem("conf", R.drawable.ic_conf, "设置")
}

@Composable
fun BottomBar (navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    @Composable
    fun itemButton(item: BottomItem) {
        IconButton(
//            modifier = if (currentRoute == item.route) {
//                Modifier.background(colorResource(id = R.color.light_sky_blue))
//            } else {
//                Modifier.background(colorResource(id = R.color.white))
//            },
            onClick = {
                navController.navigate(item.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        ) {
            Icon(
                painterResource(id = item.icon),
                contentDescription=item.title,
                tint = if (currentRoute == item.route) {
                    colorResource(id = R.color.light_sky_blue)
                } else {
                    colorResource(id = R.color.black)
                },
            )
        }
    }

    BottomAppBar (
        modifier = Modifier.fillMaxWidth(1.0f),
        backgroundColor = colorResource(id = R.color.white),
        //cutoutShape = RoundedCornerShape(50)
        cutoutShape = CircleShape
    ){
        Row (
            modifier = Modifier.fillMaxWidth(1.0f),
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            itemButton(item = BottomItem.Home)
            Row() {}
            itemButton(item = BottomItem.Conf)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreView() {
    val navController = rememberNavController()
    BottomBar(navController)
}