package com.example.jwahn37.coinkeeper.managers;

import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.example.jwahn37.coinkeeper.datas.BitCoinDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HTTPManager extends AsyncTask {

    //URL url;
    String datas;
    String url_address;

    public void getGraphData()
    {
        String graphDatas = new String();
        //url에 들어갈 data값 구한다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);

        //7일전 데이터, 현재 데이터 -> url 입력값임
        String pastDateandTime = sdf.format(calendar.getTime());
        currentDateandTime=currentDateandTime.replace('_', 'T');
        pastDateandTime=pastDateandTime.replace('_', 'T');
        //format : 2018-06-20T16:53:43

        Log.v("date", currentDateandTime+" "+pastDateandTime );


        try {
            //http://13.125.254.128:3000/api/currency/2018-06-13T00:00:00/2018-06-20T10:00:00
            //7일전부터 오늘까지의 5분단위 data를 모두 받아온다.
         //   Log.v("datas ", "http://13.125.254.128:3000/api/currency/"+pastDateandTime+"/"+currentDateandTime);
            URL url = new URL("http://13.125.254.128:3000/api/currency/"+pastDateandTime+"/"+currentDateandTime);
            graphDatas = getDatasFromServer(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // Log.v("here ", graphDatas);

        try {
            JSONArray jsonArray = new JSONArray(graphDatas);
           // JSONObject reader = new JSONObject(graphDatas);
           // String buf = reader.toString();
           // JSONArray dates = new JSONArray(buf);
            Log.v("len", String.valueOf(jsonArray.length()));
            for(int i=0; i<jsonArray.length(); i++)
            {
                String ele = jsonArray.getString(i);
                JSONObject json_ele = new JSONObject(ele);

           //     Log.v("mydate", ele);
                BitCoinDatas.setGraphData(i, json_ele.getString("date"), json_ele.getString("time"), json_ele.getInt("price_krw"));
            }

           // JSONObject date  = reader.getJSONObject("sys");
           // String date = reader.getString("date");
           // Log.v("json",date);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getDatasFromServer(URL url) throws IOException {
        String datas = new String();
        //일단 예시URL 정훈이한테 받는데로 바꾸기 //
        //get method방식의 http 통신 코드
        //url = new URL("http://crix-api-endpoint.upbit.com/v1/crix/candles/weeks?code=CRIX.UPBIT.BTC-STEEM");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            byte[] buffer = new byte[4096];
            int read = in.read(buffer, 0, 4096);

            int size_=0;
            while (read != -1) {
                byte[] tempData = new byte[read];
                System.arraycopy(buffer, 0, tempData, 0, read);
                String temp_datas = new String(tempData, "UTF-8");
                //publishProgress("낯선이 : " + tempString);
                read = in.read(buffer, 0, 4096);
                datas = datas.concat(temp_datas);
               // Log.v("len", String.valueOf(temp_datas.length()));
               // size_+=temp_datas.length();
              //  Log.v("str", temp_datas);
            }
            //Log.v("all size: ",String.valueOf(size_));
            //Log.v("mysize", String.valueOf(datas.length()));
            //Log.v("string len",String.valueOf(datas.length()));
           // Log.v("here2", datas);
          //  Log.d("here","111");
            //byte[] b = new byte[4096];
            //in.read(b,0,4096);
            //datas = new String(b, "UTF-8");
            //Log.d(datas, "111");

            //readStream(in);
        } finally {
            urlConnection.disconnect();
        }
        return datas;

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        getGraphData();

        return null;
    }
}
