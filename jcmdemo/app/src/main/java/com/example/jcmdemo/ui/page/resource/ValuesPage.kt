package com.example.jcmdemo.ui.page.resource


import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.BuildConfig
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.R

@Composable
fun ValuesPage() {
    Column() {
        // 构建时 build.gradle buildConfigField 加入，并非代码常量引入。
        Text(text = BuildConfig.ONLINE_URL)
        // 构建时 build.gradle resValue 加入，并非手动修改 res/values/*.xml 文件
        Text(text = stringResource(id = R.string.online_url))
    }
}

@Preview
@Composable
fun ValuesPagePreview() {
    DesignPreview() {
        ValuesPage()
    }
}