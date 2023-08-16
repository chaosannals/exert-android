package com.example.bootdemo

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.bootdemo.ui.MainScaffold
import com.example.bootdemo.ui.theme.AppShellTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@Composable
inline fun <reified VM : ViewModel> hiltActivityViewModel(): VM {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val defaultStoreOwner = LocalViewModelStoreOwner.current
    val vmStoreOwner = remember(inspectionMode, context, defaultStoreOwner) {
        if (inspectionMode) {
            checkNotNull(defaultStoreOwner) {
                "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
            }
        } else {
            context as MainActivity
        }
    }

    return hiltViewModel(vmStoreOwner)
}

// AndroidManifest.xml 指定使用自定义类，不能少。。。
// 这个类也是必须定义。
@HiltAndroidApp
class MainApplication : Application() {
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.enterTransition = Fade()
        window.exitTransition = Fade()
        setContent {
            MainScaffold()
        }
    }
}