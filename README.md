# FastLib-一个快捷实现UI搭建及网络请求的Android开发库
--------------------------

[![](https://img.shields.io/badge/download-demo-blue.svg)](https://raw.githubusercontent.com/AriesHoo/FastLib/master/apk/sample.apk)
[![](https://jitpack.io/v/AriesHoo/FastLib.svg)](https://jitpack.io/#AriesHoo/FastLib)
[![](https://img.shields.io/github/release/AriesHoo/FastLib.svg)](https://github.com/AriesHoo/FastLib/releases)
[![](https://img.shields.io/badge/API-15%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![](https://img.shields.io/github/license/AriesHoo/FastLib.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/简书-AriesHoo-blue.svg)](http://www.jianshu.com/u/a229eee96115)
[![](https://img.shields.io/badge/wiki-使用说明-green.svg)](https://github.com/AriesHoo/FastLib/wiki)
[![](https://img.shields.io/badge/Github-Github主仓库-blue.svg)](https://github.com/AriesHoo/FastLib)
[![](https://img.shields.io/badge/Gitee-Gitee备用仓库-red.svg)](https://gitee.com/AriesHoo/FastLib)


[因Github图片解析异常可使用Gitee备用仓库查看](https://gitee.com/AriesHoo/FastLib)

## 简介：

**FastLib** 是一个整合了常用主流开源项目的 **Android** 快速搭建**UI**库, 其中包含 **Retrofit**、**RxJava** 、**RxAndroid**、**RxLifecycle**等三方库, 并且提供较多的基础**Activity**与**Fragment**如快速搭建**微信类tab+fragment** 的**FastMainActivity**、快速搭建包含TitleBar的**FastTitleActivity**及**FastTitleFragment**、快速搭建列表刷新的**FastRefreshLoadActivity**及**FastRefreshLoadFragment**；并提供快速配置**Retrofit**相关功能的**FastRetrofit**：解决添加统一请求**header**及**多url**等常见功能。

**FastLib 只是一个快速搭建UI及网络请求的库，是常用三方库的一个封装集合,本身不具备框架特性。可在此基础上做MVP、MVVM等框架的扩展**。

[wiki](https://github.com/AriesHoo/FastLib/wiki) 基于**FastLib 2.2.12** 、**Android Studio 3.3.1**、**Gradle 4.10.1**版本编写,后期重大变更会同步更新。

Demo中使用到的网络请求api来源于[readhub](https://readhub.cn/) **版权及最终解释权归readhub所有,如有侵权请联系删除!**

## 主要功能

* 基于**Retrofit2.x**及**RxJava2.x**的网络请求封装、网络请求与生命周期绑定、快速观察者、快速loading观察者、快速返回常用错误
* 常用功能库二次封装方便调用:**Glide**加载图片封装**GlideManager**、**TabLayout+ViewPager Fragment**切换封装**TabLayoutManager**、**Logger**日志打印封装**LoggerManager**
* 多种常用界面布局:**标题+多状态+下拉刷新+列表**、**标题+ViewPager**等方便快速创建常用布局增加layout复用
* Fragment 懒加载封装
* 快速实现**Activity滑动返回**、**下拉刷新+自动加载更多**、**沉浸式状态栏+导航栏**等
* 解决一些老大难问题：**Tab+Fragment 当Activity回收后Fragment 重叠-FastMainActivity**、**软键盘无法弹起-KeyboardHelper**、**状态栏文字转换-StatusBarUtil**、**不同Fragment切换状态栏文字颜色变换-BasisFragment+StatusBarUtil**、**Android 7.0 fileprovider-FastFileUtil**、**Android 9.0 network security policy问题**等
* 提供众多常用工具类：**ToastUtil-统一样式吐司**、**FastFormatUtil-快速格式化时间、字节单位、double类型小数点保留几位**、**SizeUtil-尺寸转换屏幕宽高获取**、**SPUtil-暂存数据**等

其它功能请在demo中发现，详细使用请看[wiki](https://github.com/AriesHoo/FastLib/wiki)

[Download-蒲公英(安装密码1)](https://www.pgyer.com/pqnm)

![Download-蒲公英](https://upload-images.jianshu.io/upload_images/2828782-f32c73dc69c6158a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)

![Download-github](https://upload-images.jianshu.io/upload_images/2828782-35bf4db12fa7297f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)


## 关于混淆

1、**2.2.13_beta12版本**开始自带混淆规则，并且会自动导入，正常情况下无需手动导入。

2、**2.2.13_beta12以下版本**可参考[proguard-rules.pro](/library/proguard-rules.pro)

## 关于模板项目template

 伴随新版本 2.3.6 发布，项目增加一个模板项目 template 以完成基础配置，开发者可直接在此项目上进行开发方便。
 
## 重大更新日志 其它版本参看[Release](https://github.com/AriesHoo/FastLib/releases)
 
* 2.4.0

    * 升级: 升级BaseRecyclerViewAdapterHelper到3.x--升级变动较大主要是BaseRecyclerViewAdapterHelper包名及部分api调整参考demo
    * 升级: 升级SmartRefreshLayout到2.x-升级变动较大主要是包名及api变动及拆分了不同的刷新头和脚
    * 升级: 日常升级其它三方库版本不涉及包名及api变更

* 2.3.2

    * 优化: 优化FastManager初始化init逻辑,FastLib自动初始化解决多进程异常问题
    * 优化: 将eventbus、bga-swipebacklayout、UIWidget:tab-layout 使用compileOnly开发者根据需要导入

* 2.2.13

    * 新增: 下拉刷新接口IFastRefreshView及标题栏IFastTitleBarView
    
* 2.2.11-androidx

    * 重大变更: 2.2.11的androidx版本-起始版本
    
* 2.2.11
   
    *  最后一个support版本以后版本为androidx
    
* 2.2.10-beta6
   
    *  重大变更:重构大部分代码及实现方式删除许多类及实现方法包括三方库慎重升级与之前版本有很大差异
    *  优化:BasisFragment 控制是否为单Fragment 方法
    *  优化:调整ActivityFragmentControl 将状态栏及导航栏控制增加
    *  优化:多状态管理StatusLayoutManager调整完成
    *  优化:滑动返回控制swipeBack功能新增各种回调功能
    *  优化:将原默认配置方法调整到最终实现类功能
    *  新增:增加友盟统计功能演示
    *  新增:新增首页演示其它三方库状态栏及导航栏功能控制
    *  新增:个人MineFragment中选择头像功能三方库状态栏及导航栏控制示例
    *  优化:UI全局控制相关回调并调整三方库Activity控制
    *  优化:新增FastRetrofit 下载/上传功能
    *  优化:新增FastRetrofit 控制多BaseUrl方式
    *  新增:新增Demo检查版本功能试验下载文件功能
    *  删除:删除原多状态布局相关配置
    *  修复:修改FastRefreshActivity 设置Adapter错误BUG
    *  升级:升级部分三方库版本
    *  修复:删除IHttpRequestControl判断避免http错误时无法全局控制BUG
    *  优化:修改解析method方法增加对get方法兼容
    *  优化:修改ToastUtil 增加子线程调用自动切换主线程操作
    *  优化:优化下载功能新增FastDownloadRetrofit并新增格式化文件方法FastFormatUtil#formatDataSize
    *  修复:修复FastRetrofit无法删除单个header问题
    *  优化:优化上传文件并通过新增的FastUploadRequestBody及FastUploadRequestListener实现单个/多个文件 上传进度监听 可参看 MineFragment 示例
    *  优化:FastFileUtil 安装App在Android 7.0以下版本兼容性
    *  新增:新增IMultiStatusView 用于设置StatusLayoutManager 属性
    *  优化:调整setMultiStatusView位置并抽离设置StatusLayoutManager相关属性
    *  优化:注释及代码规范调整
    *  优化:调整列表刷新控制多状态配置及itemClick事件
    *  优化:优化下载功能统一使用全局设置Retrofit
    *  优化:优化LoggerManager可设置自定义属性
    *  优化:优化FastRetrofit日志打印控制--json格式打印
    *  新增:demo新增拖拽调整列表顺序功能
    *  新增:FastFileUtil及fast_file_path以适配更多文件系统及开发者自定义
	
* 2.2.9-beta6
        
	* 重构设置全局TitleBarView设置方式(通过TitleBarViewControl实现可参看AppImpl实现类)
	
* 2.2.9-beta5
        
	* library 直接compile UIWidget core 3.1.0版本
	
* 2.1.5 更新
            
    * 新增众多全局设置TitleBarView属性、Adapter加载动画、SmartRefreshLayout刷新配置、Glide加载占位Drawable属性等控制的FastConfig类用于全局设置应用通用属性并减少部分冗余代码及冗余drawable资源文件

* 2.1.0 更新
        
	* 将部分系统及第三方库在FastLib里使用provided编译,实际项目中需要根据项目需要compile合适的版本避免版本重复
    
## 录屏预览

![折叠屏效果一览](https://upload-images.jianshu.io/upload_images/2828782-d1baf0126dae5722.gif?imageMogr2/auto-orient/strip)

![FastRefresh.gif](https://upload-images.jianshu.io/upload_images/2828782-f3edd0729cfa1a1f.gif?imageMogr2/auto-orient/strip)

**快速实现页面下拉刷新-全局控制及局部个性化**-参考WebActivity、FastRefreshActivity、MineFragment

![](https://upload-images.jianshu.io/upload_images/2828782-fe9b07003b2c6c67.gif?imageMogr2/auto-orient/strip)

**虚拟导航栏控制**-参考[AppImpl类](/app/src/main/java/com/aries/library/fast/demo/AppImpl.java) NavigationBarControl接口实现注释说明

![模拟器软键盘控制](https://upload-images.jianshu.io/upload_images/2828782-892dd2a8a8b13bde.gif?imageMogr2/auto-orient/strip)

![华为可隐藏软键盘控制](https://upload-images.jianshu.io/upload_images/2828782-9072c1274b4a0f57.gif?imageMogr2/auto-orient/strip)

![华为全面屏手势控制](https://upload-images.jianshu.io/upload_images/2828782-0be6729640f15516.gif?imageMogr2/auto-orient/strip)

![](https://upload-images.jianshu.io/upload_images/2828782-6b2e018412256a9d.gif?imageMogr2/auto-orient/strip)

[DoraemonKit集成效果(一定要手动开启 `悬浮窗权限` )-包括沉浸式及UI微调](https://upload-images.jianshu.io/upload_images/2828782-f7b31d680557e86b.gif?imageMogr2/auto-orient/strip)

[![](https://jitpack.io/v/AriesHoo/FastLib.svg)](https://jitpack.io/#AriesHoo/FastLib)

**Gradle集成-尽量使用正式版本**

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

```
dependencies {
      implementation 'com.github.AriesHoo:FastLib:x.y.z'
}
```

**Maven集成**

```
   	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```
	<dependency>
	    <groupId>com.github.AriesHoo</groupId>
	    <artifactId>FastLib</artifactId>
	    <version>x.y.z</version>
	</dependency>
```

**Compile集成**

```
implementation project(':fastLib')
```

**包含第三方库**

```
    compileSdkVersion = 28
    minSdkVersion = 19
    minLibSdkVersion = 15
    targetSdkVersion = 28
    supportVersion = "1.1.0"
    widgetVersion = "3.2.25"
    smartRefreshLayoutVersion = "2.0.3"
    BRVAHVersion = "3.0.4"
    agentwebVersion = "4.1.4"
    glideVersion = "4.12.0"
    butterknifeVersion = "10.2.3"
    versionCode = 249
    versionName = "2.4.0"
```

```
dependencies {
    compileOnly "com.google.android.material:material:".concat(supportVersion)
    compileOnly 'androidx.appcompat:appcompat:'.concat(supportVersion)
    compileOnly "androidx.recyclerview:recyclerview:".concat(supportVersion)
    //万能适配器
    compileOnly 'com.github.CymChad:BaseRecyclerViewAdapterHelper:'.concat(BRVAHVersion)
    //webView库
    compileOnly 'com.just.agentweb:agentweb:'.concat(agentwebVersion)
    //下拉刷新库
//    compileOnly 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.0-andx-14'
    compileOnly 'com.scwang.smart:refresh-layout-kernel:'.concat(smartRefreshLayoutVersion)
    //图片加载
    compileOnly 'com.github.bumptech.glide:glide:'.concat(glideVersion)
    //常用UI控件(TitleBarView、RadiusView等)
    api "com.github.AriesHoo.UIWidget:widget-core:".concat(widgetVersion)
    //快速Tab库
    compileOnly "com.github.AriesHoo.UIWidget:tab-layout:".concat(widgetVersion)
    //日志打印
    api 'com.orhanobut:logger:2.2.0'
    //注解
    api 'com.jakewharton:butterknife:'.concat(butterknifeVersion)
    //retrofit+rxjava 网络请求及数据解析相关
    api "io.reactivex.rxjava2:rxjava:2.2.20"
    api 'io.reactivex.rxjava2:rxandroid:2.1.1'
    api 'com.squareup.retrofit2:retrofit:2.9.0'
    api 'com.squareup.retrofit2:converter-gson:2.9.0'
    api 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
    //3.12.x是支持Android 4.4的
    api 'com.squareup.okhttp3:logging-interceptor:3.12.2'
    api 'com.google.code.gson:gson:2.8.5'
    //处理rxjava内存泄漏-生命周期绑定
    api 'com.trello.rxlifecycle3:rxlifecycle-components:3.1.0'
    //滑动返回Activity
    compileOnly 'cn.bingoogolapple:bga-swipebacklayout:2.0.1@aar'
    //页面事件交互-androideventbus-停止维护
    compileOnly 'org.simple:androideventbus:1.0.5.1'
    //页面事件交互-eventbus
    compileOnly 'org.greenrobot:eventbus:3.1.1'
    //多状态视图切换
    api 'com.github.Bakumon:StatusLayoutManager:1.0.4'
}
```



## 鸣谢

1. [`Retrofit` 是 `Square`出品的网络请求库，极大的减少了 Http 请求的代码和步骤](https://github.com/square/retrofit)
2. [`RxJava` 提供优雅的响应式 API 解决异步请求以及事件处理](https://github.com/ReactiveX/RxJava)
3. [`RxAndroid` 为 Android 提供响应式 API](https://github.com/ReactiveX/RxAndroid)
4. [`Rxlifecycle`，在 Android 上使用 `RxJava` 都知道的一个坑，就是生命周期的解除订阅，这个框架通过绑定 Activity 和 Fragment 的生命周期完美解决该问题](https://github.com/trello/RxLifecycle)
5. [`Okhttp` 同样 `Square` 出品，不多介绍，做 Android 的都应该知道](https://github.com/square/okhttp)
6. [`Gson` 是 `Google` 官方的 Json Convert 框架](https://github.com/google/gson)
7. [`Butterknife` 是 `JakeWharton` 大神出品的 View 注入框架](https://github.com/JakeWharton/butterknife)
8. [`AndroidEventBus` 是一个轻量级的 `EventBus`，提供TAG支持](https://github.com/hehonghui/AndroidEventBus)
9. [`Logger` 是 `orhanobut` 出品的 Log 框架，`FastLib`提供相关封装类`LoggerManager`](https://github.com/orhanobut/logger)
10. [`Glide` 是`Google`推荐的图片加载库，`FastLib`提供相关封装类`GlideManager`](https://github.com/bumptech/glide)
11. [`BaseRecyclerViewAdapterHelper` 是`陈宇明`封装的RecyclerView 适配器帮助类](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
12. [`SmartRefreshLayout` 是`scwang90`封装的刷新库，扩展性很强](https://github.com/scwang90/SmartRefreshLayout)
13. [`AgentWeb` 是`Justson`封装的原生WebView库，使用方便扩展性很强](https://github.com/Justson/AgentWeb)
14. [`BGASwipeBackLayout` 是`bingoogolapple-王浩`封装的微信效果的Activity滑动返回库](https://github.com/bingoogolapple/BGASwipeBackLayout-Android)
15. [`StatusLayoutManager` 是`Bakumon`封装的多状态控制管理工具,方便实现加载中、加载失败、网络错误等状态](https://github.com/Bakumon/StatusLayoutManager)
16. [`TabLayout` 是`AriesHoo` forked from `FlycoTabLayout`并加以优化调整的主界面tab、滑动tab、分段选择器tab功能库](https://github.com/AriesHoo/TabLayout)
17. [`UIWidget` 是`AriesHoo` 封装的常用UI库(TitleBarView、RadiusView、StatusViewHelper、NavigationViewHelper等)](https://github.com/AriesHoo/UIWidget)

## License

```
Copyright 2017-2021Aries Hoo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



