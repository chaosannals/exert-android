package com.example.calendardemo.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.calendardemo.ui.page.HomePage
import com.example.calendardemo.ui.page.calendar.CalendarAccountPage
import com.example.calendardemo.ui.page.calendar.CalendarEventPage

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