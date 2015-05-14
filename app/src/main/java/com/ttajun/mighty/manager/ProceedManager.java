package com.ttajun.mighty.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.ttajun.mighty.GameActivity;
import com.ttajun.mighty.MightyApplication;
import com.ttajun.mighty.gameobject.Card;
import com.ttajun.mighty.gameobject.Cards;
import com.ttajun.mighty.gameobject.Deck;
import com.ttajun.mighty.gameobject.Game;
import com.ttajun.mighty.gameobject.GameState;
import com.ttajun.mighty.gameobject.Gamer;
import com.ttajun.mighty.gameobject.Hand;
import com.ttajun.mighty.pattern.StraightPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ttajun on 2015-04-27.
 */
public class ProceedManager {
    private static final String TAG = ProceedManager.class.getSimpleName();
    static ProceedManager instance = null;
    private Handler mHandler;

    private Game m_game;
    private DataManager m_data;
    private Deck m_deck;

    private String currentBid;
    private List<Integer> passCheck = new ArrayList<>();
    private List<Card> trickCardList = new ArrayList<>();

    private int trickInterval = 0;
    private boolean userResponse = false;
    private boolean userReconstruct = false;
    private boolean userFriendCheck = false;
    public void setUserResponse(boolean response) { userResponse = response; }
    public void setUserReconstruct(boolean reconstruct) { userReconstruct = reconstruct; }
    public void setUserFriendCheck(boolean friend) { userFriendCheck = friend; }

    private ProceedManager() {
        proceedInitialize();
    }

    public static ProceedManager getInstance() {
        if(instance == null) instance = new ProceedManager();
        return instance;
    }

    private void proceedInitialize() {
        m_game = Game.getInstance();
        m_data = DataManager.getInstance();
        m_deck = Deck.getInstance();

        // 게이머 생성
        for(int i=0; i < m_data.getGamerNum(); i++) {
            m_game.setGamer(i, new Gamer(m_data.getUserPlayer() + i, m_data.getGamerType(), i));
        }
        currentBid = m_game.getCurrentBid();

        //mHandler = new Handler(Looper.getMainLooper());
    }

    public void gameReStart() {
        Log.d(TAG, "gameRestart()");
        m_game.gameInitialize();
        GameManager.getInstance().cardZInitialize();
        userResponse = false;
        userFriendCheck = false;
        userReconstruct = false;
        currentBid = m_game.getCurrentBid();
        passCheck.clear();
        trickCardList.clear();
        for(int i=0; i < m_data.getGamerNum(); i++) {
            m_game.getGamer(i).gamerInitialize();
        }

        HashMap<Integer, Card> map = Cards.getInstance().getCards();
        Iterator<Card> i = map.values().iterator();
        Card card;
        while (i.hasNext()) {
            card = i.next();
            card.setFaceDown(true);
            card.setMoveLock(true);
        }
    }

    public void setUserBid(String userBid) {
        Gamer gamer;
        if(userResponse) {
            if(userBid.equals("PASS")) {
                if(!passCheck.contains(0)) passCheck.add(0);
                m_game.increaseGameTurn();
                userResponse = false;
            } else {
                gamer = m_game.getGamer(0);
                if(gamer.compareBid(userBid, currentBid)) {
                    currentBid = userBid;
                    m_game.increaseGameTurn();
                    userResponse = false;
                } else {
                    Log.d(TAG, "current bid =" + currentBid + " , Check your bid");
                }
            }
        }
    }

    public void setUserCard(Card card) {
        int trickOrder = m_game.getTrickOrder();
        if(userResponse) {
            trickCardList.add(card);

            if(trickOrder == 0) m_game.setTrickSuit(card.getSuit());
            m_game.setTrickOrder(trickOrder + 1);
            m_game.increaseGameTurn();
            userResponse = false;
        }
    }

    public void setUserFriend(String userFriend) {
        Gamer gamer;
        String tmpToken, tmpSuit, tmpRank;
        int friend = 0;
        if(userResponse && m_game.getCurrentState() == GameState.CheckFriend) {
            Log.d(TAG, "setUserFriend :" + userFriend);
            tmpToken = userFriend.substring(0, 2);
            if(tmpToken.equals("NT")) friend = 9;
            else {
                tmpSuit = userFriend.substring(0, userFriend.length()-1);
                tmpRank = userFriend.substring(userFriend.length()-1, userFriend.length());

                for(int i=0; i<m_data.getGamerNum(); i++) {
                    gamer = m_game.getGamer(i);
                    if(gamer.friendCardExist(tmpSuit, tmpRank)) {
                        friend = i;
                        break;
                    }
                }
            }
            Log.d(TAG, "Friend num:" + friend);
            m_game.getGamer(0).setMyFriend(userFriend);
            m_game.setMasterFriend(friend);
        }
    }

