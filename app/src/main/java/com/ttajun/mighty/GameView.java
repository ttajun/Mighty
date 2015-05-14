package com.ttajun.mighty;

import com.ttajun.mighty.screen.*;
import com.ttajun.mighty.gameobject.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;

/**
 * Created by ttajun on 2015-04-22.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    int m_frametime;
    Thread m_thread;
    boolean m_bRun;
    HashMap<Integer, BaseScreen> m_screens;
    BaseScreen m_currScreen;
    ResourceLoader m_loader;

    class ResourceLoader extends Thread {
        boolean m_bFinished;

        ResourceLoader() {
            m_bFinished = false;
        }

        public void run() {
            Drawables.getInstance().reset();

            TitleScreen titleScreen = (TitleScreen) m_screens.get(ScreenFactory.TITLE_SCREEN);
            titleScreen.setResources(getResources());
            GameScreen gameScreen = (GameScreen) m_screens.get(ScreenFactory.GAME_SCREEN);
            gameScreen.setResources(getResources());
            gameScreen.loadResources();

            m_bFinished = true;
        }
    }

    public GameView(Context context) {
        super(context);
        gameViewInit();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameViewInit();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gameViewInit();
    }

    private void gameViewInit() {
        m_thread = null;
        m_frametime = MightyApplication.FRAME_TIME;

        ScreenFactory sf = ScreenFactory.getInstance();
        m_screens = new HashMap<>();

        LoadingScreen loadingScreen = (LoadingScreen)sf.createScreen(ScreenFactory.LOADING_SCREEN);
        m_screens.put(ScreenFactory.LOADING_SCREEN, loadingScreen);

        TitleScreen titleScreen = (TitleScreen)sf.createScreen(ScreenFactory.TITLE_SCREEN);
        m_screens.put(ScreenFactory.TITLE_SCREEN, titleScreen);

        GameScreen gameScreen = (GameScreen)sf.createScreen(ScreenFactory.GAME_SCREEN);
        m_screens.put(ScreenFactory.GAME_SCREEN, gameScreen);

        getHolder().addCallback(this);

        m_loader = new ResourceLoader();
        m_loader.start();
    }

    public void start() {
        if( null != m_thread ) stop();
        m_thread = new Thread(this);
        m_bRun = true;
        m_thread.start();
    }

    public void stop() {
        if( null == m_thread ) return;
        m_bRun = false;
        try {
            m_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if( null != m_currScreen ) m_currScreen.cleanUp();
    }

    @Override
    public void run() {
        int retCode = 0;
        long startTime, endTime, toSleep;

        m_currScreen = m_screens.get(ScreenFactory.LOADING_SCREEN);
        m_currScreen.initialize();

        while( m_bRun ) {
            startTime = System.currentTimeMillis();

            m_currScreen.drawScreen(getHolder());
            if( ScreenFactory.LOADING_SCREEN != m_currScreen.getsCode()) {
                retCode = m_currScreen.update();
            } else {
                if( m_loader.m_bFinished ) retCode = ScreenFactory.TITLE_SCREEN;
            }

            if( retCode < 0 ) break;
            if( retCode > 0 ) {
                BaseScreen nextScreen = m_screens.get(retCode);
                if( null != nextScreen ) {
                    m_currScreen.cleanUp();
                    m_currScreen = nextScreen;
                    m_currScreen.initialize();
                }
            }
            endTime = System.currentTimeMillis();

            toSleep = m_frametime - (endTime - startTime);
            if( toSleep > 0 ) {
                try {
                    Thread.sleep(toSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        m_thread = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        saveWidthHeight(width, height);
    }

    private void saveWidthHeight(int width, int height) {
        MightyApplication app = MightyApplication.getInstance();
        app.setWidth(width);
        app.setHeight(height);
        app.calcScaleFactors();

        GameObject.setBaseStrideX(app.getLeft());
        GameObject.setBaseStrideY(app.getTop());
        GameObject.setBaseScaleX(app.getScaleX());
        GameObject.setBaseScaleY(app.getScaleY());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        saveWidthHeight(canvas.getWidth(), canvas.getHeight());
        holder.unlockCanvasAndPost(canvas);
        start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        m_currScreen.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_currScreen.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
