package com.ll.lib_common_ui.viewPageIndictors;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @Auther Kylin
 * @Data 2020/03/14
 * @Description 颜色转换页面指示器，颜色转换临界值 ${mChangePrecent}
 */
public class ColorFilpPagerTitleView extends SimplePagerTitleView {
    private float mChangePrecent = 0.5f;

    public ColorFilpPagerTitleView(Context context) {
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
        if (leavePercent >= mChangePrecent){
            setSelectedColor(mNormalColor);
        } else {
            setSelectedColor(mSelectedColor);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (enterPercent >= mChangePrecent){
            setSelectedColor(mSelectedColor);
        } else {
            setSelectedColor(mNormalColor);
        }
    }

    public float getChangePrecent() {
        return mChangePrecent;
    }

    public void setChangePrecent(float mChangePrecent) {
        this.mChangePrecent = mChangePrecent;
    }
}
