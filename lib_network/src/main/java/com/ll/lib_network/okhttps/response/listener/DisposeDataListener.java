package com.ll.lib_network.okhttps.response.listener;

/**
 * okhttps 响应处理回调接口
 *
 * @author kylin
 * @data 2020/04/27
 */
public interface DisposeDataListener {

    /**
     * 响应成功回调接口
     *
     * @param responseObj
     */
    void onSuccess(Object responseObj);

    /**
     * 响应失败回调接口
     *
     * @param responseObj
     */
    void onFailed(Object responseObj);
}
