package com.ll.lib_network.okhttps.response;

import android.os.Handler;

import com.ll.lib_network.okhttps.response.exception.OkHttpException;
import com.ll.lib_network.okhttps.response.listener.DisposeDataListener;
import com.ll.lib_network.okhttps.response.listener.DisposeDownloadListener;

import okhttp3.Callback;
/**
 * okhttps网络类型响应基类
 *
 * @author kylin
 * @data 2020/04/28
 */
public abstract class CommonBaseCallback implements Callback {
    protected final String EMPTY_MSG = "";

    protected final int NETWORK_ERROR = -1;//网络层错误
    protected final int JSON_ERROR = -2;//JSON格式错误
    protected final int IO_ERROR = -3;//IO类型错误
    protected final int OTHER_ERROR = -4;//其它类型错误


    protected void handlerMainUI(Handler handler, Runnable runnable) {
        if (null == handler || null == runnable) {
            return;
        }
        handler.post(runnable);
    }


    protected void onFailed(DisposeDataListener listener, Object responseObj) {
        if (null != listener){
            listener.onFailed(responseObj);
        }
    }
    protected void onFailed(DisposeDataListener listener, int ecode, Object emsg) {
        if (null != listener){
            listener.onFailed(new OkHttpException(ecode, emsg));
        }
    }

    protected void onSuccess(DisposeDataListener listener, Object responseObj) {
        if (null != listener){
            listener.onSuccess(responseObj);
        }
    }

    protected void onProgress(DisposeDataListener listener, int progress) {
        if (null == listener){
            return;
        }
        if (! (listener instanceof DisposeDownloadListener)){
            return;
        }
        ((DisposeDownloadListener)listener).onProgress(progress);
    }
}
