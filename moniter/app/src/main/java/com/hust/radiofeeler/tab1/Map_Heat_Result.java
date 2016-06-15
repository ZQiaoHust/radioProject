package com.hust.radiofeeler.tab1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapRadioPointInfo;
import com.hust.radiofeeler.bean2server.MapRadioResult;
import com.hust.radiofeeler.map.Map_HeatMap;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-11-18.
 */
public class Map_Heat_Result extends Activity {
   private TextView tv_freq,tv_band,tv_dieta;
    private MyTopBar topBar;
    private ListView mlistview;
    private MapRadioResult map;
    private ArrayList<Map<String,Object>> listItems;

    private ArrayList<MapRadioPointInfo> mapRadioPointInfoList = new ArrayList<>();
    /*intent方法*/
    public  final static String PAR_KEY = "com.example.administrator.testsliding.par";

    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantValues.RMAPRADIO)) {
                map = intent.getParcelableExtra("map_radio");
                // 如果接受到空消息时过滤掉
                if (null == map) {
                    return;
                }
                // 将消息展现出来。
                if (map != null) {
                    tv_freq.setText(String.valueOf(map.getCentralFreq()));
                    tv_band.setText(String.valueOf(map.getBand()));
                    tv_dieta.setText(String.valueOf(map.getDieta()));
                    listItems = Object2List(map);
                }

                if (listItems != null) {
                    SimpleAdapter simpleAdapter = new SimpleAdapter(Map_Heat_Result.this, listItems,
                            R.layout.mapradio_item, new String[]{"item_time", "item_location", "item_power"
                            , "item_radio"},
                            new int[]{R.id.item_time, R.id.item_location, R.id.item_power, R.id.item_radio});
                    mlistview.setAdapter(simpleAdapter);
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_taishi_infolist);
        InitSetting();
        InitEvent();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RMAPRADIO);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentRecevier, filter);

    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(contentRecevier);
        contentRecevier = null;
        super.onDestroy();
    }

    private void InitSetting(){
        tv_freq= (TextView) findViewById(R.id.tv_centralFreq);
        tv_band= (TextView) findViewById(R.id.tv_band);
        tv_dieta= (TextView) findViewById(R.id.tv_dieta);
        topBar= (MyTopBar) findViewById(R.id.topbar_HeatMapInfo);
        mlistview= (ListView) findViewById(R.id.listview_mapRadio);
        listItems = new ArrayList<>();
    }


    private void InitEvent(){
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Map_Heat_Result.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent(Map_Heat_Result.this, Map_HeatMap.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, map);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

    }

    private ArrayList<Map<String, Object>> Object2List(MapRadioResult map){
        ArrayList<Map<String, Object>> listItems=new ArrayList<>();
        ArrayList<MapRadioPointInfo> pointlist=map.getMapRadioPointInfoList();
        for(MapRadioPointInfo point:pointlist){
            Map<String,Object> data=new HashMap<>();
            String year = String.valueOf(point.getYear());
            String month = String.valueOf(point.getMonth()< 10 ? "0" + point.getMonth() : point.getMonth());
            String day = String.valueOf(point.getDate()< 10 ? "0" + point.getDate() : point.getDate());

            String hour = String.valueOf(point.getHour()< 10 ? "0" + point.getHour() : point.getHour());
            String min = String.valueOf(point.getMin()< 10 ? "0" + point.getMin() : point.getMin());

            String time= year + "-" + month + "-" + day + " " + hour + ":" + min;
            String location=point.getLongtitudeStyle()+point.getLongitude()+","+
                    point.getLatitudeStyle()+point.getLatitude()+","+point.getHeight();
//
            data.put("item_time",time);
            data.put("item_location",location);
            data.put("item_power",point.getEqualPower());
            data.put("item_radio",point.getrPara());
            listItems.add(data);
        }
        return listItems;
    }


}
