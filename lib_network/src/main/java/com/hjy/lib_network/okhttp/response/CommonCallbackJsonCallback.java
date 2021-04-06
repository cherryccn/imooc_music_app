package com.hjy.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hjy.lib_network.okhttp.exception.OkHttpException;
import com.hjy.lib_network.okhttp.listener.DisposeDataHandle;
import com.hjy.lib_network.okhttp.response.base.BaseCommonCallback;
import com.hjy.lib_network.okhttp.utils.ResponseEntityToModule;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 处理Json类型的响应
 * Created by hjy on 2019/12/15
 */
public class CommonCallbackJsonCallback extends BaseCommonCallback {

    private final Class<?> mClass;

    public CommonCallbackJsonCallback(DisposeDataHandle handle) {
        mListener = handle.mListener;
        mClass = handle.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                //接口回调    到主线程
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(String result) {
        if (result == null || result.trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            if (mClass == null) {
                //不需要解析，回调到应用层
                mListener.onSuccess(result);
            } else {
                //将json数据解析为java对象返回到应用层
                //ResponseEntityToModule   自定义的json数据解析类，这里可以使用gson,fastjson等
                Object obj = ResponseEntityToModule.parseJsonToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    //json不合法
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            //其他异常
            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
            e.printStackTrace();
        }
    }
}
