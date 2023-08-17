package com.example.bootdemo.ui

import android.util.Log
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bootdemo.hiltActivityViewModel
import com.example.bootdemo.ui.theme.AppShellTheme
import com.example.bootdemo.ui.widget.BottomBar
import com.example.bootdemo.ui.widget.FinishDialog
import com.example.bootdemo.ui.widget.FloatBall
import com.example.bootdemo.ui.widget.finishDialogVisible
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel 的依赖注入依赖 IO,网络和读写文件
// 预览不能支持使用依赖注入，提示是”ViewModel 不支持预览“。
// 实测，只要不使用依赖注入，ViewModel 还是可以预览的。
@HiltViewModel
class MainScaffoldViewModel @Inject constructor() : ViewModel() {
    val canBack: MutableLiveData<Boolean> = MutableLiveData(false)

    // 因为不支持依赖注入预览，所以放一起了。
    val webView: MutableLiveData<WebView> = MutableLiveData()
}

@Composable
fun MainScaffold(
    model: MainScaffoldViewModel = hiltActivityViewModel()
) {
    val context = LocalContext.current
    val router = rememberNavController()
    val navEntry = router.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    val canBack by model.canBack.observeAsState(false)

    FinishDialog()

    BackHandler() {
        Log.d("bootdemo", "MainScaffold BackHandler ${router.backQueue.size}")
        if (router.backQueue.isEmpty() || router.backQueue.size == 2) {
            finishDialogVisible.value = true
        } else {
            coroutineScope.launch(Dispatchers.Main) {
                router.navigateUp()
            }
        }
    }

    CompositionLocalProvider(
        LocalRouter provides router,
    ) {
        AppShellTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    ) {
                    Column (
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(y = (-100).dp),
                    ) {
                        FloatBall(Icons.Default.Info) {
                            router.run {
                                Log.d("bootdemo", "FloatBall BackHandler router:")
                                backQueue.forEach {
                                    Log.d("bootdemo", "FloatBall BackHandler backQueue route: ${it.destination.route}")
                                }
                            }
                        }
                        FloatBall(Icons.Default.Delete) {
                            router.run {
                                // 1. 全清
                                router.backQueue.clear()
                            }
                        }
                        val icon by remember(canBack) {
                            derivedStateOf {
                                if (canBack) Icons.Default.Done else Icons.Default.Close
                            }
                        }
                        FloatBall(icon) {
                            model.canBack.value = model.canBack.value?.not()
                        }
                    }
                    Column(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxSize()
                    ) {
                        NavHost(
                            router,
                            startDestination = "index",
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            rootGraph()
                            canBackGraph()
                            webGraph()
                            filesystemGraph()
                            lockGraph()
                        }
                        BottomBar()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScaffoldPreview() {
    MainScaffold()
}