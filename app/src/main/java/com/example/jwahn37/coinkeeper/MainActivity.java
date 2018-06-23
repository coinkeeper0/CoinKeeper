package com.example.jwahn37.coinkeeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import com.example.jwahn37.coinkeeper.datas.BitCoinDatas;
import com.example.jwahn37.coinkeeper.managers.HTTPManager;
import com.example.jwahn37.coinkeeper.managers.UIManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yalantis.phoenix.PullToRefreshView;
import com.example.jwahn37.coinkeeper.datas.StaticDatas;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
//test
//test33
public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    PullToRefreshView mPullToRefreshView;
    Toolbar toolbar;
    String test = "PREDICCTION";
    ListviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
       /* Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
       */
        /*최초 앱실행서 data를 받안온다.*/
        //HTTPManager httpManager = new HTTPManager();
        //httpManager.execute();

        /*이를 그려줘야 함*/



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
                HTTPManager httpManager = new HTTPManager();
                httpManager.execute();

                /*이를 그려줘야 함*/


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
                view_weather.setImageResource(BitCoinDatas.getPredWeather());
                TextView view_date = (TextView) convertView.findViewById(R.id.pred_date);
                view_date.setText(BitCoinDatas.getPredDate());
                TextView view_situ = (TextView) convertView.findViewById(R.id.pred_situation);
                view_situ.setText(BitCoinDatas.getPredSitu());

//                Log.v("pred date?:", BitCoinDatas.getPredDate());

            }
//hnello
            if (position == StaticDatas.LAYOUT_GPAPH) {
                //여기가 그래프 관련 화면 담당하는 모듈입니다 :)

                convertView = inflater.inflate(R.layout.graph, parent, false);
//                ImageView icon = (ImageView) convertView.findViewById(R.id.imageview);
//                icon.setImageResource(R.drawable.icon_2);
                //TextView name = (TextView) convertView.findViewById(R.id.textview);
                //name.setText("GRAPH");

                LineChart chart = (LineChart) convertView.findViewById(R.id.lineChart);

                chart.getDescription().setEnabled(false);

                ArrayList<Entry> entries = new ArrayList<Entry>();
                for(int i = 0; i < 7*24*12; i++){
                    entries.add(new Entry(i, BitCoinDatas.getGraphPrice()[i]));
                }

                LineDataSet dataSet = new LineDataSet(entries, "Bitcoin Price Last 7 Days");
                dataSet.setLineWidth(1);
                dataSet.setDrawCircles(false);
                dataSet.setColor(Color.parseColor("#FF8d0c4d"));
                dataSet.setDrawHorizontalHighlightIndicator(false);
                dataSet.setDrawHighlightIndicators(false);
                dataSet.setDrawValues(false);

                LineData lineData = new LineData(dataSet);

                XAxis xAxis = chart.getXAxis();
                xAxis.setEnabled(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                //xAxis.setTextColor(Color.BLACK);
                xAxis.enableGridDashedLine(8, 24, 0);
                //xAxis.setGranularityEnabled(true);
                //xAxis.setGranularity(24*12);

                YAxis yLAxis = chart.getAxisLeft();
                //yLAxis.setTextColor(Color.BLACK);

                YAxis yRAxis = chart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                chart.setDoubleTapToZoomEnabled(false);
                chart.setDrawGridBackground(false);

                chart.setData(lineData);

                chart.invalidate();
            }

            if (position == StaticDatas.LAYOUT_ARTICLE) {
                convertView = inflater.inflate(R.layout.article, parent, false);
                //ImageView icon = (ImageView) convertView.findViewById(R.id.imageview);
               // icon.setImageResource(R.drawable.icon_3);
               // TextView name = (TextView) convertView.findViewById(R.id.textview);
                //name.setText("ARTICLE");

                TextView article_title = (TextView) convertView.findViewById(R.id.article_title);
                //TextView article_description = (TextView) convertView.findViewById(R.id.particle_description);

                article_title.setText(BitCoinDatas.getArticle_title());
                //article_description.setText(BitCoinDatas.getArticle_description());

                ImageView article_img = (ImageView) convertView.findViewById(R.id.article_img);

                //imgae url 가져오기
                new DownloadImageTask(article_img)
                        //.execute("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
                        .execute(BitCoinDatas.getArticle_imgURL());

                //LinearLayout article_layout = (LinearLayout) convertView.findViewById(R.id.article_layout);
                RelativeLayout article_layout = (RelativeLayout) convertView.findViewById(R.id.article_layout);
                article_layout.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        //Log.v("article layout","clicked!");
                        Uri uri = Uri.parse(BitCoinDatas.getArticle_URL()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    /*
    class ArticleImageURL extends AsyncTask{

        String imgURL;
        View convertView;
        Bitmap bmp;

        ArticleImageURL(View view,String url)
        {
            imgURL = url;
            convertView = view;
        }

        public Bitmap getBmp() {
            return bmp;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            URL img_url = null;
            try {
                Log.d("bmp??", imgURL);
                img_url = new URL(imgURL);
                bmp = BitmapFactory.decodeStream(img_url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    */
}






