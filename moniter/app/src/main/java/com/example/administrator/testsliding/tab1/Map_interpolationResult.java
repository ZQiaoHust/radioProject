package com.example.administrator.testsliding.tab1;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.LocationAbnormalReply;
import com.example.administrator.testsliding.bean2server.MapInterpolationReply;
import com.example.administrator.testsliding.map.InsertHeatMap;
import com.example.administrator.testsliding.map.Service_abnormal_map;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Map_interpolationResult extends Activity {
    private ArrayList<Map<String, String>> listItems;
    private ListView mListView;
    private MyTopBar topBar;
    private MapInterpolationReply interpolationReply=new MapInterpolationReply();

    // 接受消息广播
    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            listItems.clear();
            interpolationReply = intent.getParcelableExtra("map_interpolation");
            // 如果接受到空消息时过滤掉
            if (null == interpolationReply) {
                return;
            }
            // 将消息展现出来。
            if (interpolationReply != null) {
                listItems = interpolationReply2ListItems(interpolationReply);
            }

            if (listItems != null) {
                SimpleAdapter adapter = new SimpleAdapter(Map_interpolationResult.this, listItems, R.layout.item_interpolation
                        , new String[] { "data_1", "data_2", "data_3", "data_4", "data_5", "data_6","data_7"}
                        , new int[] {R.id.item_data1
                        , R.id.item_data2
                        , R.id.item_data3
                        , R.id.item_data4
                        , R.id.item_data5
                        , R.id.item_data6
                        , R.id.item_data7});
                mListView.setAdapter(adapter);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_interpolationresult);
        listItems = new ArrayList<>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RMAPINTERPOLATION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentRecevier, filter);

        InitSetting();
        InitEvent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(contentRecevier);
        contentRecevier = null;
    }

    private void InitSetting(){
        topBar = (MyTopBar) findViewById(R.id.topbar_locationResult);
        mListView = (ListView) findViewById(R.id.listview_interpolation);
    }

    private void InitEvent(){
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Map_interpolationResult.this.finish();
            }

            @Override
            public void rightclick() {

                Intent intent = new Intent();
                intent.setClass(Map_interpolationResult.this, InsertHeatMap.class);
                startActivity(intent);

            }
        });

    }



    private ArrayList<Map<String, String>> interpolationReply2ListItems( MapInterpolationReply reply) {
        ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        Map<String, String> data = new HashMap<String, String>();
        data.put("data_" + 1, String.valueOf(reply.getAbFreq()));
        data.put("data_" + 2, String.valueOf(reply.getaBand()));
        String location=reply.getLongtitudeStyle()+reply.getLongitude()+","+
                reply.getLatitudeStyle()+reply.getLatitude()+","+reply.getHeight();
        data.put("data_" + 3, location);
        data.put("data_" + 4, String.valueOf(reply.getRadius()));
        data.put("data_" + 5, String.valueOf(reply.getDieta()));
        data.put("data_" + 6, String.valueOf(reply.getFreNum()));
        data.put("data_" + 7, String.valueOf(reply.getFreshtime()));
        datas.add(data);
        return datas;
    }

}
