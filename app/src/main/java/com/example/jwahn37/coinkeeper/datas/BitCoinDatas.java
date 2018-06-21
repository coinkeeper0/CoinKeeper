package com.example.jwahn37.coinkeeper.datas;

import android.util.Log;

public class BitCoinDatas {
    static int graph_price[] = new int[7*24*12]; //7일, 5분단위 데이터
    static String graph_date[] = new String[7*24*12];
    static String graph_time[] = new String[7*24*12];

    static int prediction;  //-2: 급락, -1: 하락, 0: 유지, 1:상승, 2:급상승

    static String article_title;
    static String article_URL;
    static String article_imgURL;
    static String article_description;

    //HTTP Manager가 setting한다.
    public static void setGraphData(int idx, String date, String time, int price)
    {
        graph_price[idx] = price;
        graph_date[idx] = date;
        graph_time[idx] = time;

        Log.v("set", String.valueOf(idx)+" "+date+" "+time+" "+String.valueOf(price));
    }
    public static void setPredictionData(int pred)
    {
        prediction=pred;
        Log.v("setPredictionData", String.valueOf(prediction));
    }
    public static void setArticleData(String title, String URL, String imgURL, String description)
    {
        article_title = title;
        article_imgURL = imgURL;
        article_description = description;
        article_URL = URL;
        Log.v("article title:", article_title);
        Log.v("article URL:", article_URL);
        Log.v("article imgurl:", article_imgURL);
        Log.v("article descrip", article_description);
    }
}
