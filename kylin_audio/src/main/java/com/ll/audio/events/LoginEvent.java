package com.ll.audio.events;

/**
 * @Auther Kylin
 * @Data 2020/4/26 - 17:06
 * @Package com.ll.audio.events
 * @Description
 */
public class LoginEvent {
    private int code = 0;
    private String message;
    private Object obj;


    public static final int SUCCESS = 1;
    public static final int FAILED = 2;

    public LoginEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public LoginEvent(int code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}