package com.flyco.tablayout;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.FragmentChangeManager;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import java.util.ArrayList;

/**
 * @Author: AriesHoo on 2018/11/30 9:48
 * @E-Mail: AriesHoo@126.com
 * @Function: 没有继承HorizontalScrollView不能滑动, 对于ViewPager无依赖
 * @Description: 1、2018-11-30 09:48:45 修改原作者在处理Activity状态回收后当前选中tab正确Fragment不正确问题{@link #onRestoreInstanceState(Parcelable)}
 * 2、2018-11-30 10:07:38 修改参数方法拼写错误及java方法废弃部分错误拼写
 * 3、2018-11-30 10:08:19 增加java方法回调值方便链式调用
 * 4、2018年11月30日11:18:41 修改原作者 https://github.com/H07000223/FlycoTabLayout 选中粗体当初始化选中第一项不生效BUG
 * * {@link #updateTabStyles()}
 */
public class CommonTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private Context mContext;
    private ArrayList<CustomTabEntity> mTabEntity = new ArrayList<>();
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private int mIndicatorStyle = STYLE_NORMAL;

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;
    private int mIndicatorGravity;

    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private int mTextSizeUnit = TypedValue.COMPLEX_UNIT_DIP;
    private float mTextSize;
    private int mTextSelectColor;
    private int mTextUnSelectColor;
    private TextBold mTextBold;
    private boolean mTextAllCaps;

    /**
     * icon
     */
    private boolean mIconVisible;
    private int mIconGravity;
    private int mIconWidth;
    private int mIconHeight;
    private int mIconMargin;

    private int mHeight;

    /**
     * anim
     */
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(1.5f);

    private FragmentChangeManager mFragmentChangeManager;

    public CommonTabLayout(Context context) {
        this(context, null, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //重写onDraw方法,需要调用这个方法来清除flag
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);

        obtainAttributes(context, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), mLastP, mCurrentP);
        mValueAnimator.addUpdateListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout);

        mIndicatorStyle = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_style, 0);
        mIndicatorColor = ta.getColor(R.styleable.CommonTabLayout_tl_indicator_color, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_height,
                dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_width, dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_corner_radius, dp2px(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_left, dp2px(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_top, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_right, dp2px(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_bottom, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.CommonTabLayout_tl_indicator_anim_enable, true);
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.CommonTabLayout_tl_indicator_bounce_enable, true);
        mIndicatorAnimDuration = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_anim_duration, -1);
        mIndicatorGravity = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_gravity, Gravity.BOTTOM);

        mUnderlineColor = ta.getColor(R.styleable.CommonTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_underline_height, dp2px(0));
        mUnderlineGravity = ta.getInt(R.styleable.CommonTabLayout_tl_underline_gravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.CommonTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_divider_width, dp2px(0));
        mDividerPadding = ta.getDimension(R.styleable.CommonTabLayout_tl_divider_padding, dp2px(12));

        mTextSize = ta.getDimension(R.styleable.CommonTabLayout_tl_textsize, 14f);
        mTextSelectColor = ta.getColor(R.styleable.CommonTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        mTextUnSelectColor = ta.getColor(R.styleable.CommonTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"));
        mTextBold = TextBold.valueOf(ta.getInt(R.styleable.CommonTabLayout_tl_textBold, TEXT_BOLD_NONE));
        mTextAllCaps = ta.getBoolean(R.styleable.CommonTabLayout_tl_textAllCaps, false);

        mIconVisible = ta.getBoolean(R.styleable.CommonTabLayout_tl_iconVisible, true);
        mIconGravity = ta.getInt(R.styleable.CommonTabLayout_tl_iconGravity, Gravity.TOP);
        mIconWidth = ta.getDimensionPixelSize(R.styleable.CommonTabLayout_tl_iconWidth, dp2px(0));
        mIconHeight = ta.getDimensionPixelSize(R.styleable.CommonTabLayout_tl_iconHeight, dp2px(0));
        mIconMargin = ta.getDimensionPixelSize(R.styleable.CommonTabLayout_tl_iconMargin, dp2px(2.5f));

        mTabSpaceEqual = ta.getBoolean(R.styleable.CommonTabLayout_tl_tab_space_equal, true);
        mTabWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_tab_width, dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.CommonTabLayout_tl_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(10));

        ta.recycle();
    }

    public CommonTabLayout setTabData(ArrayList<CustomTabEntity> tabEntity) {
        if (tabEntity == null || tabEntity.size() == 0) {
            throw new IllegalStateException("TabEntity can not be NULL or EMPTY !");
        }

        this.mTabEntity.clear();
        this.mTabEntity.addAll(tabEntity);

        notifyDataSetChanged();
        return this;
    }

    /**
     * 关联数据支持同时切换fragments
     */
    public CommonTabLayout setTabData(ArrayList<CustomTabEntity> tabEntity, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        return setTabData(tabEntity);
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTabEntity.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            if (mIconGravity == Gravity.LEFT) {
                tabView = View.inflate(mContext, R.layout.layout_tab_left, null);
            } else if (mIconGravity == Gravity.RIGHT) {
                tabView = View.inflate(mContext, R.layout.layout_tab_right, null);
            } else if (mIconGravity == Gravity.BOTTOM) {
                tabView = View.inflate(mContext, R.layout.layout_tab_bottom, null);
            } else {
                tabView = View.inflate(mContext, R.layout.layout_tab_top, null);
            }

            tabView.setTag(i);
            addTab(i, tabView);
        }
        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, View tabView) {
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText(mTabEntity.get(position).getTabTitle());
        ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(mTabEntity.get(position).getTabUnselectedIcon());

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if (mCurrentTab != position) {
                    setCurrentTab(position);
                    if (mListener != null) {
                        mListener.onTabSelect(position);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onTabReselect(position);
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }
        mTabsContainer.addView(tabView, position, lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnSelectColor);
            tv_tab_title.setTextSize(mTextSizeUnit, mTextSize);
            if (mTextAllCaps) {
                tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
            }
            if (mTextBold == TextBold.BOTH) {
                tv_tab_title.getPaint().setFakeBoldText(true);
            } else if (mTextBold == TextBold.SELECT) {
                //增加-以修正原作者第一次选中粗体不生效问题
                tv_tab_title.getPaint().setFakeBoldText(mCurrentTab == i);
            } else {
                tv_tab_title.getPaint().setFakeBoldText(false);
            }
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            if (mIconVisible) {
                iv_tab_icon.setVisibility(View.VISIBLE);
                CustomTabEntity tabEntity = mTabEntity.get(i);
                iv_tab_icon.setImageResource(i == mCurrentTab ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : mIconWidth,
                        mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : mIconHeight);
                if (mIconGravity == Gravity.LEFT) {
                    lp.rightMargin = mIconMargin;
                } else if (mIconGravity == Gravity.RIGHT) {
                    lp.leftMargin = mIconMargin;
                } else if (mIconGravity == Gravity.BOTTOM) {
                    lp.topMargin = mIconMargin;
                } else {
                    lp.bottomMargin = mIconMargin;
                }
                iv_tab_icon.setLayoutParams(lp);
            } else {
                iv_tab_icon.setVisibility(View.GONE);
            }
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = tabView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnSelectColor);
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            CustomTabEntity tabEntity = mTabEntity.get(i);
            iv_tab_icon.setImageResource(isSelect ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
            if (mTextBold == TextBold.SELECT) {
                tab_title.getPaint().setFakeBoldText(isSelect);
            }
        }
    }

    private void calcOffset() {
        final View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        mCurrentP.left = currentTabView.getLeft();
        mCurrentP.right = currentTabView.getRight();

        final View lastTabView = mTabsContainer.getChildAt(this.mLastTab);
        mLastP.left = lastTabView.getLeft();
        mLastP.right = lastTabView.getRight();
        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate();
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP);
            if (mIndicatorBounceEnable) {
                mValueAnimator.setInterpolator(mInterpolator);
            }
            if (mIndicatorAnimDuration < 0) {
                mIndicatorAnimDuration = mIndicatorBounceEnable ? 500 : 250;
            }
            mValueAnimator.setDuration(mIndicatorAnimDuration);
            mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;

        //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        if (mIndicatorWidth < 0) {
        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;
            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;

        //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        if (mIndicatorWidth < 0) {

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = p.left + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
        invalidate();
    }

    private boolean mIsFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line
        if (mIndicatorAnimEnable) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }


        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.setColor(mIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
            } else {

            }

            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2;
                }

                mIndicatorDrawable.setColor(mIndicatorColor);
                mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                        (int) mIndicatorMarginTop, (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                        (int) (mIndicatorMarginTop + mIndicatorHeight));
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        } else {
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor);
                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            height - (int) mIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            (int) mIndicatorMarginTop,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            (int) mIndicatorHeight + (int) mIndicatorMarginTop);
                }
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    /**
     * 设置当前选中Tab
     *
     * @param currentTab
     * @return
     */
    public CommonTabLayout setCurrentTab(int currentTab) {
        mLastTab = this.mCurrentTab;
        this.mCurrentTab = currentTab;
        updateTabSelection(currentTab);
        if (mFragmentChangeManager != null) {
            mFragmentChangeManager.setFragments(currentTab);
        }
        if (mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
        return this;
    }

    public CommonTabLayout setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
        return this;
    }

    /**
     * @param tabPadding
     * @return
     */
    public CommonTabLayout setTabPadding(float tabPadding) {
        return setTabPadding(dp2px(tabPadding));
    }

    /**
     * 新增方法
     *
     * @param tabPadding
     * @return
     */
    public CommonTabLayout setTabPadding(int tabPadding) {
        this.mTabPadding = tabPadding;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setTabWidth(float tabWidth) {
        return setTabWidth(dp2px(tabWidth));
    }

    /**
     * 新增方法
     *
     * @param tabWidth
     * @return
     */
    public CommonTabLayout setTabWidth(int tabWidth) {
        this.mTabWidth = tabWidth;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
        return this;
    }

    public CommonTabLayout setIndicatorHeight(float indicatorHeight) {
        return setIndicatorHeight(dp2px(indicatorHeight));
    }

    /**
     * 新增方法
     *
     * @param indicatorHeight
     * @return
     */
    public CommonTabLayout setIndicatorHeight(int indicatorHeight) {
        this.mIndicatorHeight = indicatorHeight;
        invalidate();
        return this;
    }

    public CommonTabLayout setIndicatorWidth(float indicatorWidth) {
        return setIndicatorWidth(dp2px(indicatorWidth));
    }

    /**
     * 新增方法
     *
     * @param indicatorWidth
     * @return
     */
    public CommonTabLayout setIndicatorWidth(int indicatorWidth) {
        this.mIndicatorWidth = indicatorWidth;
        invalidate();
        return this;
    }

    public CommonTabLayout setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
        return this;
    }

    public CommonTabLayout setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
        return this;
    }

    public CommonTabLayout setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                              float indicatorMarginRight, float indicatorMarginBottom) {
        return setIndicatorMargin(dp2px(indicatorMarginLeft), dp2px(indicatorMarginTop), dp2px(indicatorMarginRight), dp2px(indicatorMarginBottom));
    }

    /**
     * 新增方法
     *
     * @param indicatorMarginLeft
     * @param indicatorMarginTop
     * @param indicatorMarginRight
     * @param indicatorMarginBottom
     * @return
     */
    public CommonTabLayout setIndicatorMargin(int indicatorMarginLeft, int indicatorMarginTop,
                                              int indicatorMarginRight, int indicatorMarginBottom) {
        this.mIndicatorMarginLeft = indicatorMarginLeft;
        this.mIndicatorMarginTop = indicatorMarginTop;
        this.mIndicatorMarginRight = indicatorMarginRight;
        this.mIndicatorMarginBottom = indicatorMarginBottom;
        invalidate();
        return this;
    }

    public CommonTabLayout setIndicatorAnimDuration(long indicatorAnimDuration) {
        this.mIndicatorAnimDuration = indicatorAnimDuration;
        return this;
    }

    public CommonTabLayout setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        this.mIndicatorAnimEnable = indicatorAnimEnable;
        return this;
    }

    public CommonTabLayout setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        this.mIndicatorBounceEnable = indicatorBounceEnable;
        return this;
    }

    public CommonTabLayout setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
        return this;
    }

    public CommonTabLayout setUnderlineHeight(float underlineHeight) {
        this.mUnderlineHeight = underlineHeight;
        invalidate();
        return this;
    }

    public CommonTabLayout setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
        return this;
    }

    public CommonTabLayout setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
        return this;
    }

    public CommonTabLayout setDividerWidth(float dividerWidth) {
        this.mDividerWidth = dividerWidth;
        invalidate();
        return this;
    }

    public CommonTabLayout setDividerPadding(float dividerPadding) {
        this.mDividerPadding = dividerPadding;
        invalidate();
        return this;
    }

    /**
     * 设置文字尺寸 --新增
     *
     * @param textSizeUnit 单位 {@link TypedValue}
     * @param textSize
     * @return
     */
    public CommonTabLayout setTextSize(int textSizeUnit, float textSize) {
        this.mTextSize = textSize;
        this.mTextSizeUnit = textSizeUnit;
        updateTabStyles();
        return this;
    }

    /**
     * @param textSize
     * @return
     */
    public CommonTabLayout setTextSize(float textSize) {
        return setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }

    public CommonTabLayout setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
        return this;
    }

    /**
     * 新增
     *
     * @param textColor
     * @return
     */
    public CommonTabLayout setTextUnSelectColor(int textColor) {
        this.mTextUnSelectColor = textColor;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setTextBold(TextBold textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setIconVisible(boolean iconVisible) {
        this.mIconVisible = iconVisible;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setIconGravity(int iconGravity) {
        this.mIconGravity = iconGravity;
        notifyDataSetChanged();
        return this;
    }

    /**
     * 新增设置icon宽度
     *
     * @param iconWidth
     * @return
     */
    public CommonTabLayout setIconWidth(int iconWidth) {
        this.mIconWidth = iconWidth;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setIconWidth(float iconWidth) {
        return setIconWidth(dp2px(iconWidth));
    }

    /**
     * 新增设置icon 高度
     *
     * @param iconHeight
     * @return
     */
    public CommonTabLayout setIconHeight(int iconHeight) {
        this.mIconHeight = iconHeight;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setIconHeight(float iconHeight) {
        return setIconHeight(dp2px(iconHeight));
    }

    /**
     * 新增设置icon间margin方法
     *
     * @param iconMargin
     * @return
     */
    public CommonTabLayout setIconMargin(int iconMargin) {
        this.mIconMargin = iconMargin;
        updateTabStyles();
        return this;
    }

    public CommonTabLayout setIconMargin(float iconMargin) {
        return setIconMargin(dp2px(iconMargin));
    }

    public CommonTabLayout setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
        return this;
    }


    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public long getIndicatorAnimDuration() {
        return mIndicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return mIndicatorBounceEnable;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    /**
     * @return
     */
    public float getTextSize() {
        return mTextSize;
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnSelectColor;
    }

    public TextBold getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public int getIconGravity() {
        return mIconGravity;
    }

    public float getIconWidth() {
        return mIconWidth;
    }

    public float getIconHeight() {
        return mIconHeight;
    }

    public float getIconMargin() {
        return mIconMargin;
    }

    public boolean isIconVisible() {
        return mIconVisible;
    }


    public ImageView getIconView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
        return iv_tab_icon;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    //setter and getter

    /**
     * show MsgTipView
     */
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public CommonTabLayout showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return this;
            }

            if (!mIconVisible) {
                setMsgMargin(position, 2, 2);
            } else {
                setMsgMargin(position, 0,
                        mIconGravity == Gravity.LEFT || mIconGravity == Gravity.RIGHT ? 4 : 0);
            }

            mInitSetMap.put(position, true);
        }
        return this;
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public CommonTabLayout showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        return showMsg(position, 0);
    }

    public CommonTabLayout hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 新增方法
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     *
     * @param position
     * @param leftPadding
     * @param bottomPadding
     * @return
     */
    public CommonTabLayout setMsgMargin(int position, int leftPadding, int bottomPadding) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextSize);
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();

            float iconH = mIconHeight;
            float margin = 0;
            if (mIconVisible) {
                if (iconH <= 0) {
                    iconH = mContext.getResources().getDrawable(mTabEntity.get(position).getTabSelectedIcon()).getIntrinsicHeight();
                }
                margin = mIconMargin;
            }

            if (mIconGravity == Gravity.TOP || mIconGravity == Gravity.BOTTOM) {
                lp.leftMargin = dp2px(leftPadding);
                lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight - iconH - margin) / 2 - dp2px(bottomPadding) : dp2px(bottomPadding);
            } else {
                lp.leftMargin = dp2px(leftPadding);
                lp.topMargin = mHeight > 0 ? (int) (mHeight - Math.max(textHeight, iconH)) / 2 - dp2px(bottomPadding) : dp2px(bottomPadding);
            }

            tipView.setLayoutParams(lp);
        }
        return this;
    }

    public CommonTabLayout setMsgMargin(int position, float leftPadding, float bottomPadding) {
        return setMsgMargin(position, dp2px(leftPadding), dp2px(bottomPadding));
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    public MsgView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                //updateTabSelection(mCurrentTab); 原作者恢复状态时未将Fragment选中CurrentTab
                setCurrentTab(mCurrentTab);
            }

        }
        super.onRestoreInstanceState(state);
    }

    class IndicatorPoint {
        public float left;
        public float right;
    }

    private IndicatorPoint mCurrentP = new IndicatorPoint();
    private IndicatorPoint mLastP = new IndicatorPoint();

    class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        @Override
        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

}
