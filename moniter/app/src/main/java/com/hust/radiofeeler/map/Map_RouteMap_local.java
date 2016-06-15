package com.hust.radiofeeler.map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapRadio;
import com.hust.radiofeeler.bean2server.MapRoute;
import com.hust.radiofeeler.tab1.Map_Heat_Result;
import com.hust.radiofeeler.tab1.Map_Route_local_Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 路径图
 * 
 */
public class Map_RouteMap_local extends Activity {

    // 地图相关
    private MapView mRoute_MapView;
    private BaiduMap mRoute_BaiduMap;

    private static final LatLng GEO_WUHAN = new LatLng(30.5170810000, 114.4245410000);
    private static final String TABLE_NAME = "localFile";
    private static final String DB_NAME = "sendFileDatabase.db";//数据库名称

    private Marker mMarkerA;
    private LatLng first_latLng, end_latlng;
    //private Polyline mColorfulPolyline;
    private static List<LatLng> points = new ArrayList<LatLng>();
    private MapRoute route_setting_data;
    private Timer mTimer;
    private Button mBtn;
    private BitmapDescriptor stationMarker = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);
    private Cursor c=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDKInitializer.initialize(this);
        setContentView(R.layout.map_local_route);
        // 初始化地图
        mRoute_MapView = (MapView) findViewById(R.id.bMapView_local);
        mRoute_BaiduMap = mRoute_MapView.getMap();
        // UI初始化
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);
        mRoute_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(10);
        mRoute_BaiduMap.setMapStatus(msu);

        mBtn = (Button)findViewById(R.id.btn_local_mode);
        route_setting_data = getIntent().getParcelableExtra(Map_Route_local_Result.ROUTE_KEY);
        if (null == route_setting_data) {
            return;
        }
        if (route_setting_data != null) {
            // 将消息展现出来。
            //addfaultData();
            mTimer = new Timer();
            // start timer task
            setTimerTask();
        }
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtn.getText() == "卫星")
                {
                    mRoute_BaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    mBtn.setText("普通");
                }
                else
                {
                    mRoute_BaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    mBtn.setText("卫星");
                }

            }
        });

    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 1000, 500/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }
    private void addfaultData() {

         SQLiteDatabase db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式
        cv.put("fileName", "2016-05-20-14-22-23-333-12-fine.pwr");
        cv.put("start", 1150);
        cv.put("end", 1250);
        cv.put("location", "E114.856412N30.567412");
        cv.put("isShow", 0);
        cv.put("isChanged", 0);
        cv.put("upload", 0);
        cv.put("times", 0);

        //调用insert方法，将数据插入数据库
        db.insert(TABLE_NAME, null, cv);
        cv.clear();
        cv.put("fileName", "2016-05-20-14-23-11-333-12-fine.pwr");
        cv.put("start", 1150);
        cv.put("end", 1250);
        cv.put("location", "E114.812612N30.533412");
        cv.put("isShow", 0);
        cv.put("isChanged", 0);
        cv.put("upload", 0);
        cv.put("times", 0);

        //调用insert方法，将数据插入数据库
        db.insert(TABLE_NAME, null, cv);
        //调用insert方法，将数据插入数据库
        cv.clear();
        cv.put("fileName", "2016-05-20-14-24-11-333-12-fine.pwr");
        cv.put("start", 1150);
        cv.put("end", 1250);
        cv.put("location", "E114.880612N30.523412");
        cv.put("isShow", 0);
        cv.put("isChanged", 0);
        cv.put("upload", 00);
        cv.put("times", 0);

        //调用insert方法，将数据插入数据库
        db.insert(TABLE_NAME, null, cv);
        cv.clear();
        cv.put("fileName", "2016-05-21-14-24-11-333-12-fine.pwr");
        cv.put("start", 1150);
        cv.put("end", 1250);
        cv.put("location", "E114.855112N30.411412");
        cv.put("isShow", 0);
        cv.put("isChanged", 0);
        cv.put("upload", 00);
        cv.put("times", 0);

        //调用insert方法，将数据插入数据库
        db.insert(TABLE_NAME, null, cv);
        cv.clear();
        cv.put("fileName", "2016-05-23-14-25-11-333-12-fine.pwr");
        cv.put("start", 1150);
        cv.put("end", 1250);
        cv.put("location", "E114.305612N30.111412");
        cv.put("isShow", 0);
        cv.put("isChanged", 0);
        cv.put("upload", 00);
        cv.put("times", 0);

        //调用insert方法，将数据插入数据库
        db.insert(TABLE_NAME, null, cv);
    }



    public void clearClick() {
        // 清除所有图层
        mRoute_MapView.getMap().clear();
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
                    DrawLocalRouteData(route_setting_data.getCentralFreq(),
                            route_setting_data.getBand(), route_setting_data.getStartTime());
                    //RouteInfoList.add(RouteInfoList_update);

                    break;
                default:
                    break;
            }
        }
    };

    private void DrawLocalRouteData(int CenterFreq, int band, byte[] StartTime) {
        String[] TimeValue;
        int month, year, day, hour, minute;
        String longitudeStyle, latitudeStyle;
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        float longitude, latitude;
        //ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        //ArrayList<MapRadioPointInfo> mapRadioPointInfoList = new ArrayList<MapRadioPointInfo>();

        //得到开始时刻字符串，起始频率，终止频率
        String selectionStartTime = String.valueOf(StartTime[0] - 48) + String.valueOf(StartTime[1] - 48) + String.valueOf(StartTime[2] - 48)
                + String.valueOf(StartTime[3] - 48) + '-' + String.valueOf(StartTime[7] - 48) + String.valueOf(StartTime[8] - 48) + '-' + String.valueOf(StartTime[12] - 48) + String.valueOf(StartTime[13] - 48)
                + '-' + String.valueOf(StartTime[18] - 48) + String.valueOf(StartTime[19] - 48) + '-' + String.valueOf(StartTime[21] - 48) + String.valueOf(StartTime[22] - 48);
        String selectionStartFreq = String.valueOf(CenterFreq - band);
        String selectionEndFreq = String.valueOf(CenterFreq + band);
        //设置查询参数
        String[] SelecctArgs = {selectionStartTime,"2"};
        String[] columns = new String[]{"fileName", "location"};


        //String query = "select * from POWERSPECTRUM3 where TERMINALID= ? AND DATETIME BETWEEN ? AND ? AND STARTFREQ<=? AND ENDFREQ>=? ORDER BY DATETIME ASC";


         c = db.query(TABLE_NAME, columns, "fileName>?  and isShow <? ", SelecctArgs, null, null, "fileName" + " ASC",null);
        //判断cursor不为空 这个很重要

        if (c != null) {
            // 循环遍历cursor
            if (c.moveToFirst() == false) { //为空的Cursor
                return;
            }
            //String[] columnStr = c.getColumnNames();



            do {
                Map<String, Object> data = new HashMap<>();
                String Time = c.getString(c.getColumnIndex("fileName"));
                String location = c.getString(c.getColumnIndex("location"));

//                longitude = Float.parseFloat(location.substring(1, 11));
//                latitude = Float.parseFloat(location.substring(13, 22));
                //经纬度
                String[] locationValue=location.split(",");
                int len=locationValue[0].length();
                if(len==1)
                    longitude=0;
                else
                    longitude =Float.parseFloat(locationValue[0].substring(1, len));
                int len2=locationValue[1].length();
                if(len2==1)
                    latitude=0;
                else
                    latitude =Float.parseFloat(locationValue[1].substring(1, len2));

                if (c.isFirst()) {
                    //MarkerOptions ooA = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(stationMarker);
                    //mMarkerA = (Marker) (mRoute_BaiduMap.addOverlay(ooA));
                    first_latLng = new LatLng(latitude+Constants.LAT_OFFSET, longitude+Constants.LON_OFFSET);
                    end_latlng = new LatLng(latitude+Constants.LAT_OFFSET, longitude+Constants.LON_OFFSET);
                } else {
                    first_latLng = new LatLng(end_latlng.latitude+Constants.LAT_OFFSET, end_latlng.longitude+Constants.LON_OFFSET);
                   end_latlng = new LatLng(latitude+Constants.LAT_OFFSET, longitude+Constants.LON_OFFSET);
            }
//                Log.d("first_latLng",String.valueOf(first_latLng.latitude) +String.valueOf(first_latLng.longitude));
//                Log.d("end_latlng",String.valueOf(end_latlng.latitude) +String.valueOf(end_latlng.longitude));
//                points.add(first_latLng);
//                points.add(end_latlng);

//                OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
//                        .color(0xAAFF0000).points(points);
//                mColorfulPolyline = (Polyline) mRoute_BaiduMap.addOverlay(ooPolyline1);

                OverlayOptions options = new DotOptions().center(new LatLng(latitude+Constants.LAT_OFFSET,longitude+Constants.LON_OFFSET)).color(0xAAFF0000).radius(10);
                //points.clear();
                mRoute_BaiduMap.addOverlay(options);
                ContentValues cvv = new ContentValues();//实例化ContentValues
                cvv.put("isShow",2);//添加要更改的字段及内容
                String whereClause = "fileName=?";//修改条件
                String[] whereArgs = {Time};//修改条件的参数
                db.update(TABLE_NAME, cvv, whereClause, whereArgs);//执行修改
            } while (c.moveToNext());
        }
        c.close();

    }

    @Override
    protected void onPause() {
        mRoute_MapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mRoute_MapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        if(c!=null)
            c.close();
        if(mRoute_MapView!=null)
            mRoute_MapView.onDestroy();
        super.onDestroy();
    }


}