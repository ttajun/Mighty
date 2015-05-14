package com.ttajun.mighty.manager;

import java.util.HashMap;

/**
 * Created by ttajun on 2015-04-27.
 */
public class Orders {
    static Orders instance = null;
    static HashMap<Integer, Order> order_set = null;
    int currKey;

    public static final int GAME_MASTER_ORDER = 0;

    // Master CMD
    public static final int GM_WAITING_START = 0;

    private void Order() {
        order_set = new HashMap<Integer, Order>();
        currKey = 0;
    }

    public static Orders getInstance() {
        if(instance == null) instance = new Orders();
        return instance;
    }

    public HashMap<Integer, Order> getOrder_set() { return order_set; }

    public void put(Order o) {
        synchronized (this) {
            o.setKey(currKey++);
            order_set.put(o.getKey(), o);
        }
    }

    public Order get(Integer k) {
        Order order = null;
        synchronized (this) {
            order = order_set.get(k);
        }
        return  order;
    }

    public void remove(Integer k) {
        synchronized (this) {
            order_set.remove(k);
        }
    }

    public void reset() {
        order_set.clear();
    }
}
