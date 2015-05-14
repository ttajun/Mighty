package com.ttajun.mighty.gameobject;

import android.util.Log;

import com.ttajun.mighty.manager.HashMapSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by ttajun on 2015-04-29.
 */
public class Deck {
    private static final String TAG = Deck.class.getSimpleName();
    static Deck instance = null;
    private List<Card> deckofcards = new ArrayList<>();

    private Deck() {
    }

    public static Deck getInstance() {
        if(instance == null) instance = new Deck();
        return instance;
    }

    public void deckInitialize() {
        Cards cards = Cards.getInstance();
        HashMap<Integer, Card> map = cards.getCards();
        Iterator<Card> i = map.values().iterator();

        Log.d(TAG, "Deck Constructor");
        Card tmp;
        while(i.hasNext()) {
            tmp = i.next();
            //Log.d(TAG, "key=" + tmp.getKey() + " suit=" + tmp.getSuit() + " rank=" + tmp.getRank());
        }
        deckofcards.addAll(map.values());
    }
    //	removes and returns the first card in this Deck object (from index 0)
    public Card popFirst() {
        if (deckofcards.size() == 0) return null;
        Card temp = deckofcards.get(0);
        deckofcards.remove(0);
        return temp;
    }

    //	removes and returns the last card in this Deck object (from index size() - 1)
    public Card popLast() {
        if (deckofcards.size() == 0) return null;
        Card temp = deckofcards.get(deckofcards.size() - 1);
        deckofcards.remove(deckofcards.size() - 1);
        return temp;
    }

    public void shuffle() {
        Random gen=new Random();

        ArrayList<Card> shuffled = new ArrayList<Card>();
        while (deckofcards.size() > 0) {
            int pos = gen.nextInt(shuffled.size() + 1);
            shuffled.add(pos, popFirst());
        }
        deckofcards = shuffled;
    }

    public Card getcard(int i) {
        return deckofcards.get(i);
    }

    public int getDeckSize() { return deckofcards.size(); }
}
