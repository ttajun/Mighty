package com.ttajun.mighty.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.ttajun.mighty.Drawables;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.manager.DataManager;

/**
 * Created by ttajun on 2015-05-11.
 */
public class TurnMarker extends GameObject {
    static TurnMarker instance = null;
    int resourceId;
    int gameTurn = 0;
    Drawables ds;

    private TurnMarker(int key, int type, int resourceID) {
        super(key, type);
        this.resourceId = resourceID;
        ds = Drawables.getInstance();
        setSizeByResource(resourceId);
        x=220;
        y=350;
    }

    public static TurnMarker getInstance() {
        Context context = MightyApplication.getInstance().getApplicationContext();
        String packageName = context.getPackageName();
        int rid = context.getResources().getIdentifier("use_item00", "drawable", packageName);

        if(instance == null) instance = new TurnMarker(DataManager.TURN_MARKER,
                DataManager.TYPE_OBJECT, rid);
        return instance;
    }

    public void moveMarker(int turn) {
        gameTurn = turn;
        switch (turn) {
            case 0: x=220; y=350; break;
            case 1: x=80; y=150; break;
            case 2: x=120; y=30; break;
            case 3: x=440; y=30; break;
            case 4: x=540; y=150; break;
        }
    }

    @Override
    public void drawMain(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(((BitmapDrawable) ds.get(resourceId)).getBitmap(), 0, 0, p);
    }
}
