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
import com.hust.radiofeeler.bean2server.ModifyIngainView;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyIngainChart extends Activity {
    private TextView et_FPGAID,et_freq;
    private ListView  ingain_listview;
    private ArrayList<Map<String, Object>> listItems;
    // 接受消息广播
    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            listItems.clear();
            ModifyIngainView view = intent.getParcelableExtra("modifyIngian");
            // 如果接受到空消息时过滤掉
            if (null == view) {
                return;
            }
            // 将消息展现出来。
            if (view != null) {
                et_FPGAID.setText(view.getFPGANum());
                et_freq.setText(view.getSection());
                listItems = ModifyIngain2ListItem(view);
            }

            if (listItems != null) {
                SimpleAdapter simpleAdapter = new SimpleAdapter(ModifyIngainChart.this, listItems,
                        R.layout.modifyingain_item, new String[]{ "section", "modifyValue"},
                        new int[]{ R.id.section, R.id.modifyValue});

                ingain_listview.setAdapter(simpleAdapter);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyingainchart);
        InitSetting();

        listItems = new ArrayList<>();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RMODIFYINGAIN);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentRecevier, filter);

        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_radioResult);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                ModifyIngainChart.this.finish();
            }

            @Override
            public void rightclick() {

            }
        });
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(contentRecevier);
        contentRecevier = null;
//        stopService(intent);
//        intent = null;
        super.onDestroy();
    }


    private void InitSetting(){
        et_FPGAID= (TextView) findViewById(R.id.tv_FPGAID);
        et_freq= (TextView) findViewById(R.id.tv_freq);
        ingain_listview= (ListView) findViewById(R.id.modifyIngain_listView);
    }


    private ArrayList<Map<String, Object>> ModifyIngain2ListItem(ModifyIngainView view){
        ArrayList<Map<String, Object>> listItems;
        listItems = new ArrayList<>();
        listItems.clear();
        String[] str=view.getValue();
        for(int i=0;i<237;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("section", String.valueOf(70+25*i)+"~"+String.valueOf(95+25*i));
            map.put("modifyValue", str[i]);
            listItems.add(map);
        }
        return listItems;
    }

}
