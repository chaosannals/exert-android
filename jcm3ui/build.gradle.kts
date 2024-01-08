// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        // 全局配置的定义比 groovy 麻烦，使用也麻烦。
        // 不需要 构造组件的情况下，应该是使用 apply false 的方式统一组件版本
        // 但是 一旦需要构造组件，配置版本传递就很麻烦，出现不统一的情况，plugins 里面拿不到 rootProject..
        // 看到有人把版本写到 properties 文件里的，不过这样相当于引入2种文件格式。
        // 构造脚本的设计缺少统一配置的地方。
        set("navVersion", "2.7.5")
        set("media3Version", "1.2.0")
        set("cameraxVersion", "1.2.2")
        set("coilVersion", "2.5.0")
        set("ktorVersion", "2.3.7")
    }
    dependencies {
        val navVersion: String by rootProject.extra
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20" apply false
    id("androidx.navigation.safeargs") version "2.7.5" apply false
    id("com.android.library") version "8.1.4" apply false
}