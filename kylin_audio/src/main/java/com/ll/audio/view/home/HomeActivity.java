package com.ll.audio.view.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.ll.audio.R;
import com.ll.audio.model.Channel;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @Auther Kylin
 * @Data 2020/03/11
 * @Description app主页面
 */
public class HomeActivity extends FragmentActivity {
    private static final Channel[] CHANNELS = new Channel[]{Channel.MY, Channel.DISCOVER, Channel.Friend};

    /**
     * views
     */
    private DrawerLayout mDrawerLayout;
    private TextView mCategory, mSearch;
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;

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
        mDrawerLayout = findViewById(R.id.home_drawer_layout);
        mCategory = findViewById(R.id.home_category_view);
        mSearch = findViewById(R.id.home_search_view);
        mViewPager = findViewById(R.id.home_view_pager);
        mMagicIndicator = findViewById(R.id.home_magic_indicator);

        initMagicIndicator();
    }

    /**
     * 初始化viewpager指示器
     */
    private void initMagicIndicator() {
        mMagicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator mCommonNavigator = new CommonNavigator(HomeActivity.this);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                return null;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
    }

    private void initData() {
    }
}
