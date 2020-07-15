package com.ll.lib_audio.mediaplayer.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.ll.lib_audio.mediaplayer.app.AudioHelper;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.bean.Favourite;
import com.ll.lib_audio.mediaplayer.greendao.gen.AudioBeanDao;
import com.ll.lib_audio.mediaplayer.greendao.gen.DaoMaster;
import com.ll.lib_audio.mediaplayer.greendao.gen.DaoSession;
import com.ll.lib_audio.mediaplayer.greendao.gen.FavouriteDao;

/**
 * @Auther Kylin
 * @Data 2020/7/14 - 19:11
 * @Package com.ll.lib_audio.mediaplayer.greendao
 * @Description
 */
public class GreenDaoHelper {
    private static final String DB_NAME = "kylin_music_db";

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private AudioBeanDao mAudioBeanDao;
    private FavouriteDao mFavouriteDao;
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
        mFavouriteDao = mDaoSession.getFavouriteDao();
    }

    public AudioBeanDao getAudioBeanDao() {
        if (null == mDaoSession) {
            return null;
        }
        if (null == mAudioBeanDao) {
            mAudioBeanDao = mDaoSession.getAudioBeanDao();
        }
        return mAudioBeanDao;
    }

    public FavouriteDao getFavouriteDao() {
        if (null == mDaoSession) {
            return null;
        }
        if (null == mFavouriteDao) {
            mFavouriteDao = mDaoSession.getFavouriteDao();
        }
        return mFavouriteDao;
    }

    /**
     * 添加歌曲收藏
     *
     * @param bean
     */
    public void addFavouriteAudioBean(AudioBean bean) {
        Favourite favourite = new Favourite();
        favourite.setAudioId(bean.getId());
        favourite.setAudioBean(bean);
        if (null == mFavouriteDao) {
            mFavouriteDao = mDaoSession.getFavouriteDao();
        }
        mFavouriteDao.insertOrReplace(favourite);
    }

    /**
     * 移除歌曲收藏
     *
     * @param bean
     */
    public void removeFavouriteAudioBean(AudioBean bean) {
        if (null == mFavouriteDao) {
            mFavouriteDao = mDaoSession.getFavouriteDao();
        }
        Favourite favourite = queryFavouriteAudioBean(bean);
        mFavouriteDao.delete(favourite);
    }

    /**
     *  查询歌曲收藏
     *
     * @param bean
     */
    public Favourite queryFavouriteAudioBean(AudioBean bean) {
        if (null == mFavouriteDao) {
            mFavouriteDao = mDaoSession.getFavouriteDao();
        }
        Favourite favourite = mFavouriteDao.queryBuilder()
                .where(FavouriteDao.Properties.AudioId.eq(bean.getId()))
                .unique();
        return favourite;
    }
}
