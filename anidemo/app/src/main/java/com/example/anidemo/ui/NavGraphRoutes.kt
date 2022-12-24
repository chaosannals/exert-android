package com.example.anidemo.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.anidemo.LocalNavController
import com.example.anidemo.ui.page.GistPage
import com.example.anidemo.ui.page.HomePage
import com.example.anidemo.ui.page.InfoPage
import com.example.anidemo.ui.page.employee.EmployeeArgsType
import com.example.anidemo.ui.page.employee.EmployeeBean
import com.example.anidemo.ui.page.employee.EmployeeInfoPage
import com.example.anidemo.ui.page.employee.EmployeeListPage
import com.example.anidemo.ui.page.pulldown.PullDown2Page
import com.example.anidemo.ui.page.pulldown.PullDownPage
import com.example.anidemo.ui.page.pulldown.PullDownPushUpPage

fun NavGraphBuilder.employeeGraph() {
    // 路由 route 会直接导航到 startDestination ，确保 startDestination 确实存在。
    // 即：路由 employee 会直接导航到 employee/listing 。
    // 要确保 startDestination 的路由和 下面的路由完全一致包括参数的定义。
    // 即：employee/listing?keyword={keyword}&args={args}
    navigation("employee/listing?keyword={keyword}&args={args}", "employee") {

        composable(
            route = "employee/listing?keyword={keyword}&args={args}",
            arguments = listOf(
                navArgument("keyword") {
                    type = NavType.StringType
                    nullable = true },
                navArgument("args") {
                    type = EmployeeArgsType()
                    nullable = true },
            ),
        ) {
            // 目前测试拿不到参数，不知道是不是引用了安全传参库的缘故。（建议使用 安全传参 替代）
            val eb = LocalNavController.current.previousBackStackEntry?.arguments?.getParcelable<EmployeeBean>("employee")

            // 安全传参 需要定义 EmployeeArgsType
            val eba = it.arguments?.getParcelable<EmployeeBean>("args")
            EmployeeListPage(eb, eba)
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

fun NavGraphBuilder.pulldownGraph() {
    composable("pulldown") {
        PullDownPage()
    }
    composable("pulldown2") {
        PullDown2Page()
    }
    composable("pulldownpushup") {
        PullDownPushUpPage()
    }
}

@Composable
fun NavGraphRoutes(
    paddingValues: PaddingValues,
) {
    val pTop = paddingValues.calculateTopPadding()
    val pBottom = paddingValues.calculateBottomPadding()


    NavHost(
        LocalNavController.current,
        modifier = Modifier
            .padding(top = pTop, bottom = pBottom)
            .fillMaxSize(),
        startDestination = "gist", // 默认路由
    )
    {
        composable("home") {
            HomePage()
        }
        composable("gist") {
            GistPage()
        }
        composable("info") {
            InfoPage()
        }

        employeeGraph()
        pulldownGraph()
    }
}