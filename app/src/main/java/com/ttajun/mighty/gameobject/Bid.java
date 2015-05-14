package com.ttajun.mighty.gameobject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by ttajun on 2015-05-04.
 */
public class Bid {
    private static Bid instance = null;
    private static final String TAG = Bid.class.getSimpleName();
    private static final int JOKER_VALUE = 30;
    private static final int MIGHTY_VALUE = 30;
    private static final int FIRST_SHOT_VALUE = 30;

    Cards cards;

    private Bid() {
        cards = Cards.getInstance();
    }

    public static Bid getInstance() {
        if(instance == null) instance = new Bid();
        return instance;
    }

    public String requestMaxBid(Hand hand) {
        String bid = "";
        String tmpBid = "";
        String[] suits = {"Spade", "Diamond", "Heart", "Club"};
        int[] result = new int[suits.length];
        int value, tmpValue = 0;
        Card card;
        String tmpSuit = "";
        int first;
        int i, j;

        for(i=0; i < suits.length; i++) {
            value = 0;
            first = 0;
            for(j=0; j < hand.getsize(); j++) {
                card = hand.getcard(j);
                tmpSuit = card.getSuit();
                //Log.d(TAG, "i=" + i + " j=" + j + " value=" + card.getValue() + " suit=" + tmpSuit);
                if(tmpSuit.equals("JOKER")) {
                    value += JOKER_VALUE;
                    continue;
                }
                if(card.getValue() == 14) first++;
                if(card.getValue() == 13) {
                    if(suits[i].equals("Spade") && tmpSuit.equals("Diamond")) first++;
                    else if (suits[i].equals("Diamond") && tmpSuit.equals("Spade")) first++;
                }
                if(first > 0) value += FIRST_SHOT_VALUE;

                if(tmpSuit.equals("")) {
                    Log.d(TAG, "JOKER Found");
                    continue;
                }
                if(tmpSuit.equals(suits[i])) value += card.getValue() * 2;
            }
            result[i] = value;
        }

        for(i=0; i < result.length; i++) {
            if(result[i] > tmpValue) {
                tmpValue = result[i];
                tmpBid = suits[i];
            }
        }

        if(tmpValue > 90 && tmpValue < 130) bid = tmpBid + "6";
        else if(tmpValue > 130 && tmpValue < 150) bid = tmpBid + "7";
        else if(tmpValue > 150 && tmpValue > 170) bid = tmpBid + "8";
        else if(tmpValue > 170 && tmpValue < 200) bid = tmpBid + "9";
        else bid = "PASS";
        return bid;
    }

    public Card findFriend(Hand hand, String bid) {
        String suit, rank;
        String Friend = "";
        Card card = null;

        suit = bid.substring(0, bid.length()-1);
        Log.d(TAG, "findFriend suit:" + suit + " curBid:" + bid);

        Friend = checkMighty(hand, suit);
        if(!Friend.equals("")) {
            Log.d(TAG, "checkMighty Friend:" + Friend);
            suit = Friend.substring(0, Friend.length()-1);
            rank = Friend.substring(Friend.length()-1, Friend.length());
            Log.d(TAG, "checkMighty Friend() suit:" + suit + " rank:" + rank);
            card = cards.getCard(suit, rank);
            return card;
        }

        Friend = checkJoker(hand);
        if(!Friend.equals("")) {
            Log.d(TAG, "checkJoker Friend:" + Friend);
            card = cards.getCard("JOKER", "J");
            return card;
        }

        Friend = checkFirst(hand, suit);
        Log.d(TAG, "checkMighty Friend:" + Friend);
        suit = Friend.substring(0, Friend.length() - 1);
        rank = Friend.substring(Friend.length() - 1, Friend.length());
        card = cards.getCard(suit, rank);
        return card;
    }

