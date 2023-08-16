package com.example.hlitdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// AndroidManifest.xml 指定使用自定义类，不能少。。。
@HiltAndroidApp
class MainApplication : Application() {
}