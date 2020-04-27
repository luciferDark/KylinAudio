package com.ll.lib_network.okhttps.response.exception;

/**
 * okhttps网络Json类型异常
 *
 * @author kylin
 * @data 2020/04/27
 */
public class OkHttpException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    public int ecode;
    /**
     * 错误描述
     */
    public Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}
