package com.ll.lib_audio.mediaplayer.view.adpaters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BottomListRecycleViewAdapter
        extends RecyclerView.Adapter<BottomListRecycleViewAdapter.BottomListRecycleItemHolder> {
    private ArrayList<AudioBean> mCurrentList = null;
    private AudioBean mCurrentAudioBean = null;
    private WeakReference<Context> mContext = null;

    public BottomListRecycleViewAdapter(ArrayList<AudioBean> currentList,
                                        AudioBean currentAudioBean,
                                        Context context) {
        this.mCurrentList = currentList;
        this.mCurrentAudioBean = currentAudioBean;
        this.mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public BottomListRecycleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BottomListRecycleItemHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_bottom_list_view_recycle_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BottomListRecycleItemHolder holder, @SuppressLint("RecyclerView") final int position) {
        int showColor = mContext.get().getResources().getColor(R.color.color_white);
        if (AudioController.getInstance().getCurrentAudioBean().getId() == mCurrentList.get(position).getId()){
            holder.mPlayingIcon.setVisibility(View.VISIBLE);
            showColor = mContext.get().getResources().getColor(R.color.color_yellow);
        } else {
            holder.mPlayingIcon.setVisibility(View.GONE);
        }

        holder.mSongName.setTextColor(showColor);
        holder.mSingle.setTextColor(showColor);
        holder.mSongName.setText(mCurrentList.get(position).getName());
        holder.mSingle.setText(" - "+ mCurrentList.get(position).getSinger());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioController.getInstance().removeFromQueue(mCurrentList.get(position));
            }
        });

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioController.getInstance().setCurrrentIndex(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mCurrentList ? 0 : mCurrentList.size();
    }


    public class BottomListRecycleItemHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mContainer;
        private ImageView mPlayingIcon;
        private TextView mSongName;
        private TextView mSingle;
        private ImageView mDelete;


        public BottomListRecycleItemHolder(@NonNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.bottom_list_view_recycle_view_container);
            mPlayingIcon = itemView.findViewById(R.id.bottom_list_view_recycle_view_playing_icon);
            mSongName = itemView.findViewById(R.id.bottom_list_view_recycle_view_name);
            mSingle = itemView.findViewById(R.id.bottom_list_view_recycle_view_single);
            mDelete = itemView.findViewById(R.id.bottom_list_view_recycle_view_delete);
        }
    }

    public void updateCurrentAudio(AudioBean bean){
        this.mCurrentAudioBean = bean;
        notifyDataSetChanged();
    }
}
