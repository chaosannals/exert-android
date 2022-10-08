package com.example.jcmdemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.LocalNavController
import com.example.jcmdemo.ui.page.ConfPage
import com.example.jcmdemo.ui.page.GistPage
import com.example.jcmdemo.ui.page.HomePage
import com.example.jcmdemo.ui.page.tool.CameraPage
import com.example.jcmdemo.ui.page.tool.ListingPage
import com.example.jcmdemo.ui.page.tool.ImagesPage
import com.example.jcmdemo.ui.page.tool.VideosPage
import com.example.jcmdemo.ui.page.tool.VideoPage
import com.example.jcmdemo.ui.page.tool.VideoRecyclerPage
import com.example.jcmdemo.ui.theme.JcmdemoTheme
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.page.GistItem


@ExperimentalFoundationApi
@Composable
fun MainBox() {
    val navController = LocalNavController.current

    JcmdemoTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold (
                topBar = { TopBar() },
                bottomBar = { BottomBar() },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.routeTo(BottomItem.Gist.route)
                        },
                        shape = CircleShape,
                        //contentColor = colorResource(id = R.color.deep_sky_blue),
                        backgroundColor = colorResource(id = R.color.deep_sky_blue)
                    )
                    {
                        Icon(
                            painterResource(id = BottomItem.Gist.icon),
                            contentDescription=BottomItem.Gist.title,
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            ) {
                NavHost(
                    navController,
                    modifier = Modifier.background(colorResource(id = R.color.gray)),
                    startDestination = BottomItem.Gist.route
                )
                {
                    routeRoot()
                    routeGist()
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun MainBoxPreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        MainBox()
    }
}