package com.ll.audio.model.user;

/**
 * @Auther Kylin
 * @Data 2020/04/26
 * @Description 用户消息实体类
 */
public class UserProtocol {
    public int ecode;
    public String emsg;

    public UserModel data;

    @Override
    public String toString() {
        return "UserProtocol{" +
                "ecode=" + ecode +
                ", emsg='" + emsg + '\'' +
                ", userModel=" + data +
                '}';
    }
}
