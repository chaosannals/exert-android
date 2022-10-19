package com.example.anidemo.ui.page

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.anidemo.LocalMainScroller
import com.example.anidemo.LocalNavController
import com.example.anidemo.ui.page.employee.EmployeeBean
import com.google.gson.Gson

@Composable
fun HomePage() {
    val nc = LocalNavController.current
    val scroller = LocalMainScroller.current

    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroller),
    ) {
        Button(onClick = {
            nc.navigate("employee")
        }) {
            Text("employee")
        }

        Button(onClick = {
            nc.navigate("employee/listing")
        }) {
            Text("employee listing")
        }

        Button(onClick = {
            // 通过挂一个 bundle 传递数据，看着就很怪，目前测试拿不到参数，不知道是不是引用了安全传参库的缘故。（建议还是使用 安全传参 替代这种方法）
            val eb = EmployeeBean(1, "name", "2134324")
            nc.currentBackStackEntry?.arguments?.putParcelable("employee", eb)
            nc.navigate("employee/listing")
        }) {
            Text("employee listing add bundle")
        }

        Button(onClick = {
            // 通过挂一个 bundle 传递数据，看着就很怪，目前测试拿不到参数，不知道是不是引用了安全传参库的缘故。（建议还是使用 安全传参 替代这种方法）
            val eb = EmployeeBean(1, "name", "2134324")
            val json = Uri.encode(Gson().toJson(eb))
            nc.navigate("employee/listing?args=${json}")
        }) {
            Text("employee listing args")
        }
    }
}

@Preview(widthDp = 375)
@Composable
fun HomePagePreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalMainScroller provides rememberScrollState(),
    ) {
        HomePage()
    }
}