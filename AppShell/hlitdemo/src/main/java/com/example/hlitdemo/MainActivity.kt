package com.example.hlitdemo

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import com.example.hlitdemo.ui.theme.AppShellTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

// Hilt 仅支持扩展 ComponentActivity 的 activity，如 AppCompatActivity。
// Hilt 仅支持扩展 androidx.Fragment 的 Fragment。
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppShellTheme {
                // 谷歌扩展库
//                val systemUiController = rememberSystemUiController()
//                LaunchedEffect(Unit){
//                    while (true) {
//                        delay(4000)
//                        // 这个会导致导航栏也给收掉。
//                        systemUiController.isSystemBarsVisible = false
//                        delay(4000)
//                        systemUiController.isSystemBarsVisible = true
//                    }
//                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppShellTheme {
        Greeting("Android")
    }
}