package com.hjy.lib_network.okhttp.listener;

/**
 * 包装类
 * Created by hjy on 2019/12/15
 */
public class DisposeDataHandle {

    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;//json->object  object.class  后面要解析的json实体类
    public String mSource = null;//文件保存路径

    public DisposeDataHandle(DisposeDataListener mListener) {
        this.mListener = mListener;
    }

    public DisposeDataHandle(DisposeDataListener mListener, Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
    }

    public DisposeDataHandle(DisposeDataListener mListener, String mSource) {
        this.mListener = mListener;
        this.mSource = mSource;
    }
}
