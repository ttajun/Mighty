package com.ttajun.mighty.screen;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by ttajun on 2015-04-22.
 */
public interface Screen {
    public void setResources(Resources resources);
    public void initialize();
    public void cleanUp();
    public int update();
    public void draw(Canvas canvas);
    public boolean onKeyDown(int keyCode, KeyEvent event);
    public boolean onTouchEvent(MotionEvent event);
}
