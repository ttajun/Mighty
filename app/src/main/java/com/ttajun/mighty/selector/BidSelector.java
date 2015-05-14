package com.ttajun.mighty.selector;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.ttajun.mighty.R;
import com.ttajun.mighty.gameobject.Game;
import com.ttajun.mighty.gameobject.Gamer;
import com.ttajun.mighty.manager.ProceedManager;

/**
 * Created by ttajun on 2015-05-10.
 */
public class BidSelector extends Dialog implements AdapterView.OnItemSelectedListener {
    private static final String TAG = BidSelector.class.getSimpleName();
    Context context;
    ButtonRectangle btnPass, btnBid;
    Spinner spinSuit, spinValue;

    String suit, value, curBid;
    boolean bPass = false;
    TextView txtGlobal, txtYour;

    public BidSelector(Context context) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                onBidChange();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_selector);

        //txtGlobal = (TextView) findViewById(R.id.textGlobal);
        //curBid = Game.getInstance().getCurrentBid();
        //txtGlobal.setText(curBid);
        //Log.d(TAG, "txtGlobal:" + curBid);
        txtYour = (TextView) findViewById(R.id.textYour);

        btnPass = (ButtonRectangle) findViewById(R.id.buttonPass);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bPass = true;
                cancel();
                //Toast.makeText(context, "Click pass button", Toast.LENGTH_LONG).show();
            }
        });
        btnBid = (ButtonRectangle) findViewById(R.id.buttonBid);
        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String tmpValue = curBid.substring(curBid.length()-1, curBid.length());
                //Log.d(TAG, "value=" + value + " tmpValue=" + tmpValue);
                //if(value.compareTo(tmpValue) > 0) cancel();
                //else Toast.makeText(context, "CurBid value is" + tmpValue + ".", Toast.LENGTH_SHORT).show();
                cancel();
                //Toast.makeText(context, "Click bid button", Toast.LENGTH_LONG).show();
            }
        });

        spinSuit = (Spinner) findViewById(R.id.spinSuit);
        ArrayAdapter<CharSequence> suit_adapter = ArrayAdapter.createFromResource(context,
                R.array.card_suit_set, android.R.layout.simple_spinner_item);
        suit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSuit.setAdapter(suit_adapter);
        spinSuit.setOnItemSelectedListener(this);

        spinValue = (Spinner) findViewById(R.id.spinValue);
        ArrayAdapter<CharSequence> value_adapter = ArrayAdapter.createFromResource(context,
                R.array.card_bid_value_set, android.R.layout.simple_spinner_item);
        value_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinValue.setAdapter(value_adapter);
        spinValue.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner)parent;
        switch(spinner.getId()) {
            case R.id.spinSuit:
                suit = spinner.getItemAtPosition(position).toString();
                break;
            case R.id.spinValue:
                value = spinner.getItemAtPosition(position).toString();
                break;
        }
        txtYour.setText(suit+value);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onBidChange() {
        Game game = Game.getInstance();
        Gamer userGamer = game.getGamer(0);
        String my_bid;
        if(bPass) my_bid = "PASS";
        else my_bid = suit + value;
        Log.d(TAG, my_bid);
        Toast.makeText(context, my_bid, Toast.LENGTH_SHORT).show();
        userGamer.setMyBid(my_bid);
        ProceedManager.getInstance().setUserBid(my_bid);
        bPass = false;
        suit = "";
        value = "";
    }
}
