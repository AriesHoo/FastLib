# FastLib
--------------------------
## 简介：

一个Android项目级快速开发框架,节约大部分写常用功能时间以实现更多项目业务功能及体验上的优化.有问题欢迎issue。

[[Sample PC Download]](https://github.com/AriesHoo/FastLib/blob/master/apk/sample.apk)

[[Sample Mobile Download]](http://fir.im/hju8)

![](https://github.com/AriesHoo/FastLib/blob/master/apk/qr.png)

**Gradle集成**

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
     //compile 'com.github.AriesHoo:FastLib:2.0.1'
     compile 'com.github.AriesHoo:FastLib:${LATEST_VERSION}'
}
```

**compile集成**

```
compile project(':fastLib')

```

**包含第三方库**

```
dependencies {
    def supportVersion = "25.3.1"
    compile 'com.android.support:appcompat-v7:'.concat(supportVersion)
    compile 'com.android.support:recyclerview-v7:'.concat(supportVersion)
    //图片加载
    compile 'com.github.bumptech.glide:glide:4.0.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.0.0'
    //日志打印
    compile 'com.orhanobut:logger:2.1.1'
    //注解
    compile 'com.jakewharton:butterknife:8.8.1'
    //retrofit+rxjava 网络请求及数据解析相关
    compile "io.reactivex.rxjava2:rxjava:2.1.3"
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'com.squareup.retrofit2:retrofit:2.3.0'
    compile 'com.squareup.retrofit2:converter-gson:2.3.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.8.0'
    compile 'com.google.code.gson:gson:2.8.1'

    //处理rxjava内存泄漏-生命周期绑定
    compile 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
    //万能适配器
    compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.28'
    //滑动返回Activity
    compile 'cn.bingoogolapple:bga-swipebacklayout:1.1.0@aar'
    //快速Tab库
    compile 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
    //多状态视图切换
    compile 'com.github.MarnonDev:EasyStatusView:v1.0.3'
    //常用UI控件(TitleBarView、RadiusView等)
    compile 'com.github.AriesHoo:UIWidget:1.9.6'
    //下拉刷新库
    compile 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.2'
    //页面事件交互
    compile 'org.simple:androideventbus:1.0.5.1'
    //webView库
    compile 'com.just.agentweb:agentweb:2.0.0'
}
```

## 实现功能

* 网络请求与Activity/Fragment生命周期绑定--通过RxLifeCycle2.x
* Retrofit2.x 网络请求简单封装
* Basis开头是通用基类:[BasisActivity](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/basis/BasisActivity.java)和[BasisFragment](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/basis/BasisFragment.java)
* Fast开头的是快速创建常见功能页面:[FastMainActivity](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/activity/FastMainActivity.java)-快速创建包含tab主Activity;[FastTitleActivity](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/activity/FastTitleActivity.java)-快速创建包含TitleBarView的Activity;[FastRefreshLoadActivity](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/activity/FastRefreshLoadActivity.java)-快速创建包含TitleBarView及下拉刷新、多状态切换的Activity;[FastWebActivity](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/activity/FastWebActivity.java)快速创建应用内webView的Activity;[FastTitleFragment](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/fragment/FastTitleFragment.java)-快速创建包含TitleBarView的Fragment;[FastRefreshLoadFragment](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/fragment/FastRefreshLoadFragment.java)-快速实现下拉刷新的Fragment;[FastTitleRefreshLoadFragment](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/module/fragment/FastTitleRefreshLoadFragment.java)-快速实现包含TitleBarView及下拉刷新与多状态切换Fragment
* [FastApplication](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/FastApplication.java):快速配置ToastUtil及滑动关闭Activity的Application可以根据需要继承(需要滑动关闭Activity需要参考初始化)
* Manager类是三方库二次封装:目前有[GlideManager](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/manager/GlideManager.java)-图片加载库Glide库封装;[LoggerManager](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/manager/LoggerManager.java)-日志打印logger库封装;[RxJavaManager](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/manager/RxJavaManager.java)-RxJava实现timer;[TabLayoutManager](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/manager/TabLayoutManager.java)-FlycoTabLayout+ViewPager使用:包括CommonTabLayout、SlidingTabLayout、SegmentTabLayout在FragmentActivity与Fragment中使用的封装
* Util类为常用工具:[ActivityStackUtil](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/util/ActivityStackUtil.java)-应用Activity栈管理类;[AppUtil](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/util/AppUtil.java)-部分应用常用功能类;[SizeUtil](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/util/SizeUtil.java)-尺寸转换类;[SPUtil](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/util/SPUtil.java) -SharedPreferences使用类;[TimeFormatUtil](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/util/TimeFormatUtil.java)-时间转换类;[ToastUtil](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/util/ToastUtil.java)-单例模式toast工具类:可配置是否后台显示
* Delegate为代理类:[FastTitleDelegate](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/delegate/FastTitleDelegate.java)-快速实现包含TitleBarView的Fragment/Activity;[FastRefreshLoadDelegate](https://github.com/AriesHoo/FastLib/blob/master/library/src/main/java/com/aries/library/fast/delegate/FastRefreshLoadDelegate.java)-快速实现下拉刷新、上拉加载更多、多状态切换的Fragment/Activity
* 创建支持手势返回的Activity:继承BasisActivity一行代码实现-参考[SwipeBackActivity](https://github.com/AriesHoo/FastLib/blob/master/app/src/main/java/com/aries/library/fast/demo/module/sample/SwipeBackActivity.java)
* 沉浸式状态栏:继承FastTitleActivity/FastRefreshLoadActivity/FastWebActivity/FastTitleFragment/FastTitleRefreshLoadFragment 无需额外代码即可实现
* 状态栏白底黑字模式:同沉浸式状态栏功能继承,只需重写父类实现接口isLightStatusBarEnable即可实现;MIUI V6、Flyme 4.0、Android 6.0以上:参考[UIWidget-StatusBarUtil](https://github.com/AriesHoo/UIWidget/blob/master/library/src/main/java/com/aries/ui/util/StatusBarUtil.java)
* 快速创建圆角、全圆、按下、不可点击状态的TextView、EditText、FrameLayout、LinearLayout、RelativeLayout、TextView、RadioButton、CheckBox减少shape文件创建设置:参考库[UIWidget](https://github.com/AriesHoo/UIWidget)
* Activity/Fragment 页面事件交互(支持设置TAG)
* 万能适配器（ListView、GridView，RecyclerView):可添加多个Header和Footer
* Fragment懒加载,Activity可见时加载--统一了普通Fragment及与ViewPager配合滑动的用户可见回调
* 下拉刷新、上拉加载:支持多种效果的刷新头及自定义刷新头-参考库[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

## 注意事项

## 录屏预览

![](https://github.com/AriesHoo/FastLib/blob/master/screenshot/00.gif)

## 鸣谢

* [RapidLib-来自改库的扩展](https://github.com/MarnoDev/RapidLib)
* [ButterKnife-注解](https://github.com/JakeWharton/butterknife)
* [Glide-图片加载](https://github.com/bumptech/glide)
* [Logger-日志打印](https://github.com/orhanobut/logger)
* [BaseRecyclerViewAdapterHelper-万能适配器](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* [FlycoTabLayout-快速Tab](https://github.com/H07000223/FlycoTabLayout)
* [SmartRefreshLayout-智能刷新](https://github.com/scwang90/SmartRefreshLayout)
* [BGABanner-Android-Banner图](https://github.com/bingoogolapple/BGABanner-Android)
* [BGASwipeBackLayout-滑动返回Activity](https://github.com/bingoogolapple/BGASwipeBackLayout-Android)
* [EasyStatusView-多状态视图切换](https://github.com/MarnoDev/EasyStatusView)
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



