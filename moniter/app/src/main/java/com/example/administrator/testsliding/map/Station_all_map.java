package com.example.administrator.testsliding.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.tab1.Station_AllResult;
import com.example.administrator.testsliding.bean2server.List_StationAll;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Station_all_map extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private MapView mTerminal_MapView = null;
    private BaiduMap mTerminal_BaiduMap = null;
    private ArrayList<List_StationAll> mlist;
    private ArrayList<Marker> Markerlist;
    private int count = 0;//去掉放大缩小标记
    private BitmapDescriptor stationMarker1;
    private BitmapDescriptor stationMarker2;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_sll_map);
        mTerminal_MapView = (MapView) findViewById(R.id.TerminalMapView);
        mTerminal_BaiduMap = mTerminal_MapView.getMap();

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12);
        mTerminal_BaiduMap.setMapStatus(msu);
        //去掉放大缩小的标识
        count = mTerminal_MapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mTerminal_MapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }


        LinearLayout linearLayout;
        linearLayout = (LinearLayout)findViewById(R.id.TerminalLayout);
        linearLayout.setVisibility(View.INVISIBLE);
        mlist = getIntent().getParcelableArrayListExtra(Station_AllResult.Terminalall_KEY);
        if (null == mlist) {
            return;
        }
        try {
            AddAllTerminalMarker(mlist);
        }catch(Exception e){

        }
    }
    public void clearClick() {
        // 清除所有图层
        mTerminal_MapView.getMap().clear();
    }

    //设置显示的态势图的频率范围

    @Override
    protected void onPause() {
        mTerminal_MapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mTerminal_MapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mTerminal_MapView.onDestroy();
        super.onDestroy();
    }
    private void AddAllTerminalMarker(List<List_StationAll> AllTerminal) {
        int i = 0;
        Markerlist = new ArrayList<Marker>();
        LatLng OnlineTerminal_site;
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(AllTerminal.get(0).getLatitude(), AllTerminal.get(0).getLongitude()));

        mTerminal_BaiduMap.setMapStatus(u1);
        for(List_StationAll terminal:AllTerminal)
        {
            if(terminal.getIDcard().equals("0xFFFFFF")) {
                 stationMarker1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.marker1);
                 stationMarker2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.marke2);
            }
            else{
                 stationMarker1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.abnormal_station_big);
                 stationMarker2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.abnormal_station_small);
            }
                Log.e("CYYYY", "enter shis");
                ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
                giflist.add(stationMarker1);
                giflist.add(stationMarker2);
                Markerlist.add((Marker) mTerminal_BaiduMap.addOverlay(new MarkerOptions().position(new LatLng(terminal.getLatitude(), terminal.getLongitude())).icons(giflist)
                        .zIndex(0).period(10)));
                giflist.clear();

            }


    }
}