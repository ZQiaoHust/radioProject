package com.example.administrator.testsliding.tab1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.MapRadioPointInfo;
import com.example.administrator.testsliding.bean2server.MapRouteResult;
import com.example.administrator.testsliding.bean2server.TimeShow;
import com.example.administrator.testsliding.map.Map_RouteMap;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-11-18.
 */
public class Map_Route_Result extends Activity {
    private TextView tv_data,tv_freq,tv_band;
    private ListView mlistView;

    private ArrayList<Map<String,Object>> mlist;
    private  MapRouteResult map;
    public  final static String ROUTE_KEY = "com.example.administrator.testsliding.ROUTE";

    private ArrayList<MapRadioPointInfo> mapRadioPointInfoList = new ArrayList<>();
    private BroadcastReceiver contentRecevier=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantValues.RMAPROUTE)) {
                map = intent.getParcelableExtra("map_route");
                if (map == null) {
                    return;
                }
                if (map != null) {
                    tv_data.setText("中心站数据");
                    tv_freq.setText(String.valueOf(map.getCentralFreq()));
                    tv_band.setText(String.valueOf(map.getBand()));
                    mlist = Object2List(map);
                }
                if (mlist != null) {
                    SimpleAdapter simpleAdapter = new SimpleAdapter(Map_Route_Result.this, mlist,
                            R.layout.abnormal_frequency_item, new String[]{"item_time", "item_location", "item_power"},
                            new int[]{R.id.seq_num, R.id.freq, R.id.PowerSpectrum});
                    mlistView.setAdapter(simpleAdapter);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_route_infolist);
        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_RouteMapInfo);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Map_Route_Result.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent(Map_Route_Result.this, Map_RouteMap.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ROUTE_KEY, map);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        InitSetting();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstantValues.RMAPROUTE);
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
        tv_data= (TextView) findViewById(R.id.tv_datafrom);
        tv_freq= (TextView) findViewById(R.id.tv_centralFreq);
        tv_band= (TextView) findViewById(R.id.tv_band);
        mlistView= (ListView) findViewById(R.id.listview_mapRoute);
        mlist=new ArrayList<>();
    }
    private ArrayList<Map<String, Object>> Object2List(MapRouteResult map){
        ArrayList<Map<String, Object>> listItems=new ArrayList<>();
        ArrayList<MapRadioPointInfo> pointlist=map.getMapRadioPointInfoList();
        for(MapRadioPointInfo point:pointlist){
            Map<String,Object> data=new HashMap<>();
            String time=String.format("yyyy-MM-dd HH:mm",point.getYear(),point.getMonth(),point.getDate(),
                    point.getHour(),point.getMin());
//            String time=point.getYear()+"-"+point.getMonth()+"-"+point.getDate()+""
//                    +point.getHour()+":"+point.getMin();
//            TimeShow timeShow=new TimeShow(point.getYear(),point.getMonth(),point.getDate(),
//                    point.getHour(),point.getMin());
            String location=point.getLongtitudeStyle()+point.getLongitude()+" ,"+
                    point.getLatitudeStyle()+point.getLatitude()+" ,"+point.getHeight();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            String time=sdf.format(timeShow);
            data.put("item_time",time);
            data.put("item_location",location);
            data.put("item_power",point.getEqualPower());
            listItems.add(data);
        }
        return listItems;
    }
}
