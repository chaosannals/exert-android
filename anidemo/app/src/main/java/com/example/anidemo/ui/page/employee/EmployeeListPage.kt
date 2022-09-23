package com.example.anidemo.ui.page.employee

import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.anidemo.LocalNavController

// @RequiresApi(33)
@Composable
fun EmployeeListPage(
    eb: EmployeeBean? = null,
    eba: EmployeeBean? = null,
) {
    val nc = LocalNavController.current

    // 目前测试拿不到参数，不知道是不是引用了安全传参库的缘故。
    // 可能因为太怪，已经不建议这么做，getParcelable 方法被标记为遗弃。
    // 下面的方法重载带 class 类型，需要 API 33 @RequiresApi(33)
    //    val eb = nc.previousBackStackEntry?.arguments?.getParcelable("employee", EmployeeBean::class.java)


    Column (
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (eb == null) {
            Text("eb is null")
        }else {
            Text("id: ${eb.id}  name: ${eb.name} jobNumber: ${eb.jobNumber}")
        }

        if (eba == null) {
            Text("eba is null")
        } else {
            Text("id: ${eba.id}  name: ${eba.name} jobNumber: ${eba.jobNumber}")
        }

        for (i in 1..100) {
            Button(
                onClick = {
                    nc.navigate("employee/info/${i}")
                },
            ) {
                Text(text = i.toString())
            }
        }
    }
}

@Preview(widthDp = 375)
@Composable
fun EmployeeListPagePreview() {
    EmployeeListPage()
}