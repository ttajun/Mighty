package com.ttajun.mighty.gameobject;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttajun on 2015-04-29.
 */
public class Hand {
    private static final String TAG = Hand.class.getSimpleName();
    List<Card> the_hand = new ArrayList<>();

    List<Card> spades = new ArrayList<>();
    List<Card> hearts = new ArrayList<>();
    List<Card> diamonds = new ArrayList<>();
    List<Card> clubs = new ArrayList<>();

    public int gamer_num;

    public List<Card> get_spades() {

        for(int i = 0; i < 10; i++) {
            if (the_hand.get(i).getSuit().equals("Spade")) {
                spades.add(the_hand.get(i));
            }
        }
        return spades;
    }

    public List<Card> get_hearts() {

        for(int i = 0; i < 10; i++) {
            if (the_hand.get(i).getSuit().equals("Heart")) {
                hearts.add(the_hand.get(i));
            }
        }
        return hearts;
    }

    public List<Card> get_diamonds() {

        for(int i = 0; i < 10; i++) {
            if (the_hand.get(i).getSuit().equals("Diamond")) {
                diamonds.add(the_hand.get(i));
            }
        }
        return diamonds;
    }

    public List<Card> get_clubs() {

        for(int i = 0; i < 10; i++) {
            if (the_hand.get(i).getSuit().equals("Club")) {
                clubs.add(the_hand.get(i));
            }
        }
        return clubs;
    }

    public int getsize() {
        return the_hand.size();
    }
    public Card getcard(int cardnum) {
        return the_hand.get(cardnum);
    }
    public void removeCard(Card card) {
        the_hand.remove(card);
    }

    public Hand(int gamer_num) {
        this.gamer_num = gamer_num;
    }

    public void bringFirstHand(Deck the_deck, int gamer_num) {
        Card card;
        for (int i=0; i < 10; i++) {
            card = the_deck.getcard(i*5 + gamer_num);
            if(card == null) Log.e(TAG, "gamer[" + gamer_num + "] " + "index[" + i +"] Card Missing!!!");
            else the_hand.add(card);
        }
    }

    public void bringLastHand(Deck deck) {
        Card card;
        for(int i=0; i < 3; i++) {
            card = deck.getcard((deck.getDeckSize()-1) - i);
            //Log.d(TAG, "Deck Last(" + i + ")" + " card[" + card.getSuit() + card.getRank() + "]");
            if(card == null) Log.e(TAG, "gamer[" + gamer_num + "] " + "index[" + i +"] Card Missing!!!");
            else the_hand.add(card);
        }
    }

    public void handClear() { the_hand.clear(); }

    public void popCardInHand(Card card) {
        if (the_hand.size() == 0) {
            Log.d(TAG, "popIndexCard() is Fail. hand empty.");
            return;
        }
        the_hand.remove(card);
    }

    /*
    //CONSTRUCTOR FOR HAND BASED ON LIST<STRING>
    public Hand(List<String> the_hand_string_list) {

        for (int i=0; i < 13; i++) {
            the_hand.add(new Card(the_hand_string_list.get(i)));
        }

    }

    //NULL CONSTRUCTOR FOR HAND: SPITS OUT 13 RANDOM CARDS
    public Hand() {

        for (int i=0; i < 13; i++) {
            Deck the_deck = new Deck();
            the_deck.shuffle();

            the_hand.add(the_deck.getcard(i));
        }
    }
    */
}
