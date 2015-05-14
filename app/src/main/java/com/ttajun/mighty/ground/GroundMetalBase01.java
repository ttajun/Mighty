package com.ttajun.mighty.ground;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ttajun.mighty.Drawables;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.R;

/**
 * Created by ttajun on 2015-04-22.
 */
public class GroundMetalBase01 extends BaseGround
{
    private static final int DY = 5;
    MightyApplication app;
    int startY, nDraw;
    float scale;
    Drawable d;

    public GroundMetalBase01()
    {
        Drawables ds = Drawables.getInstance();
        d = ds.get(R.drawable.gnd_metal_base00);
        w = d.getIntrinsicWidth();
        h = d.getIntrinsicHeight();

        app = MightyApplication.getInstance();
        scale = (float)( app.getWidth() >> 1 ) / w;
        nDraw = (int)((float)(app.getHeight()) / ( h * scale ) ) + 1;
        startY = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAlpha(128);

        int y = startY;
        if( y > 0 ) y -= h;
        if( 0 == y ) nDraw++;
        for(int i=0; i<nDraw; i++)
        {
            canvas.save();
            canvas.translate(app.getLeft(), app.getTop()+y*scale);
            canvas.scale(scale, scale);
            canvas.drawBitmap(((BitmapDrawable)d).getBitmap(), 0, 0, p);
            canvas.restore();

            canvas.save();
            canvas.translate(app.getLeft()+app.getWidth(), app.getTop()+y*scale);
            canvas.scale(-scale, scale);
            canvas.drawBitmap(((BitmapDrawable)d).getBitmap(), 0, 0, p);
            canvas.restore();

            y += h;
        }
    }

    public void update()
    {
        startY += DY;
        if( startY >= h ) startY = 0;
    }

}
