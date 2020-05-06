package com.ll.lib_network.okhttps.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络请求参数封装类
 */
public class RequestParam {
    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<>();

    public RequestParam() {
        this((Map<String, String>) null);
    }

    public RequestParam(Map<String, String> source) {
        if (null == source) {
            return;
        }

        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public RequestParam(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    public void put(String key, String value) {
        if (null != key && null != value) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, Object object) throws FileNotFoundException {
        if (null != key) {
            fileParams.put(key, object);
        }
    }

    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        }
        return false;
    }
}
