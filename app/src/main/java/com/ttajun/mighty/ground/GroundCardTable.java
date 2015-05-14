package com.ttajun.mighty.ground;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ttajun.mighty.Drawables;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.R;
import com.ttajun.mighty.screen.BaseScreen;

/**
 * Created by ttajun on 2015-04-25.
 */
public class GroundCardTable extends BaseGround {
    MightyApplication app;
    Drawable d;
    int startX, startY, nDrawX, nDrawY;
    float scale;
    private static final int DY = 5;

    public GroundCardTable()
    {
        Drawables ds = Drawables.getInstance();
        //d = ds.get(R.drawable.boardimages);
        d = ds.get(R.drawable.felt_small);
        w = d.getIntrinsicWidth();
        h = d.getIntrinsicHeight();

        app = MightyApplication.getInstance();
        scale = 1.0f;

        nDrawX = (app.getWidth() / w) + 1;
        nDrawY = (app.getHeight() / h) + 1;

        startX = 0;
        startY = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAlpha(128);

        //int x = startX;
        //int y = startY;

       // w = d.getIntrinsicWidth();
        //h = d.getIntrinsicHeight();
        //Rect src = new Rect(0, 0, w, h);
        //Rect dst = new Rect(0, 0, app.getWidth(), app.getHeight());
        //canvas.drawBitmap(image, src, dst, null);

        //canvas.save();
        //canvas.translate(app.getLeft() + x , app.getTop() + y);
        //canvas.scale(scale, scale);
        //canvas.drawBitmap(((BitmapDrawable) d).getBitmap(), src, dst, p);
        //canvas.drawBitmap(((BitmapDrawable) d).getBitmap(), 0, 0, p);
        //canvas.restore();


        int x = startX;
        int y = startY;

        for(int i=0; i<nDrawY; i++)
        {
            for(int j=0; j<nDrawX; j++) {
                canvas.save();
                canvas.translate(app.getLeft() + x , app.getTop() + y);
                canvas.scale(scale, scale);
                canvas.drawBitmap(((BitmapDrawable) d).getBitmap(), 0, 0, p);
                canvas.restore();
                x += w;
            }
            x = 0;
            y += h;
        }
    }

    public void update()
    {
    }
}
