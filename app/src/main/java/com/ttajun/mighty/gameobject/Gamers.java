package com.ttajun.mighty.gameobject;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ttajun on 2015-04-25.
 */
public class Gamers {
    static Gamers instance = null;
    static HashMap<Integer, Gamer> m_gamers;

    int currKey;

    private Gamers() {
        m_gamers = new HashMap<Integer, Gamer>();
        currKey = 0;
    }

    public static Gamers getInstance() {
        if(instance == null) instance = new Gamers();
        return instance;
    }

    public HashMap<Integer, Gamer> getGamers() {
        return m_gamers;
    }

    public void put(Gamer v) {
        synchronized (this) {
            v.setKey(currKey++);
            m_gamers.put(v.getKey(), v);
        }
    }

    public Gamer get(Integer k) {
        Gamer gamer = null;
        synchronized (this) {
            gamer = m_gamers.get(k);
        }
        return gamer;
    }

    public void remove(Integer k) {
        synchronized (this) {
            m_gamers.remove(k);
        }
    }

    public void update() {
        Iterator<Gamer> r = m_gamers.values().iterator();
        Gamer gamer;
        ArrayList<Integer> arrRemove = new ArrayList<Integer>();

        while(r.hasNext()) {
            gamer = (Gamer)r.next();
            gamer.update();
            if(gamer.getRemove()) arrRemove.add(gamer.getKey());
        }

        for(int i=0; i<arrRemove.size(); i++) {
            m_gamers.remove(arrRemove.get(i));
        }
    }

    public void draw(Canvas canvas) {
        Iterator<Gamer> i = m_gamers.values().iterator();
        Gamer gamer;
        while(i.hasNext()) {
            gamer = i.next();
            gamer.draw(canvas);
        }
    }

    public void reset() {
        m_gamers.clear();
    }
}
