package com.aries.library.fast.util;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.aries.library.fast.R;
import com.aries.library.fast.manager.LoggerManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: AriesHoo on 2021/4/30 11:07
 * @E-Mail: AriesHoo@126.com
 * Function: 快速使用系统分享工具类
 * Description:
 * 因分享到部分App功能{@link #shareTextToApps(Context, String, List, String, String)}在不同系统表现不一不推荐使用;
 * 推荐使用直接分享到 app的某个模块
 * {@link #shareTextToAppActivity(Context, String, String, String, String, String)}
 * 或某个App {@link #shareTextToApp(Context, String, String, String, String)}
 * +更多 使用全部分享{@link #shareTextToAllApps(Context, String, String, String)}的组合模式
 */
public class FastShareUtil {

    /**
     * 文本类型
     */
    public final static String TYPE_TEXT = "text/plain";
    /**
     * 图片类型
     */
    public final static String TYPE_IMAGE = "image/*";
    /**
     * 视频类型
     */
    public final static String TYPE_VIDEO = "video/*";
    /**
     * 音频类型
     */
    public final static String TYPE_AUDIO = "audio/*";
    /**
     * QQ包名
     */
    public final static String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    /**
     * QQ好友
     */
    public final static String QQ_FRIEND_CLASS_NAME = "com.tencent.mobileqq.activity.JumpActivity";
    /**
     * QQ电脑
     */
    public final static String QQ_COMPUTER_CLASS_NAME = "com.tencent.mobileqq.activity.qfileJumpActivity";
    /**
     * QQ收藏夹
     */
    public final static String QQ_FAVORITES_CLASS_NAME = "cooperation.qqfav.widget.QfavJumpActivity";
    /**
     * QQ面对面快传
     */
    public final static String QQ_FACE_TO_FACE_CLASS_NAME = "cooperation.qlink.QlinkShareJumpActivity";
    /**
     * 微信包名
     */
    public final static String WE_CHAT_PACKAGE_NAME = "com.tencent.mm";
    /**
     * 微信好友
     */
    public final static String WE_CHAT_FRIEND_CLASS_NAME = "com.tencent.mm.ui.tools.ShareImgUI";
    /**
     * 微信朋友圈
     */
    public final static String WE_CHAT_TIME_LINE_CLASS_NAME = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    /**
     * 微信收藏夹
     */
    public final static String WE_CHAT_FAVORITES_CLASS_NAME = "com.tencent.mm.ui.tools.AddFavoriteUI";
    /**
     * 微博包名
     */
    public final static String WEI_BO_PACKAGE_NAME = "com.sina.weibo";
    /**
     * 微博好友
     */
    public final static String WEI_BO_FRIEND_CLASS_NAME = "com.sina.weibo.weiyou.share.WeiyouShareDispatcher";
    /**
     * 微博圈子
     */
    public final static String WEI_BO_TIME_LINE_CLASS_NAME = "com.sina.weibo.composerinde.ComposerDispatchActivity";
    /**
     * 微博故事
     */
    public final static String WEI_BO_STORY_CLASS_NAME = "com.sina.weibo.story.publisher.StoryDispatcher";
    /**
     * 钉钉包名
     */
    public final static String DING_TALK_PACKAGE_NAME = "com.alibaba.android.rimet";
    /**
     * 企业微信包名
     */
    public final static String WE_WORK_PACKAGE_NAME = "com.tencent.wework";

    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return
     */
    public static boolean isAppInstall(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return launchIntent != null;
    }

    /**
     * QQ是否安装
     *
     * @param context
     * @return
     */
    public static boolean isQQInstall(Context context) {
        return isAppInstall(context, QQ_PACKAGE_NAME);
    }

    /**
     * 微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeChatInstall(Context context) {
        return isAppInstall(context, WE_CHAT_PACKAGE_NAME);
    }

    /**
     * 微博是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeiBoInstall(Context context) {
        return isAppInstall(context, WEI_BO_PACKAGE_NAME);
    }

    /**
     * 分享文本到QQ
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToQQ(Context context, String text) {
        shareTextToApp(context, text, QQ_PACKAGE_NAME, null, null);
    }

    /**
     * 分享文本到QQ好友
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToQQFriend(Context context, String text) {
        shareTextToAppActivity(context, text, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到QQ我的电脑
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToQQComputer(Context context, String text) {
        shareTextToAppActivity(context, text, QQ_PACKAGE_NAME, QQ_COMPUTER_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到QQ收藏夹
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToQQFavorites(Context context, String text) {
        shareTextToAppActivity(context, text, QQ_PACKAGE_NAME, QQ_FAVORITES_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到微信
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeChat(Context context, String text) {
        shareTextToApp(context, text, WE_CHAT_PACKAGE_NAME, null, null);
    }

    /**
     * 分享文本到微信好友
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeChatFriend(Context context, String text) {
        shareTextToAppActivity(context, text, WE_CHAT_PACKAGE_NAME, WE_CHAT_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到微信收藏夹
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeChatFavorites(Context context, String text) {
        shareTextToAppActivity(context, text, WE_CHAT_PACKAGE_NAME, WE_CHAT_FAVORITES_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到微博
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeiBo(Context context, String text) {
        shareTextToApp(context, text, WEI_BO_PACKAGE_NAME, null, null);
    }

    /**
     * 分享文本到微博好友
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeiBoFriend(Context context, String text) {
        shareTextToAppActivity(context, text, WEI_BO_PACKAGE_NAME, WEI_BO_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到微博圈子
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeiBoTimeLine(Context context, String text) {
        shareTextToAppActivity(context, text, WEI_BO_PACKAGE_NAME, WEI_BO_TIME_LINE_CLASS_NAME, null, null);
    }

    /**
     * 分享文本到钉钉
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToDingTalk(Context context, String text) {
        shareTextToApp(context, text, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享文本到企业微信
     *
     * @param context
     * @param text    文本内容
     */
    public static void shareTextToWeWork(Context context, String text) {
        shareTextToApp(context, text, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 使用某个App进行分享-有多种方式会再次选择
     *
     * @param context
     * @param text
     * @param packageName
     * @param subject
     * @param title
     */
    public static void shareTextToApp(Context context, String text,
                                      String packageName,
                                      String subject,
                                      String title) {
        shareTextToAppActivity(context, text, packageName, null, subject, title);
    }

    /**
     * 使用某个App的某个方式进行文本分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param text         文本
     * @param packageName  包名
     * @param activityName 类名
     * @param subject      主题
     * @param title        标题
     */
    public static void shareTextToAppActivity(Context context,
                                              String text,
                                              String packageName,
                                              String activityName,
                                              String subject,
                                              String title) {
        shareText(context, text, subject, title, packageName, activityName, null);
    }

    /**
     * 使用某些App进行文本分享-不同App表现不一不推荐使用
     *
     * @param context      上下文
     * @param text         文本
     * @param packageNames 包名集合
     * @param subject      主题
     * @param title        标题
     */
    public static void shareTextToApps(Context context,
                                       String text,
                                       List<String> packageNames,
                                       String subject,
                                       String title) {
        shareText(context, text, subject, title, null, null, packageNames);
    }

    /**
     * 使用所有App进行文本分享
     *
     * @param context      上下文
     * @param text         文本
     * @param packageNames 包名集合
     * @param subject      主题
     * @param title        标题
     */
    public static void shareTextToAllApps(Context context,
                                          String text,
                                          String subject,
                                          String title) {
        shareTextToApps(context, text, null, subject, title);
    }

    /**
     * @param context      上下文
     * @param text         内容
     * @param subject      主题
     * @param title        标题
     * @param packageName  某个app包名
     * @param activityName 某个app Activity名
     * @param packageNames 某些App 在不同手机表现不一 不推荐使用
     */
    private static void shareText(Context context,
                                  String text,
                                  String subject,
                                  String title,
                                  String packageName,
                                  String activityName,
                                  List<String> packageNames) {
        if (context == null) {
            return;
        }
        Intent shareIntent = getShareTextIntent(text, subject, title);
        ///使用某个App
        if (!TextUtils.isEmpty(packageName)) {
            ///未安装某个应用
            if (!isAppInstall(context, packageName)) {
                ToastUtil.show(R.string.fast_app_not_installed);
                return;
            }
            if (TextUtils.isEmpty(activityName)) {
                shareIntent.setPackage(packageName);
            } else {
                shareIntent.setClassName(packageName, activityName);
                ///单个应用直接分享
                context.startActivity(shareIntent);
                return;
            }
        }
        Intent chooserIntent = Intent.createChooser(shareIntent, TextUtils.isEmpty(title) ? "share" : title);
        ///筛选只分享应用
        if (packageNames != null) {
            List<ResolveInfo> resInfo = context.getPackageManager().
                    queryIntentActivities(getShareTextIntent(text, subject, title), 0);
            if (!resInfo.isEmpty()) {
                List<Intent> targetedShareIntents = new ArrayList<Intent>();
                for (ResolveInfo info : resInfo) {
                    ActivityInfo activityInfo = info.activityInfo;
                    if (packageNames.contains(activityInfo.packageName)) {
                        Intent targeted = getShareTextIntent(text, subject, title);
                        targeted.setClassName(activityInfo.packageName, activityInfo.name);
                        targetedShareIntents.add(targeted);
                    }
                }
                chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), TextUtils.isEmpty(title) ? context.getString(R.string.fast_share_title) : title);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
            }
        }
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(chooserIntent);
    }

    /**
     * 获取分享文本Intent
     *
     * @param text
     * @param subject
     * @param title
     * @return
     */
    private static Intent getShareTextIntent(String text, String subject, String title) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType(TYPE_TEXT);
        if (!TextUtils.isEmpty(text)) {
            target.putExtra(Intent.EXTRA_TEXT, text);
        }
        if (!TextUtils.isEmpty(subject)) {
            target.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(title)) {
            target.putExtra(Intent.EXTRA_TITLE, title);
        }
        return target;
    }

    /**
     * 分享单图到QQ-可选好友、我的电脑、收藏
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToQQ(Context context, Uri fileUri) {
        shareImageToApp(context, fileUri, QQ_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单图到QQ好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToQQFriend(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享多图到QQ好友-分享多图只支持好友-虽然多图选择时也有我的电脑及收藏选项但是无法分享成功
     *
     * @param context
     * @param fileUris
     */
    public static void shareImagesToQQFriend(Context context, List<Uri> fileUris) {
        shareImagesToAppActivity(context, fileUris, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到QQ-我的电脑
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToQQComputer(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_COMPUTER_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到QQ-收藏夹
     *
     * @param context
     * @param fileUri
     * @param title   收藏标题
     */
    public static void shareImageToQQFavorites(Context context, Uri fileUri, String title) {
        shareImageToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FAVORITES_CLASS_NAME, null, title);
    }

    /**
     * 分享单图到QQ-面对面快传
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToQQFaceToFace(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FACE_TO_FACE_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到微信-可选好友、朋友圈、收藏夹
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeChat(Context context, Uri fileUri) {
        shareImageToApp(context, fileUri, WE_CHAT_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单图到微信好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeChatFriend(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到微信朋友圈
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeChatTimeLine(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_TIME_LINE_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到微信收藏夹
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeChatFavorites(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_FAVORITES_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到微博-内容/好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeiBo(Context context, Uri fileUri) {
        shareImageToApp(context, fileUri, WEI_BO_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多图到微博-内容/好友
     *
     * @param context
     * @param fileUris
     */
    public static void shareImagesToWeiBo(Context context, List<Uri> fileUris) {
        shareImagesToApp(context, fileUris, WEI_BO_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单图到微博好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeiBoFriend(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享多图到微博好友
     *
     * @param context
     * @param fileUris
     */
    public static void shareImagesToWeiBoFriend(Context context, List<Uri> fileUris) {
        shareImagesToAppActivity(context, fileUris, WEI_BO_PACKAGE_NAME, WEI_BO_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到微博内容
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeiBoTimeLine(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_TIME_LINE_CLASS_NAME, null, null);
    }

    /**
     * 分享多图到微博内容
     *
     * @param context
     * @param fileUris
     */
    public static void shareImagesToWeiBoTimeLine(Context context, List<Uri> fileUris) {
        shareImagesToAppActivity(context, fileUris, WEI_BO_PACKAGE_NAME, WEI_BO_TIME_LINE_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到微博故事
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeiBoStory(Context context, Uri fileUri) {
        shareImageToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_STORY_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到钉钉
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToDingTalk(Context context, Uri fileUri) {
        shareImageToApp(context, fileUri, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多图到钉钉
     *
     * @param context
     * @param fileUris
     */
    public static void shareImagesToDingTalk(Context context, List<Uri> fileUris) {
        shareImagesToApp(context, fileUris, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单图到企业微信
     *
     * @param context
     * @param fileUri
     */
    public static void shareImageToWeWork(Context context, Uri fileUri) {
        shareImageToApp(context, fileUri, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多图到企业微信
     *
     * @param context
     * @param fileUris
     */
    public static void shareImagesToWeWork(Context context, List<Uri> fileUris) {
        shareImagesToApp(context, fileUris, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单图-选择所有支持App
     *
     * @param context 上下文
     * @param fileUri 文件Uri
     * @param subject 主题
     * @param title   标题
     */
    public static void shareImageToAllApps(Context context,
                                           Uri fileUri,
                                           String subject,
                                           String title) {
        shareImageToApp(context, fileUri, null, subject, title);
    }

    /**
     * 分享多图-选择所有支持App
     *
     * @param context  上下文
     * @param fileUris 文件Uri集合
     * @param subject  主题
     * @param title    标题
     */
    public static void shareImagesToAllApps(Context context,
                                            List<Uri> fileUris,
                                            String subject,
                                            String title) {
        shareImagesToApp(context, fileUris, null, subject, title);
    }

    /**
     * 使用某个App进行单图片分享-多种方式弹出选择
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareImageToApp(Context context,
                                       Uri fileUri,
                                       String packageName,
                                       String subject,
                                       String title) {
        shareImageToAppActivity(context, fileUri, packageName, null, subject, title);
    }

    /**
     * 使用某个App进行多图片分享-多种方式弹出选择
     *
     * @param context      上下文
     * @param fileUris     文件Uri集合
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareImagesToApp(Context context,
                                        List<Uri> fileUris,
                                        String packageName,
                                        String subject,
                                        String title) {
        shareImagesToAppActivity(context, fileUris, packageName, null, subject, title);
    }

    /**
     * 使用某个App的某个方式进行单图片分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareImageToAppActivity(Context context,
                                               Uri fileUri,
                                               String packageName,
                                               String activityName,
                                               String subject,
                                               String title) {
        shareFile(context, fileUri, TYPE_IMAGE, subject, title, packageName, activityName);
    }

    /**
     * 使用某个App的某个方式进行多图片分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param fileUris     文件Uri集合
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareImagesToAppActivity(Context context,
                                                List<Uri> fileUris,
                                                String packageName,
                                                String activityName,
                                                String subject,
                                                String title) {
        shareFiles(context, fileUris, TYPE_IMAGE,
                subject, title, packageName, activityName);
    }

    /**
     * 分享单视频到QQ-可选好友、我的电脑、收藏
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToQQ(Context context, Uri fileUri) {
        shareVideoToApp(context, fileUri, QQ_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单视频到QQ好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToQQFriend(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享多视频到QQ好友-分享多图只支持好友-虽然多视频选择时也有我的电脑及收藏选项但是无法分享成功
     *
     * @param context
     * @param fileUris
     */
    public static void shareVideosToQQFriend(Context context, List<Uri> fileUris) {
        shareVideosToAppActivity(context, fileUris, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到QQ-我的电脑
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToQQComputer(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_COMPUTER_CLASS_NAME, null, null);
    }

    /**
     * 分享单图到QQ-收藏夹
     *
     * @param context
     * @param fileUri
     * @param title   收藏标题
     */
    public static void shareVideoToQQFavorites(Context context, Uri fileUri, String title) {
        shareVideoToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FAVORITES_CLASS_NAME, null, title);
    }

    /**
     * 分享单视频到QQ-面对面快传
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToQQFaceToFace(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FACE_TO_FACE_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到微信-可选好友、收藏夹
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeChat(Context context, Uri fileUri) {
        shareVideoToApp(context, fileUri, WE_CHAT_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单视频到微信好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeChatFriend(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到微信收藏夹
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeChatFavorites(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_FAVORITES_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到微博-内容/好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeiBo(Context context, Uri fileUri) {
        shareVideoToApp(context, fileUri, WEI_BO_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单视频到微博好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeiBoFriend(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到微博内容
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeiBoTimeLine(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_TIME_LINE_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到微博故事
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeiBoStory(Context context, Uri fileUri) {
        shareVideoToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_STORY_CLASS_NAME, null, null);
    }

    /**
     * 分享单视频到钉钉
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToDingTalk(Context context, Uri fileUri) {
        shareVideoToApp(context, fileUri, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多视频到钉钉
     *
     * @param context
     * @param fileUris
     */
    public static void shareVideosToDingTalk(Context context, List<Uri> fileUris) {
        shareVideosToApp(context, fileUris, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单视频到企业微信
     *
     * @param context
     * @param fileUri
     */
    public static void shareVideoToWeWork(Context context, Uri fileUri) {
        shareVideoToApp(context, fileUri, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多视频到企业微信
     *
     * @param context
     * @param fileUris
     */
    public static void shareVideosToWeWork(Context context, List<Uri> fileUris) {
        shareVideosToApp(context, fileUris, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单视频-选择所有支持App
     *
     * @param context 上下文
     * @param fileUri 文件Uri
     * @param subject 主题
     * @param title   标题
     */
    public static void shareVideoToAllApps(Context context,
                                           Uri fileUri,
                                           String subject,
                                           String title) {
        shareVideoToApp(context, fileUri, null, subject, title);
    }

    /**
     * 分享多视频-选择所有支持App
     *
     * @param context  上下文
     * @param fileUris 文件Uri集合
     * @param subject  主题
     * @param title    标题
     */
    public static void shareVideosToAllApps(Context context,
                                            List<Uri> fileUris,
                                            String subject,
                                            String title) {
        shareVideosToApp(context, fileUris, null, subject, title);
    }

    /**
     * 使用某个App进行单视频分享-多种方式弹出选择
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareVideoToApp(Context context,
                                       Uri fileUri,
                                       String packageName,
                                       String subject,
                                       String title) {
        shareVideoToAppActivity(context, fileUri, packageName, null, subject, title);
    }

    /**
     * 使用某个App进行多视频分享-多种方式弹出选择
     *
     * @param context      上下文
     * @param fileUris     文件Uri集合
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareVideosToApp(Context context,
                                        List<Uri> fileUris,
                                        String packageName,
                                        String subject,
                                        String title) {
        shareVideosToAppActivity(context, fileUris, packageName, null, subject, title);
    }

    /**
     * 使用某个App的某个方式进行单视频分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareVideoToAppActivity(Context context,
                                               Uri fileUri,
                                               String packageName,
                                               String activityName,
                                               String subject,
                                               String title) {
        shareFile(context, fileUri, TYPE_VIDEO, subject, title, packageName, activityName);
    }

    /**
     * 使用某个App的某个方式进行多视频分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param fileUris     文件Uri集合
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareVideosToAppActivity(Context context,
                                                List<Uri> fileUris,
                                                String packageName,
                                                String activityName,
                                                String subject,
                                                String title) {
        shareFiles(context, fileUris, TYPE_VIDEO,
                subject, title, packageName, activityName);
    }

    /**
     * 分享单音频到QQ-可选好友、我的电脑、收藏
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToQQ(Context context, Uri fileUri) {
        shareAudioToApp(context, fileUri, QQ_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单音频到QQ好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToQQFriend(Context context, Uri fileUri) {
        shareAudioToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享多音频到QQ好友-分享多音频只支持好友-虽然多音频选择时也有我的电脑及收藏选项但是无法分享成功
     *
     * @param context
     * @param fileUris
     */
    public static void shareAudiosToQQFriend(Context context, List<Uri> fileUris) {
        shareAudiosToAppActivity(context, fileUris, QQ_PACKAGE_NAME, QQ_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单音频到QQ-我的电脑
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToQQComputer(Context context, Uri fileUri) {
        shareAudioToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_COMPUTER_CLASS_NAME, null, null);
    }

    /**
     * 分享单音频到QQ-收藏夹
     *
     * @param context
     * @param fileUri
     * @param title   收藏标题
     */
    public static void shareAudioToQQFavorites(Context context, Uri fileUri, String title) {
        shareAudioToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FAVORITES_CLASS_NAME, null, title);
    }

    /**
     * 分享单音频到QQ-面对面快传
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToQQFaceToFace(Context context, Uri fileUri) {
        shareAudioToAppActivity(context, fileUri, QQ_PACKAGE_NAME, QQ_FACE_TO_FACE_CLASS_NAME, null, null);
    }

    /**
     * 分享单音频到微信好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToWeChatFriend(Context context, Uri fileUri) {
        shareAudioToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单音频到微信收藏夹
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToWeChatFavorites(Context context, Uri fileUri) {
        shareAudioToAppActivity(context, fileUri, WE_CHAT_PACKAGE_NAME, WE_CHAT_FAVORITES_CLASS_NAME, null, null);
    }

    /**
     * 分享单音频到微博好友
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToWeiBoFriend(Context context, Uri fileUri) {
        shareAudioToAppActivity(context, fileUri, WEI_BO_PACKAGE_NAME, WEI_BO_FRIEND_CLASS_NAME, null, null);
    }

    /**
     * 分享单音频到钉钉
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToDingTalk(Context context, Uri fileUri) {
        shareAudioToApp(context, fileUri, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多音频到钉钉
     *
     * @param context
     * @param fileUris
     */
    public static void shareAudiosToDingTalk(Context context, List<Uri> fileUris) {
        shareAudiosToApp(context, fileUris, DING_TALK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单音频到企业微信
     *
     * @param context
     * @param fileUri
     */
    public static void shareAudioToWeWork(Context context, Uri fileUri) {
        shareAudioToApp(context, fileUri, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享多音频到企业微信
     *
     * @param context
     * @param fileUris
     */
    public static void shareAudiosToWeWork(Context context, List<Uri> fileUris) {
        shareAudiosToApp(context, fileUris, WE_WORK_PACKAGE_NAME, null, null);
    }

    /**
     * 分享单音频-选择所有支持App
     *
     * @param context 上下文
     * @param fileUri 文件Uri
     * @param subject 主题
     * @param title   标题
     */
    public static void shareAudioToAllApps(Context context,
                                           Uri fileUri,
                                           String subject,
                                           String title) {
        shareAudioToApp(context, fileUri, null, subject, title);
    }

    /**
     * 分享多音频-选择所有支持App
     *
     * @param context  上下文
     * @param fileUris 文件Uri集合
     * @param subject  主题
     * @param title    标题
     */
    public static void shareAudiosToAllApps(Context context,
                                            List<Uri> fileUris,
                                            String subject,
                                            String title) {
        shareAudiosToApp(context, fileUris, null, subject, title);
    }

    /**
     * 使用某个App进行单音频分享-多种方式弹出选择
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareAudioToApp(Context context,
                                       Uri fileUri,
                                       String packageName,
                                       String subject,
                                       String title) {
        shareAudioToAppActivity(context, fileUri, packageName, null, subject, title);
    }

    /**
     * 使用某个App进行多音频分享-多种方式弹出选择
     *
     * @param context      上下文
     * @param fileUris     文件Uri集合
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareAudiosToApp(Context context,
                                        List<Uri> fileUris,
                                        String packageName,
                                        String subject,
                                        String title) {
        shareAudiosToAppActivity(context, fileUris, packageName, null, subject, title);
    }

    /**
     * 使用某个App的某个方式进行单音频分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareAudioToAppActivity(Context context,
                                               Uri fileUri,
                                               String packageName,
                                               String activityName,
                                               String subject,
                                               String title) {
        shareFile(context, fileUri, TYPE_AUDIO, subject, title, packageName, activityName);
    }

    /**
     * 使用某个App的某个方式进行多音频分享-如微信可分享 好友及收藏夹 QQ可分享 好友 电脑 收藏夹
     *
     * @param context      上下文
     * @param fileUris     文件Uri集合
     * @param packageName  包名
     * @param activityName Activity路径
     * @param subject      主题
     * @param title        标题
     */
    public static void shareAudiosToAppActivity(Context context,
                                                List<Uri> fileUris,
                                                String packageName,
                                                String activityName,
                                                String subject,
                                                String title) {
        shareFiles(context, fileUris, TYPE_AUDIO,
                subject, title, packageName, activityName);
    }

    /**
     * 分享单文件
     *
     * @param context      上下文
     * @param fileUri      文件Uri
     * @param type         文件类型
     * @param subject      主题
     * @param title        标题
     * @param packageName  包名
     * @param activityName Activity路径
     */
    public static void shareFile(Context context,
                                 Uri fileUri,
                                 String type,
                                 String subject,
                                 String title,
                                 String packageName,
                                 String activityName) {
        shareFiles(context,
                Arrays.asList(new Uri[]{fileUri}),
                type, subject, title, packageName, activityName);
    }

    /**
     * 分享多文件
     *
     * @param context      上下文
     * @param fileUris     文件Uri
     * @param type         文件类型
     * @param subject      主题
     * @param title        标题
     * @param packageName  包名
     * @param activityName Activity类路径
     */
    public static void shareFiles(Context context,
                                  List<Uri> fileUris,
                                  String type,
                                  String subject,
                                  String title,
                                  String packageName,
                                  String activityName) {
        if (context == null || fileUris == null || fileUris.isEmpty()) {
            return;
        }
        //转Uri
        ArrayList<Uri> uriArrayList = getUris(context, fileUris, type);
        if (uriArrayList == null || uriArrayList.isEmpty()) {
            return;
        }
        Intent shareIntent = getShareFileIntent(type, subject, title);
        if (uriArrayList.size() == 1) {
            if (TextUtils.isEmpty(title)) {
                Uri uri = uriArrayList.get(0);
                shareIntent.putExtra(Intent.EXTRA_TITLE, getUriName(uri));
            }
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriArrayList.get(0));
        } else {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ///使用某个App
        if (!TextUtils.isEmpty(packageName)) {
            ///未安装某个应用
            if (!isAppInstall(context, packageName)) {
                ToastUtil.show(R.string.fast_app_not_installed);
                return;
            }
            if (TextUtils.isEmpty(activityName)) {
                shareIntent.setPackage(packageName);
            } else {
                shareIntent.setClassName(packageName, activityName);
                grantUriPermission(context, shareIntent, uriArrayList);
                ///单个应用直接分享
                context.startActivity(shareIntent);
                return;
            }
        }
        grantUriPermission(context, shareIntent, uriArrayList);
        Intent chooserIntent = Intent.createChooser(shareIntent, TextUtils.isEmpty(title) ? "share" : title);
        grantUriPermission(context, chooserIntent, uriArrayList);
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(chooserIntent);
    }

    /**
     * 获取分享文件Intent
     *
     * @param type    type类型 images/* videp/* audio/*
     * @param subject
     * @param title
     * @return
     */
    private static Intent getShareFileIntent(String type, String subject, String title) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType(TextUtils.isEmpty(type) ? "*/*" : type);
        if (!TextUtils.isEmpty(subject)) {
            target.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(title)) {
            target.putExtra(Intent.EXTRA_TITLE, title);
        }
        return target;
    }

    private static ArrayList<Uri> getUris(Context context, List<Uri> listUris, String type) {
        ArrayList<Uri> uris = new ArrayList<>(listUris.size());
        for (Uri uri : listUris) {
            ///先确认Uri是否为File
            File file = null;
            String scheme = uri.getScheme() != null ? uri.getScheme() : "";
            if (scheme.toLowerCase().endsWith("file")) {
                try {
                    file = new File(new URI(uri.toString()));
                } catch (URISyntaxException uriSyntaxException) {

                }
            } else if (scheme.toLowerCase().endsWith("content")) {
                String realPath = getRealPathFromUri(context, uri);
                try {
                    if (!TextUtils.isEmpty(realPath)) {
                        file = new File(realPath);
                    }
                } catch (Exception e) {

                }
            }
            //为File
            if (file != null) {
                //不在FileProvider目录下则拷贝进目录
                if (!fileIsOnExternal(context, file)) {
                    try {
                        file = copyToExternalShareFolder(context, file, null, type);
                    } catch (IOException ioException) {

                    }
                }
            } else {
                ///非File
                try {
                    file = copyToExternalShareFolder(context, null, uri, type);
                } catch (IOException ioException) {
                }
            }
            //文件不为空
            if (file != null) {
                //通过FileProvider转换为Uri
                uris.add(FileProvider.getUriForFile(context, context.getPackageName() + ".FastFileProvider", file));
            } else {
                uris.add(uri);
            }
        }
        return uris;
    }

    /**
     * 判断文件是否在FileProvider目录下
     *
     * @param file
     * @return
     */
    private static boolean fileIsOnExternal(Context context, File file) {
        try {
            String filePath = file.getCanonicalPath();
            File externalDir = context.getExternalFilesDir(null);
            return externalDir != null && filePath.startsWith(externalDir.getCanonicalPath());
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 拷贝原始File 进 新目录
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static File copyToExternalShareFolder(Context context, File file, Uri uri, String type) throws IOException {
        File folder = getExternalShareFolder(context);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File newFile = new File(folder, uri == null ? file.getName() : System.currentTimeMillis() + "." + getFileNameEnd(type, uri));
        copy(context, uri == null ? Uri.fromFile(file) : uri, newFile);
        return newFile;
    }

    /**
     * 获取分享中转缓存目录
     *
     * @param context
     * @return
     */
    private static File getExternalShareFolder(Context context) {
        return new File(context.getExternalCacheDir(), "share");
    }

    /***
     * 拷贝文件
     * @param context
     * @param src
     * @param dst
     * @throws IOException
     */
    private static void copy(Context context, Uri src, File dst) throws IOException {
        InputStream in = context.getContentResolver().openInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    /**
     * 根据type类型返回扩展名
     *
     * @param type
     * @return
     */
    private static String getFileNameEnd(String type, Uri uri) {
        if (uri != null) {
            String uriStr = uri.toString();
            if (!TextUtils.isEmpty(uriStr) && uriStr.contains(".")) {
                String nameEnd = uriStr.substring(uriStr.lastIndexOf(".") + 1, uriStr.length());
                if (!TextUtils.isEmpty(nameEnd)) {
                    return nameEnd;
                }
            }
        }
        if (TextUtils.isEmpty(type)) {
            return "";
        }
        if (type.toLowerCase().contains("image")) {
            return "jpg";
        }
        if (type.toLowerCase().contains("video")) {
            return "mp4";
        }
        if (type.toLowerCase().contains("audio")) {
            return "mp3";
        }
        return "*";
    }

    /**
     * 获取content Uri的真实路径
     *
     * @param context
     * @param contentUri
     * @return
     */
    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.getColumnCount() > 0) {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                return path;
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    /**
     * 将读写权限授予支持的应用
     *
     * @param context
     * @param chooserIntent
     * @param fileUris
     */
    private static void grantUriPermission(Context context, Intent
            chooserIntent, List<Uri> fileUris) {
        List<ResolveInfo> resInfoList =
                context.getPackageManager()
                        .queryIntentActivities(chooserIntent, PackageManager.MATCH_DEFAULT_ONLY);
        List<Map<String, String>> activities = new ArrayList<>();
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            Map<String, String> map = new HashMap<>();
            map.put("packageName", packageName);
            map.put("activityName", resolveInfo.activityInfo.name);
            activities.add(map);
            for (Uri fileUri : fileUris) {
                context.grantUriPermission(
                        packageName,
                        fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    /**
     * 获取uri 名
     *
     * @param uri
     * @return
     */
    private static String getUriName(Uri uri) {
        if (uri != null && uri.toString() != null && uri.toString().contains("/")) {
            return uri.toString().substring(uri.toString().lastIndexOf("/") + 1, uri.toString().length());
        }
        return "";
    }
}
