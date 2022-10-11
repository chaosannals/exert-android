package com.example.jcmdemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.jcmdemo.ui.page.*
import com.example.jcmdemo.ui.page.tool.*

fun NavGraphBuilder.routeRoot() {
    composable(BottomItem.Home.route) {
        HomePage()
    }
    composable(BottomItem.Gist.route) {
        GistPage()
    }
    composable(BottomItem.Conf.route) {
        ConfPage()
    }
}

fun NavGraphBuilder.routeGist() {
    composable(GistItem.Camera.route) {
        CameraPage()
    }
    composable(GistItem.Listing.route) @ExperimentalFoundationApi {
        ListingPage()
    }
    composable(GistItem.Images.route) @ExperimentalFoundationApi {
        ImagesPage()
    }
    composable(GistItem.Videos.route) @ExperimentalFoundationApi {
        VideosPage()
    }
    composable(GistItem.Video.route) {
        VideoPage()
    }
    composable(GistItem.VideoRecycler.route) {
        VideoRecyclerPage()
    }
    composable(GistItem.SinSpray.route) {
        SinSpray()
    }
    composable(GistItem.SinSpray2.route) {
        SinSpray2()
    }
    composable(GistItem.ScrollDragBox.route) {
        ScrollDragBox()
    }
    composable(GistItem.PathDataParser.route) {
        PathDataParser()
    }
    composable(GistItem.WebViewBox.route) {
        WebViewBox()
    }
    composable(GistItem.WebViewX5Box.route) {
        WebViewX5Box()
    }
}