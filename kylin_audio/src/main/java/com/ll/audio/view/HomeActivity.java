package com.ll.audio.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.ll.audio.R;

/**
 * @Auther Kylin
 * @Data 2020/03/11
 * @Description app主页面
 */
public class HomeActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        initViews();
        initData();
    }

    /**
     * 初始化views
     */
    private void initViews() {
    }

    private void initData() {
    }
}
