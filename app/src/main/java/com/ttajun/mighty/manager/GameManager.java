package com.ttajun.mighty.manager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ttajun.mighty.Drawables;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.R;
import com.ttajun.mighty.gameobject.Card;
import com.ttajun.mighty.gameobject.Cards;
import com.ttajun.mighty.gameobject.Deck;
import com.ttajun.mighty.gameobject.Game;
import com.ttajun.mighty.gameobject.GameObject;
import com.ttajun.mighty.gameobject.GameState;
import com.ttajun.mighty.gameobject.Gamer;
import com.ttajun.mighty.gameobject.Gamers;
import com.ttajun.mighty.gameobject.Hand;
import com.ttajun.mighty.gameobject.TurnMarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by ttajun on 2015-04-25.
 */
public class GameManager {
    static GameManager instance = null;
    private static final String TAG = GameManager.class.getSimpleName();

    Cards cards;
    DataManager dataManager;
    Drawables ds;
    Context context;
    MightyApplication app;
    ProceedManager proceed;
    Deck deck;
    Game game;
    TurnMarker turnMarker;

    static TreeMap<Integer, Integer> objectZ;
    private Card selectedCard = null;

    private GameManager() {
        objectZ = new TreeMap<>();
    }

    public static GameManager getInstance() {
        if(instance == null) instance = new GameManager();
        return instance;
    }

    public void initialize() {
        cards = Cards.getInstance();
        dataManager = DataManager.getInstance();
        ds = Drawables.getInstance();
        app = MightyApplication.getInstance();
        proceed = ProceedManager.getInstance();
        deck = Deck.getInstance();
        game = Game.getInstance();
        turnMarker = TurnMarker.getInstance();

        context = MightyApplication.getInstance().getApplicationContext();
        String packageName = context.getPackageName();

        //카드의 리소스 생성 및 등록
        Card tmpCard;
        int i = 0, rid = 0, faceDownId;
        int cardSetX = 0, cardSetY = 0;

        faceDownId = context.getResources().getIdentifier(dataManager.getFaceDown(),
                "drawable", packageName);
        cardSetX = 360;
        cardSetY = 180;
        for(i=0; i < dataManager.getMaxCardCount() ; i++) {

            rid = context.getResources().getIdentifier(dataManager.getFilename(i),
                    "drawable", packageName);

            tmpCard = new Card(i, dataManager.getTypeCard(), rid);

            tmpCard.setX(cardSetX);
            tmpCard.setY(cardSetY);
            tmpCard.setZ(i);
            tmpCard.setFaceDownRId(faceDownId);
            cards.put(tmpCard);
            synchronized (this) {
                objectZ.put(i, i);
            }
        }
        deck.deckInitialize();

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        metrics = context.getResources().getDisplayMetrics();
        Log.d("test", "density=" + metrics.density);
        Log.d("test", "densityDpi=" + metrics.densityDpi);
        Log.d("test", "scaledDensity=" + metrics.scaledDensity);
        Log.d("test", "widthPixels=" + metrics.widthPixels);
        Log.d("test", "heightPixels=" + metrics.heightPixels);
        Log.d("test", "xDpi=" + metrics.xdpi);
        Log.d("test", "yDpi=" + metrics.ydpi);
        Log.d("[APP INFO]", "width = " + app.getWidth() + "  height = " + app.getHeight());
        Log.d("[APP INFO]", "scaleX = " + app.getScaleX() + "  scaleY = " + app.getScaleY());
        Log.d("[APP INFO]", "top = " + app.getTop() + "  bottom = " + app.getBottom());
        Log.d("[APP INFO]", "left = " + app.getLeft() + "  right = " + app.getRight());
        */
    }

    public void update() {
        proceed.update();
    }

    public void checkObject(int x, int y) {
        HashMap<Integer, Card> map = cards.getCards();
        Iterator<Card> i = map.values().iterator();

        Rect rectCard;
        Card card, tmpCard = null;

        while (i.hasNext()) {
            card = i.next();
            rectCard = card.getRect();

            if(rectCard.contains(x, y)) {
                if(tmpCard == null) tmpCard = card;
                else {
                    if(card.getZ() > tmpCard.getZ()) tmpCard = card;
                }
            }
        }

        if(tmpCard == null) {
            //카드를 선택하지 않음
        } else {
            selectedCard = tmpCard;
            if(selectedCard.getMoveLock()) {
                selectedCard = null;
                return;
            }
            tmpCard.setTouchDX(tmpCard.getX() - x);
            tmpCard.setTouchDY(tmpCard.getY() - y);
            changeObjectZ(tmpCard.getZ(), dataManager.getTypeCard());
        }
        if(selectedCard != null) Log.d("selected card", "x = " + selectedCard.getX() + "  y = " + selectedCard.getY() + "  key = " + selectedCard.getKey());
    }

    public void changeObjectZ(int z, int type) {
        //objectZ은 오브젝트의 z값이 key 이고 key 값이 value 이다.
        //헷갈리지 말것.
        //선택된 카드는 objectZ에서 빼서 z값을 최상위로 설정 후 다시 넣는다.
        int curObjectZ, newObjectZ;
        Card card;

        synchronized (this) {
            card = cards.get(objectZ.get(z));
            curObjectZ = card.getZ();
            newObjectZ = objectZ.lastKey() + 1;
            objectZ.remove(curObjectZ);
            objectZ.put(newObjectZ, card.getKey());
        }
        card.setZ(newObjectZ);
        //Log.d("changeObjectZ", "Z order Change => " + "old z : " + curObjectZ + " new z " + newObjectZ);
        //z값 계속 증가만 하는데 리맵핑이 필요한듯
    }

