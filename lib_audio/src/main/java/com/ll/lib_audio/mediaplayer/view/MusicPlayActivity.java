package com.ll.lib_audio.mediaplayer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.ll.lib_audio.R;

/**
 * @Auther Kylin
 * @Data 2020/7/15 - 16:59
 * @Package com.ll.lib_audio.mediaplayer.view
 * @Description
 */
public class MusicPlayActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music_play_acitivity);
    }
}
