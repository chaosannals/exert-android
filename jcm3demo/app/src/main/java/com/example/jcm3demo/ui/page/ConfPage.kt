package com.example.jcm3demo.ui.page

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3demo.ui.LauncherSwitch

@ExperimentalMaterial3Api
@Composable
fun ConfPage() {
    val context = LocalContext.current
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextButton(
            onClick = {
                val name = LauncherSwitch.currentLauncher(context)
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
            },
        ) {
            Text(
                text="Current Launcher"
            )
        }
        LauncherSwitch.APP_SET.forEach {
            TextButton(
                onClick = {
                    LauncherSwitch.enableLauncher(context, it)
                },
            ) {
                Text(text = it)
            }
        }
        TextButton(
            onClick = {
                val name = LauncherSwitch.currentLauncher(context)
                LauncherSwitch.restart(context, name!!, true)
            },
        ) {
            Text("Restart")
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun ConfPagePreview() {
    ConfPage()
}