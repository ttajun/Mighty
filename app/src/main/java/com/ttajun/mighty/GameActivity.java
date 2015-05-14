package com.ttajun.mighty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloatSmall;

import com.ttajun.mighty.gameobject.Game;
import com.ttajun.mighty.manager.GameManager;
import com.ttajun.mighty.manager.ProceedManager;
import com.ttajun.mighty.selector.BidSelector;
import com.ttajun.mighty.selector.FriendSelector;

import java.util.Calendar;

/**
 * Created by ttajun on 2015-04-22.
 */
public class GameActivity extends Activity {
    GameView m_gameview;
    public static Context m_context;
    public static MightyHandler m_handler;
    private static final String TAG=GameActivity.class.getSimpleName();
    public static final int RESULT_DIALOG_OPEN = 0;
    ButtonFloatSmall btnBid, btnDrop, btnFriend, btnReset;

    private AlertDialog mDialog = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_context = this;
        m_handler = new MightyHandler();
        m_gameview = (GameView)findViewById(R.id.MightySurface);

        btnBid = (ButtonFloatSmall) findViewById(R.id.buttonBid);
        btnDrop = (ButtonFloatSmall) findViewById(R.id.buttonDrop);
        btnFriend = (ButtonFloatSmall) findViewById(R.id.buttonFriend);
        btnReset = (ButtonFloatSmall) findViewById(R.id.buttonReset);

        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidSelector bidSelector = new BidSelector(GameActivity.this);
                bidSelector.show();
            }
        });

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameManager gameManager = GameManager.getInstance();
                gameManager.checkGameZone();
            }
        });

        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendSelector friendSelector = new FriendSelector(GameActivity.this);
                friendSelector.show();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProceedManager.getInstance().gameReStart();
            }
        });

        //가로고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public MightyHandler getM_handler() { return m_handler; }
    public int getM_Result_open() { return RESULT_DIALOG_OPEN; }

    class MightyHandler extends Handler {

        protected MightyHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case RESULT_DIALOG_OPEN:
                    mDialog = createResultDialog();
                    mDialog.show();
                    break;
                default:
                    break;
            }
        }
    }

    private AlertDialog createResultDialog() {
        String result = Game.getInstance().getTrickResult();
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("RESULT");
        ab.setMessage(result);
        ab.setCancelable(false);
        ab.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        ab.setPositiveButton("new Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ProceedManager.getInstance().gameReStart();
                setDismiss(mDialog);
            }
        });

        ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setDismiss(mDialog);
            }
        });

        return ab.create();
    }

    private void setDismiss(Dialog dialog){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
            m_gameview.onKeyDown(keyCode, event);
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_gameview.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStop() {
        cleanUp();
        super.onStop();
    }

    private static final int MSG_TIMER_EXPIRED = 1;
    private static final int BACKEY_TIMEOUT = 2000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrentTimeInMillis = 0;

    @Override
    public void onBackPressed() {
        if(!mIsBackKeyPressed){
            mIsBackKeyPressed = true;
            mCurrentTimeInMillis = Calendar.getInstance().getTimeInMillis();
            Toast.makeText(this, "Pressing the back button will exit once more.", Toast.LENGTH_SHORT).show();
            startTimer();
        } else {
            mIsBackKeyPressed = false;
            if(Calendar.getInstance().getTimeInMillis() <= (mCurrentTimeInMillis + (BACKEY_TIMEOUT))){
                finish();
            }
        }
    }

    private void startTimer(){
        mTimerHander.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKEY_TIMEOUT);
    }

    private Handler mTimerHander = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case MSG_TIMER_EXPIRED:
                    mIsBackKeyPressed = false;
                    break;
            }
        }
    };

    private void cleanUp() {
        SoundManager.getInstance().release();
    }
}
