package com.example.jcmdemo.ui.page

import com.example.jcmdemo.R


enum class GistItem(var route: String, var icon: Int) {
    Home("home", R.drawable.ic_home),
    Conf("conf", R.drawable.ic_conf),
    Camera("camera", R.drawable.ic_camera),
    Listing("listing", R.drawable.ic_view_list),
    Images("images", R.drawable.ic_photo_library),
    Videos("videos", R.drawable.ic_video_library),
    Video("video", R.drawable.ic_ondemand_video),
    VideoRecycler("video_recycler", R.drawable.ic_featured_video),
    SinSpray("sin_spray", R.drawable.ic_broken_image),
    SinSpray2("sin_spray2", R.drawable.ic_broken_image),
    ScrollDragBox("scroll_drag_box", R.drawable.ic_view_carousel),
    ScrollCarousel("scroll_carousel", R.drawable.ic_view_carousel),
    ScrollCarousel2("scroll_carousel2", R.drawable.ic_view_carousel),
    PathDataParser("path_data_parser", R.drawable.ic_image),
    WebViewBox("web_view_box", R.drawable.ic_web),
    WebViewX5Box("web_view_x5_box", R.drawable.ic_web),
    ImageCropperPage("image_cropper", R.drawable.ic_crop),
    ImageCropper2Page("image_cropper2", R.drawable.ic_crop),
//    Conf1("conf", R.drawable.ic_conf),
//    Conf2("conf", R.drawable.ic_conf),
//    Conf3("conf", R.drawable.ic_conf),
}