package com.example.bootdemo.ui.page.canback

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.bootdemo.hiltActivityViewModel
import com.example.bootdemo.ui.LocalRouter
import com.example.bootdemo.ui.MainScaffoldViewModel
import com.example.bootdemo.ui.widget.finishDialogVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CanBackButton(
    text: String,
    route: String,
) {
    val mode = LocalInspectionMode.current
    val router = if (mode) null else LocalRouter.current

    Button(
        onClick = { router?.navigate(route) },
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun CanBackPage(
    activityVm: MainScaffoldViewModel = hiltActivityViewModel()
) {
//    val router = rememberNavController() // 不应该多个 rememember 的 控制器
    val router = if (LocalInspectionMode.current) null else LocalRouter.current
    val coroutineScope = rememberCoroutineScope()
    val canBack by activityVm.canBack.observeAsState(false)

    // 多个 BackHandler 会导致 backQueue.size 混乱
    BackHandler(canBack) {
        Log.d("bootdemo", "CanBackPage BackHandler call")
        router?.run {
            Log.d("bootdemo", "CanBackPage BackHandler size: ${backQueue.size}")
            if (backQueue.isEmpty() || backQueue.size == 2) {
                finishDialogVisible.value = true
            } else {
                coroutineScope.launch(Dispatchers.Main) {
                    navigateUp()
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
    ) {
        CanBackButton("Lv1（平坦）", "can-back-lv1")
        CanBackButton("Lv1 2（Navigation）", "can-back-2-lv1")
        CanBackButton("Lv1 3", "can-back-3-lv1")
    }
}

@Preview
@Composable
fun CanBackPagePreview() {
    CanBackPage()
}