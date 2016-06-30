package com.hust.radiofeeler.map;

/**
 * Created by Administrator on 2016/1/8.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;

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
import com.hust.radiofeeler.tab1.Service_locationResult;
import com.hust.radiofeeler.bean2server.LocationAbnormalReply;

/**
 * Created by Administrator on 2015/10/26.
 */
public class Service_abnormal_map extends Activity  {
    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private MapView mAbnormal_MapView = null;
    private BaiduMap mAbnormal_BaiduMap = null;
    private int count = 0;//去掉放大缩小标记
    private static LocationAbnormalReply locationAbnormalMapData = new LocationAbnormalReply();
    private InfoWindow mInfoWindow;

    private static TextView dialog_text;

    @Override

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_abnormal_map);
        mAbnormal_MapView = (MapView) findViewById(R.id.AbnormalMapView);
        mAbnormal_BaiduMap = mAbnormal_MapView.getMap();
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);

        mAbnormal_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20);
        mAbnormal_BaiduMap.setMapStatus(msu);
        //去掉放大缩小的标识
        count = mAbnormal_MapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mAbnormal_MapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }

        Intent mintent=getIntent();
        locationAbnormalMapData = mintent.getParcelableExtra(Service_locationResult.ABNORMAL_KEY);
        if (null == locationAbnormalMapData) {
            return;
        }

        if (locationAbnormalMapData != null) {
            // 将消息展现出来。
            Log.d("mapdataget","size is "+String.valueOf(locationAbnormalMapData.getaBand()));
            DrawRadioAbnormalMap(locationAbnormalMapData,2.66);
        }




        locationAbnormalMapData = getIntent().getParcelableExtra(Service_locationResult.ABNORMAL_KEY);
        if (null == locationAbnormalMapData) {
            return;
        }

        if (locationAbnormalMapData != null) {
            // 将消息展现出来。
            Log.d("mapdataget","size is "+String.valueOf(locationAbnormalMapData.getaBand()));
            DrawRadioAbnormalMap(locationAbnormalMapData,2.66);
        }
        mAbnormal_BaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                DrawInfoWindow(marker);
                return true;
            }

        });

    }

    private void DrawInfoWindow(Marker marker){
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.location_tips);
        InfoWindow.OnInfoWindowClickListener listener = null;

        button.setText("经纬度："+locationAbnormalMapData.getLatitudeStyle()+String.valueOf(locationAbnormalMapData.getLatitude())
                +" "+locationAbnormalMapData.getLongtitudeStyle()+String.valueOf(locationAbnormalMapData.getLongitude())+'\n'+"功率值："+locationAbnormalMapData.getEqualPower());
        listener = new InfoWindow.OnInfoWindowClickListener() {
            public void onInfoWindowClick() {
                ShowDialog();
            }
        };
        LatLng ll =new LatLng(marker.getPosition().latitude+ Constants.LAT_OFFSET,marker.getPosition().longitude+Constants.LON_OFFSET) ;
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
        mAbnormal_BaiduMap.showInfoWindow(mInfoWindow);


    }
    //设置显示的态势图的频率范围


    private void  DrawRadioAbnormalMap(LocationAbnormalReply AbnormalReply,double Index) {
        double Power_cnt,Power_circle;
        double distance;
        Marker marker;
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(AbnormalReply.getLatitude()+ Constants.LAT_OFFSET, AbnormalReply.getLongitude()+Constants.LON_OFFSET));

        mAbnormal_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mAbnormal_BaiduMap.setMapStatus(msu);
        Power_cnt = Math.floor(AbnormalReply.getEqualPower());
        //Power_1st = Power_cnt-20;
        //Power_sed = Power_cnt - 40;
        // Power_thd = Power_cnt -60;

        Log.e("abnormal","Power is "+String.valueOf(Math.floor(AbnormalReply.getEqualPower())));

        if(Power_cnt == 0)
            return;
        BitmapDescriptor AbnormalMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.marker1);
        MarkerOptions oo1 = new MarkerOptions().position(new LatLng(AbnormalReply.getLatitude()+Constants.LAT_OFFSET,AbnormalReply.getLongitude()+Constants.LON_OFFSET)).icon(AbnormalMarker)
                .zIndex(0).period(10);
        oo1.animateType(MarkerOptions.MarkerAnimateType.grow);
        marker = (Marker)mAbnormal_BaiduMap.addOverlay(oo1);

        for(int i = 1;i<9;i++)
        {

            LatLng llCircle = new LatLng(AbnormalReply.getLatitude()+Constants.LAT_OFFSET, AbnormalReply.getLongitude()+Constants.LON_OFFSET);
            Power_circle = Power_cnt-5*i;
            distance = Math.sqrt(Math.pow(10,(Power_cnt-Power_circle)/(5*Index)));
            Log.e("distance",String.valueOf(distance));
            OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
                    .center(llCircle).stroke(new Stroke(5, 0xAA000000))
                    .radius((int)(distance*1000));

            mAbnormal_BaiduMap.addOverlay(ooCircle);

            LatLng llText = new LatLng((AbnormalReply.getLatitude()+Constants.LAT_OFFSET+distance/111+0.001), AbnormalReply.getLongitude()+Constants.LON_OFFSET);
            //构建文字Option对象，用于在地图上添加文字
            OverlayOptions textOption = new TextOptions()
                    .fontSize(30)
                    .fontColor(0xFFFF00FF)
                    .text("功率值"+String.valueOf(Power_circle) +"dBm")
                    .position(llText);
//在地图上添加该文字对象并显示
            mAbnormal_BaiduMap.addOverlay(textOption);
        }

    }

    private void ShowDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.map_station_info_dialog, null);
        dialog_text = (TextView)view.findViewById(R.id.center_freq_info);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getAbFreq()));

        dialog_text = (TextView)view.findViewById(R.id.signal_bandwidth);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getaBand()));
        dialog_text = (TextView)view.findViewById(R.id.module_mode);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getModem()));
        dialog_text = (TextView)view.findViewById(R.id.module_attribute);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getModemPara()));
        dialog_text = (TextView)view.findViewById(R.id.altitude_longitude);
        dialog_text.setText(locationAbnormalMapData.getLatitudeStyle()+String.valueOf(locationAbnormalMapData.getLatitude())
                +" "+locationAbnormalMapData.getLongtitudeStyle()+String.valueOf(locationAbnormalMapData.getLongitude()));
        dialog_text = (TextView)view.findViewById(R.id.transmit_power);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getEqualPower()));
        dialog_text = (TextView)view.findViewById(R.id.Loss_index);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getrPara()));
        dialog_text = (TextView)view.findViewById(R.id.Activity_degree);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getLiveness()));
        dialog_text = (TextView)view.findViewById(R.id.Service_attribute);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getWork()));
        dialog_text = (TextView)view.findViewById(R.id.is_illegal);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getIsLegal()));
        dialog_text = (TextView)view.findViewById(R.id.belong_institute);
        dialog_text.setText(String.valueOf(locationAbnormalMapData.getOrganizer()));
        AlertDialog.Builder dialogInfo = new AlertDialog.Builder(this);
        dialogInfo.setTitle("台站详细信息");

        dialogInfo.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        dialogInfo.setView(view);
        AlertDialog Mapdialog = dialogInfo.create();

        Mapdialog.show();
        mAbnormal_BaiduMap.hideInfoWindow();
    }



    @Override
    protected void onPause() {
        mAbnormal_MapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAbnormal_MapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAbnormal_MapView.onDestroy();
        super.onDestroy();
    }

    private void clearClick() {
        // 清除所有图层
        mAbnormal_MapView.getMap().clear();

    }
}

