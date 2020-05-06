package com.ll.audio.api;

import com.ll.audio.model.user.UserProtocol;
import com.ll.lib_network.okhttps.client.CommonOkhttpsClient;
import com.ll.lib_network.okhttps.request.CommonRequest;
import com.ll.lib_network.okhttps.request.RequestParam;
import com.ll.lib_network.okhttps.response.listener.DisposeDataHandler;
import com.ll.lib_network.okhttps.response.listener.DisposeDataListener;

/**
 * @Auther Kylin
 * @Data 2020/4/26 - 15:48
 * @Package com.ll.audio.api
 * @Description 网络请求中心
 */
public class ApiCenter {

    /**
     * 网络请求常量配置
     */
    public static class HttpConstants {
        private static final String ROOL_URL = "http://39.97.122.129";

        private static final String LOGIN = ROOL_URL + "/module_voice/login_phone";
    }

    /**
     * 调用Get请求发送消息
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void getRequest(String url, RequestParam params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkhttpsClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandler(listener, clazz));
    }

    /**
     * 调用Get请求发送消息
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void postRequest(String url, RequestParam params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkhttpsClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandler(listener, clazz));
    }


    /**
     * 登录请求接口调用
     *
     * @param listener
     */
    public static void login(DisposeDataListener listener) {
        RequestParam params = new RequestParam();
        params.put("mb", "18734924592");
        params.put("pwd", "999999q");
        getRequest(HttpConstants.LOGIN, params, listener, UserProtocol.class);
    }
}
