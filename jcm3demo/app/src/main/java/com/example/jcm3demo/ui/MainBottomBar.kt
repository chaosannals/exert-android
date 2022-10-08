package com.example.jcm3demo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jcm3demo.LocalNavController
import com.example.jcm3demo.R

@Composable
fun MainBottomBar() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar (
        modifier = Modifier
            .fillMaxWidth(),
        //.border(BorderStroke(2.dp, colorResource(id = R.color.gray))),
        containerColor = colorResource(id = R.color.white),
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            MainBottomBarItem.values().filter {
                !it.isFloat
            }.forEach {
                IconButton(
                    onClick = { navController.routeTo(it.route) },
                ) {
                    Icon(
                        painter = painterResource(id = it.iconRid),
                        contentDescription = it.title,
                        tint = if (currentRoute == it.route.route) {
                            colorResource(id = R.color.light_sky_blue)
                        } else {
                            colorResource(id = R.color.black)
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainBottomBarPreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        MainBottomBar()
    }
}