package com.ttajun.mighty.gameobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.style.ScaleXSpan;
import android.util.Log;

import com.ttajun.mighty.Drawables;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.R;
import com.ttajun.mighty.manager.DataManager;
import com.ttajun.mighty.manager.GameManager;
import com.ttajun.mighty.manager.ObjectManager;

import java.util.Comparator;

/**
 * Created by ttajun on 2015-04-24.
 */
public class Card extends GameObject {
    float speedX, speedY;
    int resourceId;
    int faceUpRId, faceDownRId;
    boolean bFaceDown = true;
    int movingX, movingY;
    boolean bCardMoving = false;
    boolean bMoveLock = true;
    Drawables ds;

    private String rank, suit;
    private int value;

    String[] suits={"Spade", "Diamond", "Heart", "Club"};
    String[] ranks={"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    int[] values={14, 13, 12, 11, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    public Card(int key, int type, int resourceId) {
        super(key, type);
        this.resourceId = resourceId;
        this.faceUpRId = resourceId;
        ds = Drawables.getInstance();
        setSizeByResource(resourceId);

        if(key == 0) {
            suit = "JOKER";
            rank = "J";
            value = 15;
        } else {
            suit = suits[(key-1)/13];
            rank = ranks[(key-1)%13];
            value = values[(key-1)%13];
        }
        //int w = ds.get(resourceId).getIntrinsicWidth();
        //int h = ds.get(resourceId).getIntrinsicHeight();
        //Log.d("Card Create", " key = " + key + " type = " + type + " resourceid = " + resourceId);
        //Log.d("Card Create", " x = " + x + " y = " + y);
        //Log.d("Card Create", " w = " + w + " h = " + h);

        //Log.d("Card Create", " baseStrideX = " + baseStrideX + " baseStrideY = " + baseStrideY);
        //Log.d("Card Create", " baseScaleX = " + baseScaleX + " baseScaleY = " + baseScaleY);
        //Log.d("Card Create", " ScaleX =" + scaleX + " scaleY = " + scaleY);
    }

    public void setFaceDown(boolean faceDown) { bFaceDown = faceDown; }
    public void setMoveLock(boolean moveLock) { bMoveLock = moveLock; }
    public boolean getMoveLock() { return bMoveLock; }
    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getValueFromRank(String inRank) {

        int the_value = 0;

        for (int i=0; i < 13; i++) {
            if (ranks[i].matches(inRank)) {
                the_value = values[i];
            }
        }

        return the_value;
    }

    public void setFaceDownRId(int faceDownRId) {
        this.faceDownRId = faceDownRId;
    }

    public void setMoving(int x, int y) {
        movingX = x;
        movingY = y;
        bCardMoving = true;
    }

    @Override
    public void update() {
       // x += speedX;
       // y += speedY;

        if(x + w < 0 || x > MightyApplication.BASE_WIDTH
                || y + h < 0 || y > MightyApplication.BASE_HEIGHT)
            bRemove = true;
    }

    @Override
    public void drawMain(Canvas canvas) {
        //Log.d("Card Instance", "Card draw called!!!");
        int offsetX = 20;
        int offsetY = 20;

        if(bCardMoving) {
            if(x == movingX && y == movingY) {
                bCardMoving = false;
                return;
            }
            if(Math.abs(x-movingX) < offsetX) offsetX = 1;
            if(Math.abs(y-movingY) < offsetY) offsetY = 1;
            if(x > movingX) x -= offsetX;
            else if(x < movingX) x += offsetX;

            if(y > movingY) y -= offsetY;
            else if(y < movingY) y += offsetY;
        }

        if(bFaceDown) { resourceId = faceDownRId; }
        else { resourceId = faceUpRId; }

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(((BitmapDrawable) ds.get(resourceId)).getBitmap(), 0, 0, p);
        /*
        int z = gameManager.getMax_z_order();
        z++;
        setZ(z);
        gameManager.setMax_z_order(z);
        */
        //ds.get(resourceId).draw(canvas);
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

    /*
    @Override
    public int compareTo(Object o) {
        return (z > (Card)o.getZ() ? 1 : 0);
    }


    1.     public int compareTo(Object o){
        2.         return title.compareTo(((Song)o).title );
        3.     }
    */


}
