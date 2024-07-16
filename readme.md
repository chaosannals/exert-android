# android

## 路径

- dataDir 应用私有路径，只有 root 后才能看到
- filesDir 应用私有路径，在 dataDir 里面，只有 root 后才能看到，放文件。
- getExternalFilesDir(your_folder) 需要指定目录名,不需要 root 就可以查看，放文件。
- cacheDir 应用私有路径，在 dataDir 里面，只有 root 后才能看到，放缓存。
- externalCacheDir 应用私有路径，不需要 root 就可以查看，放缓存。

要通过 Intent 分享内部路径的文件，高版本安卓需要用 FileProvider.getUriForFile 的同时把整个 扩展跟都暴露出去。

```xml
<external-path name="external_root" path="."/>
```

## 图标生成

[AndroidAssetStudio](https://github.com/romannurik/AndroidAssetStudio)

这个项目是个网页项目他的 github.io 可以生成图标文件。

Android Studio 自带的工具导入图片可以，导入图标后会有一些机型有问题。

AndroidAssetStudio 不维护，又推荐了这个，界面基本类似，感觉更高大上了。还支持 ios 的图标。
[icon kitchen](https://icon.kitchen/)

## WebView chrome 远程调试

PC 网页打开 chrome://inspect/#devices
兼容很差，调试端和被调试端不同版本的会导致 inspect 打开 404 .
官方 chrome 安装不能多版本共存。网上找别人的绿色版本，根据要调试的机型的 chrome 版本去下载，这样多个版本就可以调试不同版本的 chrome 了。

```kotlin
// 全局打开调试模式，连上设备，在 PC 的页面链接。
WebView.setWebContentsDebuggingEnabled(true)
```

## Kotlin Multiplatform Mobile

KMM 目录结构类似 Futter ，但目前结构混乱，同样是以多平台目录配合公共目录的形式，公共目录里面居然混入了 iosMain 这种特定平台的东西。

androidApp 和 iosApp 也是类似 Flutter 那种纯粹的启动器。而是有部分业务代码混入。 Greeting 这种业务示例按理说应该只出现在 shared 目录下。

不过好在新建的空白项目 android 下还能正常启动。

## Jetpack Compose

```kotlin
// 使用了 WindowCompat.setDecorFitsSystemWindows(window, false) 读出的 padding 值就会变成 0
WindowInsets.statusBars
LocalWindowInsets.current // 弃用

// 在调用 WindowCompat.setDecorFitsSystemWindows(window, false) 前先把值读出来
val resourceId = applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
if (resourceId > 0) {
    val height = applicationContext.resources.getDimensionPixelSize(resourceId) // 高度值
}

WindowCompat.setDecorFitsSystemWindows(window, false)
```

### Modifier 修改器

修改器本身带顺序，所以表现力上比  CSS 要好很多。
例子：.clickable {}.clip()  和 clip().clickable{} 区别在于裁剪区和点击区。而 CSS 则不能，因为各个属性都是申明式，先后顺序是属性优先级决定的。 backgroud: red; width 100px;  和 width: 100px background: red 一样的。


## 目录

### anidemo
### jcmdemo

- WebView X5


## 逆向分析工具

### [https://github.com/MobSF/Mobile-Security-Framework-MobSF](Mobile-Security-Framework-MobSF)

## Android Studio

现在官网下载 Android Studio 后 Windows 下通过管理界面下载 命令行工具 ndk 等，新版本下很多原有工具默认不安装。

## Kotlin Jetpack Compose

### 预览（@Preview）与 自定义的 CompositionLocal

自定义提供的 staticCompositionLocalOf 和 compositionLocalOf 在预览里面必须通过 CompositionLocalProvider 提供，不然会毫无报错且不可预览（之后不知道会不会改进报错）。

## 迁移 avd 仿真

~/.android/avd/Nexus_4_API_29.avd 移动到 /path/to/.android/avd/Nexus_4_API_29.avd
注：.android/avd 这个目录结构是必须的可能因为 path.rel 配置。

修改 ~/.android/avd/Nexus_4_API_29.ini 的 path 到新位置
```ini
avd.ini.encoding=UTF-8
path=\path\to\.android\avd\Nexus_4_API_29.avd
path.rel=avd\Nexus_4_API_29.avd
target=android-29
```

注：修改后要重启 Android Studio 的管理器，不然会识别不到状态。

## ExoPlayer（新出的 media3 已经进入 RC 阶段，之后谷歌会停止ExoPlayer维护，之后用 Media3 替代）

通过 GitHub 拉取源码，编译

### ffmpeg 软解码

```bash
# 以下命令大多需要 root 权限，sudo。

# 安装依赖

apt install ninja-build
# android ndk 需要手动下载解压，NDK_PATH 要设置
# 安装 gradle 和 Android sdk
apt install gradle android-sdk

# 启用代理， WSL 代理监听端口要 0.0.0.0 不要只接收 127.0.0.1 
apt install android-sdk -o Acquire::http::proxy=http://172.18.208.1:1088

# 失败会提示使用下面命令修复
apt-get update --fix-missing

# 如果 找不到 可以用 find 找，不过系统太大会很卡。
find / -name sdkmanager
#设置环境变量，构建脚本需要
ANDROID_HOME="/usr/lib/android-sdk"
# 或者在 ExoPlayer 项目下设置 local.properties 指定  sdk.dir=/usr/lib/android-sdk  

# /mnt/d/Wsl/cmdline-tools/bin
# 需要命令工具
mkdir -p /usr/lib/android-sdk/cmdline-tools/latest
cp -r /mnt/d/Wsl/cmdline-tools/* /usr/lib/android-sdk/cmdline-tools/latest

# 接受协议，执行后要 y 接受协议。
/usr/lib/android-sdk/cmdline-tools/latest/bin/sdkmanager --licenses

FFMPEG_MODULE_PATH="$(pwd)/extensions/ffmpeg/src/main"
FFMPEG_PATH="${FFMPEG_MODULE_PATH}/jni/ffmpeg"
# 目前文档是 ndk r21 ，使用 r25 报错，其他版本未测试。
NDK_PATH="/mnt/d/Wsl/android-ndk-r21e"
# 可以更多类型 
# Sample format	 | Decoder name(s)
# Vorbis	vorbis
# Opus	opus
# FLAC	flac
# ALAC	alac
# PCM μ-law	pcm_mulaw
# PCM A-law	pcm_alaw
# MP1, MP2, MP3	mp3
# AMR-NB	amrnb
# AMR-WB	amrwb
# AAC	aac
# AC-3	ac3
# E-AC-3	eac3
# DTS, DTS-HD	dca
# TrueHD	mlp truehd

# ENABLED_DECODERS=(vorbis opus flac)
ENABLED_DECODERS=(vorbis opus flac alac pcm_mulaw pcm_alaw mp3 amrnb amrwb aac ac3 eac3 truehd)

HOST_PLATFORM="linux-x86_64"
# HOST_PLATFORM="darwin-x86_64"

ln -s "$FFMPEG_PATH" ffmpeg

# 生成 二进制 在 extensions/ffmpeg/src/main/jni 目录下执行
./build_ffmpeg.sh "${FFMPEG_MODULE_PATH}" "${NDK_PATH}" "${HOST_PLATFORM}" "${ENABLED_DECODERS[@]}"

# ExoPlayer 项目下执行 打包 aar
./gradlew extension-ffmpeg:assembleRelease
```

## 证书

```bash
# 通过安卓自带工具查看证书信息，需要输入证书 key
keytool -list -v -keystore jcm3demo.jks

# 导出证书文件
keytool -exportcert -alias key0 -keystore /c/path/to/key.jks -file /c/path/to/key.cer
# -export 和 -exportcert 目前看是一样的
keytool -export -alias key0 -keystore /c/path/to/key.jks -file /c/path/to/key.cer
# Windows 下 打开 .cer 文件可以查看  详细信息  里面有  公钥
# TODO 找到命令直接输出 公钥 hex 格式

# 打印 公钥 和 证书 base64 格式
keytool -list -rfc --keystore /c/path/to/key.jks | openssl x509 -inform pem -pubkey

# 上面的指令 老版本加 -v 参数可以输出 MD5 指纹
# 此版的证书 java8 带的 keytool 无法识别
# 但是 MD5 被抛弃后，新版本加 -v 只有 SHA1 和 SHA256
# 通过把 输出结果传递给 openssl 去取得 MD5
# openssl 通过 msys2 使用。
keytool -exportcert -alias key0 -keystore /c/path/to/key.jks | openssl dgst -md5 

# 默认的 Android Studio 生成的调试证书放在用户目录下，密码是 Android 或 android
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore |  openssl dgst -md5 

# 调试证书在用户目录  .android 下 debug.keystore 通过下面命令查看 SHA1
# 密码: android
keytool -list -v -keystore debug.keystore -storepass android -keypass android

# 生成证书
# 需要 Java JDK
# [alias] 为别名
# [test] 为文件名
keytool -genkey -alias [alias] -keyalg RSA -keysize 2048 -validity 36500 -keystore [test].keystore

# 如果不存在 debug.keystore 生成，注意要尽量用原版的信息。很多默认命令依赖这些信息。
# 密码最好是 android ，别名密码也最好是 android 。
keytool -genkey -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 36500 -keystore debug.keystore

# jks 转 keystore
# 输入以下命令后会依次要求输入
# 1. keystore 新密钥，因为我们是转换，所以使用原 jks 的密钥
# 2. 再输入一遍，新密钥
# 3. 源（jks）密钥，因为是转换所以同上
# 4. keyAlias 的密钥 keyPassword
keytool -importkeystore -srckeystore ./release.jks -srcstoretype JKS -deststoretype PKCS12 -destkeystore ./release.keystore
```

```bash
# 手动签名
apksigner.bat sign --ks ~/.android/debug.keystore --ks-pass pass:android path/to/your.apk
```

## 百度地图

百度地图 官方文档使用 v7.4.0 没有
SDKInitializer.setAgreePrivacy(application, true)
LocationClient.setAgreePrivacy(true)
等这些方法，可能没有权限问题的限制，但是可能涉及窃取隐私。

仓库 v7.5.0 之后有该方法，需要同意出卖隐私，否则有部分功能不可用。
（其实就是让你选择要不要出卖隐私，老版本默认出卖。）


开发调试时鉴权失败，直接复制 SHA1 去填开发 SHA1。或者 debug.keystore 的。


## 深度链接 Deep Link

```bash
# 通过 URL Intent 唤起 app24
adb shell am start -W -a android.intent.action.VIEW -d "http://www.example.com/app24" com.example.app24

# 因为注册了多种，所以以下也是可以的。
adb shell am start -W -a android.intent.action.VIEW -d "app1://www.example1.com/path1" com.example.app24
adb shell am start -W -a android.intent.action.VIEW -d "app2://www.example2.com/path2" com.example.app24

# AndroidManifest.xml 注册的 intent-filter 的 data 是混合过滤的。
adb shell am start -W -a android.intent.action.VIEW -d "app2://www.example1.com/path2" com.example.app24

# 传参 Kotlin Activity
adb shell am start -W -a android.intent.action.VIEW -d "app4://www.example4.com/p/ath2?a=123&b=432432"

# 传参 Java Activity
adb shell am start -W -a android.intent.action.VIEW -d "app44://www.example44.com/p/ath2?a=123&b=432432"
```


## Activity 过渡动画

```kotlin
// 在 setContent 或 setContentView 之前调用
window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
window.enterTransition = Fade()
window.exitTransition = Fade()

// 唤起该带过渡动画Activity 的 Intent 要带上参数。makeSceneTransitionAnimation
val intent = Intent()
intent.setClass(context, MainActivity::class.java)
context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity).toBundle())
```

## LiveData 和 StateFlow

StateFlow 是个异步的 Rx ，数据跟对象，没做自动释放数据的处理。
LiveData 在 StateFlow 的基础上会根据 Activity Fragment 生命周期自动删除数据。

注：很多数据都是挂全局，从这点上看，LiveData 代码上做多了反而不好。


## 路由

返回队列默认是  null  和  NavHost 的 startDestination 两个元素，即 backQueue.size == 2。
返回队列的类型声明是  ArrayDeque<NavBackStackEntry> 也就是非空，第一个元素却出现了 null 。
清空 backQueue 后使用 navigate 会重新往里面添加 null 和 导航目地项。

注：多级的 BackHandler 里使用 navigateUp() 会导致 backQueue.size 计数混乱 == 0 而实际 backQueue 元素可能不为空（比如 3 个）。

```kotlin
// 返回队列
navController.backQueue

// 返回
navController.navigateUp()
// 返回
navController.popBackStack()
```

## 调试用图片链接

```
https://picsum.photos/id/$pid/400/400.jpg
```

## 代理

```
Auto-detect proxy settings:
腾讯： https://mirrors.cloud.tencent.com/AndroidSDK/
阿里： https://mirrors.aliyun.com/android.googlesource.com/
```

## 常见报错

```
error: string too large to encode using UTF-8 written instead as 'STRING_TOO_LARGE'.
```

注：仍然没有解决，只要是新项目就不会，但是 2022 年建的项目升级开发库后就会。

注：以下的操作有时候有效。但是还是会报这个错。

注：这个报错不会影响到编译，只会出现一条报错。但是却影响到任务组合，如果 gradle 脚本组合使用了编译任务，会不执行之后的操作，第二次执行时，因为此任务已经做过有缓存会被跳过，得以继续。所以需要2次才能执行完组合任务。

这个是由于有一些文件太大，Java 限制的内存太少了。

配置 gradle.properties 把内存加大到不报错。


```ini
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8

org.gradle.jvmargs=-Xmx8192m -Dfile.encoding=UTF-8
```
