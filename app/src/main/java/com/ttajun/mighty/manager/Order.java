package com.ttajun.mighty.manager;

/**
 * Created by ttajun on 2015-04-27.
 */
public class Order {
    public int orderMaster;
    public int cmd;
    public int key;

    public Order(int orderMaster) {
        this.orderMaster = orderMaster;
    }

    public int getOrderMaster() { return orderMaster; }
    public void setOrderMaster(int orderMaster) { this.orderMaster = orderMaster; }
    public int getCmd() { return cmd; }
    public void setCmd(int cmd) { this.cmd = cmd; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

}
