package com.example.appshell.ui.page


import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.db.WebViewConf
import com.example.appshell.db.ensureWebViewConf
import com.example.appshell.ui.si
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5WebShell


@Composable
fun HomePage() {
    val context = LocalContext.current
    var conf: WebViewConf? by remember {
        mutableStateOf(null)
    }

    ensureWebViewConf { conf = it }

    val sbb = WindowInsets.statusBars.getBottom(LocalDensity.current)
    val sbt = WindowInsets.statusBars.getTop(LocalDensity.current)

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        conf?.let {c ->
            X5WebShell(c) {
                it.evaluateJavascript("""
                    javascript:
                    ${c.valName}.tabbarHeight = ${49.si};
                    ${c.valName}.statusbarHeight = ${sbt - sbb};
                    ${c.valName}.token = "${c.token}";
                    """.trimIndent()) {
                    //Toast.makeText(context, "set ${c.valName} $it", Toast.LENGTH_LONG).show()
                }
//                val code = "javascript:${c.valName}.say();"
                val code = "javascript:JSON.stringify(${c.valName});"
                Log.d("x5demo", "call ${code}")
                it.evaluateJavascript("${code}") {
                    Toast.makeText(context, "info: $it", Toast.LENGTH_LONG).show()
                    Log.d("x5demo", "called")
                }

                it.evaluateJavascript("""
                    javascript:
                    ${c.valName}.Toast("AAAABBBB");
                    ${c.valName}.toggleTabbarVisibile(true);
                    ${c.valName}.toggleTabbarVisibile(false);
                    ${c.valName}.invalidToken();
                    ${c.valName}.Say();
                """.trimIndent()) {
                    // Toast.makeText(context, "call toast: $it", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
//    BackHandler(enabled = false) {
//        Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
//    }
}

@Preview
@Composable
fun HomePagePreview() {
    DesignPreview {
        HomePage()
    }
}