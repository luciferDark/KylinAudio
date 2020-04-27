package com.ll.lib_network.okhttps.client;


import com.ll.lib_network.okhttps.config.CommonOkhttpsConfig;
import com.ll.lib_network.okhttps.response.CommonFileCallback;
import com.ll.lib_network.okhttps.response.CommonJsonCallback;
import com.ll.lib_network.okhttps.response.listener.DisposeDataHandler;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttpsClient 封装，对外提供接口
 *
 * @author kylin
 * @date 2020/4/18
 */
public class CommonOkhttpsClient {
    private static OkHttpClient mOkHttpClient;

    //------------------单例----------------------//
    private static CommonOkhttpsClient mInstance;

    private CommonOkhttpsClient() {
    }

    public static CommonOkhttpsClient newInstance() {
        if (null == mInstance) {
            synchronized (CommonOkhttpsClient.class) {
                if (null == mInstance) {
                    mInstance = new CommonOkhttpsClient();
                }
            }
        }

        return mInstance;
    }

    private static void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 添加域名拦截器处理，这边不做处理直接返回
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        // 添加公共请求head 拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", CommonOkhttpsConfig.HEAD_AGENT)
                        .build();
                return chain.proceed(request);
            }
        });

        //设置超时时间
        builder.connectTimeout(CommonOkhttpsConfig.TIME_CONNECT_OUT, CommonOkhttpsConfig.TIME_Unit)
                .readTimeout(CommonOkhttpsConfig.TIME_READ_OUT, CommonOkhttpsConfig.TIME_Unit)
                .writeTimeout(CommonOkhttpsConfig.TIME_WRITE_OUT, CommonOkhttpsConfig.TIME_Unit);
        //添加重定向
        builder.followRedirects(true);

        mOkHttpClient = builder.build();
    }

    static {
        initOkHttpClient();
    }

    /**
     *  get请求
     * @return
     */
    public static Call get(Request request, DisposeDataHandler handler){
        return request(request, handler);
    }
    /**
     *  post请求
     * @return
     */
    public static Call post(Request request, DisposeDataHandler handler){
        return request(request, handler);
    }

    /**
     * request
     * @param request
     * @param handler
     * @return
     */
    public static Call request(Request request, DisposeDataHandler handler){
        if (null == mOkHttpClient){
            return null;
        }
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handler));
        return call;
    }
    /**
     *  文件下载请求
     * @return
     */
    public static Call downloadFile(Request request, DisposeDataHandler handler){
        if (null == mOkHttpClient){
            return null;
        }
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handler));

        return call;
    }
}
