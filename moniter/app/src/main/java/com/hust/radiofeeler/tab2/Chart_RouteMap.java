package com.hust.radiofeeler.tab2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.pickerview.TimePickerView;

import com.hust.radiofeeler.Database.DatabaseHelper;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapRoute;
import com.hust.radiofeeler.compute.ComputePara;
import com.hust.radiofeeler.tab1.Map_Route_local_Result;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 路径图
 */
public class Chart_RouteMap extends Activity {

    // 地图相关
    private MapView mRoute_MapView;
    private BaiduMap mRoute_BaiduMap;
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    TimePickerView pvTime;
    private ComputePara computePara = new ComputePara();

    private LatLng Location_latlng = new LatLng(30.5170810000, 114.4245410000);
    private static final String TABLE_NAME = "localFile";
    private static final String DB_NAME = "sendFileDatabase.db";//数据库名称

    //private Polyline mColorfulPolyline;
    private List<LatLng> points = new ArrayList<LatLng>();
    private MapRoute route_setting_data;
    private Timer mTimer;
    private Button mBtn;
    private String time = null;
    private String enterTime = null;
    private BitmapDescriptor stationMarker = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);
    private Cursor c = null;
    private boolean isStartCurrentRoute = false;
    private Polyline mPolyline, mColorfulPolyline;
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDKInitializer.initialize(this);
        dbHelper = DatabaseHelper.getInstance(this);//单例模式
        db = dbHelper.getWritableDatabase();
        setContentView(R.layout.chart_routemap);
        time = getTimeSec(0);
       enterTime = getTimeSec(0);

        Log.d("Chart", time);

        MyTopBar topBar = (MyTopBar) findViewById(R.id.topbar_chart);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Chart_RouteMap.this.finish();
            }

            @Override
            public void rightclick() {

                showDialog_Save();
            }
        });
        mRoute_MapView = (MapView) findViewById(R.id.bMapView_local);
        mRoute_BaiduMap = mRoute_MapView.getMap();
        startMyLoc();
        // 初始化地图

        // UI初始化
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(Location_latlng);
        mRoute_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15);
        mRoute_BaiduMap.setMapStatus(msu);

        mBtn = (Button) findViewById(R.id.btn_local_mode);
        if (Constants.time != null)
            DrawBeforeRoute(Constants.time);//近来就画历史

        Log.d("Chart", Constants.time+"");

        if (mTimer == null) {
            mTimer = new Timer();
            // start timer task
            setTimerTask();
        }


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtn.getText() == "卫星") {
                    mRoute_BaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    mBtn.setText("普通");
                } else {
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
        }, 500, 50/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
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
                    DrawLocalRouteData();
                    break;
                default:
                    break;
            }
        }
    };

