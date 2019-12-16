package com.hjy.lib_network.okhttp;

import com.hjy.lib_network.okhttp.Interceptor.LogInterceptor;
import com.hjy.lib_network.okhttp.listener.DisposeDataHandle;
import com.hjy.lib_network.okhttp.listener.DisposeDataListener;
import com.hjy.lib_network.okhttp.response.CommonCallbackFileCallback;
import com.hjy.lib_network.okhttp.response.CommonCallbackJsonCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用来发送get/post请求的工具类，包括设置一些请求的共用参数
 * Created by hjy on 2019/12/15
 */
public class CommonOkHttpClient {

    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    //完成对OkHttpClient的初始化
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)//不使用代理
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true)//允许重定向
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;//返回true则表示对域名都是信任的；
                    }
                })
                //添加公共请求头
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("User-Agent", "Imooc-Mobile")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(LogInterceptor.get());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * get请求
     *
     * @return
     */
    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonCallbackJsonCallback(handle));
        return call;
    }

    /**
     * post请求
     *
     * @return
     */
    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonCallbackJsonCallback(handle));
        return call;
    }

    /**
     * 文件下载请求
     *
     * @return
     */
    public static Call downLoadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonCallbackFileCallback(handle));
        return call;
    }
}
