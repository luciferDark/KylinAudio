package com.ll.audio.model;

/**
 * @Auther Kylin
 * @Data 2020/03/12
 * @Description  主界面显示页签枚举类
 */
public enum Channel {
    MY("我的", 0x01),
    DISCOVER("发现", 0x02),
    Friend("朋友", 0x03),
    VIDEO("视频", 0x04),
    CLOUD("云村", 0x05)
    ;

    public static final int MY_ID = 0x01;
    public static final int DISCOVER_ID = 0x02;
    public static final int Friend_ID = 0x03;
    public static final int VIDEO_ID= 0x04;
    public static final int CLOUD_ID = 0x05;

    private final String key;
    private final int value;
    Channel(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}
