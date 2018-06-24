package com.example.jwahn37.coinkeeper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jwahn37.coinkeeper.datas.StaticDatas;
import com.example.jwahn37.coinkeeper.managers.HTTPManager;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);
        new LaunchTask().execute();

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = String.valueOf(StaticDatas.PUSH_NAME);//getString(StaticDatas.PUSH_NAME);
            String description = StaticDatas.PUSH_DESCRIPTION;//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(StaticDatas.PUSH_CAHNNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private class LaunchTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            createNotificationChannel();

        }

        @Override
        protected Void doInBackground(Integer... params) {
            HTTPManager httpManager = new HTTPManager();
            // httpManager.execute();
            httpManager.getBitcoinDatas();
            //httpManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
           // String str_result= new RunInBackGround().execute().get();
            Log.d("test", "execute");
           // SystemClock.sleep(2000);
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           // Intent intent;
            //  intent.putExtra(StaticDatas.STATUS_CODE, StaticDatas.STATUS_USER_REGISTER);
            /*최초 앱실행서 data를 받안온다.*/


        }
    }
}
