// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
//        classpath("com.aliyun.oss:aliyun-sdk-oss:3.15.1") // 这个库，高版本的 kotlin 就会有类型错误。
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}