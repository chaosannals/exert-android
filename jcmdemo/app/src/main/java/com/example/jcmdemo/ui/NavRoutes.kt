package com.example.jcmdemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.jcmdemo.ui.page.*
import com.example.jcmdemo.ui.page.carousel.CarouselPage
import com.example.jcmdemo.ui.page.carousel.ScrollCarousel2Page
import com.example.jcmdemo.ui.page.carousel.ScrollCarouselPage
import com.example.jcmdemo.ui.page.carousel.ScrollDragPage
import com.example.jcmdemo.ui.page.graphic2d.PathDataParserPage
import com.example.jcmdemo.ui.page.graphic2d.SinSpray2Page
import com.example.jcmdemo.ui.page.graphic2d.SinSprayPage
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
        SinSprayPage()
    }
    composable(GistItem.SinSpray2.route) {
        SinSpray2Page()
    }
    composable(GistItem.ScrollDragBox.route) {
        ScrollDragPage()
    }
    composable(GistItem.ScrollCarousel.route) {
        ScrollCarouselPage()
    }
    composable(GistItem.ScrollCarousel2.route) {
        ScrollCarousel2Page()
    }
    composable(GistItem.Carousel.route) {
        CarouselPage()
    }
    composable(GistItem.PathDataParser.route) {
        PathDataParserPage()
    }
    composable(GistItem.WebViewBox.route) {
        WebViewBox()
    }
    composable(GistItem.WebViewX5Box.route) {
        WebViewX5Box()
    }
    composable(GistItem.WebViewX5Map.route) {
        WebViewX5Map()
    }
    composable(GistItem.ImageCropperPage.route) {
        ImageCropperPage()
    }
    composable(GistItem.ImageCropper2Page.route) {
        ImageCropper2Page()
    }
}