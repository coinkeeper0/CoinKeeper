package com.example.jwahn37.coinkeeper.datas;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BitCoinDatas {
    static float graph_price[] = new float[7*24*12]; //7일, 5분단위 데이터
    static String graph_date[] = new String[7*24*12];
    static String graph_time[] = new String[7*24*12];

    static int prediction;  //-2: 급락, -1: 하락, 0: 유지, 1:상승, 2:급상승
    static String pred_date;
    static String pred_situ;
    static int pred_weather;


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

        //Log.v("set", String.valueOf(idx)+" "+date+" "+time+" "+String.valueOf(price));
    }
    public static void setPredictionData(int pred)
    {
        prediction=pred;
        //og.v("setPredictionData", String.valueOf(prediction));
        switch(prediction)  //-2: 급락, -1: 하락, 0: 유지, 1:상승, 2:급상승
        {
            case -2 :   pred_weather = StaticDatas.PRED_THUNDER;
                        pred_situ = StaticDatas.PRED_RDECREASE;
                        break;

            case -1:    pred_weather = StaticDatas.PRED_RAINY;
                        pred_situ = StaticDatas.PRED_DECREASE;
                        break;

            case 0:     pred_weather = StaticDatas.PRED_CLOUDY;
                        pred_situ = StaticDatas.PRED_NORMAL;
                        break;

            case 1:     pred_weather = StaticDatas.PRED_SUN_CLOUNDY;
                        pred_situ = StaticDatas.PRED_INCREASE;
                        break;

            case 2:     pred_weather = StaticDatas.PRED_SUNNY;
                        pred_situ = StaticDatas.PRED_RINCREASE;
                        break;

        }

        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm");
        pred_date = sdf.format(new Date());

        //Log.v("current_date:",currentDateandTime);
        //pred_date = "Tue May 27 13:15";
        //pred_weather = StaticDatas.PRED_SUNNY;
        //pred_situ =  StaticDatas.PRED_RINCREASE;

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

    public static String[] getGraphDate() {return graph_date;}
    public static String[] getGraphTime() {return graph_time;}
    public static float[] getGraphPrice() {return graph_price;}
    public static String getPredDate() {return pred_date;}
    public static String getPredSitu() {return pred_situ;}
    public static int getPredWeather() {return pred_weather;}
    public static String getArticle_title() {return article_title;}
    public static String getArticle_URL(){return article_URL;}
    public static String getArticle_imgURL(){return article_imgURL;}
    public static String getArticle_description(){return article_description;}

}
