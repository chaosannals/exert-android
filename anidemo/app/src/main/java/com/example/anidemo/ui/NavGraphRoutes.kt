package com.example.anidemo.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.anidemo.LocalNavController
import com.example.anidemo.ui.page.HomePage
import com.example.anidemo.ui.page.employee.EmployeeInfoPage
import com.example.anidemo.ui.page.employee.EmployeeListPage

@Composable
fun NavGraphRoutes(
    scroller: ScrollState,
    paddingValues: PaddingValues,
) {
    val pTop = paddingValues.calculateTopPadding()
    val pBottom = paddingValues.calculateBottomPadding()

    NavHost(
        LocalNavController.current,
        modifier = Modifier
            .padding(top = pTop, bottom = pBottom)
            .verticalScroll(scroller),
        startDestination = "home", // 默认路由
    )
    {
        composable("home") {
            HomePage()
        }

        // 路由 route 会直接导航到 startDestination ，确保 startDestination 确实存在。
        // 即：路由 employee 会直接导航到 employee/listing 。
        navigation("employee/listing", "employee") {

            composable("employee/listing") {
                EmployeeListPage()
            }

            composable(
                route = "employee/info/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.LongType },
                ),
            ) {
                val id = it.arguments?.getLong("id")
                EmployeeInfoPage(id)
            }
        }
    }
}