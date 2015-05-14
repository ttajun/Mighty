package com.ttajun.mighty.manager;

/**
 * Created by ttajun on 2015-04-27.
 */
public class DashBoard {
    static DashBoard instance = null;

    private DashBoard() {

    }

    public static DashBoard getInstance() {
        if(instance == null) instance = new DashBoard();
        return instance;
    }
}
