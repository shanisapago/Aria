package com.example.aria;

import android.util.SparseIntArray;

public class DayListComparator implements java.util.Comparator<DayListItem> {

    @Override
    public int compare(DayListItem d1, DayListItem d2) {
        int h1 = Integer.parseInt(d1.time.substring(0,d1.time.indexOf(':')));
        int m1 = Integer.parseInt(d1.time.substring(d1.time.indexOf(':')+1,d1.time.length()));
        int h2 = Integer.parseInt(d2.time.substring(0,d2.time.indexOf(':')));
        int m2 = Integer.parseInt(d2.time.substring(d2.time.indexOf(':')+1,d2.time.length()));
        if(h1-h2==0)
            return m1-m2;
        return h1-h2;
    }
}