package com.hust.radiofeeler.tab1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapRadioPointInfo;
import com.hust.radiofeeler.bean2server.MapRoute;
import com.hust.radiofeeler.bean2server.MapRouteResult;
import com.hust.radiofeeler.map.Map_RouteMap_local;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/4/14.
 */
public class Map_Route_local_Result extends Activity {
    private TextView tv_data, tv_freq, tv_band;

    private ListView mlistView;
    private SimpleAdapter simpleAdapter;
    private MapRoute route_setting_data;
    private ArrayList<Map<String, Object>> RouteInfoList = new ArrayList<>();
    private ArrayList<Map<String, Object>> RouteInfoList_update = new ArrayList<>();
    private MapRouteResult map = new MapRouteResult();
    private byte[] startTime, endTime;
    private int default_height = 100, default_rPara = 2, default_Power = -20;

    private ArrayList<MapRadioPointInfo> mapRadioPointInfoList = new ArrayList<>();
    public final static String ROUTE_KEY = "com.example.administrator.testsliding.ROUTE";
    public final static String LOCAL_ROUTE_KEY = "com.example.administrator.testsliding.LOCALROUTE_data";
    private static final String DB_NAME = "sendFileDatabase.db";//
    // ���ݿ�����
    private static final String TABLE_NAME = "localFile";

