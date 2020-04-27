package com.ll.lib_network.okhttps.response.listener;

/**
 * 接口回调信息封装类
 *
 * @author kylin
 * @data 2020/04/27
 */
public class DisposeDataHandler {
    public DisposeDataListener mDisposeDataListener = null;
    public Class<?> mClazz = null;
    public String mSource = null;//文件保存路径

    public DisposeDataHandler(DisposeDataListener mDisposeDataListener) {
        this.mDisposeDataListener = mDisposeDataListener;
    }

    public DisposeDataHandler(DisposeDataListener mDisposeDataListener, Class<?> mClazz) {
        this.mDisposeDataListener = mDisposeDataListener;
        this.mClazz = mClazz;
    }

    public DisposeDataHandler(DisposeDataListener mDisposeDataListener, String mSource) {
        this.mDisposeDataListener = mDisposeDataListener;
        this.mSource = mSource;
    }
}
