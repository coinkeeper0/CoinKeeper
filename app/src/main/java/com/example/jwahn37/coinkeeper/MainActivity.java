package com.example.jwahn37.coinkeeper;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jwahn37.coinkeeper.managers.UIManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yalantis.phoenix.PullToRefreshView;
import com.example.jwahn37.coinkeeper.datas.StaticDatas;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    PullToRefreshView mPullToRefreshView;
    Toolbar toolbar;
    String test = "PREDICCTION";
    ListviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Coin Keeper");
        //git test

        //refresh view
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("mPullToRefreshView", "REFRESHstart!");

                //서버로부터 데이터 다시 받아와서 업데이트하자. 이곳에서 서버로부터 데이터를 요청후, view필드에 새로운 데이터삽입한다.
                //각 UI에 대한 클래스가 존재했으면 함.
                test="WORLD";

                //여기서 서버로부터 데이터를 받아온다.
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        Log.d("mPullToRefreshView", "REFRESH!");
                        adapter.notifyDataSetChanged(); //view 다시 업데이트해준다.
                    }
                }, StaticDatas.REFRESH_DELAY);

            }
        });

        //UI manager
        UIManager uiManager = new UIManager();
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListviewAdapter(this, uiManager);
        listView.setAdapter(adapter);
       // SystemClock.sleep(5000);
       // test="WORLD";
       // adapter.notifyDataSetChanged();

    }

    public class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int layout;
        private UIManager uiManager;

        public ListviewAdapter(Context context, UIManager uiManager) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.uiManager = uiManager;
        }

        @Override
        public int getCount() {
            return StaticDatas.NUM_LAYOUT;
        }

        @Override
        public String getItem(int position) {
            return uiManager.getName(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*여기서 layout관리하면 된다.*/
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.prediction, parent, false);
            }

            Log.d("dapterposition:", Integer.toString(position));


            if (position == StaticDatas.LAYOUT_PREDICTION) {

                convertView = inflater.inflate(R.layout.prediction, parent, false);
                ImageView view_weather = (ImageView) convertView.findViewById(R.id.pred_weather);
                view_weather.setImageResource(uiManager.getPredWeather());
                TextView view_date = (TextView) convertView.findViewById(R.id.pred_date);
                view_date.setText(uiManager.getPredDate());
                TextView view_situ = (TextView) convertView.findViewById(R.id.pred_situation);
                view_situ.setText(uiManager.getPredSitu());

            }

            if (position == StaticDatas.LAYOUT_GPAPH) {
                //여기가 그래프 관련 화면 담당하는 모듈입니다 :)

                convertView = inflater.inflate(R.layout.graph, parent, false);
//                ImageView icon = (ImageView) convertView.findViewById(R.id.imageview);
//                icon.setImageResource(R.drawable.icon_2);
                //TextView name = (TextView) convertView.findViewById(R.id.textview);
                //name.setText("GRAPH");

                LineChart chart = (LineChart) convertView.findViewById(R.id.lineChart);
                ArrayList<Entry> entries = new ArrayList<Entry>();
                entries.add(new Entry(0f, 100f));
                entries.add(new Entry(1f, 50f));
                entries.add(new Entry(2f, 150f));
                entries.add(new Entry(3f, 75f));
                LineDataSet dataSet = new LineDataSet(entries, "Price");
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate();
            }

            if (position == StaticDatas.LAYOUT_ARTICLE) {
                convertView = inflater.inflate(R.layout.article, parent, false);
                ImageView icon = (ImageView) convertView.findViewById(R.id.imageview);
               // icon.setImageResource(R.drawable.icon_3);
                TextView name = (TextView) convertView.findViewById(R.id.textview);
                name.setText("ARTICLE");
            }

            return convertView;
        }
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
       // return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "환경설정 버튼 클릭됨", Toast.LENGTH_LONG).show();
                SettingFragment settingFragment = new SettingFragment();
                settingFragment.show(getSupportFragmentManager(), "SETTING");

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
               // Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }




    }

}






