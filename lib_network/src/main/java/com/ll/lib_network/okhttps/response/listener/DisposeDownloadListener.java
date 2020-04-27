package com.ll.lib_network.okhttps.response.listener;

/**
 * okhttps 下载响应处理回调接口
 *
 * @author kylin
 * @data 2020/04/28
 */
public interface DisposeDownloadListener extends DisposeDataListener {

    /**
     * 响应进度回调接口
     *
     * @param progress
     */
    void onProgress(int progress);
}