//    private String getCurrentTime(int m) {
//        //得到开始时刻
//        int month, year, day, hour, minute, sec;
//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        hour = calendar.get(Calendar.HOUR_OF_DAY);
//        minute = calendar.get(Calendar.MINUTE);
//        if (m <= minute)
//            minute = minute - m;
//        else {
//            if (m < 60) {
//                hour = hour - 1;//这是个bug
//                minute = 60 + minute - m;
//            } else {
//                int i = m % 60;
//                int j = m / 60;
//                if (i > minute) {
//                    j++;
//                }
//                hour = hour - j;
//                minute = j * 60 + minute - m;
//            }
//        }
//        sec = calendar.get(Calendar.SECOND);
//        String Syear = String.valueOf(year);
//        String Smonth = String.valueOf(month < 10 ? "0" + month : month);
//        String sday = String.valueOf(day < 10 ? "0" + day : day);
//
//        String Shour = String.valueOf(hour < 10 ? "0" + hour : hour);
//        String Smin = String.valueOf(minute < 10 ? "0" + minute : minute);
//        String SEC = String.valueOf(sec < 10 ? "0" + sec : sec);
//
//        String s = Syear + "-" + Smonth + "-" + sday + "-" + Shour + "-" + Smin + "-" + SEC;
//        return s;
//    }
    private String getTimeSec(int m) {
        //得到开始时刻
        Date date =new Date();
        long sec=date.getTime()/1000;
        if(m>0)
            sec=sec-m*60;
       return String.valueOf(sec);
    }


    private void DrawLocalRouteData() {
        String[] TimeValue;
        String longitudeStyle, latitudeStyle;
        //SQLiteDatabase db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        float longitude, latitude;
        //设置查询参数
        String[] SelecctArgs = {enterTime, "2"};
        String[] columns = new String[]{"fileName", "location"};
        LatLng before_point, next_point;
        if (db == null)
            return;
        try {
            c = db.query(TABLE_NAME, null, "fileTime>?  and isShow <? ", SelecctArgs, null, null, "fileName" + " ASC", null);
            //判断cursor不为空 这个很重要
            if (c == null)
                return;

            if (c != null) {
                // 循环遍历cursor
                if (c.moveToFirst() == false) { //为空的Cursor
                    return;
                }

                do {
                    Map<String, Object> data = new HashMap<>();
                    String Time = c.getString(c.getColumnIndex("fileName"));
                    String location = c.getString(c.getColumnIndex("location"));
                    //经纬度
                    String[] locationValue = location.split(",");
                    int len = locationValue[0].length();
                    if (len == 1)
                        longitude = 0;
                    else
                        longitude = Float.parseFloat(locationValue[0].substring(1, len));
                    int len2 = locationValue[1].length();
                    if (len2 == 1)
                        latitude = 0;
                    else
                        latitude = Float.parseFloat(locationValue[1].substring(1, len2));

//                if (c.isFirst()) {
//                    //MarkerOptions ooA = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(stationMarker);
//                    //mMarkerA = (Marker) (mRoute_BaiduMap.addOverlay(ooA));
//                    first_latLng = new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET);
//                    end_latlng = new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET);
//                } else {
//                   // if (DistanceUtil.getDistance(first_latLng, new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET)) > 5)
//                    {
//                        first_latLng = new LatLng(end_latlng.latitude + Constants.LAT_OFFSET, end_latlng.longitude + Constants.LON_OFFSET);
//                        end_latlng = new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET);
//                        Log.d("first_latLng",String.valueOf(first_latLng.latitude) +String.valueOf(first_latLng.longitude));
//                        Log.d("end_latlng",String.valueOf(end_latlng.latitude) +String.valueOf(end_latlng.longitude));
//                        points.add(first_latLng);
//                        points.add(end_latlng);
//
//                        OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
//                                .color(0xAAFF0000).points(points);
//                          mRoute_BaiduMap.addOverlay(ooPolyline1);
//                        points.clear();
//                    }

                    //   }
//               if (points.size() == 0)
//                   points.add(new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET));
//               points.add(new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET));
//               Log.d("first_latLng", String.valueOf(latitude) + "," + String.valueOf(longitude));

                    OverlayOptions options = new DotOptions().center(new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET)).color(0xAAFF0000).radius(16);
                    //points.clear();
                    mRoute_BaiduMap.addOverlay(options);
                    ContentValues cvv = new ContentValues();//实例化ContentValues
                    cvv.put("isShow", 2);//添加要更改的字段及内容
                    String whereClause = "fileName=?";//修改条件
                    String[] whereArgs = {Time};//修改条件的参数
                    db.update(TABLE_NAME, cvv, whereClause, whereArgs);//执行修改
                } while (c.moveToNext());
            }
//       OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
//               .color(0xAAFF0000).points(points);
//       mRoute_BaiduMap.addOverlay(ooPolyline1);
//       Log.d("first draw", "route");
//       points.clear();
            c.close();
        } catch (Exception e) {

        }

    }


    public void startMyLoc() {
        // 开启定位图层
        mRoute_BaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(Chart_RouteMap.this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {


            //Log.d("",location.getLatitude()+","+location.getLongitude());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mRoute_BaiduMap == null) {
                return;
            }
            Log.e("getLocType", String.valueOf(location.getLocType()));
            Log.e("LATLNG", String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            Location_latlng = new LatLng(location.getLatitude(),
                    location.getLongitude());

//            if (isFirstLoc) {
//                isFirstLoc = false;
//
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(Location_latlng).zoom(16.0f);
//                m_BaiduMap.animateMapStatus(M apStatusUpdateFactory.newMapStatus(builder.build()));
            //initOverlay(ll);
            //m_BaiduMap.setOnMapStatusChangeListener(statusListener);
            //m_BaiduMap.setOnMarkerClickListener(markerListener);
        }
    }

    public void onDestroy() {
        //LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver);
        // 退出时销毁定位
        mTimer.cancel();
        if (c != null)
            c.close();

        mLocClient.stop();
        // 关闭定位图层
        mRoute_BaiduMap.setMyLocationEnabled(false);
        if (mRoute_MapView != null)
            mRoute_MapView.onDestroy();
        mRoute_MapView = null;
        // 取消监听 SDK 广播
        //getActivity().unregisterReceiver(mReceiver);
        //stationsAsyncTask.cancel(true);
        super.onDestroy();
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


    private void showDialog_Save() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.routemap_setting, null);
        final EditText EditText01 = (EditText) view.findViewById(R.id.edit1);
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setTitle("请输入路径图查看的起始时间：");
        bulider.setIcon(R.drawable.ditu2);

        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!EditText01.getText().toString().equals("")) {
                    int m = (int) Double.parseDouble(EditText01.getText().toString());
                    //time = getCurrentTime(m);
                    time=getTimeSec(m);
                    Log.d("Chart", "设置：" + time);

                }
                Toast.makeText(Chart_RouteMap.this, "设置成功", Toast.LENGTH_SHORT).show();
                if (mTimer != null)
                    mTimer.cancel();
                clearClick();
                DrawBeforeRoute(time);

                mTimer = new Timer();
                setTimerTask();


            }
        });

        bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Chart_RouteMap.this, "设置失败", Toast.LENGTH_SHORT).show();
            }
        });
        bulider.setView(view);
        AlertDialog dialog = bulider.create();
        dialog.show();


    }

    private void DrawBeforeRoute(String stime) {

        float longitude, latitude;
        //设置查询参数
//        String ss=stime+"-"+String.format("%d-%d-%s.%s", 0, Constants.ID, "fine", "pwr");//需要改进
//        if(Constants.filetail!=null)
//           ss=stime+"-"+Constants.filetail;
        String[] SelecctArgs = {stime};
        String[] columns = new String[]{"fileName", "location"};
        List<LatLng> points = new ArrayList<LatLng>();
        LatLng before_point, next_point;
        if (db == null)
            return;

        before_point = new LatLng(30.5, 114.3);
        try {
//            c = db.rawQuery("SELECT * FROM localFile WHERE fileName > ?", SelecctArgs);
            c = db.query(TABLE_NAME, null, "fileTime>?  ", SelecctArgs, null, null, "fileName" + " ASC", null);
            //判断cursor不为空 这个很重要
            if (c == null)
                return;


            if (c != null) {
                // 循环遍历cursor
                if (c.moveToFirst() == false) { //为空的Cursor
                    return;
                }
                do {
                    String location = c.getString(c.getColumnIndex("location"));

                    //经纬度
                    String Time = c.getString(c.getColumnIndex("fileName"));
                    String[] locationValue = location.split(",");
                    int len = locationValue[0].length();
                    if (len == 1)
                        longitude = 0;
                    else
                        longitude = Float.parseFloat(locationValue[0].substring(1, len));
                    int len2 = locationValue[1].length();
                    if (len2 == 1)
                        latitude = 0;
                    else
                        latitude = Float.parseFloat(locationValue[1].substring(1, len2));

                    if (c.isFirst()) {
                        //MarkerOptions ooA = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(stationMarker);
                        //mMarkerA = (Marker) (mRoute_BaiduMap.addOverlay(ooA));
                        before_point = new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET);
                        //next_point = new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET);
                        points.add(before_point);
                    } else {
                        if (DistanceUtil.getDistance(before_point, new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET)) > 15) {
                            next_point = new LatLng(latitude + Constants.LAT_OFFSET, longitude + Constants.LON_OFFSET);
                            points.add(next_point);
                            before_point = next_point;
                        }

                    }
                    //points.add(new LatLng(+ Constants.LAT_OFFSET,longitude+ Constants.LON_OFFSET));
                } while (c.moveToNext());
            }
            OverlayOptions ooPolyline = new PolylineOptions().width(15)
                    .color(0xAAFF0000).points(points);

            mPolyline = (Polyline) mRoute_BaiduMap.addOverlay(ooPolyline);
            //mPolyline.setDottedLine(dottedLine.isChecked());
            Log.d("firstbeforedraw", "route");
            c.close();
        }catch(Exception e){

        }


    }
//
}