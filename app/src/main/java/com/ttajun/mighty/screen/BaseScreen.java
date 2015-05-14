package com.ttajun.mighty.screen;

import com.ttajun.mighty.gameobject.GameObject;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by ttajun on 2015-04-22.
 */
public class BaseScreen implements Screen {
    protected Resources m_resources;
    protected int retCode;
    protected int sCode;

    public BaseScreen() {

    }

    public void setResources(Resources resources) {
        m_resources = resources;
    }

    public final void drawScreen(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        synchronized (holder) {
            draw(canvas);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void initialize() {
        retCode = 0;
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public int getsCode() {
        return sCode;
    }

    public void beforeDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(GameObject.getBaseStrideX(), GameObject.getBaseStrideY());
        canvas.scale(GameObject.getBaseScaleX(), GameObject.getBaseScaleY());
    }

    public void afterDraw(Canvas canvas) {
        canvas.restore();
    }
}
