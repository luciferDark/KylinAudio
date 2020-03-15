package com.ll.lib_common_ui.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @Auther Kylin
 * @Data 2020/03/15
 * @Description 手机屏幕和分辨率单位转换辅助类
 */
public class ScreenUtil {

    /**
     *  获取手机屏幕密度
     * @param context
     * @return
     */
    public static float getDensity(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     *  dip 单位转换 px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue){
        final float scale = getDensity(context);
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *  px 单位转换 dip
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = getDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }
}
