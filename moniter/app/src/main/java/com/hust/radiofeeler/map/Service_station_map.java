package com.hust.radiofeeler.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.hust.radiofeeler.R;


/**
 * Created by Administrator on 2015-11-18.
 */

public class Service_station_map extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);

    /**
     * 地图控件
     */
    private MapView mStation_MapView = null;
    /**
     * 地图实例
     */
    private BaiduMap mStation_BaiduMap = null;

    private int count = 0;

    private Marker mMarkerA;
    private Marker mMarkerB;
    private CheckBox animationBox = null;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_staitonmap);
        mStation_MapView = (MapView) findViewById(R.id.StationMapView);
        mStation_BaiduMap = mStation_MapView.getMap();
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);

        mStation_BaiduMap.setMapStatus(u1);
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


    }

}
