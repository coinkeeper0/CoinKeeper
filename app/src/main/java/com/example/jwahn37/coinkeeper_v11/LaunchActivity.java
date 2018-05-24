package com.example.jwahn37.coinkeeper_v11;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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


            Log.d("test", "execute");

            SystemClock.sleep(1500);
            startActivity(intent);
            finish();
            return null;
        }

    }
}
