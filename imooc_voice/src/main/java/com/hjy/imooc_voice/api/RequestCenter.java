package com.hjy.imooc_voice.api;

import com.hjy.lib_network.okhttp.CommonOkHttpClient;
import com.hjy.lib_network.okhttp.listener.DisposeDataHandle;
import com.hjy.lib_network.okhttp.listener.DisposeDataListener;
import com.hjy.lib_network.okhttp.request.CommonRequest;
import com.hjy.lib_network.okhttp.request.RequestParams;

/**
 *
 * Created by hjy on 2019/12/15
 */
public class RequestCenter {

    public static void get(String url, RequestParams params, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void get(String url, RequestParams params, RequestParams headers, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params, headers), new DisposeDataHandle(listener, clazz));
    }


    public static void post(String url, RequestParams params, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void post(String url, RequestParams params, RequestParams headers, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params, headers), new DisposeDataHandle(listener, clazz));
    }

    public static void postJson(String url, String json, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createPostJsonRequest(url, json), new DisposeDataHandle(listener, clazz));
    }

    public static void postJson(String url, String json, RequestParams headers, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createPostJsonRequest(url, json, headers), new DisposeDataHandle(listener, clazz));
    }

    public static void postMulti(String url, RequestParams params, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createMultiPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void postMulti(String url, RequestParams params, RequestParams headers, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createMultiPostRequest(url, params, headers), new DisposeDataHandle(listener, clazz));
    }

}