    public void update() {
        Gamer gamer;
        Hand hand;
        String bid = "";
        GameState m_gameState;
        int m_gameTurn;

        m_gameState = m_game.getCurrentState();
        switch(m_gameState) {
            case BeforeStart:
                //Log.d(TAG, "BeforeStart !!!");
                m_deck.shuffle();
                for(int i=0; i<m_data.getGamerNum(); i++) {
                    gamer = m_game.getGamer(i);
                    hand = gamer.getHand();
                    hand.bringFirstHand(m_deck, i);
                    gamer.handSpread();
                    m_game.setCurrentState(GameState.CheckBid);
                }
                break;

            case CheckBid:
                if(passCheck.size() >= m_game.getNumGamer() -1) {
                    for(int i=0; i < m_game.getNumGamer(); i++) {
                        if(!passCheck.contains(i)) {
                            m_game.setGameMaster(i);
                            m_game.setGameTurn(i);
                            m_game.setCurrentBid(currentBid);
                            userResponse = false;
                        }
                    }
                    m_game.setCurrentState(GameState.ReconstructionHand);
                    break;
                }

                m_gameTurn = m_game.getGameTurn();
                if(m_gameTurn == 0) { // 사용자
                    if(userResponse) break;
                    else {
                        if(!passCheck.contains(0)) {
                            userResponse = true;
                            break;
                        }
                    }
                } else {
                    gamer = m_game.getGamer(m_gameTurn);
                    bid = gamer.requestBid(currentBid);
                    if (bid.equals("PASS")) {
                        if (!passCheck.contains(m_gameTurn)) passCheck.add(m_gameTurn);
                    } else {
                        currentBid = bid;
                    }
                }
                m_game.increaseGameTurn();
                break;

            case ReconstructionHand:
                if(userReconstruct) {
                    m_game.setCurrentState(GameState.CheckFriend);
                    userReconstruct = false;
                    userResponse = false;
                    break;
                }
                if(userResponse) break;

                gamer = m_game.getGamer(m_game.getGameMaster());
                hand = gamer.getHand();
                hand.bringLastHand(m_deck);
                gamer.handSpread();

                if(m_game.getGameMaster() == 0) {
                    userResponse = true;
                } else {
                    gamer.requestDrop(currentBid);
                    m_game.setCurrentState(GameState.CheckFriend);
                }
                break;

            case CheckFriend:
                if(userFriendCheck) {
                    m_game.setCurrentState(GameState.MainPhase);
                    userFriendCheck = false;
                    userResponse = false;
                    mainPhaseInit();
                    break;
                }
                if(userResponse) break;

                gamer = m_game.getGamer(m_game.getGameMaster());
                int friend = -1;
                Card card;

                if(m_game.getGameMaster() == 0) {
                    userResponse = true;
                } else {
                    card = gamer.requestFriend(m_game.getCurrentBid());
                    gamer.setMyFriend(card.getSuit() + card.getRank());
                    Log.d(TAG, "Friend Card(" + card.getSuit() + card.getRank() + ")");
                    for(int i=0; i<m_data.getGamerNum(); i++) {
                        gamer = m_game.getGamer(i);
                        if(gamer.friendCardExist(card.getSuit(), card.getRank())) {
                            friend = i;
                            break;
                        }
                    }
                    m_game.setMasterFriend(friend);
                    m_game.setCurrentState(GameState.MainPhase);
                    mainPhaseInit();
                }
                break;

            case MainPhase:
                int trickRound;
                int trickOrder;
                String trickSuit;
                String trickBidSuit;
                Card curTrickBoss;

                if(trickCardList.size() == m_game.getNumGamer()) {
                    int round = m_game.getTrickRound();
                    int gamerNum, picCount = 0;
                    trickSuit = m_game.getTrickSuit();
                    trickBidSuit = m_game.getBidTrickSuit();

                    if(trickInterval < 33) {
                        trickInterval++;
                        break;
                    } else trickInterval = 0;

                    Card winCard = null;

                    for(int i=0; i < trickCardList.size(); i++) {
                        card = trickCardList.get(i);
                        if(winCard == null) winCard = card;
                        else {
                            winCard = compareCardInTrick(card, winCard, trickSuit, trickBidSuit);
                        }

                        if(card.getValue() >= 10) picCount++;
                    }
                    if(winCard == null) Log.d(TAG, "Round(" + round + ") Judge Fail. Check.");
                    else Log.d(TAG, "Round(" + round + ") win Card is " + winCard.getSuit() + winCard.getRank());

                    Log.d(TAG, "Picture Card count is " + picCount);

                    gamerNum = findCardOwner(winCard);
                    if(!m_game.isMasterSide(gamerNum)) {
                        m_game.increasePicture(picCount);
                    }
                    m_game.setGameTurn(gamerNum);

                    for(int i=0; i < trickCardList.size(); i++) {
                        card = trickCardList.get(i);
                        gamerNum = findCardOwner(card);
                        gamer = m_game.getGamer(gamerNum);
                        gamer.getHand().popCardInHand(card);
                        card.setMoveLock(true);
                        card.setMoving(625, 365);
                    }

                    round++;
                    m_game.setTrickRound(round);
                    m_game.setTrickInit();
                    trickCardList.clear();

                    if(round >= 10) {
                        m_game.setCurrentState(GameState.CheckResult);
                    }
                    break;
                }

                m_gameTurn = m_game.getGameTurn();
                if(m_gameTurn == 0) { // 사용자
                    if(!userResponse) userResponse = true;
                    break;
                } else {
                    trickRound = m_game.getTrickRound();
                    trickOrder = m_game.getTrickOrder();
                    trickSuit = m_game.getTrickSuit();
                    trickBidSuit = m_game.getBidTrickSuit();
                    curTrickBoss = m_game.getCurTrickBoss();

                    gamer = m_game.getGamer(m_gameTurn);
                    card = gamer.requestTrickCard(trickRound, trickOrder, trickSuit, trickBidSuit, curTrickBoss);
                    trickCardList.add(card);

                    if(trickOrder == 0) {
                        m_game.setTrickSuit(card.getSuit());
                        m_game.setCurTrickBoss(card);
                    }
                    m_game.setTrickOrder(trickOrder+1);
                    m_game.increaseGameTurn();
                }
                break;

            case CheckResult:
                if(!userResponse) {
                    Log.d(TAG, "Game State CheckResult. Master:" + m_game.getGameMaster() + " Friend:" + m_game.getMasterFriend());
                    Log.d(TAG, "Game State CheckResult. Bid:" + m_game.getCurrentBid() + " Pic:" + m_game.getPictureNum());
                    String strBid;
                    String result;
                    int bidValue, tmpValue;

                    strBid = m_game.getCurrentBid();
                    result = "Master:" + m_game.getGameMaster() + " Bid: " + strBid + " Result:";
                    bidValue = Integer.parseInt(strBid.substring(strBid.length() - 1, strBid.length()));
                    tmpValue = m_game.getPictureNum() - (10 - bidValue);

                    if(tmpValue < 0) result += " WIN!!! Score +" + Math.abs(tmpValue);
                    else if(tmpValue == 1) result += "Draw. ";
                    else result +="Lose. Score -" + tmpValue;

                    Log.d(TAG, "<<< Game Result >>>");
                    Log.d(TAG, result);
                    m_game.setTrickResult(result);

                    //Handler handler = ((GameActivity)GameActivity.m_context).getM_handler();
                    mHandler = ((GameActivity)GameActivity.m_context).getM_handler();
                    Message msg = mHandler.obtainMessage();
                    msg.what = ((GameActivity)GameActivity.m_context).getM_Result_open();
                    //msg.what = ((GameActivity)GameActivity.m_context).RESULT_DIALOG_OPEN;
                    //msg.what = 0; // RESULT_DIALOG_OPEN;
                    mHandler.sendMessage(msg);
                    //((GameActivity)GameActivity.m_context).createResultDialog(result);
                    userResponse = true;
                }
                break;
        }
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

    public int findCardOwner(Card card) {
        Gamer gamer;
        int gamer_num = -1;
        for(int i=0; i<m_data.getGamerNum(); i++) {
            gamer = m_game.getGamer(i);
            if(gamer.friendCardExist(card.getSuit(), card.getRank())) {
                gamer_num = i;
            }
        }
        if(gamer_num < 0) Log.d(TAG, "findCardOwner() Fail.");
        return gamer_num;
    }

    public void mainPhaseInit() {
        Log.d(TAG, "Main Phase");
        Log.d(TAG, "Master:" + m_game.getGameMaster() + " Friend:" + m_game.getMasterFriend());
        Log.d(TAG, "Current Bid:" + m_game.getCurrentBid());

        String suit = currentBid.substring(0, currentBid.length()-1);
        m_game.setTrickBidSuit(suit);
    }
}
