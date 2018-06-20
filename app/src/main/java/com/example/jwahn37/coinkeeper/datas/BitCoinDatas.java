package com.example.jwahn37.coinkeeper.datas;

import android.util.Log;

public class BitCoinDatas {
    static int graph_price[] = new int[7*24*12]; //7일, 5분단위 데이터
    static String graph_date[] = new String[7*24*12];
    static String graph_time[] = new String[7*24*12];

    //HTTP Manager가 setting한다.
    public static void setGraphData(int idx, String date, String time, int price)
    {
        graph_price[idx] = price;
        graph_date[idx] = date;
        graph_time[idx] = time;

        Log.v("set", String.valueOf(idx)+" "+date+" "+time+" "+String.valueOf(price));
    }

}
