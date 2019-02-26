# FastLib-一个快捷实现UI搭建及网络请求的Android开发库
--------------------------

[![](https://img.shields.io/badge/download-demo-blue.svg)](https://raw.githubusercontent.com/AriesHoo/FastLib/master/apk/sample.apk)
[![](https://jitpack.io/v/AriesHoo/FastLib.svg)](https://jitpack.io/#AriesHoo/FastLib)
[![](https://img.shields.io/github/release/AriesHoo/FastLib.svg)](https://github.com/AriesHoo/FastLib/releases)
[![API](https://img.shields.io/badge/API-15%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![GitHub license](https://img.shields.io/github/license/AriesHoo/FastLib.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/简书-AriesHoo-blue.svg)](http://www.jianshu.com/u/a229eee96115)

## 简介：

一个Android项目级快速开发框架,节约大部分写常用功能时间以实现更多项目业务功能及体验上的优化.有问题欢迎issue。

Demo中使用到的网络请求api来源于[豆瓣API V2](https://developers.douban.com/wiki/?title=api_v2) ***版权及最终解释权归d豆瓣所有,如有侵权请联系删除!***

## 主要功能

* 基于Retrofit2.x及RxJava2.x的网络请求封装、网络请求与生命周期绑定、快速观察者、快速loading观察者、快速返回常用错误
* 常用功能库二次封装方便调用:Glide加载图片封装、TabLayout+ViewPager Fragment切换封装、Logger日志打印封装
* 多种常用界面布局:标题+多状态+下拉刷新+列表、标题+ViewPager等方便快速创建常用布局增加layout复用
* Fragment 懒加载封装
* 快速实现Activity滑动返回、下拉刷新加载更多、沉浸式等

其它功能请在demo中发现

[[Download]](https://raw.githubusercontent.com/AriesHoo/FastLib/master/apk/sample.apk)

![](/apk/qr.png)

## 录屏预览

![](https://github.com/AriesHoo/FastLib/blob/master/screenshot/02.gif)

虚拟导航栏控制-参考[AppImpl类](/app/src/main/java/com/aries/library/fast/demo/AppImpl.java) NavigationBarControl接口实现注释说明

![](https://github.com/AriesHoo/FastLib/blob/master/screenshot/00.gif)

![](https://github.com/AriesHoo/FastLib/blob/master/screenshot/01.gif)

开启GPU过度绘制检测+GPU呈现模式分析

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
      implementation 'com.github.AriesHoo:FastLib:2.2.11-androidx'
     //implementation 'com.github.AriesHoo:FastLib:2.2.11'
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
	    <version>2.2.1</version>
	</dependency>
```

**Compile集成**

```
implementation project(':fastLib')
```

**包含第三方库**

```
    compileSdkVersion = 28
    buildToolsVersion = "28.0.3"
    minSdkVersion = 21
    targetSdkVersion = 28
    supportVersion = "28.0.0"
```

```
dependencies {
        compileOnly 'com.android.support:design:'.concat(supportVersion)
        compileOnly 'com.android.support:appcompat-v7:'.concat(supportVersion)
        compileOnly 'com.android.support:recyclerview-v7:'.concat(supportVersion)
        //万能适配器
        compileOnly 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.40'
        //webView库
        compileOnly 'com.just.agentweb:agentweb:4.0.2'
        //下拉刷新库
        compileOnly 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.5.1'
        //图片加载
        compileOnly 'com.github.bumptech.glide:glide:4.5.0'
        //常用UI控件(TitleBarView、RadiusView等)
        api 'com.github.AriesHoo.UIWidget:widget-core:3.2.6'
        //日志打印
        api 'com.orhanobut:logger:2.1.1'
        //注解
        api 'com.jakewharton:butterknife:8.8.1'
        //retrofit+rxjava 网络请求及数据解析相关
        api "io.reactivex.rxjava2:rxjava:2.1.14"
        api 'io.reactivex.rxjava2:rxandroid:2.0.1'
        api 'com.squareup.retrofit2:retrofit:2.3.0'
        api 'com.squareup.retrofit2:converter-gson:2.3.0'
        api 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
        api 'com.squareup.okhttp3:logging-interceptor:3.8.0'
        api 'com.google.code.gson:gson:2.8.5'
        //处理rxjava内存泄漏-生命周期绑定
        api 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
        //滑动返回Activity
        api 'cn.bingoogolapple:bga-swipebacklayout:1.1.8@aar'
        //快速Tab库
        api 'com.github.AriesHoo:TabLayout:1.0.1'
        //页面事件交互
        api 'org.simple:androideventbus:1.0.5.1'
        //多状态视图切换
        api 'com.github.Bakumon:StatusLayoutManager:1.0.4'
}
```

## 实现功能-多看注释及版本修改说明

* 支持全局多种参数配置(加载更多+多状态页面+网络加载Loading+下拉刷新头+TitleBarView属性设置+Activity滑动关闭-类微信+http网络返回及错误处理+Activity与Fragment生命周期监听+主页返回键控制) 参看[FastManager](/library/src/main/java/com/aries/library/fast/FastManager.java) 实现及 [App](/app/src/main/java/com/aries/library/fast/demo/App.java)配置
* 支持修改三方库 TitleBar 及虚拟导航栏沉浸式 及 Activity滑动关闭功能
* 网络请求与Activity/Fragment生命周期绑定--通过RxLifeCycle2.x
* Retrofit2.x+RxJava2.x 网络请求简要封装支持多BaseUrl、支持快速下载、快速上传文件功能 参看[FastRetrofit](/library/src/main/java/com/aries/library/fast/retrofit/FastRetrofit.java)
* Basis开头是通用基类:[BasisActivity](/library/src/main/java/com/aries/library/fast/basis/BasisActivity.java)和[BasisFragment](/library/src/main/java/com/aries/library/fast/basis/BasisFragment.java)
* Fast开头的是快速创建常见功能页面:[FastMainActivity](/library/src/main/java/com/aries/library/fast/module/activity/FastMainActivity.java)-快速创建包含tab主Activity;[FastTitleActivity](/library/src/main/java/com/aries/library/fast/module/activity/FastTitleActivity.java)-快速创建包含TitleBarView的Activity;[FastRefreshLoadActivity](/library/src/main/java/com/aries/library/fast/module/activity/FastRefreshLoadActivity.java)-快速创建包含TitleBarView及下拉刷新、多状态切换的Activity;[FastWebActivity](/library/src/main/java/com/aries/library/fast/module/activity/FastWebActivity.java)快速创建应用内webView的Activity;[FastTitleFragment](/library/src/main/java/com/aries/library/fast/module/fragment/FastTitleFragment.java)-快速创建包含TitleBarView的Fragment;[FastRefreshLoadFragment](/library/src/main/java/com/aries/library/fast/module/fragment/FastRefreshLoadFragment.java)-快速实现下拉刷新的Fragment;[FastTitleRefreshLoadFragment](/library/src/main/java/com/aries/library/fast/module/fragment/FastTitleRefreshLoadFragment.java)-快速实现包含TitleBarView及下拉刷新与多状态切换Fragment
* Manager类是三方库二次封装:目前有[GlideManager](/library/src/main/java/com/aries/library/fast/manager/GlideManager.java)-图片加载库Glide库封装;[LoggerManager](/library/src/main/java/com/aries/library/fast/manager/LoggerManager.java)-日志打印logger库封装;[RxJavaManager](/library/src/main/java/com/aries/library/fast/manager/RxJavaManager.java)-RxJava实现timer;[TabLayoutManager](/library/src/main/java/com/aries/library/fast/manager/TabLayoutManager.java)-FlycoTabLayout+ViewPager使用:包括CommonTabLayout、SlidingTabLayout、SegmentTabLayout在FragmentActivity与Fragment中使用的封装
* Util类为常用工具:[FastStackUtil](/library/src/main/java/com/aries/library/fast/util/FastStackUtil.java)-应用Activity栈管理类;[FastUtil](/library/src/main/java/com/aries/library/fast/util/FastUtil.java)-部分应用常用功能类;[SizeUtil](/src/main/java/com/aries/library/fast/util/SizeUtil.java)-尺寸转换类;[SPUtil](/library/src/main/java/com/aries/library/fast/util/SPUtil.java) -SharedPreferences使用类;[TimeFormatUtil](/library/src/main/java/com/aries/library/fast/util/TimeFormatUtil.java)-时间转换类;[ToastUtil](/library/src/main/java/com/aries/library/fast/util/ToastUtil.java)-单例模式toast工具类:可配置是否后台显示
* Delegate为代理类:[FastTitleDelegate](/library/src/main/java/com/aries/library/fast/delegate/FastTitleDelegate.java)-快速实现包含TitleBarView的Fragment/Activity;[FastRefreshLoadDelegate](/library/src/main/java/com/aries/library/fast/delegate/FastRefreshLoadDelegate.java)-快速实现下拉刷新、上拉加载更多、多状态切换的Fragment/Activity
* 沉浸式状态栏:继承FastTitleActivity/FastRefreshLoadActivity/FastWebActivity/FastTitleFragment/FastTitleRefreshLoadFragment 无需额外代码即可实现
* 状态栏白底黑字模式:同沉浸式状态栏功能继承,只需重写父类实现接口isLightStatusBarEnable即可实现;MIUI V6、Flyme 4.0、Android 6.0以上:参考[UIWidget-StatusBarUtil](/library/src/main/java/com/aries/ui/util/StatusBarUtil.java)
* 快速创建圆角、全圆、按下、不可点击状态的TextView、EditText、FrameLayout、LinearLayout、RelativeLayout、RadioButton、CheckBox减少shape文件创建设置:参考库[UIWidget](https://github.com/AriesHoo/UIWidget)
* Activity/Fragment 页面事件交互(支持设置TAG)
* 万能适配器（ListView、GridView，RecyclerView):可添加多个Header和Footer
* Fragment懒加载,Activity可见时加载--统一了普通Fragment及与ViewPager配合滑动的用户可见回调
* 下拉刷新、上拉加载:支持多种效果的刷新头及自定义刷新头-参考库[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

## 注意事项

## 重大更新日志 其它版本参看[Release](https://github.com/AriesHoo/FastLib/releases)

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
    

## 鸣谢

* [ButterKnife-注解](https://github.com/JakeWharton/butterknife)
* [Retrofit2.X-网络请求](https://github.com/square/retrofit)
* [RxJava2.X-响应式编程](https://github.com/ReactiveX/RxJava)
* [RxLifeCycle2.X-网络请求生命周期绑定](https://github.com/trello/RxLifecycle)
* [Glide-图片加载](https://github.com/bumptech/glide)
* [Logger-日志打印](https://github.com/orhanobut/logger)
* [BaseRecyclerViewAdapterHelper-万能适配器](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* [TabLayout-快速Tab(forked from FlycoTabLayout)](https://github.com/AriesHoo/TabLayout)
* [SmartRefreshLayout-智能刷新](https://github.com/scwang90/SmartRefreshLayout)
* [BGABanner-Android-Banner图](https://github.com/bingoogolapple/BGABanner-Android)
* [BGASwipeBackLayout-滑动返回Activity](https://github.com/bingoogolapple/BGASwipeBackLayout-Android)
* [StatusLayoutManager-多状态视图切换](https://github.com/Bakumon/StatusLayoutManager)
* [UIWidget-常用UI库](https://github.com/AriesHoo/UIWidget)
* [AndroidEventBus-页面事件交互](https://github.com/hehonghui/AndroidEventBus)
* [AgentWeb-原生WebView快速集成库](https://github.com/Justson/AgentWeb)

## License

```
Copyright 2017 Aries Hoo

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



