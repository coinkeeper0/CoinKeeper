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

    public void getArticleData()
    {
        String articleDatas = new String();
        try {
            //http://13.125.254.128:3000/api/news 은 기사를 받아온다.
            URL url = new URL("http://13.125.254.128:3000/api/news");
            articleDatas = getDatasFromServer(url);
            Log.v("article: ", articleDatas);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(articleDatas);
            JSONObject jsonData = null;
            int i;
            for(i=0; i<jsonArray.length(); i++)
            {
                jsonData = new JSONObject(jsonArray.getString(i));
                if(jsonData.getString("image_url")!="null")   break;  //imgae url 있으면 리턴
            }
            Log.v("current idx: ",String.valueOf(i));
          //  BitCoinDatas.setPredictionData(jsonData.getInt("label_0"));
            BitCoinDatas.setArticleData(jsonData.getString("name"), jsonData.getString("url"),
                                        jsonData.getString("image_url"),jsonData.getString("description") );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getPredictionData()
    {
        String predictionDatas = new String();
        try {
            //http://13.125.254.128:3000/api/prediction 은 예측값을 받아온다.
            URL url = new URL("http://13.125.254.128:3000/api/prediction");
            predictionDatas = getDatasFromServer(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonData = new JSONObject(predictionDatas);
            BitCoinDatas.setPredictionData(jsonData.getInt("label_0"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


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

        //Log.v("date", currentDateandTime+" "+pastDateandTime );

        try {
            //http://13.125.254.128:3000/api/currency/2018-06-13T00:00:00/2018-06-20T10:00:00
            //7일전부터 오늘까지의 5분단위 data를 모두 받아온다.
            URL url = new URL("http://13.125.254.128:3000/api/currency/"+pastDateandTime+"/"+currentDateandTime);
            graphDatas = getDatasFromServer(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(graphDatas);
            Log.v("len", String.valueOf(jsonArray.length()));
            for(int i=0; i<jsonArray.length(); i++)
            {
                String ele = jsonArray.getString(i);
                JSONObject json_ele = new JSONObject(ele);
                BitCoinDatas.setGraphData(i, json_ele.getString("date"), json_ele.getString("time"), json_ele.getInt("price_krw"));
            }

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
                read = in.read(buffer, 0, 4096);
                datas = datas.concat(temp_datas);
            }

        } finally {
            urlConnection.disconnect();
        }
        return datas;

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        getGraphData();
        getPredictionData();
        getArticleData();
        return null;
    }
}
