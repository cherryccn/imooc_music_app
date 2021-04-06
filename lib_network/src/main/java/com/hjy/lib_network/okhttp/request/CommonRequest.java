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
 * 生成request对象(暂不完整有待完善)
 * Created by hjy on 2019/12/14
 */
public class CommonRequest {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse("application/octet-stream");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

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
     * Post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * Post请求--Json
     *
     * @param url
     * @param json
     * @return
     */
    public static Request createPostJsonRequest(String url, String json) {
        return createPostJsonRequest(url, json, null);
    }

    /**
     * 文件上传
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createMultiPostRequest(String url, RequestParams params) {
        return createMultiPostRequest(url, params, null);
    }

    /**
     * Get--可带请求头
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String newUrl = urlBuilder.substring(0, urlBuilder.length() - 1);
        //添加请求头
        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder()
                .url(newUrl)
                .headers(mHeaderBuilder.build())
                .get()
                .build();
    }

    /**
     * Post--可带请求头
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
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
     * PostJson--可带请求头
     *
     * @param url
     * @param json
     * @param headers
     * @return
     */
    public static Request createPostJsonRequest(String url, String json, RequestParams headers) {
        //Json数据提交
        RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);
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
                .post(body)
                .build();
    }

    /**
     * 文件上传请求-可带请求头
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createMultiPostRequest(String url, RequestParams params, RequestParams headers) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(
                            Headers.of("Content-Disposition", "form-data;name=\"" + entry.getKey() + "\""),
                            RequestBody.create((File) entry.getValue(), MEDIA_TYPE_FILE));
                } else if (entry.getValue() instanceof String) {
                    requestBody.addPart(
                            Headers.of("Content-Disposition", "form-data;name=\"" + entry.getKey() + "\""),
                            RequestBody.create((String) entry.getValue(), null));
                }
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
                .post(requestBody.build())
                .build();
    }
}
