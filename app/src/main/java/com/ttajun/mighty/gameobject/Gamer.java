package com.ttajun.mighty.gameobject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.manager.DataManager;
import com.ttajun.mighty.manager.GameManager;
import com.ttajun.mighty.manager.ProceedManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by ttajun on 2015-04-25.
 */
public class Gamer extends GameObject {
    private static final String TAG = Gamer.class.getSimpleName();
    int gamer_num;
    Hand hand;
    int cardPosX, cardPosY, cardDir;
    String myBid = "";
    String myMaxBid = "";
    Bid bid;

    String gamerMessage;
    String gamerMessage2 = "";
    int msgX, msgY;
    Paint pMessage;

    Game game;
    //ProceedManager proceed;

    public Gamer(int key, int type, int gamer_num) {
        super(key, type);
        this.gamer_num = gamer_num;
        hand = new Hand(gamer_num);
        Log.d(TAG, "Gamer [" + gamer_num + "] was created!!!");
        gamerMessage = " Bid [ " + myBid + " ]";

        Context context = MightyApplication.getInstance().getApplicationContext();

        pMessage = new Paint();
        pMessage.setAntiAlias(true);
        pMessage.setStrokeWidth(5);
        pMessage.setStrokeCap(Paint.Cap.ROUND);
        pMessage.setTextSize(12);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        pMessage.setTypeface(tf);
        //pMessage.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        pMessage.setColor(Color.WHITE);

        game = Game.getInstance();
        bid = Bid.getInstance();
        //proceed = ProceedManager.getInstance();
    }

    public Hand getHand() { return hand; }

    public void handSpread() {
        Card card;
        boolean moveLock = true;
        boolean faceDown = true;

        int offset = 10;
        int guest = DataManager.USER_PLAYER + gamer_num;
        switch(guest) {
            case DataManager.USER_PLAYER:
                cardPosX = 260;
                cardPosY = 350;
                cardDir = 0;
                moveLock = false;
                faceDown = false;
                offset = 20;
                msgX = 260;
                msgY = 340;
                break;
            case DataManager.GUEST_PLAYER1:
                cardPosX = 120;
                cardPosY = 170;
                cardDir = 1;
                msgX = 120;
                msgY = 150;
                break;
            case DataManager.GUEST_PLAYER2:
                cardPosX = 180;
                cardPosY = 30;
                cardDir = 0;
                msgX = 180;
                msgY = 20;
                break;
            case DataManager.GUEST_PLAYER3:
                cardPosX = 470;
                cardPosY = 30;
                cardDir = 0;
                msgX = 470;
                msgY = 20;
                break;
            case DataManager.GUEST_PLAYER4:
                cardPosX = 580;
                cardPosY = 170;
                cardDir = 1;
                msgX = 580;
                msgY = 150;
                break;
        }
        for(int i=0; i < hand.getsize(); i++) {
            GameManager gameManager = GameManager.getInstance();
            card = hand.getcard(i);
            card.setMoveLock(moveLock);
            //faceDown = false;
            card.setFaceDown(faceDown);
            gameManager.changeObjectZ(card.getZ(), DataManager.TYPE_CARD);
            //card.setZ(i + gamer_num*hand.getsize());
            if(cardDir == 0) card.setMoving(cardPosX + i * offset, cardPosY);
            else card.setMoving(cardPosX, cardPosY + i * offset);
            //Log.d(TAG, "id=" + gamer_num + " value=" + card.getValue() + " suit=" + card.getSuit());
        }
    }

    public void setMyBid(String bid) {
        myBid = bid;
        gamerMessage = " Bid [ " + myBid + " ]";
    }

    public void setMyFriend(String friend) {
        if(gamer_num == 1 || gamer_num == 4) gamerMessage2 = " Friend [ " + friend + " ]";
        else gamerMessage += " Friend [ " + friend + " ]";
    }

