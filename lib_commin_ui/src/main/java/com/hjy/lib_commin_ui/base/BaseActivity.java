package com.hjy.lib_commin_ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hjy.lib_commin_ui.utils.StatusBarUtil;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this, Color.WHITE, 1.0f, true);

    }
}
