import com.google.protobuf.gradle.* // kotlin script 需要自行引入命令空间
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//import java.net.http.HttpClient // java11+ 才能用，gradle 要用 java8

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf") version "0.8.19"
}

android {
    namespace = "com.example.bootdemo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.bootdemo"
        minSdk = 24
        targetSdk = 33
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
        kotlinOptions.freeCompilerArgs += "-Xmulti-platform"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // 路由导航
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // DI
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")
    // DI 和 jetpack 导航库关联库
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // SSH Client
    implementation("com.jcraft:jsch:0.1.55")

    // 图片显示
    implementation("io.coil-kt:coil-compose:2.2.0")

    // Jetpack 扩展的 preference 名字空间在 androidx
    // 默认 android 提供的 preference 都被标记未废弃.
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Data Store 弱类型
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0") // Rx 可选

    // Data Store 强类型依赖 ProtoBuf
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-rxjava3:1.0.0") // Rx 可选

    // protobuf
    implementation("com.google.protobuf:protobuf-javalite:3.18.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

abstract class BuildCustomTask: DefaultTask() {
    @get:Input
    @get:Option(option="message", description="some message.")
    abstract val message: Property<String>


    @get:Input
    @get:Option(option="id", description="the id.")
    abstract val id: Property<Int>

    @TaskAction
    fun action1() {
        println("BuildCustomTask action1 ${message.get()}")
    }

    @TaskAction
    fun action2() {
//        println("BuildCustomTask action2 ${id}")
    }
}

// task 会有提示：推荐通过 tasks.register 注册
val buildCustomTask = tasks.register<BuildCustomTask>("buildCustom") {
    println("BuildCustomTask register")

    id.set(44)
    message.set("some message")

    doFirst {

    }

    doLast {

    }
}

tasks.register("buildCustom2") {
    doFirst {

    }

    // kotlin 不需要在 configure 方法内调用
    dependsOn(buildCustomTask)
}

abstract class BuildCustom3Task: DefaultTask() {
    @TaskAction
    fun action1() {
        val url = URL("https://baidu.com")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.useCaches = false

        conn.doOutput = true
        DataOutputStream(conn.outputStream).use { wr ->
            wr.writeBytes("")
        }
        conn.inputStream.use {
            val rd = BufferedReader(InputStreamReader(it))
            val sb = StringBuilder()
            while(true) {
                val line = rd.readLine() ?: break
                sb.append(line)
                sb.append("\r\n")
            }
            println(sb.toString())
        }
    }
}

// kotlin 特有的写法
val buildCustom3 by tasks.registering(BuildCustom3Task::class) {
    val hereDirectory = file("./") // 这个是 build.kts 文件所在目录

    doLast {
        val entities = hereDirectory.listFiles()?.sorted()
        entities?.forEach { entity ->
            if (entity.isFile) {
                println("file: ${entity.absolutePath}")
            }
            if (entity.isDirectory) {
                println("dir: ${entity.absolutePath}")
            }
        }
    }

    doFirst {
        println("build info:")
        println(GradleVersion.current())
        println(GradleVersion.version("8.0"))
        println(layout.buildDirectory.get().asFile.absolutePath)
    }

    dependsOn(buildCustomTask)
}