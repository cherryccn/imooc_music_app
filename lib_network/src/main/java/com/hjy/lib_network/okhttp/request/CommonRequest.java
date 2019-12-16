package com.hjy.lib_network.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 对外提供get/post/文件上传请求
 * Created by hjy on 2019/12/14
 */
public class CommonRequest {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse("application/octet-stream");

    /**
     * Get请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * Post请求--Json
     * @param url
     * @param params
     * @return
     */
    public static Request createPostByJsonRequest(String url, RequestParams params) {
        return createPostByJsonRequest(url, params, null);
    }



    public static Request createPostByFormRequest(String url, RequestParams params) {
        return createPostByFormRequest(url, params, null);
    }

    /**
     * Post请求对象，以Json的方式
     * 可以带请求头的
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostByJsonRequest(String url, RequestParams params, RequestParams headers) {
        //FormBody--表单数据提交
        FormBody.Builder mFormBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //参数遍历
                mFormBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        //请求头
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
     * Post请求对象，以Form表单的方式
     * 可以带请求头的
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostByFormRequest(String url, RequestParams params, RequestParams headers) {
        //FormBody--表单数据提交
        FormBody.Builder mFormBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //参数遍历
                mFormBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        //请求头
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

    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
            //参数遍历
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
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


    /**
     * 文件上传请求
     */
    public static Request createMultiPostRequest(String url, RequestParams params) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);//指定表单类型
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(
                            Headers.of("content-Disposition", "form-data;name=\"" + entry.getKey() + "\""),
                            RequestBody.create((File) entry.getValue(), MEDIA_TYPE_FILE));
                } else if (entry.getValue() instanceof String) {
                    requestBody.addPart(
                            Headers.of("content-Disposition", "form-data;name=\"" + entry.getKey() + "\""),
                            RequestBody.create((String) entry.getValue(), null));
                }
            }
        }
        return new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
    }

}
