package com.example.jwahn37.coinkeeper.managers;

import com.example.jwahn37.coinkeeper.datas.StaticDatas;


public class UIManager {

    String[] names= new String[StaticDatas.NUM_LAYOUT];

    public UIManager()
    {
        names[StaticDatas.LAYOUT_PREDICTION]=StaticDatas.PREDICTION;
        names[StaticDatas.LAYOUT_GPAPH]=StaticDatas.GRAPH;
        names[StaticDatas.LAYOUT_ARTICLE]=StaticDatas.ARTICLE;

       // pred_date = "Tue May 27 13:15";
        //pred_weather = StaticDatas.PRED_SUNNY;
       // pred_situ =  StaticDatas.PRED_RINCREASE;
    }

    public String getName(int position)
    {
        return names[position];
    }

}
