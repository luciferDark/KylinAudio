package com.ll.lib_common_ui.viewPageIndictors;

import android.content.Context;

/**
 * @Auther Kylin
 * @Data 2020/03/14
 * @Description 缩放转换页面指示器，缩放最大比例 ${mMiniScale}
 */
public class ScaleTransitionPageTitleView extends ColorFilpPagerTitleView {
    private float mMiniScale = 0.9f;

    public ScaleTransitionPageTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);
        float mScaleValue = mMiniScale + (mMiniScale - 1) * leavePercent;
        setScaleX(mScaleValue);
        setScaleY(mScaleValue);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);
        float mScaleValue = mMiniScale + (1 - mMiniScale) * enterPercent;
        setScaleX(mScaleValue);
        setScaleY(mScaleValue);
    }

    public float getmMiniScale() {
        return mMiniScale;
    }

    public void setmMiniScale(float mMiniScale) {
        this.mMiniScale = mMiniScale;
    }
}
