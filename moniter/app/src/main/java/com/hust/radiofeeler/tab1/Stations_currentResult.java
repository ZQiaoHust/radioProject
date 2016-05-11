package com.hust.radiofeeler.tab1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.StationCurrentReply;
import com.hust.radiofeeler.map.Stations_CurrentResult_map;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Stations_currentResult  extends Activity {
    private ListView mlistview;
    private ArrayList<Map<String, String>> listItems;
    private static StationCurrentReply reply = new StationCurrentReply();
    public  final static String CURRENTSTATION_KEY = "com.example.administrator.testsliding.current_station";
    // 接受消息广播
    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("map","Stations_currentResult收到");
            if (action.equals(ConstantValues.RSTATION_CURRENT)) {
                reply = intent.getParcelableExtra("station_current");
                // 如果接受到空消息 r时过滤掉
                if (null == reply) {
                    return;
                }
                // 将消息展现出来。
                if (reply != null) {
                    listItems = Current2ListItems(reply);
                }
                if (listItems != null) {
                    SimpleAdapter adapter = new SimpleAdapter(Stations_currentResult.this, listItems, R.layout.item_stationcurrent
                            , new String[]{"data_1", "data_2", "data_3", "data_4", "data_5", "data_6",
                            "data_7", "data_8", "data_9", "data_10", "data_11", "data_12"}
                            , new int[]{R.id.item_data1
                            , R.id.item_data2
                            , R.id.item_data3
                            , R.id.item_data4
                            , R.id.item_data5
                            , R.id.item_data6
                            , R.id.item_data7
                            , R.id.item_data8
                            , R.id.item_data9
                            , R.id.item_data10
                            , R.id.item_data11
                            , R.id.item_data12});
                    mlistview.setAdapter(adapter);
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stations_currentresult);
        mlistview= (ListView) findViewById(R.id.listview_stationCurrent);
        listItems = new ArrayList<>();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RSTATION_CURRENT);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentRecevier, filter);

        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_currentResult);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Stations_currentResult.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent(Stations_currentResult.this, Stations_CurrentResult_map.class);
                Bundle mBundle = new Bundle();
                if(reply!=null) {
                    mBundle.putParcelable(CURRENTSTATION_KEY, reply);
                }
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        //initViews();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(contentRecevier);
        contentRecevier = null;
    }
    private void initViews() {
        List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
        Map<String, String> data = null;


            data = new HashMap<String, String>();

            data.put("data_" + 1, "Date_" + 1  );
            data.put("data_" + 2, "Date_" + 2  );
            data.put("data_" + 3, "Date_" + 3  );
            data.put("data_" + 4, "Date_" + 4  );
            data.put("data_" + 5, "Date_" + 5  );
            data.put("data_" + 6, "Date_" + 6  );
            data.put("data_" + 7, "Date_" + 7  );
            data.put("data_" + 8, "Date_" + 8  );
            data.put("data_" + 9, "Date_" + 9  );
            data.put("data_" + 10, "Date_" + 10 );
            data.put("data_" + 11, "Date_" + 11  );
            data.put("data_" + 12, "Date_" + 12 );
            datas.add(data);

        SimpleAdapter adapter = new SimpleAdapter(this, datas, R.layout.item_stationcurrent
                , new String[] { "data_1", "data_2", "data_3", "data_4", "data_5", "data_6",
                "data_7", "data_8", "data_9", "data_10", "data_11","data_12" }
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
                , R.id.item_data11
                ,R.id.item_data12});
        mlistview.setAdapter(adapter);
    }

    private ArrayList<Map<String, String>> Current2ListItems(StationCurrentReply reply) {
        ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        Map<String, String> data = null;
        data = new HashMap<String, String>();
        data.put("data_" + 1, reply. getASICIIId());
        data.put("data_" + 2, String.valueOf(reply.getIDcard()));
        String location=reply.getLongtitudeStyle()+reply.getLongitude()+","+
                reply.getLatitudeStyle()+reply.getLatitude()+","+reply.getHeight();
        data.put("data_" + 3, location);
        data.put("data_" + 4, String.valueOf(reply.getCentralFreq()));
        data.put("data_" + 5, String.valueOf(reply.getEqualPower()));
        data.put("data_" + 6, String.valueOf(reply.getrPara()));
        data.put("data_" + 7, String.valueOf(reply.getaBand()));
        data.put("data_" + 8, reply.getModem());
        data.put("data_" + 9, String.valueOf(reply.getModemPara()));
        data.put("data_" + 10, reply.getWork());
        data.put("data_" + 11, String.valueOf(reply.getLiveness()));
        data.put("data_" + 12, String.valueOf(reply.getRule()));

        datas.add(data);
        return datas;
    }
}
