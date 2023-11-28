package com.example.jcm3ui.ui.page


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3ui.CameraActivity
import com.example.jcm3ui.ui.routeTo
import com.example.jcm3ui.ui.sdp

@Composable
fun HomeButton(
    title: String,
    path: String,
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(10.sdp)
            .clickable {
                routeTo(path)
            }
    )
}

@Preview
@Composable
fun HomePage() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.Blue)
    ) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.getStringExtra("uri")
                Toast.makeText(context, "uri: ${uri}", Toast.LENGTH_SHORT).show()
            }
        }
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClass(context, CameraActivity::class.java)
            launcher.launch(intent)
        }) {
            Text("相机 Activity")
        }
        HomeButton("文件选择", "demo/file-pick")
        HomeButton("拍摄", "demo/camera-shot")
        HomeButton(title = "Popup -> Dialog 层", path = "demo/popup-make-dialog")
    }
}