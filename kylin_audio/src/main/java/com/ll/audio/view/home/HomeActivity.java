package com.ll.audio.view.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ll.audio.R;
import com.ll.audio.events.LoginEvent;
import com.ll.audio.model.Channel;
import com.ll.audio.model.user.UserManager;
import com.ll.audio.model.user.UserProtocol;
import com.ll.audio.view.home.adpaters.HomeViewPagerAdapter;
import com.ll.audio.view.login.LoginActivity;
import com.ll.lib_common_ui.base.BaseFragmentActivity;
import com.ll.lib_common_ui.viewPageIndictors.ScaleTransitionPageTitleView;
import com.ll.lib_image_loader.glide.app.ImageLoaderManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Auther Kylin
 * @Data 2020/03/11
 * @Description app主页面
 */
public class HomeActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final Channel[] CHANNELS = new Channel[]{Channel.MY, Channel.DISCOVER, Channel.Friend};

    public static final String TAG = "KYLIN";

    /**
     * views
     */
    private DrawerLayout mDrawerLayout;
    private TextView mCategory, mSearch;
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;
    private HomeViewPagerAdapter mViewPagerAdapter;
    private TextView mUserLogin;

    private ImageView categoryPortrait;
    private LinearLayout categoryUnloginLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(HomeActivity.this);
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
        mUserLogin = findViewById(R.id.home_category_login);

        categoryPortrait = findViewById(R.id.home_category_portrait);
        categoryUnloginLayout = findViewById(R.id.home_category_unlogin_layout);

        mCategory.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mUserLogin.setOnClickListener(this);

        initViewPager();
        initMagicIndicator();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        if (null != mViewPager) {
            mViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
                    CHANNELS);
            mViewPager.setAdapter(mViewPagerAdapter);
        }
    }

    /**
     * 初始化viewpager指示器
     */
    private void initMagicIndicator() {
        mMagicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator mCommonNavigator = new CommonNavigator(HomeActivity.this);
        mCommonNavigator.setAdjustMode(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                //创建pagerTitleView
                SimplePagerTitleView mTitleView = new ScaleTransitionPageTitleView(context);
                mTitleView.setText(CHANNELS[index].getKey());
                mTitleView.setTextSize(19);
                mTitleView.setNormalColor(Color.parseColor("#999999"));
                mTitleView.setSelectedColor(Color.parseColor("#000000"));
                mTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                mTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == mViewPager) {
                            return;
                        }
                        mViewPager.setCurrentItem(index);
                    }
                });
                return mTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });

        mMagicIndicator.setNavigator(mCommonNavigator);//设置MagicIndicator的Navigator
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);//绑定Viewpager和indicator
    }

    /**
     * 初始化Datas
     */
    private void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(HomeActivity.this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        switch (id) {
            case R.id.home_category_view:
                onClickCategory();
                break;
            case R.id.home_search_view:
                break;
            case R.id.home_category_login:
                onClickLogin();
                break;
        }
    }

    private void onClickCategory() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            showLoginedStatus(UserManager.getInstance().hasLogined());
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void onClickLogin() {
        Log.d(TAG, "click the login button");

        if (!UserManager.getInstance().hasLogined()) {
            startActivity(HomeActivity.this, LoginActivity.class);
        } else {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {
        if (null == event) {
            return;
        }

        if (event.getCode() == LoginEvent.SUCCESS) {
            Log.d(TAG, "onLogin: SUCCESS");
            showLoginedStatus(true);
            if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        } else if (event.getCode() == LoginEvent.FAILED) {
            Log.d(TAG, "onLogin: FAILED");
        }
    }

    private void showLoginedStatus(boolean isLogin) {
        if (null == categoryPortrait || null == categoryUnloginLayout) {
            return;
        }

        categoryPortrait.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        UserProtocol user = UserManager.getInstance().getUser();
        if (null != user && null != user.data && !TextUtils.isEmpty(user.data.photoUrl)) {
            Log.d(TAG, "showLoginedStatus: "+ user.data.photoUrl);
            String urlReplace = "http://img1.imgtn.bdimg.com/it/u=3170379310,1742401393&fm=11&gp=0.jpg";
            ImageLoaderManager.newInstance().loadImageForCircle(categoryPortrait, urlReplace);
        }
        categoryUnloginLayout.setVisibility(isLogin ? View.GONE : View.VISIBLE);
    }
}
