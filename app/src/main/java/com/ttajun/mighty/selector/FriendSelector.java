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
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.ttajun.mighty.R;
import com.ttajun.mighty.gameobject.Game;
import com.ttajun.mighty.gameobject.Gamer;
import com.ttajun.mighty.manager.ProceedManager;

/**
 * Created by ttajun on 2015-05-10.
 */
public class FriendSelector extends Dialog implements AdapterView.OnItemSelectedListener {
    Context context;
    ButtonRectangle btnFriend;
    Spinner spinSuit, spinValue;
    String suit, value;
    private static final String TAG = FriendSelector.class.getSimpleName();

    public FriendSelector(Context context) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onFriendChange();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_selector);

        btnFriend = (ButtonRectangle) findViewById(R.id.buttonFriend);
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        spinSuit = (Spinner) findViewById(R.id.spinFriendSuit);
        ArrayAdapter<CharSequence> suit_adapter = ArrayAdapter.createFromResource(context,
                R.array.card_suit_set, android.R.layout.simple_spinner_item);
        suit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSuit.setAdapter(suit_adapter);
        spinSuit.setOnItemSelectedListener(this);

        spinValue = (Spinner) findViewById(R.id.spinFriendValue);
        ArrayAdapter<CharSequence> value_adapter = ArrayAdapter.createFromResource(context,
                R.array.card_value_set, android.R.layout.simple_spinner_item);
        value_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinValue.setAdapter(value_adapter);
        spinValue.setOnItemSelectedListener(this);
    }

    public void onFriendChange() {
        Game game = Game.getInstance();
        ProceedManager proceedManager = ProceedManager.getInstance();
        Gamer userGamer = game.getGamer(0);
        String myFriend = suit + value;
        Log.d(TAG, myFriend);
        Toast.makeText(context, myFriend, Toast.LENGTH_SHORT).show();
        userGamer.setMyFriend(myFriend);
        proceedManager.setUserFriend(myFriend);
        proceedManager.setUserFriendCheck(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner)parent;
        switch(spinner.getId()) {
            case R.id.spinFriendSuit:
                suit = spinner.getItemAtPosition(position).toString();
                break;
            case R.id.spinFriendValue:
                value = spinner.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