    public void moveObject(int x, int y) {
        int tmpX, tmpY;

        if ( null != selectedCard ) {
            tmpX = x + selectedCard.getTouchDX();
            tmpY = y + selectedCard.getTouchDY();
            if(tmpX < app.getLeft()) tmpX = app.getLeft();
            if(tmpX > app.getRight()-selectedCard.getW())
                tmpX = app.getRight()-selectedCard.getW();
            if(tmpY < app.getTop()) tmpY = app.getTop();
            if(tmpY >  app.getBottom()-selectedCard.getH())
                tmpY =  app.getBottom()-selectedCard.getH();
            selectedCard.setX(tmpX);
            selectedCard.setY(tmpY);
            //Log.d("MOVE OBJECT", " x = " + selectedCard.getX() + "  y = " + selectedCard.getY() + "  key = " + selectedCard.getKey());
        }
    }


    public void releaseObject() {
        if(selectedCard != null) {
            selectedCard.setTouchDX(0);
            selectedCard.setTouchDY(0);
           // Log.d("Release OBJECT", " x = " + selectedCard.getX() + "  y = " + selectedCard.getY());
           // Log.d("Release OBJECT", " key = " + selectedCard.getKey());
        }
        selectedCard = null;
    }

    public void cardZInitialize() {
        TreeMap<Integer, Integer> tmp = new TreeMap<>();

        synchronized (this) {
            Iterator<Integer> i = objectZ.values().iterator();
            int key, index = 0;
            Card card;

            while (i.hasNext()) {
                key = i.next();
                card = cards.get(key);
                index++;
                card.setX(360);
                card.setY(180);
                card.setZ(index);
                tmp.put(index, key);
            }

            objectZ.clear();
            objectZ.putAll(tmp);
        }
    }

    public void checkGameZone() {
        Rect rectTable = new Rect(230, 150, 530, 300);
        Rect rectDrop = new Rect(600, 340, 650, 390);
        List<Card> dropCards = new ArrayList<>();
        Card card;
        Hand hand;

        Log.d(TAG, "Check Game Zone");
        int gameTurn = game.getGameTurn();
        GameState gameState = game.getCurrentState();
        int i;

        Log.d(TAG, "Game State : " + gameState);
        if(gameTurn == 0) {
            switch (gameState) {
                case ReconstructionHand:
                    Log.d(TAG, "Game Master: " + game.getGameMaster());
                    if(game.getGameMaster() != 0) return;

                    hand = game.getGamer(0).getHand();
                    for(i=0; i < hand.getsize(); i++) {
                        card = hand.getcard(i);
                        Log.d(TAG, "Hand(" + i + ")" + " card[" + card.getSuit() + card.getRank() + "]");
                        Log.d(TAG, "Card x:" + card.getX() + " y:" + card.getY());
                        if(rectDrop.contains(card.getX(), card.getY())) {
                            Log.d(TAG,"Match card: " + " card[" + card.getSuit() + card.getRank() + "]");
                            dropCards.add(card);
                        } else {
                            Log.d(TAG,"Not Match card: " + " card[" + card.getSuit() + card.getRank() + "]");
                        }
                    }

                    if(dropCards.size() == 3) {
                        for(i=0; i<dropCards.size(); i++) {
                            card = dropCards.get(i);
                            hand.removeCard(card);
                            card.setMoveLock(true);
                            proceed.setUserReconstruct(true);
                            proceed.setUserResponse(false);
                        }
                    } else {
                        Toast.makeText(context, dropCards.size() + " cards. check plz", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MainPhase:
                    Log.d(TAG, "Game Turn: " + game.getGameTurn());
                    if(game.getGameTurn() != 0) return;

                    hand = game.getGamer(0).getHand();
                    for(i=0; i < hand.getsize(); i++) {
                        card = hand.getcard(i);
                        Log.d(TAG, "Hand(" + i + ")" + " card[" + card.getSuit() + card.getRank() + "]");
                        Log.d(TAG, "Card x:" + card.getX() + " y:" + card.getY());
                        if(rectTable.contains(card.getX(), card.getY())) {
                            Log.d(TAG,"Match card: " + " card[" + card.getSuit() + card.getRank() + "]");
                            proceed.setUserCard(card);
                        } else {
                            Log.d(TAG,"Not Match card: " + " card[" + card.getSuit() + card.getRank() + "]");
                        }
                    }
                    break;
            }
        }
    }

    public void draw(Canvas canvas) {
        synchronized (this) {
            Iterator<Integer> i = objectZ.values().iterator();
            int key;
            Card card;

            while (i.hasNext()) {
                key = i.next();
                card = cards.get(key);
                card.draw(canvas);
            }
        }

        Gamer gamer;
        for(int i=0; i < dataManager.getGamerNum(); i++) {
            gamer = game.getGamer(i);
            gamer.draw(canvas);
        }

        turnMarker.draw(canvas);
    }
}
