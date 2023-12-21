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
        minSdk = 26
        targetSdk = 34
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
    // 但是之后 openFd 打开 zip 又不报错了。（可能是缓存 bug）
//    androidResources {
//        noCompress.add("zip")
////        ignoreAssetsPatterns.add("!**/*.zip")
//    }

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

    val navVersion = "2.7.5"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    val media3Version="1.2.0"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

    val cameraxVersion = "1.2.2"
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-video:${cameraxVersion}")

    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")

    val coilVersion = "2.5.0"
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil-gif:$coilVersion")
    implementation("io.coil-kt:coil-svg:$coilVersion")
    implementation("io.coil-kt:coil-video:$coilVersion")

    // 视频压缩 这个库没发布在中央仓库，用 www.jitpack.io 源
    implementation("com.github.yellowcath:VideoProcessor:2.4.2")

    // 图片压缩 这个库没发布在中央仓库，源丢失了，可能发布的 jcenter
//    implementation("top.zibin:Luban:1.1.8")

    val ktorVersion = "2.3.7"
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
