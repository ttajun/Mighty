package com.ttajun.mighty.gameobject;

import android.graphics.Canvas;
import android.util.Log;

import com.ttajun.mighty.manager.HashMapSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ttajun on 2015-04-24.
 */
public class Cards {
    static Cards instance = null;
    static HashMap<Integer, Card> card_set;
    private static final String TAG = Cards.class.getSimpleName();

    int currKey;
    int zOrder;

    private Cards() {
        card_set = new HashMap<Integer, Card>();
        currKey = 0;
        zOrder = 0;
    }

    public static Cards getInstance() {
        if(null == instance) instance = new Cards();
        return instance;
    }

    public HashMap<Integer, Card> getCards() {
        return card_set;
    }

    public void put(Card v) {
        synchronized (this) {
            v.setKey(currKey++);
            card_set.put(v.getKey(), v);
        }
    }

    public Card get(Integer k) {
        Card card = null;
        synchronized (this) {
            card = card_set.get(k);
        }
        return card;
    }

    public void remove(Integer k) {
        synchronized (this) {
            card_set.remove(k);
        }
    }

    public Card getCard(String suit, String rank) {
        Card card = null;
        Iterator<Card> i = card_set.values().iterator();

        if(suit.equals("") || rank.equals("")) {
            Log.d(TAG, "Invalid Cards.getCard() parameter.");
            return card;
        }

        Log.d(TAG, "getCard() suit:" + suit + " rank:" + rank);
        while (i.hasNext()) {
            card = i.next();
            Log.d(TAG, "getCard() card suit:" + card.getSuit() + " rank:" + card.getRank());
            if(card.getSuit().equals(suit) && card.getRank().equals(rank)) break;
        }
        return card;
    }

    public void update() {
        Iterator<Card> r = card_set.values().iterator();
        Card card;
        ArrayList<Integer> arrRemove = new ArrayList<Integer>();

        while(r.hasNext()) {
            card = (Card)r.next();
            card.update();
            if(card.getRemove()) arrRemove.add(card.getKey());
        }

        for(int i=0; i<arrRemove.size(); i++)
            card_set.remove(arrRemove.get(i));

    }
    //int tmplog = 0;
    public void draw(Canvas canvas) {
        Log.d("Cards", "Cards draw called!!!");
        HashMapSort mapSort = HashMapSort.getInstance();
        //mapSort.sortByZ(card_set);
        mapSort.sortByKey(card_set);
        Iterator<Card> i = card_set.values().iterator();

        Card card;
        while(i.hasNext()) {
            card = (Card)i.next();
            card.draw(canvas);
            //card.setZ(zOrder++);

            //if(tmplog == 0) {
            //    Log.d("Cards draw >>>>>" , "key =" + card.key);
           // }
        }
        zOrder = 0;
       // tmplog = 1;

    }

    public void reset() {
        card_set.clear();
    }
}
