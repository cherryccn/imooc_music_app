package com.hjy.imooc_voice.api;

/**
 *  Http常量类，存放一些url等
 */
public class HttpConstant {

    private static final String ROOT_URL = "http://imooc.com/api";

    /**
     * 登陆接口
     */
    public static String LOGIN = ROOT_URL + "/module_voice/login_phone";

    /**
     * 首页请求接口
     */
    public static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

    public static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

    public static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";


}
