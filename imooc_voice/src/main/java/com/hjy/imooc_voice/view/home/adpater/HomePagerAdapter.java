package com.hjy.imooc_voice.view.home.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hjy.imooc_voice.model.CHANNEL;
import com.hjy.imooc_voice.view.discovery.DiscoveryFragment;
import com.hjy.imooc_voice.view.friend.FriendFragment;
import com.hjy.imooc_voice.view.mine.MineFragment;

/**
 * 首页ViewPager的Adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

  private CHANNEL[] mList;

  public HomePagerAdapter(FragmentManager fm, CHANNEL[] datas) {
    super(fm);
    mList = datas;
  }

  //初始化对应的fragment
  @Override
  public Fragment getItem(int position) {
    int type = mList[position].getValue();
    switch (type) {
      case CHANNEL.MINE_ID:
        return MineFragment.newInstance();
      case CHANNEL.DISCORY_ID:
        return DiscoveryFragment.newInstance();
      case CHANNEL.FRIEND_ID:
        return FriendFragment.newInstance();
    }
    return null;
  }

  @Override
  public int getCount() {
    return mList == null ? 0 : mList.length;
  }

}