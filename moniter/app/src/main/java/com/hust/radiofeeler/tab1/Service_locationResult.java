package com.hust.radiofeeler.tab1;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.LocationAbnormalReply;
import com.hust.radiofeeler.map.Service_abnormal_map;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Service_locationResult extends Activity {
    private ArrayList<Map<String, String>> listItems;
    private ListView mListView;
    private MyTopBar topBar;
    private LocationAbnormalReply abnormal = new LocationAbnormalReply();
    public  final static String ABNORMAL_KEY = "com.example.administrator.testsliding.abnormal_map";

    // 接受消息广播
    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            listItems.clear();
            abnormal = intent.getParcelableExtra("abnormal_location");
            // 如果接受到空消息时过滤掉
            if (null == abnormal) {
                return;
            }
            // 将消息展现出来。
            if (abnormal != null) {
                listItems = Abnormal2ListItems(abnormal);
            }

            if (listItems != null) {
                SimpleAdapter adapter = new SimpleAdapter(Service_locationResult.this, listItems, R.layout.item_locationabnormal
                        , new String[] { "data_1", "data_2", "data_3", "data_4", "data_5", "data_6",
                        "data_7", "data_8", "data_9", "data_10", "data_11" }
                        , new int[] {R.id.item_data1
                        , R.id.item_data2
                        , R.id.item_data3
                        , R.id.item_data4
                        , R.id.item_data5
                        , R.id.item_data6
                        , R.id.item_data7
                        , R.id.item_data8
                        , R.id.item_data9
                        , R.id.item_data10
                        , R.id.item_data11});
                mListView.setAdapter(adapter);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_locationresult);
        listItems = new ArrayList<>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RABNORMAL_LOCATION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentRecevier, filter);

        InitSetting();
        InitEvent();
       // initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(contentRecevier);
        contentRecevier = null;
    }

    private void InitSetting(){
        topBar = (MyTopBar) findViewById(R.id.topbar_locationResult);
        mListView = (ListView) findViewById(R.id.listview_locationAbnormal);
    }

    private void InitEvent(){
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_locationResult.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent(Service_locationResult.this,Service_abnormal_map.class);
                Bundle mBundle = new Bundle();

                mBundle.putParcelable(ABNORMAL_KEY, abnormal);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

    }



    private ArrayList<Map<String, String>> Abnormal2ListItems( LocationAbnormalReply abnormal) {
        ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        Map<String, String> data = null;
        data = new HashMap<String, String>();
        data.put("data_" + 1, String.valueOf(abnormal.getAbFreq()));
        data.put("data_" + 2, String.valueOf(abnormal.getaBand()));
        data.put("data_" + 3, abnormal.getModem());
        data.put("data_" + 4, String.valueOf(abnormal.getModemPara()));
        String location=abnormal.getLongtitudeStyle()+abnormal.getLongitude()+","+
                abnormal.getLatitudeStyle()+abnormal.getLatitude()+","+abnormal.getHeight();
        data.put("data_" + 5, location);
        data.put("data_" + 6, String.valueOf(abnormal.getEqualPower()));
        data.put("data_" + 7, String.valueOf(abnormal.getrPara()));
        data.put("data_" + 8, String.valueOf(abnormal.getLiveness()));
        data.put("data_" + 9, abnormal.getWork());
        data.put("data_" + 10, String.valueOf(abnormal.getIsLegal()));
        data.put("data_" + 11, abnormal.getOrganizer());
        datas.add(data);
        return datas;
    }

}
