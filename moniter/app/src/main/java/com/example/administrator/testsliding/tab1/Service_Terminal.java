package com.example.administrator.testsliding.tab1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.List_TerminalOnline;
import com.example.administrator.testsliding.bean2server.Terminal_Online;
import com.example.administrator.testsliding.bean2server.Terminal_Register;
import com.example.administrator.testsliding.map.Terminal_allregister_map;
import com.example.administrator.testsliding.map.Terminal_online_map;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/23.
 */
public class Service_Terminal extends Activity {
   private Button btn_online,btn_register;
    private  MyTopBar topBar;
    private ListView mListView;
    private LinearLayout lilay;
    private boolean IsOnline=false,Isregis=false;
    private ArrayList<List_TerminalOnline> mlist;
    public  final static String Terminalonline_KEY = "com.example.administrator.testsliding.terminalall_map";
    public  final static String TerminalallRegister_KEY = "com.example.administrator.testsliding.terminalall_map";
    private ArrayList<Map<String, Object>> listItems = new ArrayList<>();

    private BroadcastReceiver contentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listItems.clear();
            mlist = new ArrayList<List_TerminalOnline>();
            if (intent.getAction().equals(ConstantValues.RTERMINAL_ONLINE)) {
                mlist = intent.getParcelableArrayListExtra("terminal_onlineresult");
            }
            if (intent.getAction().equals(ConstantValues.RTERMINAL_REGISTER)) {
                mlist = intent.getParcelableArrayListExtra("terminal_registerresult");
            }
            if (mlist != null) {
                listItems = Arraylist2Items(mlist);
                if (listItems != null) {
                    //使用无线电规划表的布局
                    SimpleAdapter simpleAdapter = new SimpleAdapter(Service_Terminal.this, listItems,
                            R.layout.serviceradio_item, new String[]{"section_num", "section", "section_attriutes"},
                            new int[]{R.id.section_num, R.id.section, R.id.section_attriutes});

                    mListView.setAdapter(simpleAdapter);

                }

            }
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_terminal);
        mlist=new ArrayList<>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RTERMINAL_ONLINE);
        filter.addAction(ConstantValues.RTERMINAL_REGISTER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentReceiver, filter);
        initView();
        InitEvent();
    }

    private void initView() {
        topBar= (MyTopBar) findViewById(R.id.topbar_serviceterminal);
      btn_online= (Button) findViewById(R.id.btn_terminalOnlie);
        btn_register= (Button) findViewById(R.id.btn_terminalRegis);
        mListView= (ListView) findViewById(R.id.slistview_terminal);
        lilay= (LinearLayout) findViewById(R.id.lilay_terminal);
    }

    private void InitEvent() {
        btn_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////同时发送所有在网终端和注册终端的属性查询
                btn_online.setBackgroundColor(0xff00ddff);
                btn_register.setBackgroundColor(0xffC4C5C5);
                Terminal_Online online = new Terminal_Online();
                online.setEquipmentID(Constants.ID);
                Broadcast.sendBroadCast(Service_Terminal.this,
                        ConstantValues.TERMINAL_ONLINE, "terminal_online", online);
                IsOnline=true;
                Isregis=false;
                lilay.setVisibility(View.VISIBLE);

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_online.setBackgroundColor(0xffC4C5C5);
                btn_register.setBackgroundColor(0xff00ddff);
                Terminal_Register terminal = new Terminal_Register();
                terminal.setEquipmentID(Constants.ID);
//
                Broadcast.sendBroadCast(Service_Terminal.this,
                        ConstantValues.TERMINAL_REGISTER, "terminal_register", terminal);
                IsOnline=false;
                Isregis=true;
                lilay.setVisibility(View.VISIBLE);
            }
        });


        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_Terminal.this.finish();
            }

            @Override
            public void rightclick() {
                if (IsOnline&(!Isregis)) {
                    Intent intent = new Intent(Service_Terminal.this, Terminal_online_map.class);
                    Bundle mBundle = new Bundle();
                    if(mlist!=null) {
                        mBundle.putParcelableArrayList(Terminalonline_KEY, mlist);
                        intent.putExtras(mBundle);
                    }
                    startActivity(intent);
                }else if(Isregis&(!IsOnline)) {
                    Intent intent = new Intent(Service_Terminal.this, Terminal_allregister_map.class);
                    Bundle mBundle = new Bundle();
                    if(mlist!=null) {
                        mBundle.putParcelableArrayList(TerminalallRegister_KEY, mlist);
                        intent.putExtras(mBundle);
                    }
                    startActivity(intent);
                }

            }
        });
    }
    @Override
    public void onDestroy() {
        unregisterReceiver(contentReceiver);
        contentReceiver = null;
        super.onDestroy();
    }

    private ArrayList<Map<String, Object>> Arraylist2Items(ArrayList<List_TerminalOnline> mlist) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        for (List_TerminalOnline online : mlist) {
            Map<String, Object> data = new HashMap<>();
            data.put("section_num", online.getNum());
            data.put("section", online.getIDnum());
            //位置
            String str1 = online.getLongtitudeStyle() + String.valueOf(online.getLongitude());
            String str2 = online.getLatitudeStyle() + String.valueOf(online.getLatitude());
            String str3 = String.valueOf(online.getHeight());
            data.put("section_attriutes", str1 + "," + str2 + "," + str3);

            listItems.add(data);

        }
        return listItems;
    }

}
