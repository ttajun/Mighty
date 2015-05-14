package com.ttajun.mighty.manager;

import com.ttajun.mighty.gameobject.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by ttajun on 2015-04-26.
 */
public class HashMapSort {
    static HashMapSort instance = null;

    private HashMapSort() {

    }

    public static HashMapSort getInstance() {
        if(instance == null) instance = new HashMapSort();
        return instance;
    }

    public static void sortByKey(final HashMap hashMap) {
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(hashMap.keySet());

        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Object v1 = hashMap.get(o1);
                Object v2 = hashMap.get(o2);

                return ((Comparable) v1).compareTo(v2);
            }
        });
        //Collections.reverse(list);
    }

    public static void sortByZ(final HashMap hashMap) {
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(hashMap.keySet());

        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Card v1 = (Card)hashMap.get(o1);
                Card v2 = (Card)hashMap.get(o2);

                return (v1.getZ() > v2.getZ() ? 1:0);
            }
        });
        //Collections.reverse(list);
    }
}
