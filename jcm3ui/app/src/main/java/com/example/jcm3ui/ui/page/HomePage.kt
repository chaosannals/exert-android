package com.example.jcm3ui.ui.page


import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3ui.CameraActivity
import com.example.jcm3ui.PickActivity
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
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.getStringExtra("uri")
                Toast.makeText(context, "camera uri: ${uri}", Toast.LENGTH_SHORT).show()
            }
            if (it.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "camera cancel", Toast.LENGTH_SHORT).show()
            }
        }
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClass(context, CameraActivity::class.java)
            cameraLauncher.launch(intent)
        }) {
            Text("相机 Activity")
        }

        val pickLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.getStringExtra("result")
                Toast.makeText(context, "pick result: ${uri}", Toast.LENGTH_SHORT).show()
            }

            if (it.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "pick cancel", Toast.LENGTH_SHORT).show()
            }
        }
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setClass(context, PickActivity::class.java)
                pickLauncher.launch(intent)
            }
        ) {
            Text("文件选择器 Activity")
        }

        HomeButton("文件选择", "demo/file-pick")
        HomeButton("拍摄", "demo/camera-shot")
        HomeButton(title = "Popup -> Dialog 层", path = "demo/popup-make-dialog")
        HomeButton(title = "压缩", path = "demo/compress")
        HomeButton(title = "缓存", path = "demo/cache")
        HomeButton(title = "缩略（等比）", path = "demo/thumbnail")
        HomeButton(title = "Http", path = "demo/http")
        HomeButton(title = "Intent", path = "demo/intent")
        HomeButton(title = "录音", path = "demo/audio-record")

        HomeButton(title = "布局（LazyVerticalGird）", path = "layout/lazy-vertical-grid")
        HomeButton(title = "布局（LazyVerticalStaggeredGrid）", path = "layout/lazy-vertical-staggered-grid")
        HomeButton(title = "布局（CustomDrawLazyVerticalGird）", path = "layout/custom-draw-lazy-vertical-grid")
    }
}