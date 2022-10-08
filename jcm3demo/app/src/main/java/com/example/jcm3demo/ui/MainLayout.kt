package com.example.jcm3demo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jcm3demo.LocalNavController
import com.example.jcm3demo.R
//import androidx.compose.material3.rememberTopAppBarState
//import androidx.compose.animation.rememberSplineBasedDecay

@ExperimentalMaterial3Api
@Composable
fun MainLayout() {
    val navController = LocalNavController.current
    val floatItem = MainBottomBarItem.Tool
//    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
//    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
//        decayAnimationSpec,
//        rememberTopAppBarState()
//    )

    Scaffold (
        topBar = { MainTopBar() },
        bottomBar = { MainBottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.routeTo(floatItem.route)
                },
                shape = CircleShape,
                containerColor = colorResource(id = R.color.deep_sky_blue)
            )
            {
                Icon(
                    painterResource(id = floatItem.iconRid),
                    contentDescription=floatItem.title,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        val pTop = it.calculateTopPadding()
        val pBottom = it.calculateBottomPadding()
        NavHost(
            navController,
            modifier = Modifier
                .padding(top = pTop, bottom = pBottom)
                .background(colorResource(id = R.color.gray)),
            startDestination = RouteItem.Tool.route,
        )
        {
            RouteItem.values().forEach{ item ->
                composable(item.route) {
                    item.page()
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun MainLayoutPreview() {
    MainLayout()
}