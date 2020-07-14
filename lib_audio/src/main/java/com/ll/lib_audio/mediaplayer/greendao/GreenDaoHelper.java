package com.ll.lib_audio.mediaplayer.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.ll.lib_audio.mediaplayer.app.AudioHelper;
import com.ll.lib_audio.mediaplayer.greendao.gen.AudioBeanDao;
import com.ll.lib_audio.mediaplayer.greendao.gen.DaoMaster;
import com.ll.lib_audio.mediaplayer.greendao.gen.DaoSession;

/**
 * @Auther Kylin
 * @Data 2020/7/14 - 19:11
 * @Package com.ll.lib_audio.mediaplayer.greendao
 * @Description
 */
public  class GreenDaoHelper {
    private static final String DB_NAME = "kylin_music_db";

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private AudioBeanDao mAudioBeanDao;
    //------------------单例----------------------//
    private static GreenDaoHelper mInstance = null;

    private GreenDaoHelper() {
        initDataBase();
    }
    public static GreenDaoHelper getInstance() {
        if (null == mInstance) {
            synchronized (GreenDaoHelper.class) {
                if (null == mInstance) {
                    mInstance = new GreenDaoHelper();
                }
            }
        }
        return mInstance;
    }
    //------------------单例----------------------//

    /**
     * 初始化数据库
     */
    private void initDataBase() {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(AudioHelper.getInstance().getContext(),
                DB_NAME, null);
        mSQLiteDatabase = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mSQLiteDatabase);
        mDaoSession = mDaoMaster.newSession();
        mAudioBeanDao = mDaoSession.getAudioBeanDao();
    }


}
