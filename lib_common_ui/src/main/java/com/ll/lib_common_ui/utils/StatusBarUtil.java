package com.ll.lib_common_ui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Auther Kylin
 * @Data 2020/03/15
 * @Description 状态栏辅助类
 */
public class StatusBarUtil {
    public static final int LIGHT_MODE_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    public static final int DARK_MODE_FLAG = View.SYSTEM_UI_FLAG_VISIBLE;


    /**
     * 设置透明bar
     *
     * @param activity
     */
    public static void transparentlyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 大于21
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //大于19
            Window window = activity.getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 获取屏幕StatusBar的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int h = 0;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Field field = clazz.getField("status_bar_height");
            int id = (int) field.get(null);

            h = context.getResources().getDimensionPixelSize(id);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return h;
    }

    /**
     * 获取屏幕虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        vh = displayMetrics.heightPixels - display.getHeight();
        /*try {
            Class clazz = Class.forName("android.view.Display");
            Method method = clazz.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            vh = displayMetrics.heightPixels - display.getHeight();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return vh;
    }

    /**
     * 设置不同系统版本为light模式
     *
     * @param activity
     */
    public static void statusBarLightMode(Activity activity) {
        boolean lightMode = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (miuiSystemStatusBarMode(activity, lightMode)) {

            } else if (flymeSystemStatusBarMode(activity, lightMode)) {

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(LIGHT_MODE_FLAG);
            }
        }
    }

    /**
     * 设置不同系统版本为light模式
     *
     * @param activity
     * @param type
     */
    public static void statusBarLightMode(Activity activity, int type) {
        statusBarMode(activity, true, type);
    }

    /**
     * 设置不同系统版本为dark模式
     *
     * @param activity
     */
    public static void statusBarDarkMode(Activity activity) {
        boolean lightMode = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (miuiSystemStatusBarMode(activity, lightMode)) {

            } else if (flymeSystemStatusBarMode(activity, lightMode)) {

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(DARK_MODE_FLAG);
            }
        }
    }

    /**
     * 设置不同系统版本为dark模式
     *
     * @param activity
     * @param type
     */
    public static void statusBarDarkMode(Activity activity, int type) {
        statusBarMode(activity, false, type);
    }

    /**
     * 设置不同系统不同version的StatusBar模式
     *
     * @param activity
     * @param lightMode 是否是黑暗模式
     * @param type
     */
    private static void statusBarMode(Activity activity, boolean lightMode, int type) {
        if (type == 1) {
            miuiSystemStatusBarMode(activity, lightMode);
        } else if (type == 2) {
            flymeSystemStatusBarMode(activity, lightMode);
        } else if (type == 3) {
            if (null != activity) {
                int mode = lightMode ? LIGHT_MODE_FLAG : DARK_MODE_FLAG;
                activity
                        .getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(mode);
            }
        }
    }

    /**
     * 适配MIUI系统statusBar模式
     *
     * @param activity
     * @param lightMode
     * @return
     */
    private static boolean miuiSystemStatusBarMode(Activity activity, boolean lightMode) {
        boolean result = false;
        Window window = activity.getWindow();
        if (null != window) {
            Class clazz_Window = window.getClass();

            int darkModeFlag;
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");

                darkModeFlag = field.getInt(layoutParams);

                Method extraFlagField = clazz_Window.getMethod("setExtraFlags", int.class, int.class);
                if (lightMode) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }

                result = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.getDecorView()
                            .setSystemUiVisibility(
                                    lightMode ? DARK_MODE_FLAG : LIGHT_MODE_FLAG);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 适配FlyMe系统statusBar模式
     *
     * @param activity
     * @param lightMode
     * @return
     */
    private static boolean flymeSystemStatusBarMode(Activity activity, boolean lightMode) {
        boolean result = false;
        Window window = activity.getWindow();
        if (null != window) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag =
                        WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (lightMode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}
