package com.hjy.imooc_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hjy.imooc_voice.R;
import com.hjy.imooc_voice.api.HttpParams;
import com.hjy.imooc_voice.view.login.manager.UserManager;
import com.hjy.imooc_voice.view.login.user.LoginEvent;
import com.hjy.imooc_voice.view.login.user.User;
import com.hjy.lib_common_ui.base.BaseActivity;
import com.hjy.lib_common_ui.utils.StatusBarUtil;
import com.hjy.lib_network.okhttp.listener.DisposeDataListener;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements DisposeDataListener{

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this, getResources().getColor(R.color.color_cc0000), 1.0f, true);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpParams.login(LoginActivity.this);
            }
        });
    }

    @Override
    public void onSuccess(Object responseObj) {
        //处理正常逻辑
        User user = (User) responseObj;
        UserManager.getInstance().saveUser(user);
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    public void onFailure(Object reasonObj) {
        //登录失败逻辑
    }
}
