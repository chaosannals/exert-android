package com.example.kjcompose.ui

import com.example.kjcompose.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_box, "Home")
    object Music : NavigationItem("music", R.drawable.ic_music, "Music")
    object Movies : NavigationItem("movies", R.drawable.ic_cards, "Movies")
    object Books : NavigationItem("books", R.drawable.ic_home, "Books")
    object Profile : NavigationItem("profile", R.drawable.ic_home, "Profile")
}