package com.example.jcm3demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.baidu.location.LocationClient
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.common.BaiduMapSDKException
import com.example.jcm3demo.ui.MainLayout
import com.example.jcm3demo.ui.U
import com.example.jcm3demo.ui.page.tool.writeLog
import com.example.jcm3demo.ui.sdp
import com.example.jcm3demo.ui.theme.Jcm3demoTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 7.4.0 没有这些接口，7.5.0 有并需要同意才能使用。
        //SDKInitializer.setAgreePrivacy(application, true)
        //  v9.2.9版本起加入
        //LocationClient.setAgreePrivacy(true)
        try {
            SDKInitializer.initialize(application)
            SDKInitializer.setHttpsEnable(true)
            SDKInitializer.setCoordType(CoordType.BD09LL)
            BDL.init(application, this)
        } catch (e : BaiduMapSDKException) {
            writeLog(this,"bdm error : ${e.message} ${e.stackTraceToString()}")
        }
        writeLog(this,"displayDp : ${U.displayDp}  375 -> ${375.sdp}")
        setContent {
            Jcm3demoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
                color= colorResource(id = R.color.gray),
                ) {
                    MainLayout()
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Jcm3demoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.gray),
        ) {
            MainLayout()
        }
    }
}