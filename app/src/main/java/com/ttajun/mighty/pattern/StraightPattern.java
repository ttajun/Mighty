package com.ttajun.mighty.pattern;

import com.ttajun.mighty.gameobject.GameObject;
import com.ttajun.mighty.gameobject.Movable;

/**
 * Created by ttajun on 2015-04-24.
 */
public class StraightPattern implements Pattern {

    @Override
    public void update(GameObject target, Movable movable) {
        movable.setY((int)(movable.getY() + movable.getSpeedY()));
    }
}
