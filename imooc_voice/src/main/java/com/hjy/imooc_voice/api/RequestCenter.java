package com.hjy.imooc_voice.api;

import com.hjy.imooc_voice.model.user.User;
import com.hjy.lib_network.okhttp.CommonOkHttpClient;
import com.hjy.lib_network.okhttp.listener.DisposeDataHandle;
import com.hjy.lib_network.okhttp.listener.DisposeDataListener;
import com.hjy.lib_network.okhttp.request.CommonRequest;
import com.hjy.lib_network.okhttp.request.RequestParams;

/**
 * API请求
 * Created by hjy on 2019/12/15
 */
public class RequestCenter {

    static class HttpConstants {
        private static final String ROOT_URL = "http://imooc.com/api";

        /**
         * 首页请求接口
         */
        private static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

        private static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";


        /**
         * 登陆接口
         */
        public static String LOGIN = ROOT_URL + "/module_voice/login_phone.html";
    }

    //根据参数发送所有post请求
    public static void getRequest(String url, RequestParams params, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params, Class<?> clazz, DisposeDataListener listener) {
        CommonOkHttpClient.post(CommonRequest.createPostByFormRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", "18734924592");
        params.put("pwd", "999999q");
        RequestCenter.getRequest("https://api.xsmcfx.com/cms/api/services/app/Textcast/GetStandpointViewListByPaged?EquipmentNumber=AF6A7C18F5FF0EEDBE9976F840E5900F363CCC9E&SkipCount=0&Account=&BusinessPlatform=1", params, User.class, listener);
    }
}
