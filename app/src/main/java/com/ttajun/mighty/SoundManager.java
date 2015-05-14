package com.ttajun.mighty;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by ttajun on 2015-04-22.
 */
public class SoundManager implements SoundPool.OnLoadCompleteListener {
    public static final int MAX_STREAMS = 10;
    public static final int NO_LOOP = 0;
    public static final int LOOP = -1;

    static SoundManager instance = null;
    SoundPool pool;
    HashMap<Integer, Integer> sounds;
    int toBeLoaded;

    SoundManager() {
        pool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        pool.setOnLoadCompleteListener(this);
        sounds = new HashMap<Integer, Integer>();
        toBeLoaded = 0;
    }

    public static SoundManager getInstance() {
        if(null == instance) instance = new SoundManager();
        return instance;
    }

    public void release() {
        pool.release();
        pool = null;
        instance = null;
    }

    public void addPool(Context context, int resId) {
        synchronized (pool) {
            toBeLoaded++;
        }
        Integer sound = pool.load(context, resId, 0);
        sounds.put(resId, sound);
    }

    public int play(int resId, int loop) {
        Integer sound = sounds.get(resId);
        if(null == sound) return -1;
        return pool.play(sound, 1, 1, 0, loop, 1.0f);
    }

    public void pause(int streamId) {
        pool.pause(streamId);
    }

    public void stop(int streamId) {
        pool.stop(streamId);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        synchronized (pool) {
            toBeLoaded--;
            pool.notify();
        }
    }

    public void waitForLoading() {
        while(true) {
            synchronized (pool) {
                if(0 == toBeLoaded) return;
                try {
                    pool.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
