package com.hjy.lib_network.okhttp;

import com.hjy.lib_network.okhttp.Interceptor.LogInterceptor;
import com.hjy.lib_network.okhttp.listener.DisposeDataHandle;
import com.hjy.lib_network.okhttp.response.CommonCallbackFileCallback;
import com.hjy.lib_network.okhttp.response.CommonCallbackJsonCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用来发送get/post请求的工具类，包括设置一些请求的共用参数，https支持
 * Created by hjy on 2019/12/15
 */
public class CommonOkHttpClient {

    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    //完成对OkHttpClient的配置
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)//不使用代理
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true)//支持重定向
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;            //返回true则表示对域名都是信任的，https支持
                    }
                })
//                .sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager())//信任所有https点
                //  为所有请求添加请求头，看个人需求
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("User-Agent", "Imooc-Mobile")// 标明发送本次请求的客户端
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(LogInterceptor.get());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 通过构造好的Request,Callback去发送请求
     *
     */
    public static void get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonCallbackJsonCallback(handle));
    }

    public static void post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonCallbackJsonCallback(handle));
    }

    public static Call downLoadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonCallbackFileCallback(handle));
        return call;
    }
}
