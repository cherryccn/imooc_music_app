package com.hjy.lib_network.okhttp.response.base;

import android.os.Handler;

import com.hjy.lib_network.okhttp.listener.DisposeDataListener;
import com.hjy.lib_network.okhttp.listener.DisposeDownloadListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理Json类型的响应/文件类型的响应 的基类
 * Created by hjy on 2019/12/15
 */
public class BaseCommonCallback implements Callback {
    protected final String EMPTY_MSG = "";
    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2;    // the JSON relative error
    protected final int IO_ERROR = -3;      // the IO relative error
    protected final int OTHER_ERROR = -4;   // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    protected Handler mDeliveryHandler;
    protected DisposeDataListener mListener;
    protected DisposeDownloadListener mDownloadListener;

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

    }
}
