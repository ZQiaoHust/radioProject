package com.hust.radiofeeler.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.List_TerminalOnline;
import com.hust.radiofeeler.tab1.Service_Terminal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Terminal_online_map extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private MapView mTerminal_MapView = null;
    private BaiduMap mTerminal_BaiduMap = null;
    private ArrayList<List_TerminalOnline> mlist;
    private ArrayList<Marker> Markerlist;
    private InfoWindow mInfoWindow;
    private int count = 0;//去掉放大缩小标记


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



         mlist = getIntent().getParcelableArrayListExtra(Service_Terminal.Terminalonline_KEY);
        if (null == mlist) {
            return;
        }
       try {
           AddOnlineTerminalMarker(mlist);
       }catch(Exception e){

       }


        mTerminal_BaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                for(int i=0;i<mlist.size();i++){
                    if (marker == Markerlist.get(i)) {
                        Button button = new Button(getApplicationContext());
                        button.setBackgroundResource(R.drawable.location_tips);
                        InfoWindow.OnInfoWindowClickListener listener = null;
                        button.setText(String.valueOf(mlist.get(i).getIDnum()));
                        listener = new InfoWindow.OnInfoWindowClickListener() {
                            public void onInfoWindowClick() {
                                mTerminal_BaiduMap.hideInfoWindow();
                            }
                        };
                        LatLng ll = marker.getPosition();
                        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                        mTerminal_BaiduMap.showInfoWindow(mInfoWindow);
                        }
                    }
                return true;
            }

        });

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
        clearClick();
        super.onDestroy();
    }

    private void AddOnlineTerminalMarker(List<List_TerminalOnline> OnlineTerminal) {
if(OnlineTerminal!=null) {
    Markerlist = new ArrayList<Marker>();
    LatLng OnlineTerminal_site;
    ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
    BitmapDescriptor stationMarker1 = BitmapDescriptorFactory
            .fromResource(R.drawable.profuserbig);
    BitmapDescriptor stationMarker2 = BitmapDescriptorFactory
            .fromResource(R.drawable.pofusersmall);
    MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(OnlineTerminal.get(0).getLatitude()+ Constants.LAT_OFFSET, OnlineTerminal.get(0).getLongitude()+Constants.LON_OFFSET));

    mTerminal_BaiduMap.setMapStatus(u1);
    for (List_TerminalOnline terminal : OnlineTerminal) {
        if (terminal.getStyle() == 0) {
            stationMarker1 = BitmapDescriptorFactory
                    .fromResource(R.drawable.profuserbig);
            stationMarker2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.pofusersmall);
        } else if (terminal.getStyle() == 1) {
            stationMarker1 = BitmapDescriptorFactory
                    .fromResource(R.drawable.normaluserbig);
            stationMarker2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.normalusersmall);
        } else if (terminal.getStyle() == 2) {
            stationMarker1 = BitmapDescriptorFactory
                    .fromResource(R.drawable.profsearchbig);
            stationMarker2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.profsearchsmall);
        } else if (terminal.getStyle() == 3) {
            stationMarker1 = BitmapDescriptorFactory
                    .fromResource(R.drawable.normalsearchbig);
            stationMarker2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.normalsearchsmall);
        }
        giflist.add(stationMarker1);
        giflist.add(stationMarker2);
        Markerlist.add((Marker) mTerminal_BaiduMap.addOverlay(new MarkerOptions().position(new LatLng(terminal.getLatitude()+Constants.LAT_OFFSET, terminal.getLongitude()+Constants.LON_OFFSET)).icons(giflist)
                .zIndex(0).period(10)));
        giflist.clear();
    }
}
    }

}