package com.ll.audio.model.user;


/**
 * @Auther Kylin
 * @Data 2020/04/26
 * @Description 用户数据实体类
 */
public class UserModel {
    public String userID; //用户唯一标识符
    public String name; //用户昵称
    public String tick; //用户心情描述
    public String photoUrl;// 用户头像地址
    public String smallPhotoUrl;// 用户小头像地址
    public String bigPhotoUrl;// 用户高清头像地址
    public String mobile; // 用户手机号
    public String deviceID; // 用户设备标识符
    public String platform; // 用户平台
    public String channel; // 用户渠道

    @Override
    public String toString() {
        return "UserModel{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", tick='" + tick + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", smallPhotoUrl='" + smallPhotoUrl + '\'' +
                ", bigPhotoUrl='" + bigPhotoUrl + '\'' +
                ", mobile='" + mobile + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", platform='" + platform + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
