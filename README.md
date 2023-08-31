# 高仿网易云首页

使用开源的[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)作为接口，高仿网易云首页。
本项目只是为了学习而开发，并不是一个破解软件，不提供下载任何歌曲服务，其中包括付费歌曲！
付费歌曲只能试听30s

项目中代码有详细注释，可自行下载运行

该项目使用Jetpack Compose + MVI开发，并且用到了以下组件

* [SplashScreen](https://developer.android.com/develop/ui/views/launch/splash-screen)适配Android12以下手机的启动页。
* [Navigating with Compose](https://developer.android.com/jetpack/compose/navigation)负责页面导航。
* [Hilt with Compose](https://developer.android.com/training/dependency-injection/hilt-android)使用Hilt依赖注入
* [Retrofit](https://github.com/square/retrofit)网络请求
* [OkHttp](https://github.com/square/okhttp)网络请求
* [Serialization](https://github.com/Kotlin/kotlinx.serialization)解析数据
* [JakeWharton Serialization](https://github.com/JakeWharton/retrofit2-kotlinx-serialization-converter)Retrofit 解析传输数据
* [System UI Controller](https://google.github.io/accompanist/systemuicontroller/)设置状态栏和导航栏样式。
* [Landscapist](https://github.com/skydoves/landscapist)适用于Compose的图片加载组件
* [MMKV](https://github.com/Tencent/MMKV)本地持久化存储
* [Zxing](https://github.com/zxing/zxing)生成二维码
* [ExoPlayer](https://github.com/google/ExoPlayer)播放音乐

## 演示视频
<video src='https://github.com/Chen-Xi-g/CloudMusic/blob/main/img/91257d91_20230831_134825_948.mp4' width=360/>
