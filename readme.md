# android

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

## ExoPlayer

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

# 调试证书在用户目录  .android 下 debug.keystore 通过下面命令查看 SHA1
keytool -list -v -keystore debug.keystore -storepass android -keypass android

# 需要 Java JDK
# [alias] 为别名
# [test] 为文件名
keytool -genkey -alias [alias] -keyalg RSA -keysize 2048 -validity 36500 -keystore [test].keystore
```

## 百度地图

百度地图 官方文档使用 v7.4.0 没有
SDKInitializer.setAgreePrivacy(application, true)
LocationClient.setAgreePrivacy(true)
等这些方法，可能没有权限问题的限制，但是可能涉及窃取隐私。

仓库 v7.5.0 之后有该方法，需要同意出卖隐私，否则有部分功能不可用。
（其实就是让你选择要不要出卖隐私，老版本默认出卖。）


开发调试时鉴权失败，直接复制 SHA1 去填开发 SHA1。或者 debug.keystore 的。
