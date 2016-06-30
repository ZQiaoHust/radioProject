package com.hust.radiofeeler.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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
import com.hust.radiofeeler.bean2server.List_StationAll;
import com.hust.radiofeeler.tab1.Stations_registerResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Stations_registerResult_map extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private MapView mStation_MapView = null;
    private BaiduMap mStation_BaiduMap = null;
    ArrayList<List_StationAll> mlist=new ArrayList<>();
    private int count = 0;//去掉放大缩小标记
    private ArrayList<Marker> Markerlist;
    private static TextView dialog_text_1;
//    dialog_text_2,dialog_text_3,dialog_text_4,dialog_text_5,
//            dialog_text_6,dialog_text_7,dialog_text_8,dialog_text_9,dialog_text_10,dialog_text_11;
    private InfoWindow mInfoWindow;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_staitonmap);
        mStation_MapView = (MapView) findViewById(R.id.StationMapView);
        mStation_BaiduMap = mStation_MapView.getMap();

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14);
        mStation_BaiduMap.setMapStatus(msu);
        //去掉放大缩小的标识
        count = mStation_MapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mStation_MapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }

        mlist = getIntent().getParcelableArrayListExtra(Stations_registerResult.Stations_registerResult_KEY);
        if (null == mlist) {
            return;
        }
        try {
            AddRegisterStationMarker(mlist);
        }catch(Exception e){

        }


        mStation_BaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                for(int i=0;i<mlist.size();i++){
                    if (marker == Markerlist.get(i)) {
                                ShowDialog(i);
                    }
                }
                return true;
            }

        });
    }

    private void ShowDialog(int index) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.map_registerstation_info_dialog, null);
        dialog_text_1 = (TextView)view.findViewById(R.id.belonging);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getASICII()));

        dialog_text_1 = (TextView)view.findViewById(R.id.IDcard);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getIDcard()));
        dialog_text_1 = (TextView)view.findViewById(R.id.signal_bandwidth);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getaBand())+"Mhz");
        dialog_text_1 = (TextView)view.findViewById(R.id.module_mode);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getModem()));
        dialog_text_1 = (TextView)view.findViewById(R.id.module_attribute);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getModemPara()));
        dialog_text_1 = (TextView)view.findViewById(R.id.altitude_longitude);
        dialog_text_1.setText(mlist.get(index).getLatitudeStyle()+String.valueOf(mlist.get(index).getLatitude())
                +" "+mlist.get(index).getLongtitudeStyle()+String.valueOf(mlist.get(index).getLongitude()));

        dialog_text_1 = (TextView)view.findViewById(R.id.center_freq);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getSection())+"Mhz");
        dialog_text_1 = (TextView)view.findViewById(R.id.maxpower);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getMaxPower())+"dBm");
        dialog_text_1 = (TextView)view.findViewById(R.id.Activity_degree);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getLiveness()));
        dialog_text_1 = (TextView)view.findViewById(R.id.Service_attribute);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getWork()));
        dialog_text_1 = (TextView)view.findViewById(R.id.ruleRadius);
        dialog_text_1.setText(String.valueOf(mlist.get(index).getRuleRadius())+"km");
        AlertDialog.Builder dialogInfo = new AlertDialog.Builder(this);
        dialogInfo.setTitle("台站详细信息");

        dialogInfo.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        dialogInfo.setView(view);
        AlertDialog Mapdialog = dialogInfo.create();

        Mapdialog.show();
        mStation_BaiduMap.hideInfoWindow();
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
    private void AddRegisterStationMarker(List<List_StationAll> RegisterStation) {
        Markerlist = new ArrayList<Marker>();
        LatLng OnlineTerminal_site;
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(RegisterStation.get(0).getLatitude()+Constants.LAT_OFFSET, RegisterStation.get(0).getLongitude()+ Constants.LON_OFFSET));

        mStation_BaiduMap.setMapStatus(u1);
        for(List_StationAll station:RegisterStation)
        {
            BitmapDescriptor stationMarker1 = BitmapDescriptorFactory
                    .fromResource(R.drawable.marker1);
            BitmapDescriptor stationMarker2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.marke2);
            Log.e("CYYYY", "enter shis");
            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(stationMarker1);
            giflist.add(stationMarker2);
            Markerlist.add((Marker) mStation_BaiduMap.addOverlay(new MarkerOptions()
                    .position(new LatLng(station.getLatitude(), station.getLongitude())).icons(giflist)
                    .zIndex(0).period(10)));
        }
    }
}