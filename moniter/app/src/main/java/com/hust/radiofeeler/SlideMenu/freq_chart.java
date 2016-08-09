package com.hust.radiofeeler.SlideMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hust.radiofeeler.R;
import com.hust.radiofeeler.map.Map_HeatMap;
import com.hust.radiofeeler.view.MyTopBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by ariesuk on 2016/8/4.
 */

public class freq_chart extends Activity {
    private TextView tv_ServiceAttr,tv_startfreq,tv_endfreq;
    private MyTopBar topBar;
    private ListView mlistview;
    private ArrayList<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
    private  Map<String,Object> map;
    private String Edit_freq_content;
    public  final static String PAR_KEY = "com.example.administrator.testsliding.freqpar";

//    private static final String DB_NAME = "sendFileDatabase.db";//
//    // 数据库名称
//    private static final String TABLE_NAME = "offsetFile";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freq_chart);
        InitSetting();
        //GetOffsetChartData();
        GetfreqChartData();

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // 第position项被单击时激发该方法。
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id)
            {

                final Adapter adapter=parent.getAdapter();
                if (adapter == null)
                    return;
                map =(Map<String, Object>) adapter.getItem(position);

                    Edit_freq_content = "业务属性:"+map.get("item_name")+"；起始频率:"+map.get("item_startfreq")+"；终止频率:"+map.get("item_endfreq");
                    Intent intent = new Intent();
                    intent.putExtra("result", Edit_freq_content);
                /*
                 * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
                 */
                    setResult(1001, intent);
                    //    结束当前这个Activity对象的生命
                    finish();


            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void InitSetting(){
        tv_ServiceAttr= (TextView) findViewById(R.id.tv_service_attr);
        tv_startfreq= (TextView) findViewById(R.id.tv_startfreq);
        tv_endfreq= (TextView) findViewById(R.id.tv_endfreq);
        topBar= (MyTopBar) findViewById(R.id.topbar_freqchart);
        mlistview= (ListView) findViewById(R.id.servicefreq_listview);
        listItems = new ArrayList<>();
    }

    public void GetfreqChartData() {
        InputStream inputStream = getResources().openRawResource(R.raw.freqchart);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array;
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> data = new HashMap<>();
                JSONObject object = array.getJSONObject(i);
                double startfreq = object.getDouble("startfreq");
                double endfreq = object.getDouble("endfreq");
                String name = object.getString("name");

                data.put("item_startfreq",startfreq);
                data.put("item_endfreq",endfreq);
                data.put("item_name",name);
                listItems.add(data);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (listItems != null) {
            SimpleAdapter simpleAdapter = new SimpleAdapter(freq_chart.this, listItems,
                    R.layout.freq_item, new String[]{"item_startfreq", "item_endfreq", "item_name"
                    },
                    new int[]{R.id.item_startfreq, R.id.item_endfreq, R.id.item_name});
            mlistview.setAdapter(simpleAdapter);
        }

    }


    private void InitEvent(){
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                freq_chart.this.finish();
            }

            @Override
            public void rightclick() {

            }
        });

    }


}



