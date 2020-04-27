package com.ll.lib_network.okhttps.response;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ll.lib_network.okhttps.response.exception.OkHttpException;
import com.ll.lib_network.okhttps.response.listener.DisposeDataHandler;
import com.ll.lib_network.okhttps.response.listener.DisposeDataListener;
import com.ll.lib_network.okhttps.utils.ResponseEntityToModule;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * okhttps网络Json类型响应封装
 *
 * @author kylin
 * @data 2020/04/27
 */
public class CommonJsonCallback extends CommonBaseCallback {
    public DisposeDataListener mDisposeDataListener;
    private Class<?> mClazz;//需要转换的类型实例字节码

    private Handler mDeliverHandler;//用于主线程切换

    public CommonJsonCallback(DisposeDataHandler mHandler) {
        this.mDisposeDataListener = mHandler.mDisposeDataListener;
        this.mClazz = mHandler.mClazz;

        mDeliverHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        handlerMainUI(mDeliverHandler, new Runnable() {
            @Override
            public void run() {
                onFailed(mDisposeDataListener, NETWORK_ERROR, e);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        handlerMainUI(mDeliverHandler, new Runnable() {
            @Override
            public void run() {
                handlerResponse(result);
            }
        });
    }

    /**
     *  文件结果Json类型处理
     * @param result
     */
    private void handlerResponse(String result) {
        if (TextUtils.isEmpty(result)){
            onFailed(mDisposeDataListener, NETWORK_ERROR, EMPTY_MSG);
            return;
        }

        try {
            if (null == mClazz){
                //不做Json数据处理直接抛给应用层
                onSuccess(mDisposeDataListener, result);
            } else {
                Object obj = ResponseEntityToModule.parseJsonToModule(result, mClazz);
                if (null == obj){
                    onFailed(mDisposeDataListener, JSON_ERROR, EMPTY_MSG);
                } else {
                    onSuccess(mDisposeDataListener, obj);
                }
            }
        } catch (Exception e){
            onFailed(mDisposeDataListener, JSON_ERROR, e);
        }
    }
}
