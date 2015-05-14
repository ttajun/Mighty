package com.ttajun.mighty.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.R;
import com.ttajun.mighty.SoundManager;
import com.ttajun.mighty.ground.BaseGround;
import com.ttajun.mighty.ground.GroundMetalBase00;

/**
 * Created by ttajun on 2015-04-22.
 */
public class TitleScreen extends BaseScreen {
    MightyApplication app;
    int BGM;
    BaseGround ground;
    float scale;

    //title
    Paint pTitle;
    Bitmap bmTitle;
    Canvas cvsTitle;
    Rect rectTitle;
    int titleX, titleY;
    int leftG, rightG;
    int DL, DR;

    static final String txtTitle = "Mighty Game";

    //message
    Paint pMessage;
    Bitmap bmMessage;
    Canvas cvsMessage;
    Rect rectMessage;
    int messageX, messageY;

    static final String txtMessage = "Touch Screen to Start";
    static final int BLINK_FRAMES = 7;
    int nFrames;
    boolean bVisible;

    public TitleScreen() {
        sCode = ScreenFactory.TITLE_SCREEN;
    }

    @Override
    public void initialize() {
        retCode = 0;
        ground = new GroundMetalBase00();
        app = MightyApplication.getInstance();

//	Title

        rectTitle = new Rect();

        pTitle = new Paint();
        pTitle.setAntiAlias(true);
        pTitle.setStrokeWidth(10);
        pTitle.setStrokeCap(Paint.Cap.ROUND);
        pTitle.setTextSize(64);
        pTitle.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        pTitle.getTextBounds(txtTitle, 0, txtTitle.length(), rectTitle);

        bmTitle = Bitmap.createBitmap(rectTitle.width(), rectTitle.height(), Bitmap.Config.ARGB_8888);
        cvsTitle = new Canvas(bmTitle);

        titleX = ( app.BASE_WIDTH - rectTitle.width() ) >> 1;
        titleY = 100;

        leftG = 0xff;
        rightG = 0x00;

        DL = 8;
        DR = -8;

//	Message

        rectMessage = new Rect();

        pMessage = new Paint();
        pMessage.setAntiAlias(true);
        pMessage.setStrokeWidth(5);
        pMessage.setStrokeCap(Paint.Cap.ROUND);
        pMessage.setTextSize(40);
        pMessage.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        pMessage.getTextBounds(txtMessage, 0, txtMessage.length(), rectMessage);
        pMessage.setColor(Color.WHITE);

        bmMessage = Bitmap.createBitmap(rectMessage.width(), rectMessage.height(), Bitmap.Config.ARGB_8888);
        cvsMessage = new Canvas(bmMessage);

        cvsMessage.drawColor(0, PorterDuff.Mode.CLEAR);
        cvsMessage.drawText(txtMessage, 0, rectMessage.height(), pMessage);

        messageX = ( app.BASE_WIDTH - rectMessage.width() ) >> 1;
        messageY = 400;

        nFrames = BLINK_FRAMES;
        bVisible = true;

// Sound Manager

        SoundManager sm = SoundManager.getInstance();
        BGM = sm.play(R.drawable.music_menu, sm.LOOP);

    }

    @Override
    public void cleanUp() {
        SoundManager sm = SoundManager.getInstance();
        sm.stop(BGM);
    }

    @Override
    public int update() {
        ground.update();
        leftG += DL;
        if( leftG < 0x00 )
        {
            leftG = 0x00;
            DL = -DL;
        }
        else if( leftG > 0xff )
        {
            leftG = 0xff;
            DL = -DL;
        }
        rightG += DR;
        if( rightG < 0x00 )
        {
            rightG = 0x00;
            DR = -DR;
        }
        else if( rightG > 0xff )
        {
            rightG = 0xff;
            DR = -DR;
        }

        nFrames--;
        if( nFrames < 0 )
        {
            nFrames = BLINK_FRAMES;
            bVisible = !bVisible;
        }
        return retCode;
    }

    @Override
    public void draw(Canvas canvas) {
        if( 0 != retCode ) return;

        canvas.drawColor(Color.BLACK);
        canvas.clipRect(app.getLeft(), app.getTop(), app.getRight(), app.getBottom());

        ground.draw(canvas);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        beforeDraw(canvas);

        int color0 = ( leftG << 24 ) | 0x00ff0000;
        int color1 = ( rightG << 24 ) | 0x00ff0000;
        LinearGradient shader = new LinearGradient(0, 0,
                rectTitle.width(), 0,
                color0, color1, Shader.TileMode.REPEAT);

        pTitle.setShader(shader);
        cvsTitle.drawColor(0, PorterDuff.Mode.CLEAR);
        cvsTitle.drawText(txtTitle, 0, rectTitle.height(), pTitle);

        canvas.drawBitmap(bmTitle, titleX, titleY, p);
        if( bVisible ) canvas.drawBitmap(bmMessage, messageX, messageY, p);

        afterDraw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch( event.getAction() )
        {
            case MotionEvent.ACTION_DOWN:
                retCode = ScreenFactory.GAME_SCREEN;
                break;
        }
        return true;
    }
}
