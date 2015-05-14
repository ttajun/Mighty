package com.ttajun.mighty;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

/**
 * Created by ttajun on 2015-04-22.
 */
public class Drawables {
    HashMap<Integer, Drawable> m_drawables;
    static Drawables instance = null;
    private Context context = null;

    Drawables() {
        m_drawables = new HashMap<Integer, Drawable>();
        context = MightyApplication.getInstance().getApplicationContext();
    }

    public static Drawables getInstance() {
        if( null == instance ) instance = new Drawables();
        return instance;
    }

    public void put(int k, Drawable d) {
        m_drawables.put(k, d);
    }

    public Drawable get(int k) {
        if(m_drawables.get(k) == null) {
            Drawable d = context.getResources().getDrawable(k);
            put(k, d);
        }
        return m_drawables.get(k);
    }

    public void reset() {
        m_drawables.clear();
    }
}
