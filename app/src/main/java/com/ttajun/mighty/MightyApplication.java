package com.ttajun.mighty;

import android.app.Application;

/**
 * Created by ttajun on 2015-04-22.
 */
public class MightyApplication extends Application {
    static MightyApplication instance = null;
    int width, height;
    float scaleX, scaleY;
    int top, left;
    int bottom, right;
    int viewWidth, viewHeight;

    // ttajun
    public static final int BASE_WIDTH = 800;
    public static final int BASE_HEIGHT = 600;
    static final int FRAME_TIME = 33;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) { this.width = width; }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void calcScaleFactors() {
        viewWidth = width;
        viewHeight = height;

        float baseRatio = (float)BASE_WIDTH / BASE_HEIGHT;
        float currRatio = (float)width / height;

        left = 0;
        top = 0;
        right = width;
        bottom = height;

        if(currRatio > baseRatio) {
            viewWidth = (int)(viewHeight * baseRatio);
            left = (width - viewWidth) >> 1;
            right = left + viewWidth;
        } else if(currRatio < baseRatio) {
            viewHeight = (int)(viewWidth / baseRatio);
            top = (height - viewHeight) >> 1;
            bottom = top + viewHeight;
        }

        scaleX = (float)viewWidth / BASE_WIDTH;
        scaleY = (float)viewHeight / BASE_HEIGHT;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public static MightyApplication getInstance() {
        return instance;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public int getBaseWidth() {
        return BASE_WIDTH;
    }

    public int getBaseHeight() {
        return BASE_HEIGHT;
    }

    public int getViewWidth() { return viewWidth; }
    public int getViewHeight() { return viewHeight; }
}
