package com.ll.audio.model.user;

/**
 * @Auther Kylin
 * @Data 2020/04/26
 * @Description 用户数据管理类
 */
public class UserManager {
    //------------------单例----------------------//
    private static UserManager mInstance = null;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (null == mInstance) {
            synchronized (UserManager.class) {
                if (null == mInstance) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }
    //------------------单例----------------------//

    private UserProtocol mUser = null;

    /**
     * 获取当前内存登录用户
     *
     * @return
     */
    public UserProtocol getUser() {
        return mUser;
    }

    /**
     * 存储用户数据到内存信息
     *
     * @param mUser
     */
    public void setUser(final UserProtocol mUser) {
        this.mUser = mUser;
        saveLocalUser(mUser);
    }

    /**
     * 存储用户数据到本地
     *
     * @param mUser
     */
    public void saveLocalUser(UserProtocol mUser) {

    }

    /**
     * 获取本地用户数据
     *
     * @param mUser
     * @return
     */
    public UserProtocol getLocalUser(UserProtocol mUser) {
        return null;
    }

    /**
     * 移除用户数据
     */
    public void removeUser() {
        this.mUser = null;
        removeUserLocal();
    }

    /**
     * 移除本地数据库
     */
    public void removeUserLocal() {

    }

    /**
     * 判断用户是否登录了
     *
     * @return
     */
    public boolean hasLogined() {
        if (null == mUser) {
            return false;
        }
        return true;
    }
}