    private Timer mTimer = new Timer();
    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_route_local_infolist);
        MyTopBar topBar = (MyTopBar) findViewById(R.id.topbar_RouteMapInfo_local);

        String testStr = "E115.1536N30.896547";
        InitSetting();
        route_setting_data = getIntent().getParcelableExtra(Map_Route_Setting.LOCAL_ROUTE_KEY);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Map_Route_local_Result.this.finish();
            }

            @Override
            public void rightclick() {

                Intent intent = new Intent(Map_Route_local_Result.this, Map_RouteMap_local.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ROUTE_KEY, route_setting_data);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        tv_data.setText("local");
        tv_freq.setText(String.valueOf(route_setting_data.getCentralFreq()));
        tv_band.setText(String.valueOf(route_setting_data.getBand()));
        startTime = route_setting_data.getStartTime();
        getLocalRouteData(route_setting_data.getCentralFreq(), route_setting_data.getBand(), route_setting_data.getStartTime());

        // init timer
        // mTimer = new Timer();
        // start timer task
        //setTimerTask();
        if (RouteInfoList != null) {
            simpleAdapter = new SimpleAdapter(Map_Route_local_Result.this, RouteInfoList,
                    R.layout.abnormal_frequency_item_local, new String[]{"item_time", "item_location"},
                    new int[]{R.id.seq_num_local, R.id.freq_local});
            mlistView.setAdapter(simpleAdapter);
        }
    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 1000, 3000/* ��ʾ1000����֮�ᣬÿ��1000�������һ�� */);
    }

    /**
     * do some action
     */
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    getLocalRouteData(route_setting_data.getCentralFreq(),
                            route_setting_data.getBand(), route_setting_data.getStartTime());
                    //RouteInfoList.add(RouteInfoList_update);
                    simpleAdapter.notifyDataSetChanged();

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        //RouteInfoList.clear();
        Log.d("map", "onPause()");
        super.onPause();
    }

    @Override
    public void onStart() {
        setTimerTask();
        Log.d("map", "setTimerTask()");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.d("map", "timer.cancel()");
        mTimer.cancel();


        if (c != null)
            c.close();
        super.onDestroy();
    }

    private void getLocalRouteData(int CenterFreq, int band, byte[] StartTime) {
        String[] TimeValue;
        int month, year, day, hour, minute;
        String longitudeStyle, latitudeStyle;
        String having = "isShow=0";
        float longitude, latitude;
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        ArrayList<MapRadioPointInfo> mapRadioPointInfoList = new ArrayList<MapRadioPointInfo>();

        //�õ���ʼʱ���ַ�������ʼƵ�ʣ���ֹƵ��
        String selectionStartTime = String.valueOf(StartTime[0] - 48) + String.valueOf(StartTime[1] - 48) + String.valueOf(StartTime[2] - 48)
                + String.valueOf(StartTime[3] - 48) + '-' + String.valueOf(StartTime[7] - 48) + String.valueOf(StartTime[8] - 48) + '-' + String.valueOf(StartTime[12] - 48) + String.valueOf(StartTime[13] - 48)
                + '-' + String.valueOf(StartTime[18] - 48) + String.valueOf(StartTime[19] - 48) + '-' + String.valueOf(StartTime[21] - 48) + String.valueOf(StartTime[22] - 48);
        String selectionStartFreq = String.valueOf(CenterFreq - band);
        String selectionEndFreq = String.valueOf(CenterFreq + band);
        //���ò�ѯ����
        String[] SelecctArgs = {selectionStartTime, "0"};//
        String[] columns = new String[]{"fileName", "location"};
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

//        ContentValues cv = new ContentValues();
//        //��ContentValues���������ݣ���-ֵ��ģʽ
//        cv.put("fileName", "2016-05-16-14-22-23-333-12-fine.pwr");
//        cv.put("start", 1150);
//        cv.put("end", 1250);
//        cv.put("location", "E114.856412N30.567412");
//        cv.put("isShow", 0);
//        cv.put("isChanged", 0);
//        cv.put("upload", 0);
//        cv.put("times", 0);
//
//        //����insert�����������ݲ������ݿ�
//        db.insert(TABLE_NAME, null, cv);
//        cv.clear();
//        cv.put("fileName", "2016-05-16-14-23-11-333-12-fine.pwr");
//        cv.put("start", 1150);
//        cv.put("end", 1250);
//        cv.put("location", "E114.855612N30.533412");
//        cv.put("isShow", 0);
//        cv.put("isChanged", 0);
//        cv.put("upload", 0);
//        cv.put("times", 0);
//
//        //����insert�����������ݲ������ݿ�
//        db.insert(TABLE_NAME, null, cv);
//        //����insert�����������ݲ������ݿ�
//        cv.clear();
//        cv.put("fileName", "2016-05-16-14-24-11-333-12-fine.pwr");
//        cv.put("start", 1150);
//        cv.put("end", 1250);
//        cv.put("location", "E114.801612N30.522412");
//        cv.put("isShow", 0);
//        cv.put("isChanged", 0);
//        cv.put("upload", 00);
//        cv.put("times", 0);
//
//        //����insert�����������ݲ������ݿ�
//        db.insert(TABLE_NAME, null, cv);
//        cv.clear();
//        cv.put("fileName", "2016-05-16-14-24-11-333-12-fine.pwr");
//        cv.put("start", 1150);
//        cv.put("end", 1250);
//        cv.put("location", "E114.875612N30.529412");
//        cv.put("isShow", 0);
//        cv.put("isChanged", 0);
//        cv.put("upload", 00);
//        cv.put("times", 0);
//
//        //����insert�����������ݲ������ݿ�
//        db.insert(TABLE_NAME, null, cv);
//        cv.clear();
//        cv.put("fileName", "2016-05-16-14-25-11-333-12-fine.pwr");
//        cv.put("start", 1150);
//        cv.put("end", 1250);
//        cv.put("location", "E114.115612N30.887412");
//        cv.put("isShow", 0);
//        cv.put("isChanged", 0);
//        cv.put("upload", 00);
//        cv.put("times", 0);
//
//        //����insert�����������ݲ������ݿ�
//        db.insert(TABLE_NAME, null, cv);
        //String query = "select * from POWERSPECTRUM3 where TERMINALID= ? AND DATETIME BETWEEN ? AND ? AND STARTFREQ<=? AND ENDFREQ>=? ORDER BY DATETIME ASC";
        //Cursor c = db.query(TABLE_NAME, columns, "fileName>? and start=? and end=? ", SelecctArgs, null, null, "fileName" + " ASC",null);
        c = db.query(TABLE_NAME, columns, "fileName>?  and isShow = ?", SelecctArgs, null, null, "fileName" + " ASC", null);
        //�ж�cursor��Ϊ�� �������Ҫ

        if (c != null) {
            // ѭ������cursor
            if (c.moveToFirst() == false) { //Ϊ�յ�Cursor
                return;
            }
            //String[] columnStr = c.getColumnNames();
            int i = 0;

            do {
                Map<String, Object> data = new HashMap<>();
                String Time = c.getString(c.getColumnIndex("fileName"));
                String location = c.getString(c.getColumnIndex("location"));
                TimeValue = Time.split("-");
                month = Integer.parseInt(TimeValue[1]);
                year = Integer.parseInt(TimeValue[0]);
                day = Integer.parseInt(TimeValue[2]);
                hour = Integer.parseInt(TimeValue[3]);
                minute = Integer.parseInt(TimeValue[4]);
                //经纬度
                String[] locationValue = location.split(",");
                longitudeStyle = locationValue[0].substring(0, 1);
                int len = locationValue[0].length();
                if (len == 1)
                    longitude = 0;
                else
                    longitude = Float.parseFloat(locationValue[0].substring(1, len));
                latitudeStyle = locationValue[1].substring(0, 1);
                int len2 = locationValue[1].length();
                if (len2 == 1)
                    latitude = 0;
                else
                    latitude = Float.parseFloat(locationValue[1].substring(1, len2));

                //Log.v("info", "�ļ��� " + name + "��γ��Ϊ " + hp);
                MapRadioPointInfo mMapRadioPointInfo = new MapRadioPointInfo(month, year, day, hour,
                        longitudeStyle, minute, longitude, latitudeStyle, latitude, default_height, default_Power, default_rPara);
                mapRadioPointInfoList.add(i, mMapRadioPointInfo);
                i++;
                data.put("item_time", TimeValue[0] + '-' + TimeValue[1] + '-' + TimeValue[2] + ' ' + TimeValue[3] + ':' + TimeValue[4] + ':' + TimeValue[5]);
                data.put("item_location", location);
                RouteInfoList.add(data);
                ContentValues cvv = new ContentValues();//ʵ����ContentValues
                cvv.put("isShow", 1);//���Ҫ���ĵ��ֶμ�����
                String whereClause = "fileName=?";//�޸�����
                String[] whereArgs = {Time};//�޸������Ĳ���
                db.update(TABLE_NAME, cvv, whereClause, whereArgs);//ִ���޸�
            } while (c.moveToNext());
            map.setMapRadioPointInfoList(mapRadioPointInfoList);
        }
        c.close();

    }


    private void InitSetting() {
        tv_data = (TextView) findViewById(R.id.tv_datafrom_local);
        tv_freq = (TextView) findViewById(R.id.tv_centralFreq_local);
        tv_band = (TextView) findViewById(R.id.tv_band_local);
        mlistView = (ListView) findViewById(R.id.listview_mapRoute_local);
        RouteInfoList = new ArrayList<>();
    }

}

