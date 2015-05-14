package com.ttajun.mighty.manager;

import java.util.HashMap;

/**
 * Created by ttajun on 2015-04-25.
 */
public class DataManager {
    static DataManager instance = null;
    static HashMap<Integer, String> data_set;
    int max_z_order;

    public static final int MAX_CARD_COUNT = 53;
    public static final int JOKER = 0;
    public static final int SPADE_A = 1;
    public static final int SPADE_K = 2;
    public static final int SPADE_Q = 3;
    public static final int SPADE_J = 4;
    public static final int SPADE_10 = 5;
    public static final int SPADE_9 = 6;
    public static final int SPADE_8 = 7;
    public static final int SPADE_7 = 8;
    public static final int SPADE_6 = 9;
    public static final int SPADE_5 = 10;
    public static final int SPADE_4 = 11;
    public static final int SPADE_3 = 12;
    public static final int SPADE_2 = 13;
    public static final int DIAMOND_A = 14;
    public static final int DIAMOND_K = 15;
    public static final int DIAMOND_Q = 16;
    public static final int DIAMOND_J = 17;
    public static final int DIAMOND_10 = 18;
    public static final int DIAMOND_9 = 19;
    public static final int DIAMOND_8 = 20;
    public static final int DIAMOND_7 = 21;
    public static final int DIAMOND_6 = 22;
    public static final int DIAMOND_5 = 23;
    public static final int DIAMOND_4 = 24;
    public static final int DIAMOND_3 = 25;
    public static final int DIAMOND_2 = 26;
    public static final int HEART_A = 27;
    public static final int HEART_K = 28;
    public static final int HEART_Q = 29;
    public static final int HEART_J = 30;
    public static final int HEART_10 = 31;
    public static final int HEART_9 = 32;
    public static final int HEART_8 = 33;
    public static final int HEART_7 = 34;
    public static final int HEART_6 = 35;
    public static final int HEART_5 = 36;
    public static final int HEART_4 = 37;
    public static final int HEART_3 = 38;
    public static final int HEART_2 = 39;
    public static final int CLUB_A = 40;
    public static final int CLUB_K = 41;
    public static final int CLUB_Q = 42;
    public static final int CLUB_J = 43;
    public static final int CLUB_10 = 44;
    public static final int CLUB_9 = 45;
    public static final int CLUB_8 = 46;
    public static final int CLUB_7 = 47;
    public static final int CLUB_6 = 48;
    public static final int CLUB_5 = 49;
    public static final int CLUB_4 = 50;
    public static final int CLUB_3 = 51;
    public static final int CLUB_2 = 52;

    public static final int TYPE_CARD = 100;
    public static final int TYPE_GAMER = 101;
    public static final int TYPE_OBJECT = 102;

    public static final int USER_PLAYER = 200;
    public static final int GUEST_PLAYER1 = 201;
    public static final int GUEST_PLAYER2 = 202;
    public static final int GUEST_PLAYER3 = 203;
    public static final int GUEST_PLAYER4 = 204;
    public static final int PLAYER_NUM = 5;

    public static final int TURN_MARKER = 210;

    public static final String FACEDOWN = "b1fv";

    private DataManager() {
        data_set = new HashMap<Integer, String>();

        data_set.put(JOKER, "jr");
        data_set.put(SPADE_A, "s1");
        data_set.put(SPADE_2, "s2");
        data_set.put(SPADE_3, "s3");
        data_set.put(SPADE_4, "s4");
        data_set.put(SPADE_5, "s5");
        data_set.put(SPADE_6, "s6");
        data_set.put(SPADE_7, "s7");
        data_set.put(SPADE_8, "s8");
        data_set.put(SPADE_9, "s9");
        data_set.put(SPADE_10, "s10");
        data_set.put(SPADE_J, "sj");
        data_set.put(SPADE_Q, "sq");
        data_set.put(SPADE_K, "sk");
        data_set.put(DIAMOND_A, "d1");
        data_set.put(DIAMOND_2, "d2");
        data_set.put(DIAMOND_3, "d3");
        data_set.put(DIAMOND_4, "d4");
        data_set.put(DIAMOND_5, "d5");
        data_set.put(DIAMOND_6, "d6");
        data_set.put(DIAMOND_7, "d7");
        data_set.put(DIAMOND_8, "d8");
        data_set.put(DIAMOND_9, "d9");
        data_set.put(DIAMOND_10, "d10");
        data_set.put(DIAMOND_J, "dj");
        data_set.put(DIAMOND_Q, "dq");
        data_set.put(DIAMOND_K, "dk");
        data_set.put(HEART_A, "h1");
        data_set.put(HEART_2, "h2");
        data_set.put(HEART_3, "h3");
        data_set.put(HEART_4, "h4");
        data_set.put(HEART_5, "h5");
        data_set.put(HEART_6, "h6");
        data_set.put(HEART_7, "h7");
        data_set.put(HEART_8, "h8");
        data_set.put(HEART_9, "h9");
        data_set.put(HEART_10, "h10");
        data_set.put(HEART_J, "hj");
        data_set.put(HEART_Q, "hq");
        data_set.put(HEART_K, "hk");
        data_set.put(CLUB_A, "c1");
        data_set.put(CLUB_2, "c2");
        data_set.put(CLUB_3, "c3");
        data_set.put(CLUB_4, "c4");
        data_set.put(CLUB_5, "c5");
        data_set.put(CLUB_6, "c6");
        data_set.put(CLUB_7, "c7");
        data_set.put(CLUB_8, "c8");
        data_set.put(CLUB_9, "c9");
        data_set.put(CLUB_10, "c10");
        data_set.put(CLUB_J, "cj");
        data_set.put(CLUB_Q, "cq");
        data_set.put(CLUB_K, "ck");

    }

    public static DataManager getInstance() {
        if(instance == null) instance = new DataManager();
        return instance;
    }

    public HashMap<Integer, String> getCards() {
        return data_set;
    }

    public String getFilename(Integer k) {
        String card = null;
        synchronized (this) {
            card = data_set.get(k);
        }
        return card;
    }

    public void remove(Integer k) {
        synchronized (this) {
            data_set.remove(k);
        }
    }

    public String getFaceDown() {
        return FACEDOWN;
    }

    public int getGamerNum() { return PLAYER_NUM; }
    public int getGamerType() { return TYPE_GAMER; }
    public int getUserPlayer() { return USER_PLAYER; }

    public int getMax_z_order() {
        return max_z_order;
    }

    public void setMax_z_order(int z) {
        max_z_order = z;
    }

    public int getMaxCardCount() {
        return MAX_CARD_COUNT;
    }

    public int getTypeCard() {
        return TYPE_CARD;
    }
}


