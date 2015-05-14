package com.ttajun.mighty.gameobject;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.ttajun.mighty.Drawables;

/**
 * Created by ttajun on 2015-04-22.
 */
public class GameObject {
    protected int key;
    protected int type;
    protected int x, y;
    protected int w, h;
    protected boolean bRemove;
    protected int touchDX, touchDY;

    protected int moveX, moveY;
    protected boolean bMoving;
    public boolean movingObject(int key) { return bMoving; }

    static int baseStrideX, baseStrideY;
    static float baseScaleX, baseScaleY;

    float scaleX, scaleY;

    protected int z;

    public GameObject(int key, int type) {
        this.key = key;
        this.type = type;
        scaleX = scaleY = 1.0f;
        bRemove = false;
    }

    public void init() {

    }

    public void setSizeByResource(int resource) {
        Drawables ds = Drawables.getInstance();
        Drawable d = ds.get(resource);
        w = d.getIntrinsicWidth();
        h = d.getIntrinsicHeight();
    }

    public void beforeDraw(Canvas canvas) {
        canvas.save();
        //canvas.translate(baseStrideX, baseStrideY);
        //canvas.scale(baseScaleX * scaleX, baseScaleY * scaleY);
        canvas.translate(x, y);
    }

    public void afterDraw(Canvas canvas) {
        canvas.restore();
    }

    public void drawMain(Canvas canvas) {

    }

    public void draw(Canvas canvas) {
        beforeDraw(canvas);
        drawMain(canvas);
        afterDraw(canvas);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
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

    public static int getBaseStrideX() {
        return baseStrideX;
    }

    public static void setBaseStrideX(int baseScaleX) {
        GameObject.baseScaleX = baseScaleX;
    }

    public static int getBaseStrideY() {
        return baseStrideY;
    }

    public static void setBaseStrideY(int baseScaleY) {
        GameObject.baseScaleY = baseScaleY;
    }

    public static float getBaseScaleX() {
        return baseScaleX;
    }

    public static void setBaseScaleX(float baseScaleX) {
        GameObject.baseScaleX = baseScaleX;
    }

    public static float getBaseScaleY() {
        return baseScaleY;
    }

    public static void setBaseScaleY(float baseScaleY) {
        GameObject.baseScaleY = baseScaleY;
    }

    public boolean getRemove() {
        return bRemove;
    }

    public void setRemove(boolean bRemove) {
        this.bRemove = bRemove;
    }

    public void update() {

    }

    public void move() {

    }

    public void setKey(int key) {
        this.key = key;
    }

    public Rect getRect() {
        return new Rect(x, y, x + w - 1, y + h - 1);
    }

    public int getTouchDX() { return touchDX; }
    public void setTouchDX(int dx) { this.touchDX = dx; }

    public int getTouchDY() { return touchDY; }
    public void setTouchDY(int dy) { this.touchDY = dy; };

}
