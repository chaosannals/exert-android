import java.net.URI

// Android Studio 自动居然把 include 插到这里。。。。

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI.create("https://www.jitpack.io")
        }
    }
}

rootProject.name = "jcm3ui"
include(":app")
include(":jcm3wv")
include(":app:ffmpeglib")
