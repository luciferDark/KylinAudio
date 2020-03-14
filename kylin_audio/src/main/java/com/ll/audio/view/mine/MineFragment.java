package com.ll.audio.view.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ll.audio.model.Channel;

/**
 * @Auther Kylin
 * @Data 2020/03/14
 * @Description fragment 中的 我的 界面
 */
public class MineFragment extends Fragment {
    private Context mContext;

    private static MineFragment mInstance;

    public static Fragment newInstance() {
        if (mInstance == null) {
            synchronized (MineFragment.class) {
                if (mInstance == null) {
                    mInstance = new MineFragment();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = new TextView(this.mContext);
        ((TextView) mRootView).setText(Channel.MY.getKey());
        return mRootView;
    }
}
