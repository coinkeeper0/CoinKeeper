package com.example.jwahn37.coinkeeper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jwahn37.coinkeeper.managers.HTTPManager;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);
        new LaunchTask().execute();

    }

    private class LaunchTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Intent intent;
            intent = new Intent(LaunchActivity.this, MainActivity.class);
            //  intent.putExtra(StaticDatas.STATUS_CODE, StaticDatas.STATUS_USER_REGISTER);
            /*최초 앱실행서 data를 받안온다.*/
            HTTPManager httpManager = new HTTPManager();
            //httpManager.execute();
            httpManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



            Log.d("test", "execute");

            SystemClock.sleep(1500);

            startActivity(intent);
            finish();
            return null;
        }

    }
}
