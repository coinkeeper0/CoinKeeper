package com.example.jwahn37.coinkeeper.managers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPManager extends AsyncTask {

    URL url;
    String datas;
    public void getDatasFromServer() throws IOException {

        //일단 예시URL 정훈이한테 받는데로 바꾸기 //
        //get method방식의 http 통신 코드
        url = new URL("http://crix-api-endpoint.upbit.com/v1/crix/candles/weeks?code=CRIX.UPBIT.BTC-STEEM");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            byte[] buffer = new byte[4096];
            int read = in.read(buffer, 0, 4096);
            while (read != -1) {
                byte[] tempData = new byte[read];
                System.arraycopy(buffer, 0, tempData, 0, read);
                String tempString = new String(tempData, "UTF-8");
                //publishProgress("낯선이 : " + tempString);
                read = in.read(buffer, 0, 4096);
                Log.v("here ", tempString);
            }
          //  Log.d("here","111");
            //byte[] b = new byte[4096];
            //in.read(b,0,4096);
            //datas = new String(b, "UTF-8");
            //Log.d(datas, "111");

            //readStream(in);
        } finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            getDatasFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
