package com.hust.radiofeeler.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.tab1.Stations_currentResult;
import com.hust.radiofeeler.bean2server.StationCurrentReply;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Stations_CurrentResult_map extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private MapView mStation_MapView = null;
    private BaiduMap mStation_BaiduMap = null;

    private int count = 0;//去掉放大缩小标记

    private static StationCurrentReply CurrentStationMapData = new StationCurrentReply();
    private InfoWindow mInfoWindow;
    private static TextView dialog_text;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map_staitonmap);
        mStation_MapView = (MapView) findViewById(R.id.StationMapView);
        mStation_BaiduMap = mStation_MapView.getMap();

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12);
        mStation_BaiduMap.setMapStatus(msu);
        //去掉放大缩小的标识
        count = mStation_MapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mStation_MapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }

        CurrentStationMapData = getIntent().getParcelableExtra(Stations_currentResult.CURRENTSTATION_KEY);
        if (null == CurrentStationMapData) {
            return;
        }

        if (CurrentStationMapData != null) {
            // 将消息展现出来。
            MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(CurrentStationMapData.getLatitude()+Constants.LAT_OFFSET,CurrentStationMapData.getLongitude()+Constants.LON_OFFSET));

            mStation_BaiduMap.setMapStatus(u1);
            Log.d("mapdataget", "size is " + String.valueOf(CurrentStationMapData.getaBand()));
            try {
                DrawCurrentRadioMap(CurrentStationMapData);
            }catch (Exception e){

            }
        }

        mStation_BaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.location_tips);
                InfoWindow.OnInfoWindowClickListener listener = null;
                //if (marker == mMarkerA) {
                button.setText("归属单位：  "+String.valueOf(CurrentStationMapData.getASICIIId())+'\n'+"经纬度："+CurrentStationMapData.getLatitudeStyle()+String.valueOf(CurrentStationMapData.getLatitude())
                        +" "+CurrentStationMapData.getLongtitudeStyle()+String.valueOf(CurrentStationMapData.getLongitude())+'\n'+"功率值："
                        +CurrentStationMapData.getEqualPower()  +'\n'+"调制方式：  "+CurrentStationMapData.getModem() +'\n'+"调制参数：  "+String.valueOf(CurrentStationMapData.getModemPara()));
                listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        //ShowDialog();
                        mStation_BaiduMap.hideInfoWindow();
                    }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                mStation_BaiduMap.showInfoWindow(mInfoWindow);

                return true;
            }
        });
    }

    public void clearClick() {
        // 清除所有图层
        mStation_MapView.getMap().clear();
    }

    //设置显示的态势图的频率范围

    @Override
    protected void onPause() {
        mStation_MapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mStation_MapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mStation_MapView.onDestroy();
        super.onDestroy();
    }
    private void  DrawCurrentRadioMap(StationCurrentReply CurrentReply) {
        double Power_cnt,Power_circle;
        double distance;
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(CurrentReply.getLatitude()+Constants.LAT_OFFSET, CurrentReply.getLongitude()+Constants.LON_OFFSET));

        mStation_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mStation_BaiduMap.setMapStatus(msu);
        Log.e("abnormal", "enter this");
        Power_cnt = Math.floor(CurrentReply.getEqualPower());
        Log.e("abnormal","Power is "+String.valueOf(Math.floor(CurrentReply.getEqualPower())));

        BitmapDescriptor AbnormalMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.marker1);
        MarkerOptions oo1 = new MarkerOptions().position(new LatLng(CurrentReply.getLatitude()+Constants.LAT_OFFSET,CurrentReply.getLongitude()+Constants.LON_OFFSET)).icon(AbnormalMarker)
                .zIndex(0).period(10);
        oo1.animateType(MarkerOptions.MarkerAnimateType.grow);
        mStation_BaiduMap.addOverlay(oo1);

        for(int i = 1;i<9;i++)
        {

            LatLng llCircle = new LatLng(CurrentReply.getLatitude()+ Constants.LAT_OFFSET, CurrentReply.getLongitude()+Constants.LON_OFFSET);
            Power_circle = Power_cnt-5*i;
            distance = Math.sqrt(Math.pow(10,(Power_cnt-Power_circle)/(5*CurrentReply.getrPara())));
            Log.e("distance",String.valueOf(distance));
            OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
                    .center(llCircle).stroke(new Stroke(5, 0xAA000000))
                    .radius((int) (distance * 1000));

            mStation_BaiduMap.addOverlay(ooCircle);

            LatLng llText = new LatLng((CurrentReply.getLatitude()+ Constants.LAT_OFFSET+distance/111+0.001), CurrentReply.getLongitude()+ Constants.LON_OFFSET);
            //构建文字Option对象，用于在地图上添加文字
            OverlayOptions textOption = new TextOptions()
                    .fontSize(30)
                    .fontColor(0xFFFF00FF)
                    .text("功率值" + String.valueOf(Power_circle) + "dBm")
                    .position(llText);
//在地图上添加该文字对象并显示
            mStation_BaiduMap.addOverlay(textOption);
        }

    }


}