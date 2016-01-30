package com.example.administrator.testsliding.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.view.DateTimePickDialogUtil;

import java.util.ArrayList;
import java.util.List;


public class Map_StationMap extends Activity {
    // 地图相关



    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);

    /**
     * 地图控件
     */
    private MapView mMapView = null;
    /**
     * 地图实例
     */
    private BaiduMap mBaiduMap = null;
    /**
     * 热力图实例
     */
    private HeatMap heatmap;

    private boolean isExitStationMarker = false;
    private boolean isExitRadioOringinMarker = false;

    private int count = 0;
    private SDKReceiver mReceiver;

    private Marker mMarkerA;
    private InfoWindow mInfoWindow;

    //添加覆盖物
    BitmapDescriptor stationMarker = null;

    //路径图对象
    ImageButton heat_to_radiomap ;
    ImageButton  heat_to_routemap;


    private Spinner mySpinner;
    private ArrayAdapter<String> Spinner_adapter;
    private List<String> FreqSelectList = new ArrayList<String>();

    private EditText iuputtime1,inputtime2;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_basemap);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);

        mBaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12);
        mBaiduMap.setMapStatus(msu);
        //去掉放大缩小的标识
        count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }


        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);




        heat_to_radiomap = (ImageButton)findViewById(R.id.radioMap_heat);
        heat_to_routemap = (ImageButton)findViewById(R.id.RouteMap_heat);

        View.OnClickListener RouterListener = new View.OnClickListener() {
            public void onClick(View v) {
                //onDestroy();
                Intent intent=new Intent(Map_StationMap.this,Map_RouteMap.class);
                startActivity(intent);
            }
        };

        View.OnClickListener RadioListener = new View.OnClickListener() {
            public void onClick(View v) {
                //onDestroy();
                Intent intent=new Intent(Map_StationMap.this,Map_HeatMap.class);
                startActivity(intent);
            }
        };

        heat_to_routemap.setOnClickListener(RouterListener);
        heat_to_radiomap.setOnClickListener(RadioListener);

    }

    private void ShowDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.map_station_info_dialog, null);

        AlertDialog.Builder dialogInfo = new AlertDialog.Builder(this);
        dialogInfo.setTitle("台站详细信息");
        dialogInfo.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        dialogInfo.setView(view);
        AlertDialog Mapdialog = dialogInfo.create();
        Mapdialog.show();
        mBaiduMap.hideInfoWindow();
    }

    //检验Key是否验证通过
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();//获得Intent的MIME type
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(getApplicationContext(), "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
                        Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                Toast.makeText(getApplicationContext(), "key 验证成功! 功能可以正常使用",
                        Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(getApplicationContext(), "网络错误",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
    }




    //设置显示的态势图的频率范围
    private void setFreqbandwidth() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.map_setting_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("参数设置");
        FreqSelectList.add("FM调频广播频段（88-108MHz）");
        FreqSelectList.add("GSM下行频段I（35-960MHz）");
        FreqSelectList.add("GSM下行频段II（1804-1880MHz）");
        FreqSelectList.add("IS95CDMA下行频段（2555-25750MHz）");
        FreqSelectList.add("TD 3G频段（1880-1900MHz）");
        FreqSelectList.add("TD LTE频段I（2320-2370MHz）");
        FreqSelectList.add("TD LTE频段II（2575-2635MHz）");
        FreqSelectList.add("WCDMA下行频段（2130-2145MHz）");
        FreqSelectList.add("联通TD LTE频段I（2300-2320MHz）");
        FreqSelectList.add("联通TD LTE频段II（2555-2575MHz）");
        FreqSelectList.add("电信TD LTE频段I（2370-2390MHz）");
        FreqSelectList.add("电信TD LTE频段II（2635-26550MHz）");
        FreqSelectList.add("cdma2000 下行频段（2110-2125MHz）");
        FreqSelectList.add("LTE FDD频段I（1850-1880MHz）");
        FreqSelectList.add("LTE FDD频段II（2145-2170MHz）");
        FreqSelectList.add("ISM 433M频段（433.05-434.790MHz）");
        FreqSelectList.add("ISM 工业频段（902-9280MHz）");
        FreqSelectList.add("ISM 科学研究频段（2420-2483.50MHz）");
        FreqSelectList.add("ISM 医疗频段（5725-5850MHz）");
        mySpinner = (Spinner)view.findViewById(R.id.Spinner_base_radio);
        Spinner_adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_custom, FreqSelectList);
        Spinner_adapter.setDropDownViewResource(R.layout.simple_spinner_item_custom);
        mySpinner.setAdapter(Spinner_adapter);
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /* 将mySpinner 显示*/
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                /**
                 *
                 */
                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
        //========时间输入框初始化和点击事件============================================//
        iuputtime1= (EditText) findViewById(R.id.inputTime1);
        inputtime2= (EditText) findViewById(R.id.inputTime2);
        iuputtime1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(Map_StationMap.this);
                dateTimePicKDialog.dateTimePicKDialog(iuputtime1);

            }
        });

        inputtime2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(Map_StationMap.this);
                dateTimePicKDialog.dateTimePicKDialog(inputtime2);
            }
        });
        //========================================================================//
        builder.setIcon(R.drawable.map_settings);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //添加覆盖物
    public void PointStation() {
        // add marker overlay

        stationMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        MarkerOptions ooA = new MarkerOptions().position(GEO_WUHAN).icon(stationMarker)
                .zIndex(9);

        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        //添加标记和InfoWindow
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.location_tips );
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA) {
                    button.setText("华中科技大学南一楼站点");
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            ShowDialog();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

    }

}