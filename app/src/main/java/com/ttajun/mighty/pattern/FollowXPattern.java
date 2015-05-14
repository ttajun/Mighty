package com.ttajun.mighty.pattern;

import com.ttajun.mighty.gameobject.GameObject;
import com.ttajun.mighty.gameobject.Movable;

/**
 * Created by ttajun on 2015-04-24.
 */
public class FollowXPattern implements Pattern {
    static int DIFFX_MIN = 5;

    @Override
    public void update(GameObject target, Movable movable) {
        int x = movable.getX();
        int y = movable.getY();

        float speedX = movable.getSpeedX();
        float speedY = movable.getSpeedY();

        y += speedY;

        int dx = target.getX() - x;
        if(dx > DIFFX_MIN) x += speedX;
        else if(dx < -DIFFX_MIN) x -= speedX;

        movable.setX(x);
        movable.setY(y);
    }
}