    public String requestBid(String currentBid) {
        //Bid bid = Bid.getInstance();
        String suit, value;

        if(myMaxBid.equals("")) myMaxBid = bid.requestMaxBid(hand);

        Log.d(TAG, "Gamer[" + gamer_num + "]" + " requestBid() myMaxBid=" + myMaxBid);
        if(myMaxBid.equals("PASS")) {
            myBid = "PASS";
        } else {
            suit = myMaxBid.substring(0, myMaxBid.length()-1);
            value = currentBid.substring(currentBid.length()-1, currentBid.length());
            if(compareBid(myMaxBid, currentBid)) myBid = increaseBid(suit+value);
            else myBid = "PASS";
        }
        setMyBid(myBid);
        return myBid;
    }

    public boolean compareBid(String bid1, String bid2) {
        int bidValue1, bidValue2;
        String tmpToken1, tmpToken2;

        if(bid1 == null || bid2 == null) {
            Log.d(TAG , " bid value is null");
            return false;
        }

        if(bid1.equals("PASS") || bid2.equals("PASS")) {
            Log.d(TAG, " bid value is PASS");
            return false;
        }

        tmpToken1 = bid1.substring(bid1.length() - 1, bid1.length());
        bidValue1 = Integer.parseInt(tmpToken1);

        tmpToken2 = bid2.substring(bid2.length()-1, bid2.length());
        bidValue2 = Integer.parseInt(tmpToken2);

        return (bidValue1 > bidValue2);
    }

    public String increaseBid(String bid) {
        String iBid = "";
        int bidValue;
        String tmpToken, tmpSuits;

        if(bid == null) {
            Log.d(TAG, " increase bid value is null");
            return iBid;
        }

        if(bid.equals("PASS")) {
            Log.d(TAG, " increase bid value is PASS");
            return iBid;
        }

        tmpToken = bid.substring(bid.length()-1, bid.length());
        tmpSuits = bid.substring(0, bid.length() - 1);
        bidValue = Integer.parseInt(tmpToken);
        bidValue++;
        iBid = tmpSuits+ String.valueOf(bidValue);

        Log.d(TAG, "Gamer[" + gamer_num + "]" + "increaseBid() bid: " + bid + " increaseBid: " + iBid);
        return iBid;
    }

    public void requestDrop(String curBid) {
        if(hand.getsize() != 13) {
            Log.d(TAG, "Guest(" + gamer_num + ") don't have enough card to drop.");
        } else {
            bid.dropCardInHand(hand, curBid);
        }
    }

    public boolean friendCardExist(String suit, String rank) {
        Card card = null;

        if(suit.equals("") || rank.equals("")) {
            Log.d(TAG, "Invalid Cards.getCard() parameter.");
            return false;
        }

        for(int i=0; i < hand.getsize(); i++) {
            card = hand.getcard(i);
            if(suit.equals(card.getSuit()) && rank.equals(card.getRank())) return true;
        }
        return false;
    }

    public Card requestFriend(String curBid) {
        Card card;

        card = bid.findFriend(hand, curBid);
        Log.d(TAG, "requestFriend Card(" + card.getSuit() + card.getRank() + ")");

        return card;
    }

    public Card requestTrickCard(int tRound, int tOrder, String tSuit, String tBidSuit, Card curBoss) {
        Log.d(TAG, "requestTrickCard() tRound:" + tRound + " tOrder:" + tOrder);
        Log.d(TAG, "requestTrickCard() tSuit:" + tSuit + " tBidSuit:" + tBidSuit);
        int i;
        Card card;
        List<Card> pCards;
        List<Card> fCards;

        pCards = possibleCard(hand, tRound, tSuit, tBidSuit);
        for(i=0; i<pCards.size(); i++) {
            card = pCards.get(i);
            Log.d(TAG, "possibleCard (" + i + ") is " + card.getSuit() + card.getRank());
        }
        if(pCards.size() == 0) Log.d(TAG, "possibleCard empty");

        fCards = favorableCard(pCards, tRound, tSuit, tBidSuit, curBoss);
        for(i=0; i<fCards.size(); i++) {
            card = fCards.get(i);
            Log.d(TAG, "favorableCard (" + i + ") is " + card.getSuit() + card.getRank());
        }
        if(fCards.size() == 0) Log.d(TAG, "favorableCard empty");

        card = null;
        if(fCards.size() != 0) card = fCards.get(0);
        if(pCards.size() != 0 && card == null) card = pCards.get(0);
        if(card == null) card = hand.getcard(0);
        moveTrickCard(card);
        return card;
    }

