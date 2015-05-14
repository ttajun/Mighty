package com.ttajun.mighty.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.ttajun.mighty.Drawables;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.R;
import com.ttajun.mighty.SoundManager;
import com.ttajun.mighty.gameobject.Card;
import com.ttajun.mighty.gameobject.Cards;
import com.ttajun.mighty.gameobject.GameObject;
import com.ttajun.mighty.ground.BaseGround;
import com.ttajun.mighty.ground.GroundCardTable;
import com.ttajun.mighty.manager.DataManager;
import com.ttajun.mighty.manager.GameManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class GameScreen extends BaseScreen {
    int BGM;
    BaseGround ground;
    MightyApplication app;
    GameManager gameManager;

    public GameScreen() {

    }

    public void loadResources() {
        /*
        SoundManager sm = SoundManager.getInstance();
        Context context = MightyApplication.getInstance().getApplicationContext();

        for(int i = R.drawable.boom; i< R.drawable.power; i++)
        {
            sm.addPool(context, i);
        }
        sm.waitForLoading();
        */
    }

    @Override
    public void initialize() {
        ground = new GroundCardTable();
        app = MightyApplication.getInstance();
        gameManager = GameManager.getInstance();

        gameManager.initialize();
    }

    @Override
    public void cleanUp() {
        SoundManager sm = SoundManager.getInstance();
        sm.stop(BGM);
    }

    @Override
    public int update() {
        gameManager.update();
        return 0;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.clipRect(app.getLeft(), app.getTop(), app.getRight(), app.getBottom());

        ground.draw(canvas);
        gameManager.draw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("onTouchEvent", "X = " + event.getX() + "  Y = " + event.getY());
        switch( event.getAction() )
        {
            case MotionEvent.ACTION_DOWN:
                gameManager.checkObject((int)event.getX(), (int)event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                gameManager.moveObject((int)event.getX(), (int)event.getY());
                break;

            case MotionEvent.ACTION_UP:
                gameManager.releaseObject();
                break;
        }
        return true;
    }

}