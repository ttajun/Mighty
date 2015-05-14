package com.ttajun.mighty.gameobject;

/**
 * Created by ttajun on 2015-04-24.
 */
public class Movable extends GameObject {
    float speedX, speedY;

    public Movable(int key, int type) {
        super(key, type);
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }
}
