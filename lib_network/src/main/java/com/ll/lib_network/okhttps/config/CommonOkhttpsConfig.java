package com.ll.lib_network.okhttps.config;


import java.util.concurrent.TimeUnit;

/**
 * okhttpsClient 配置常量
 *
 * @author kylin
 * @date 2020/4/18
 */
public class CommonOkhttpsConfig {
    public static final int TIME_OUT = 30;
    public static final int TIME_CONNECT_OUT = 30;
    public static final int TIME_READ_OUT = 30;
    public static final int TIME_WRITE_OUT = 30;
    public static final TimeUnit TIME_Unit = TimeUnit.SECONDS;
    public static final String HEAD_AGENT = "webView_kylin";
}
