package com.hjy.imooc_voice.api;

import com.hjy.imooc_voice.model.user.User;
import com.hjy.lib_network.okhttp.listener.DisposeDataListener;
import com.hjy.lib_network.okhttp.request.RequestParams;

/**
 * API请求
 */
public class HttpManage {

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", "18734924592");
        params.put("pwd", "999999q");
        RequestCenter.get(HttpConstant.LOGIN, params, User.class, listener);
    }
}
