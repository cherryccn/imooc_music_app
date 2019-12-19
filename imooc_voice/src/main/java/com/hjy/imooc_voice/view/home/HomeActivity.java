package com.hjy.imooc_voice.view.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.hjy.imooc_voice.R;
import com.hjy.imooc_voice.api.HttpManage;
import com.hjy.imooc_voice.model.CHANNEL;
import com.hjy.imooc_voice.view.home.adpater.HomePagerAdapter;
import com.hjy.imooc_voice.view.login.LoginActivity;
import com.hjy.imooc_voice.view.login.manager.UserManager;
import com.hjy.lib_commin_ui.base.BaseActivity;
import com.hjy.lib_network.okhttp.exception.OkHttpException;
import com.hjy.lib_network.okhttp.listener.DisposeDataListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final CHANNEL[] CHANNELS = new CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.FRIEND,};

    private DrawerLayout mDrawerLayout;
    private View mToggleView;
    private View mSearchView;
    private ViewPager mViewPager;
    private HomePagerAdapter mHomePagerAdapter;

    private View unLogginLayout;
    private ImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mToggleView.setOnClickListener(this);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);

        mViewPager = findViewById(R.id.view_pager);
        mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), CHANNELS);
        mViewPager.setAdapter(mHomePagerAdapter);
        initMagicIndicator();

        //登录相关UI
        unLogginLayout = findViewById(R.id.unloggin_layout);
        unLogginLayout.setOnClickListener(this);
        mPhotoView = findViewById(R.id.avatr_view);
    }

    private void initData() {
        HttpManage.login(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.d("aaaa", "onSuccess: " + responseObj.toString());
            }

            @Override
            public void onFailure(Object reasonObj) {
                OkHttpException exception = (OkHttpException) reasonObj;
                Log.d("aaaa", "getEcode: " + exception.getEcode() + "   getEmsg: " + exception.getEmsg());
            }

        });
    }

    /**
     * 初始化指示器
     */
    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(CHANNELS[index].getKey());
                simplePagerTitleView.setTextSize(19);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unloggin_layout:
                if (!UserManager.getInstance().hasLogin()) {
                    LoginActivity.start(this);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
                break;
            case R.id.toggle_view:
                break;
            case R.id.search_view:
                break;
        }
    }
}
