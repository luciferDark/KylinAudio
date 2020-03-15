package com.ll.lib_common_ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.ll.lib_common_ui.utils.StatusBarUtil;

/**
 * @Auther Kylin
 * @Data 2020/03/15
 * @Description baseFragmentActivity
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setImmersionTitleBar();
    }

    /**
     * 设置沉浸式效果
     */
    private void setImmersionTitleBar() {
        StatusBarUtil.statusBarLightMode(this);
    }
}