    public String checkMighty(Hand hand, String suit) {
        Card card;
        String mighty = "";

        if(suit.equals("Spade")) mighty = "DiamondA";
        else mighty = "SpadeA";

        for(int i=0; i < hand.getsize(); i++) {
            card = hand.getcard(i);
            if(card.getValue() == 14) {
                if (suit.equals("Spade") && card.getSuit().equals("Diamond")) mighty = "";
                else if (card.getSuit().equals("Spade")) mighty = "";
            }
        }
        return mighty;
    }

    public String checkJoker(Hand hand) {
        String joker = "JOKER";
        for(int i=0; i < hand.getsize(); i++) {
            if(hand.getcard(i).getSuit().equals("JOKER")) joker="";
        }
        return joker;
    }

    public String checkFirst(Hand hand, String suit) {
        Card card;
        CardMap map = new CardMap();
        String first = "";
        int i;

        for(i=0; i < hand.getsize(); i++) {
            card = hand.getcard(i);
            if(card.getSuit().equals("JOKER")) continue;
            map.putCard(card);
        }

        first = map.getFirstShot(suit);
        if(!first.equals("")) return first;

        first = map.getTrumpHigh(suit);
        return first;

        /*
        while(useless.size() != 3) {
            if(useless.size() > 3) {
                for(i=0; i < useless.size(); i++) {
                    card = useless.get(i);
                    if(map.checkCardParent(card) || card.getValue() >= 10) {
                        useless.remove(i);
                        continue;
                    }
                }
                popLastValueInList(useless);
            } else if(useless.size() < 3) {
                card = map.lastCardInSuitTmp(hand, suit);
                if(card == null) {
                    Log.d(TAG, "Somethings wrong.");
                } else {
                    useless.add(card);
                }
            }
        }
        */
    }

    public void dropCardInHand(Hand hand, String curBid) {
        Card card;
        List<Card> useless = new ArrayList<>();
        TreeMap<Integer, Card> dropCard = new TreeMap<>();
        CardMap map = new CardMap();
        int i, key;
        String suit;

        suit = curBid.substring(0, curBid.length() -1);
        Log.d(TAG, "dropCardInHand suit:" + suit + " curBid:" + curBid);

        for(i=0; i < hand.getsize(); i++) {
            card = hand.getcard(i);
            if(card.getSuit().equals("JOKER")) continue;
            if(card.getValue() == 14 || card.getSuit().equals(suit)) {
                map.putCard(card);
                continue;
            }

            if(card.getValue() == 13) {
                if ((suit.equals("Spade") && card.getSuit().equals("Diamond")) || card.getSuit().equals("Spade")) {
                    map.putCard(card);
                    continue;
                }
            }
            useless.add(card);
        }

        for(i=0; i<useless.size(); i++) Log.d(TAG, "useless Card(" + useless.get(i).getSuit() + useless.get(i).getRank() + ")");

        if(useless.size() < 3) {
            Log.d(TAG, "useless card num < 3, add small trump card in useless.");
            int tmpCount = 3 - useless.size();
            for(i=0; i<tmpCount; i++) {
                card = map.lastCardInSuit(suit, i+1);
                useless.add(card);
            }
        }

        for(i=0; i<useless.size(); i++) {
            card = useless.get(i);
            if(card.getValue() >= 10) key = 15 - card.getValue();
            else key = card.getValue() + 10;

            dropCard.put(key, card);
        }

        for(i=0; i<3; i++) {
            int tmpKey = dropCard.firstKey();
            card = dropCard.get(tmpKey);
            dropCard.remove(tmpKey);

            hand.removeCard(card);
            card.setMoveLock(true);
            card.setMoving(625, 365);
        }
    }

    void popLastValueInList(List<Card> list) {
        Card card;
        int tmpValue, tmpIndex = 0;

        tmpValue = list.get(0).getValue();
        for(int i=0; i < list.size(); i++) {
            card = list.get(i);
            if(card.getValue() < tmpValue) {
                tmpValue = card.getValue();
                tmpIndex = i;
            }
        }
        list.remove(tmpIndex);
    }

