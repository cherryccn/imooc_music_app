package com.hjy.lib_network.okhttp.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;

/**
 * 对外提供get/post/文件上传请求
 * Created by hjy on 2019/12/14
 */
public class CommonRequest {

    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 可以带请求头的post请求对象
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
        FormBody.Builder mFormBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //参数遍历
                mFormBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                //请求头遍历
                mHeaderBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder()
                .url(url)
                .headers(mHeaderBuilder.build())
                .post(mFormBodyBuilder.build())
                .build();
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * 可以带请求头的Get请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urBuilder = new StringBuilder(url).append("?");
        for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
            //参数遍历
            urBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                //请求头遍历
                mHeaderBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder()
                .url(url)
                .headers(mHeaderBuilder.build())
                .get()
                .build();

    }
}
