val navVersion: String by rootProject.extra
val media3Version: String by rootProject.extra
val cameraxVersion: String by rootProject.extra
val coilVersion: String by rootProject.extra
val ktorVersion: String by rootProject.extra

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.jcm3ui"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jcm3ui"
        minSdk = 26 // 这个如果要进应用商店，大概率也要大于等于30，虽然明文规定是 TargetSdkVesion>=30
        // 其实这个规定应该是在应用商店的审核早就实行了一部分，因为很多带了旧权限的都不过审。
        targetSdk = 34 // 修改这个版本可以让 APP 使用旧的无需权限API（如文件），但是过不了审核。
        // 国家要求在 2024 年 1月起把 API 低于 30 的都不能上架应用商店。而这些需要权限基本都在这个版本加入。
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        // 高版本 Android Studio 的断点查看闭包变量值功能，会强制等待 adb
        // 覆盖默认配置，默认配置会强制要求等待 adb 。
        // 覆盖默认配置又会丢失 断点调试数据的获取。
//        debug {
//            isDebuggable = true
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // 不起效，assets 里的 zip 文件还是会被压缩导致不能 openFd 拿到大小。
    // 修改后要卸载 app 后重装起效。
    androidResources {
        noCompress.add("zip")
//        ignoreAssetsPatterns.add("!**/*.zip")
    }

//    androidComponents {
//        onVariants(selector().all()) {
//            it.androidResources.noCompress.add(".zip")
//        }
//    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("androidx.navigation:navigation-compose:$navVersion")

    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-video:${cameraxVersion}")

    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")

    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil-gif:$coilVersion")
    implementation("io.coil-kt:coil-svg:$coilVersion")
    implementation("io.coil-kt:coil-video:$coilVersion")

    // 视频压缩 这个库没发布在中央仓库，用 www.jitpack.io 源
    implementation("com.github.yellowcath:VideoProcessor:2.4.2")

    // 图片压缩 这个库没发布在中央仓库，源丢失了，可能发布的 jcenter
//    implementation("top.zibin:Luban:1.1.8")

    // 音频转换 未发布中心仓库，用 https://jitpack.io
    //implementation("com.github.adrielcafe:ffmpeg-android-java:v0.3.2") // 没仓库
//    implementation("com.github.adrielcafe:AndroidAudioConverter:0.0.8")
//    implementation("io.auxo.ame:ame-lite:0.1")

//    implementation("nl.bravobit:android-ffmpeg:1.1.7")  // 没仓库

    // ktor clien
    implementation("io.ktor:ktor-client-core:$ktorVersion")
//    implementation("io.ktor:ktor-client-okhttp:$ktorVersion") // 服务器传输后直接断开会抛出异常。
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    // ktor server
    implementation("io.ktor:ktor-server-jetty:$ktorVersion") // 至少 minSDK26
    implementation("io.ktor:ktor-server-cors:$ktorVersion") // 跨域
    implementation("io.ktor:ktor-server-compression:$ktorVersion") // 压缩
    implementation("io.ktor:ktor-network-tls-certificates:$ktorVersion") // 证书 SSL HTTPS

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