    public class CardMap {
        String map[][] = new String[4][14];
        CardMap() {
            for(int i=0; i < map.length; i++) {
                for(int j=0; j < map[i].length; j++) {
                    map[i][j] = "";
                }
            }

            map[0][0] = "Spade";
            map[1][0] = "Diamond";
            map[2][0] = "Heart";
            map[3][0] = "Club";
        }

        void putCard(Card card) {
            if(!map[0][0].equals("Spade") || map.length != 4 || map[0].length != 14) {
                Log.d(TAG, "Invalid Map");
                return;
            }

            if(card.getSuit().equals("JOKER")) {
                Log.d(TAG, "Joker can't put the map.");
                return;
            }

            for(int i=0; i < map.length; i++) {
                if(map[i][0].equals(card.getSuit())) map[i][15-card.getValue()] = card.getRank();
            }
        }

        String[] getCardLine(Card card) {
            String[] tmpMap = new String[14];
            for(int i=0; i < map.length; i++) {
                if(map[i][0].equals(card.getSuit())) return map[i];
            }
            return tmpMap;
        }

        boolean checkCardParent(Card card) {
            String[] tmpMap;
            boolean result = true;
            tmpMap = getCardLine(card);
            for(int i=1; i < (15-card.getValue()); i++) {
                if(tmpMap[i].equals("")) result = false;
            }
            return result;
        }

        Card lastCardInSuitTmp(Hand hand, String suit) {
            Card card = null;
            String cardSuit = "";
            int value = 0;

            for(int i=0; i < map.length; i++) {
                if(map[i][0].equals(suit)) {
                    for(int j=0; j < map[i].length; j++) {
                        if(!map[i][j].equals("")) {
                            cardSuit = map[i][j];
                            value = j;
                        }
                    }
                }
            }

            for(int k=0; k < hand.getsize(); k++) {
                card = hand.getcard(k);
                if(card.getValue() == value && card.getSuit().equals(cardSuit)) break;
                card = null;
            }
            return card;
        }

        Card lastCardInSuit(String suit, int index) {
            Card card = null;
            String rank = "";
            int tmpIndex = 0;

            for(int i=0; i < map.length; i++) {
                if(map[i][0].equals(suit)) {
                    for(int j=map[i].length -1; j < 1; j--) {
                        if(!map[i][j].equals("")) {
                            tmpIndex++;
                            if(tmpIndex == index) {
                                rank = map[i][j];
                                break;
                            }
                        }
                    }
                }
            }

            card = cards.getCard(suit, rank);
            if(card == null) Log.d(TAG, "lastCardInSuit get null card.");
            return card;
        }

        String getFirstShot(String suit) {
            String tmpSuit, tmpRank, friend = "";

            int tmpCnt = 0;
            for(int i=0; i < map.length; i++) {
                tmpSuit = map[i][0];
                if(!tmpSuit.equals(suit)) {
                    for(int j=map[i].length-1; j < 1; j--) {
                        if(!map[i][j].equals("")) {
                            tmpCnt++;
                        }
                    }

                    if(tmpCnt != 0) {
                        if(suit.equals("Spade") && tmpSuit.equals("Diamond")) tmpRank = "K";
                        else if(suit.equals("Diamond") && tmpSuit.equals("Spade")) tmpRank = "K";
                        else tmpRank = "A";

                        return tmpSuit + tmpRank;
                    }
                }
            }

            return friend;
        }

        String getTrumpHigh(String suit) {
            String tmpSuit, tmpRank = "";

            for(int i=0; i < map.length; i++) {
                tmpSuit = map[i][0];
                if(tmpSuit.equals(suit)) {
                    for(int j=1; j < map[i].length; j++) {
                        if(map[i][j].equals("")) {
                            tmpRank = map[i][j];
                            break;
                        }
                    }
                }
            }

            return suit+tmpRank;
        }
    }
}
