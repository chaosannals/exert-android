package com.example.anidemo.ui

import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.anidemo.LocalNavController

@Composable
fun MainBottomBar() {
    val nc = LocalNavController.current
    val navBackStackEntry by nc.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar (
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth(),
        containerColor = Color.White,
    ){
        IconButton(onClick = {
            nc.navigate("home")
        }) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "首页",
                tint = if (currentRoute == "home") {
                    Color.Cyan
                } else {
                    Color.Black
                },
            )
        }

        IconButton(onClick = {
            nc.navigate("employee")
        }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "员工",
                tint = if (currentRoute == "employee") {
                    Color.Cyan
                } else {
                    Color.Black
                },
            )
        }

        IconButton(onClick = {
            nc.navigate("home")
        }) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "首页",
                tint = if (currentRoute == "home") {
                    Color.Cyan
                } else {
                    Color.Black
                },
            )
        }
    }
}