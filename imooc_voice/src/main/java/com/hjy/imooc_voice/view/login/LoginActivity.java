package com.hjy.imooc_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hjy.imooc_voice.R;
import com.hjy.lib_commin_ui.base.BaseActivity;
import com.hjy.lib_commin_ui.utils.StatusBarUtil;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this, getResources().getColor(R.color.color_cc0000), 1.0f, true);
        setContentView(R.layout.activity_login);
    }
}
