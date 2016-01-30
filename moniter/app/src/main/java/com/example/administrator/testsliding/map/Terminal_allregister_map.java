package com.example.administrator.testsliding.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
import com.example.administrator.testsliding.bean2server.List_TerminalOnline;
import com.example.administrator.testsliding.tab1.Service_Terminal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
public class Terminal_allregister_map extends Activity{

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private MapView mTerminal_MapView = null;
    private BaiduMap mTerminal_BaiduMap = null;

    private int count = 0;//去掉放大缩小标记
    private ArrayList<List_TerminalOnline> mlist;
    private ArrayList<Marker> Markerlist;


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

        //添加标识
//        ImageView ProfUser,NormalUser,ProfSearch,NormalSearch;
//        ProfUser = new ImageView(this);
//        ProfUser.setImageDrawable(this.getResources().getDrawable(R.drawable.profuserbig));
//
//
//        LinearLayout.LayoutParams para = null;
//        para.topMargin = 300;
//        para.leftMargin = 20;
//        ProfUser.setLayoutParams(para);
        //接收数据
        mlist = getIntent().getParcelableArrayListExtra(Service_Terminal.TerminalallRegister_KEY);
        if (null == mlist) {
            return;
        }

        try {
            AddOnlineTerminalMarker(mlist);
        }catch (Exception e){

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
    private void AddOnlineTerminalMarker(List<List_TerminalOnline> RegisterTerminal) {
        Markerlist = new ArrayList<Marker>();
        LatLng OnlineTerminal_site;
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor stationMarker1 = BitmapDescriptorFactory
                .fromResource(R.drawable.profuserbig);
        BitmapDescriptor stationMarker2 = BitmapDescriptorFactory
                .fromResource(R.drawable.pofusersmall);
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(RegisterTerminal.get(0).getLatitude(),RegisterTerminal.get(0).getLongitude()));

        mTerminal_BaiduMap.setMapStatus(u1);
        for(List_TerminalOnline terminal:RegisterTerminal)
        {
            if(terminal.getStyle() == 0)
            {
                stationMarker1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.profuserbig);
                 stationMarker2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.pofusersmall);
            }else if(terminal.getStyle() == 1)
            {
                stationMarker1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.normaluserbig);
                stationMarker2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.normalusersmall);
            }else if(terminal.getStyle() == 2)
            {
                stationMarker1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.profsearchbig);
                stationMarker2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.profsearchsmall);
            }
            else if(terminal.getStyle() == 3)
            {
                stationMarker1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.normalsearchbig);
                stationMarker2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.normalsearchsmall);
            }
            giflist.add(stationMarker1);
            giflist.add(stationMarker2);
            Markerlist.add((Marker)mTerminal_BaiduMap.addOverlay(new MarkerOptions().position(new LatLng(terminal.getLatitude(), terminal.getLongitude())).icons(giflist)
                    .zIndex(0).period(10)));
            giflist.clear();
        }
    }
}
