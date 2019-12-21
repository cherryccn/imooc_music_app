package com.hjy.imooc_voice.view.login.manager;

import com.hjy.imooc_voice.view.login.user.User;

/**
 * 单例管理登录用户信息
 * Created by hjy on 2019/12/19
 */
public class UserManager {

    private static UserManager mInstance;
    private User mUser;

    public static UserManager getInstance() {
        //双检查机制
        if (mInstance == null) {
            //给类的字节码加锁（因为每个类对应的字节码是唯一的，这个class对象能保证只有一把锁，所以能确保对象是单例的）
            //确保线程安全，否则出现多个单例对象
            synchronized (UserManager.class) {
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存用户信息到内存
     */
    public void saveUser(User user) {
        mUser = user;
        saveLocal(user);
    }

    /**
     * 持久化用户信息
     * 保存到数据库
     *
     * @param user
     */
    private void saveLocal(User user) {

    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public User getUser() {
        return mUser;
    }

    /**
     * 从本地获取
     *
     * @return
     */
    public User getLocal() {
        return null;
    }

    /**
     * 从内存中删除用户信息
     */
    public void removeUser() {
        mUser = null;
        removeLocal();
    }

    /**
     * 从数据库中删除用户信息
     */
    private void removeLocal() {

    }


    /**
     * 判断是否登录过
     *
     * @return
     */
    public boolean hasLogin() {
        return getUser() != null;
    }
}
