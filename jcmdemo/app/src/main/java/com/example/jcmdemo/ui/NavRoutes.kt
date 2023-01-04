package com.example.jcmdemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jcmdemo.ui.page.*
import com.example.jcmdemo.ui.page.carousel.*
import com.example.jcmdemo.ui.page.form.FileDialogPage
import com.example.jcmdemo.ui.page.form.InputFormPage
import com.example.jcmdemo.ui.page.graphic2d.PathDataParserPage
import com.example.jcmdemo.ui.page.graphic2d.SinSpray2Page
import com.example.jcmdemo.ui.page.graphic2d.SinSprayPage
import com.example.jcmdemo.ui.page.tool.*
import com.example.jcmdemo.ui.page.window.PopupPage

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

    // navigation 的路由路径和 composable 路由路径不管哪一级都是全称
    // 所以不能重复，且跳转时不需要拼接。
    navigation(GistItem.Carousel.route, "carousel") {
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
        composable(GistItem.Carousel2.route) {
            Carousel2Page()
        }
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
    composable(GistItem.PictureViewPage.route) {
        PictureViewerPage()
    }
    composable(GistItem.PopupPage.route) {
        PopupPage()
    }

    navigation(GistItem.InputFormPage.route, "form") {
        composable(GistItem.FileDialogPage.route) {
            FileDialogPage()
        }
        composable(GistItem.InputFormPage.route) {
            InputFormPage()
        }
    }
}