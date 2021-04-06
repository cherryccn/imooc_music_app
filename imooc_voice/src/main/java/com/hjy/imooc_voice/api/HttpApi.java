package com.hjy.imooc_voice.api;

/**
 *  Http常量类，存放一些url等
 */
public class HttpApi {

//    private static final String ROOT_URL = "http://imooc.com/api";
    private static final String ROOT_URL="http:39.97.122.129";

    /**
     * 登陆接口
     */
    public static final String LOGIN = ROOT_URL + "/module_voice/login_phone";

    /**
     * 首页请求接口
     */
    public static final String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

    public static final String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

    public static final String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";


}
