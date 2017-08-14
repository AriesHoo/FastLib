package com.aries.library.fast.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

import com.aries.library.fast.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;


/**
 * Created: AriesHoo on 2017-03-13 18:33
 * Function: Glide 工具类支持加载常规、圆角、圆形图片
 * Desc:
 */
public class GlideManager {

    private static int sCommonPlaceholder;
    private static int sCirclePlaceholder;
    private static int sRoundPlaceholder;


    static {
        sCommonPlaceholder = R.drawable.fast_shape_placeholder_common;
        sCirclePlaceholder = R.drawable.fast_shape_placeholder_circle;
        sRoundPlaceholder = R.drawable.fast_shape_placeholder_round;
    }

    /**
     * 设置圆形图片的占位图
     *
     * @param circlePlaceholder
     */
    public static void setCirclePlaceholder(int circlePlaceholder) {
        sCirclePlaceholder = circlePlaceholder;
    }

    /**
     * 设置正常图片的占位符
     *
     * @param commonPlaceholder
     */
    public static void setCommonPlaceholder(int commonPlaceholder) {
        sCommonPlaceholder = commonPlaceholder;
    }

    /**
     * 设置圆角图片的占位符
     *
     * @param roundPlaceholder
     */
    public static void setsRoundPlaceholder(int roundPlaceholder) {
        sRoundPlaceholder = roundPlaceholder;
    }

    /**
     * 普通加载图片
     *
     * @param obj
     * @param iv
     * @param placeholderResource
     */
    public static void loadImg(Object obj, ImageView iv, int placeholderResource) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholderResource)
                .placeholder(placeholderResource)
                .fallback(placeholderResource)
                .dontAnimate()).into(iv);
    }

    public static void loadImg(Object obj, ImageView iv) {
        loadImg(obj, iv, sCommonPlaceholder);
    }

    /**
     * 加载圆形图片
     *
     * @param obj
     * @param iv
     * @param placeholderResource 占位图
     */
    public static void loadCircleImg(Object obj, ImageView iv, int placeholderResource) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholderResource)
                .placeholder(placeholderResource)
                .fallback(placeholderResource)
                .dontAnimate()
                .transform(new CircleCrop())).into(iv);
    }

    public static void loadCircleImg(Object obj, ImageView iv) {
        loadCircleImg(obj, iv, sCirclePlaceholder);
    }

    /**
     * 加载圆角图片
     *
     * @param obj                 加载的图片资源
     * @param iv
     * @param dp                  圆角尺寸-dp
     * @param placeholderResource -占位图
     * @param isOfficial-是否官方模式圆角
     */
    public static void loadRoundImg(Object obj, ImageView iv, float dp, int placeholderResource, boolean isOfficial) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholderResource)
                .placeholder(placeholderResource)
                .fallback(placeholderResource)
                .dontAnimate()
                .transform(isOfficial ? new RoundedCorners(dp2px(dp)) : new GlideRoundTransform(iv.getContext(), dp2px(dp)))).into(iv);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, boolean isOfficial) {
        loadRoundImg(obj, iv, dp, sRoundPlaceholder, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp) {
        loadRoundImg(obj, iv, dp, true);
    }

    public static void loadRoundImg(Object obj, ImageView iv, boolean isOfficial) {
        loadRoundImg(obj, iv, 4, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv) {
        loadRoundImg(obj, iv, true);
    }

    private static RequestOptions getRequestOptions() {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop() // 填充方式
                .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL); //缓存策略
        return requestOptions;
    }

    private static int dp2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private static class GlideRoundTransform extends BitmapTransformation {
        int radius = 0;

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }
}