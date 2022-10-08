package com.example.jcmdemo.ui

import androidx.navigation.NavController
import androidx.navigation.NavHostController

fun NavController.routeTo(route: String) {
    navigate(route) {
//        graph.startDestinationRoute?.let { route ->
//            popUpTo(route) {
//                saveState = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
    }
}