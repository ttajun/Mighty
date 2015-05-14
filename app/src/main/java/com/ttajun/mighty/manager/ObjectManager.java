package com.ttajun.mighty.manager;

import com.ttajun.mighty.gameobject.Card;
import com.ttajun.mighty.gameobject.GameObject;

import java.util.HashMap;

/**
 * Created by ttajun on 2015-04-25.
 */
public class ObjectManager {
    static ObjectManager instance = null;
    static HashMap<Integer, GameObject> object_set;

    int Z_order;
    int currKey = 0;

    private ObjectManager() {
        Z_order = 0;
    }

    public static ObjectManager getInstance() {
        if(instance == null) instance = new ObjectManager();
        return instance;
    }

    public HashMap<Integer, GameObject> getObjects() {
        return object_set;
    }

    public void put(GameObject v) {
        synchronized (this) {
            v.setKey(currKey++);
            object_set.put(v.getKey(), v);
        }
    }

    public GameObject get(Integer k) {
        GameObject object = null;
        synchronized (this) {
            object = object_set.get(k);
        }
        return object;
    }

    public void remove(Integer k) {
        synchronized (this) {
            object_set.remove(k);
        }
    }

    public int getZ_order() {
        return Z_order;
    }

    public void setZ_order(int z) {
        Z_order = z;
    }
}
