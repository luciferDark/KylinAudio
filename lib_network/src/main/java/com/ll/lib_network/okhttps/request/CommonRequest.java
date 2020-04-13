package com.ll.lib_network.okhttps.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * get/post/file upload
 * 解耦封装
 *
 * @author kylin
 * @date 2020/4/13
 */
public class CommonRequest {
    public static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    /**
     *  创建Get请求
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParam params) {
        return createGetRequest(url,params, null);
    }

    /**
     *  创建Get请求
     * @param url
     * @param params
     * @param heads
     * @return
     */
    public static Request createGetRequest(String url, RequestParam params, RequestParam heads) {
        StringBuilder mBuilder = new StringBuilder(url);
        if (null != params) {
            mBuilder.append("?");
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        // 添加请求头
        Headers.Builder mHeadersBuilder = new Headers.Builder();
        if (null != heads) {
            for (Map.Entry<String, String> entry : heads.urlParams.entrySet()) {
                mHeadersBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request mRequest = new Request.Builder()
                .url(mBuilder.toString())
                .headers(mHeadersBuilder.build())
                .get()
                .build();
        return mRequest;
    }

    /**
     * 创建post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParam params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 创建post请求
     *
     * @param url
     * @param params
     * @param heads
     * @return
     */
    public static Request createPostRequest(String url, RequestParam params, RequestParam heads) {
        // 添加请求参数
        FormBody.Builder mFormBodyBuilder = new FormBody.Builder();
        if (null != params) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        // 添加请求头
        Headers.Builder mHeadersBuilder = new Headers.Builder();
        if (null != heads) {
            for (Map.Entry<String, String> entry : heads.urlParams.entrySet()) {
                mHeadersBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request mRequest = new Request.Builder()
                .url(url)
                .headers(mHeadersBuilder.build())
                .post(mFormBodyBuilder.build())
                .build();
        return mRequest;
    }

    /**
     *  创建文件上传请求
     * @param url
     * @param params
     * @return
     */
    public static Request createMultiPostRequest(String url, RequestParam params){
        MultipartBody.Builder mMutipartBodyBuilder = new MultipartBody.Builder();
        mMutipartBodyBuilder.setType(MultipartBody.FORM);
        if (null != params){
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()){
                if (entry.getValue() instanceof File){
                    mMutipartBodyBuilder.addPart(
                            Headers.of("content-Disposition","form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String){
                    mMutipartBodyBuilder.addPart(
                            Headers.of("content-Disposition","form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        Request mRequest = new Request.Builder()
                .url(url)
                .post(mMutipartBodyBuilder.build())
                .build();
        return  mRequest;
    }
}
