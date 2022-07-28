package com.example.jcm3demo.ui

import com.example.jcm3demo.R

enum class MainBottomBarItem(var route: RouteItem, var iconRid: Int, var title: String, var isFloat: Boolean = false) {
    Home(RouteItem.Home, R.drawable.ic_home, "首页"),
    Tool(RouteItem.Tool, R.drawable.ic_apps, "工具", true),
    Conf(RouteItem.Conf, R.drawable.ic_settings_applications, "设置"),
}