package com.hjy.lib_audio.mediaplayer.core;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 继承系统的midiaPlayer，做一个带状态的MediaPlayer
 */
public class CustomMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

    public enum Status {
        IDEL,           //初始状态，即空状态
        INITALIZED,     //初始化状态
        STARTED,        //开始状态
        PAUSED,         //暂停状态
        STOPPTED,       //停止状态
        COMPLETED       //完成状态
    }

    private OnCompletionListener mCompletionListener;
    private Status mStatus;


    public CustomMediaPlayer() {
        super();
        mStatus = Status.IDEL;
        super.setOnCompletionListener(this);
    }

    /**
     * 重置方法
     */
    @Override
    public void reset() {
        super.reset();
        mStatus = Status.IDEL;
    }

    /**
     * 设置播放资源方法
     */
    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        mStatus = Status.INITALIZED;

    }

    /**
     * 开始方法
     */
    @Override
    public void start() throws IllegalStateException {
        super.start();
        mStatus = Status.STARTED;
    }

    /**
     * 暂停
     */
    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        mStatus = Status.PAUSED;
    }

    /**
     * 停止
     */
    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        mStatus = Status.STOPPTED;
    }

    /**
     * 完成
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mStatus = Status.COMPLETED;
    }

    public Status getStatus() {
        return mStatus;
    }

    public boolean isComplete() {
        return mStatus == Status.COMPLETED;
    }

    public void setCompleteListener(OnCompletionListener listener) {
        mCompletionListener = listener;
    }
}
