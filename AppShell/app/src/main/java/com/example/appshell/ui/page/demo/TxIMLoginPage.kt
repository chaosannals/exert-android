package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.TxIM
import com.example.appshell.ui.widget.DesignPreview
import com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED
import com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINING
import com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGOUT
import com.tencent.imsdk.v2.V2TIMUserStatus

@Composable
fun TxIMLoginPage() {
    val totalStatus = LocalTotalStatus.current

    var userId by remember {
        mutableStateOf("test-1")
    }
    var userSignature by remember {
        mutableStateOf("")
    }

    LaunchedEffect(totalStatus) {
        if (TxIM.loginStatus == V2TIM_STATUS_LOGINED) {
            totalStatus.routeTo("tx-im-page")
        }
        // V2TIM_STATUS_LOGINED 1
        // V2TIM_STATUS_LOGINING 2
        // V2TIM_STATUS_LOGOUT 3
    }

    // 登录
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text("User Id")
            },
            value = userId,
            onValueChange = { userId = it },
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text("User Signature")
            },
            value = userSignature,
            onValueChange = { userSignature = it },
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick =
            {
                TxIM.login(userId, userSignature) {
                    totalStatus.routeTo("tx-im-page")
                }
            },
        ) {
            Text(
                text = "登录"
            )
        }
    }

}

@Preview
@Composable
fun TxIMLoginPagePreview() {
    DesignPreview {
        TxIMLoginPage()
    }
}