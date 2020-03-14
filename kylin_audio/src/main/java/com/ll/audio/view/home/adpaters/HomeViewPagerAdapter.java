package com.ll.audio.view.home.adpaters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ll.audio.model.Channel;
import com.ll.audio.view.discover.DiscoverFragment;
import com.ll.audio.view.friend.FriendFragment;
import com.ll.audio.view.mine.MineFragment;

/**
 * @Auther Kylin
 * @Data 2020/03/14
 * @Description app主页面ViewPagerAdapter
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    private Channel[] mChannel;

    public HomeViewPagerAdapter(FragmentManager fm, Channel[] mChannel) {
        super(fm);
        this.mChannel = mChannel;
    }

    @Override
    public Fragment getItem(int i) {
        int type = mChannel[i].getValue();
        //初始化对于的Fragment
        switch (type) {
            case Channel.MY_ID:
                return MineFragment.newInstance();
            case Channel.DISCOVER_ID:
                return DiscoverFragment.newInstance();
            case Channel.Friend_ID:
                return FriendFragment.newInstance();
            case Channel.VIDEO_ID:
                ;
            case Channel.CLOUD_ID:
                ;
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.mChannel == null ? 0 : this.mChannel.length;
    }
}
