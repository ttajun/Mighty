package com.ttajun.mighty.pattern;

import com.ttajun.mighty.gameobject.GameObject;
import com.ttajun.mighty.gameobject.Movable;

/**
 * Created by ttajun on 2015-04-24.
 */
public interface Pattern {
    void update(GameObject target, Movable movable);
}
