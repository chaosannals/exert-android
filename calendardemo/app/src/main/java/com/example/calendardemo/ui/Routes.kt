package com.example.calendardemo.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.calendardemo.ui.page.HomePage
import com.example.calendardemo.ui.page.calendar.CalendarAccountPage
import com.example.calendardemo.ui.page.calendar.CalendarEventPage
import com.example.calendardemo.ui.page.web.GoogleWebViewPage
import com.example.calendardemo.ui.page.web.MyWebViewPage
import com.example.calendardemo.ui.widget.GoogleWebView

fun NavGraphBuilder.rootGraph() {
    composable("/") {
        HomePage()
    }
}

fun NavGraphBuilder.calendarGraph() {
    navigation("/calendar/index", "/calendar") {
        composable("/calendar/index") {
            CalendarAccountPage()
        }
        composable("/calendar/event") {
            CalendarEventPage()
        }
    }
}

fun NavGraphBuilder.webGraph() {
    navigation("/web/google", "/web") {
        composable("/web/google") {
            GoogleWebViewPage()
        }
        composable("/web/my") {
            MyWebViewPage()
        }
    }
}