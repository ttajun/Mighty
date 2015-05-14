package com.ttajun.mighty.gameobject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.manager.DataManager;
import com.ttajun.mighty.screen.GameScreen;

/**
 * Created by ttajun on 2015-05-02.
 */
public class Game {
    private static final String TAG = Game.class.getSimpleName();
    static Game instance = null;
    Context context;
    TurnMarker turnMarker;

    int numGamer;
    Gamer[] gamers;
    GameState currentState;
    int gameTurn;
    int gameMaster;
    String currentBid = "NT5";
    int masterFriend;

    int trickRound = 0;
    int trickOrder = 0;
    String trickSuit = "";
    String trickBidSuit = "";
    int pictureNum = 0;
    Card curTrickBoss = null;
    String trickResult = "";

    private Game() {
        context = MightyApplication.getInstance().getApplicationContext();
        gameTurn = 0;
        numGamer = DataManager.PLAYER_NUM;
        gamers = new Gamer[numGamer];
        currentState = GameState.BeforeStart;
        turnMarker = TurnMarker.getInstance();
    }

    public static Game getInstance() {
        if(instance == null) instance = new Game();
        return instance;
    }

    public void gameInitialize() {
        gameTurn = 0;
        gameMaster = -1;
        currentBid = "NT5";
        masterFriend = -1;
        currentState = GameState.BeforeStart;
        turnMarker.moveMarker(gameTurn);
        trickRound = 0;
        trickOrder = 0;
        trickSuit = "";
        trickBidSuit = "";
        pictureNum = 0;
        curTrickBoss = null;
        trickResult = "";
    }

    public void setTrickInit() {
        trickOrder = 0;
        trickSuit = "";
        curTrickBoss = null;
    }

    public void setTrickResult(String result) { trickResult = result; }
    public String getTrickResult() { return trickResult; }

    public void setCurTrickBoss(Card card) { curTrickBoss = card; }
    public Card getCurTrickBoss() { return curTrickBoss; }

    public void increasePicture(int count) { pictureNum += count; }
    public int getPictureNum() { return pictureNum; }
    public boolean isMasterSide(int gamer) { return (gamer == gameMaster || gamer == masterFriend); }

    public String getBidTrickSuit() { return trickBidSuit; }
    public void setTrickBidSuit(String suit) { trickBidSuit = suit; }

    public String getTrickSuit() { return trickSuit; }
    public void setTrickSuit(String suit) { trickSuit = suit; }

    public int getTrickOrder() { return trickOrder; }
    public void setTrickOrder(int order) { trickOrder = order; }

    public int getTrickRound() { return trickRound; }
    public void setTrickRound(int round) { trickRound = round; }

    public int getNumGamer() { return numGamer; }

    public Gamer getGamer(int index) { return gamers[index]; }
    public void setGamer(int index, Gamer gamer) { gamers[index] = gamer; }

    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState newState) { currentState = newState; }

    public int getGameTurn() { return gameTurn; }
    public void setGameTurn(int gameTurn) { this.gameTurn = gameTurn; turnMarker.moveMarker(gameTurn); }
    public void increaseGameTurn() {
        if(gameTurn >= numGamer -1) gameTurn = 0;
        else gameTurn++;
        turnMarker.moveMarker(gameTurn);
    }

    public int getGameMaster() { return gameMaster; }
    public void setGameMaster(int gameMaster) { this.gameMaster = gameMaster; }

    public String getCurrentBid() { return currentBid; }
    public void setCurrentBid(String bid) { currentBid = bid; }

    public int getMasterFriend() { return masterFriend; }
    public void setMasterFriend(int friend) { masterFriend = friend; }

    public void popupToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void popupMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
