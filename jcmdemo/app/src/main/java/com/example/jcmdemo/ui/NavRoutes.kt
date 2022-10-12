package com.example.jcmdemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.graphics.Color
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
    composable(GistItem.ScrollCarousel.route) {
        ScrollCarousel(
            height = 400.sdp,
        ) {
            for (i in 0..8) {
                val color = when(i % 3) {
                    0 -> Color.Red
                    1 -> Color.Green
                    2 -> Color.Blue
                    else -> Color.White
                }
                ScrollDragItem(
                    color = color,
                    title = i.toString(),
                    titleColor = Color.White,
                    titleSize = 44.ssp,
                )
            }
        }
    }
    composable(GistItem.ScrollCarousel2.route) {
        ScrollCarousel2(
            height = 400.sdp,
        ) {
            for (i in 0..8) {
                val color = when(i % 3) {
                    0 -> Color.Red
                    1 -> Color.Green
                    2 -> Color.Blue
                    else -> Color.White
                }
                ScrollDragItem(
                    color = color,
                    title = i.toString(),
                    titleColor = Color.White,
                    titleSize = 44.ssp,
                )
            }
        }
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
    composable(GistItem.ImageCropperPage.route) {
        ImageCropperPage()
    }
    composable(GistItem.ImageCropper2Page.route) {
        ImageCropper2Page()
    }
}