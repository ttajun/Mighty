package com.ttajun.mighty.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.ttajun.mighty.MightyApplication;

/**
 * Created by ttajun on 2015-04-22.
 */
public class LoadingScreen extends BaseScreen {
    private static final int BORDER_X = 20;
    private static final int BORDER_Y = 20;
    private static final int OVAL_WIDTH = 50;
    private static final int OVAL_HEIGHT = 50;
    private static final float ARC_ANGLE = 22.5f;

    int angle;
    RectF oval;
    int cx, cy, radius;

    public LoadingScreen() {
        sCode = ScreenFactory.LOADING_SCREEN;
    }

    public void initialize() {
        retCode = 0;
        angle = 0;

        MightyApplication app = MightyApplication.getInstance();
        oval = new RectF();

        oval.right = app.getWidth() - BORDER_X;
        oval.bottom = app.getHeight() - BORDER_Y;
        oval.left = oval.right - OVAL_WIDTH;
        oval.top = oval.bottom - OVAL_HEIGHT;

        cx = (int) (oval.left + ((int)(oval.right - oval.left) >> 1));
        cy = (int) (oval.top + ((int)(oval.bottom - oval.top) >> 1));
        radius = 10;
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public int update() {
        return retCode;
    }

    @Override
    public void draw(Canvas canvas) {
        if(0 != retCode) return;
        canvas.drawColor(Color.DKGRAY);

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        float targetAngle = (int)(angle / ARC_ANGLE) * ARC_ANGLE;

        canvas.drawArc(oval, 0,targetAngle, true, p);

        p.setColor(Color.DKGRAY);
        canvas.drawCircle(cx, cy, radius, p);
        angle += 5;
        if(angle > 360) angle = 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
