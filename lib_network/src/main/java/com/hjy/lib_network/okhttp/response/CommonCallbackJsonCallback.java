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

    private Class<?> mClass;

    public CommonCallbackJsonCallback(DisposeDataHandle handle) {
        mListener = handle.mListener;
        mClass = handle.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        final String result = response.body().string();
        Log.d("bbbbb", "result: "+result);
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
            //业务层不需要解析
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                //解析为实体对象，可以用gson ，fastjson替换
                Object obj = ResponseEntityToModule.parseJsonToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                }else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR,EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(JSON_ERROR,EMPTY_MSG));
        }
    }
}