    public void moveTrickCard(Card card) {
        int guest = DataManager.USER_PLAYER + gamer_num;
        switch(guest) {
            case DataManager.GUEST_PLAYER1:
                cardPosX = 240;
                cardPosY = 230;
                break;
            case DataManager.GUEST_PLAYER2:
                cardPosX = 290;
                cardPosY = 130;
                break;
            case DataManager.GUEST_PLAYER3:
                cardPosX = 420;
                cardPosY = 130;
                break;
            case DataManager.GUEST_PLAYER4:
                cardPosX = 470;
                cardPosY = 230;
                break;
        }
        GameManager.getInstance().changeObjectZ(card.getZ(), DataManager.TYPE_CARD);
        card.setMoving(cardPosX, cardPosY);
        card.setFaceDown(false);
        //Log.d(TAG, "id=" + gamer_num + " value=" + card.getValue() + " suit=" + card.getSuit());
    }

    public List<Card> possibleCard(Hand hand, int tRound, String tSuit, String tBidSuit) {
        List<Card> cards = new ArrayList<>();
        Card card;
        String suit;

        for(int i=0; i < hand.getsize(); i++) {
            card = hand.getcard(i);
            suit = card.getSuit();

            if(!tSuit.equals("") && !tSuit.equals(suit)) continue;
            if(tRound == 0 && (suit.equals("JOKER") || suit.equals(tBidSuit))) continue;
            cards.add(card);
        }
        return cards;
    }

    public List<Card> favorableCard(List<Card> pCards, int tRound, String tSuit, String tBidSuit, Card curBoss) {
        List<Card> cards = new ArrayList<>();
        Card card, tmpCard;

        if(curBoss != null) {
            for(int i=0; i < pCards.size(); i++) {
                card = pCards.get(i);
                tmpCard = compareCardInTrick(card, curBoss, tSuit, tBidSuit);
                if(card == tmpCard) cards.add(card);
            }
        }
        return cards;
    }

    public Card compareCardInTrick(Card cardA, Card cardB, String trickSuit, String bidSuit) {
        int cardA_value, cardB_value;

        Log.d(TAG, "compareCardInTrick() cardA:" + cardA.getSuit() + cardA.getRank());
        Log.d(TAG, "compareCardInTrick() cardB:" + cardB.getSuit() + cardB.getRank());
        Log.d(TAG, "trickSuit:" + trickSuit + " bidSuit:" + bidSuit);
        cardA_value = cardValueInTrick(cardA, trickSuit, bidSuit);
        cardB_value = cardValueInTrick(cardB, trickSuit, bidSuit);
        Log.d(TAG, "cardA_value:" + cardA_value + " cardB_value:" + cardB_value);

        if(cardA_value > cardB_value) return cardA;
        else return cardB;
    }

    public int cardValueInTrick(Card card, String trickSuit, String bidSuit) {
        int cardValue = card.getValue();
        String cardSuit = card.getSuit();

        if(cardSuit.equals("JOKER")) return 500;
        if(cardValue == 14) {
            if(bidSuit.equals("Spade") && cardSuit.equals("Diamond")) return 1000;
            if(cardSuit.equals("Spade")) return 1000;
        }
        if(cardSuit.equals(bidSuit)) return 14*3 + cardValue;
        if(cardSuit.equals(trickSuit)) return 14*2 + cardValue;
        return cardValue;
    }

    public void gamerInitialize() {
        hand.handClear();
        myBid = "";
        myMaxBid = "";
        gamerMessage = " Bid [ " + myBid + " ]";
        gamerMessage2 = "";
    }
    @Override
    public void update() {

    }

    @Override
    public void drawMain(Canvas canvas) {
        canvas.drawText(gamerMessage, msgX, msgY, pMessage);
        if(!gamerMessage2.equals("")) canvas.drawText(gamerMessage2, msgX, msgY + 15, pMessage);
    }
}
