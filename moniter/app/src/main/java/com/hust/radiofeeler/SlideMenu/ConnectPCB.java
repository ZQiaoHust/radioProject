package com.hust.radiofeeler.SlideMenu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.radiofeeler.Bean.Connect;
import com.hust.radiofeeler.Bean.Query;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.Mina.MinaClientService;
import com.hust.radiofeeler.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by H on 2015/10/24.
 */
public class ConnectPCB extends Activity {
    private TextView mIp;
    StringBuilder resultList;
    ArrayList<String> connectedIP;
    private String PCBIP;
    private int connectStyle;
    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private int ID=11;//硬件的设备ID号
    private  String IP="192.168.43.195";//WIFI的IP
    private Connect connect=null;

    private BroadcastReceiver ConnectPCBReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantValues.ConnectPCBQuery)) {
                connect = intent.getParcelableExtra("data");
                if (connect == null) {
                    return;
                }

                if(connect.getConn()==1)
                    Toast.makeText(ConnectPCB.this, "当前接入方式是：WIFI", Toast.LENGTH_SHORT).show();
                else if(connect.getConn()==2)
                    Toast.makeText(ConnectPCB.this, "当前接入方式是：蓝牙", Toast.LENGTH_SHORT).show();
                else if(connect.getConn()==3)
                    Toast.makeText(ConnectPCB.this, "当前接入方式是：USB", Toast.LENGTH_SHORT).show();

            }
        }
    };

//    private ArrayList<String> getConnectIp() throws Exception {
//        ArrayList<String> connectIpList = new ArrayList<>();
//        BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] splitted = line.split(" +");
//            if (splitted != null && splitted.length >= 4) {
//                String ip = splitted[0];
//                connectIpList.add(ip);
//            }
//        }
//        return connectIpList;
//    }

//    private String getConnectIp() throws Exception {
//        BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
//        String line;
//        line = br.readLine();
//        return line;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conectpcb);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.ConnectPCBQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(ConnectPCBReceiver, filter);
        spinner= (Spinner) findViewById(R.id.spinner_WIFI);
       // mIp = (TextView) findViewById(R.id.et_ID);
        initspinnerSetting();
        initEvent();
    }


    private void initEvent() {

        findViewById(R.id.ConnectPCB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mIp.setText("");
//                try {
//                    connectedIP = getConnectIp();
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                resultList = new StringBuilder();
//                for (String ip : connectedIP) {
//                    resultList.append(ip);
//                    resultList.append("\n");
//
//                }
//                String textString = resultList.toString();
//                Constants.PCBIP=textString;
//                mIp.setText(textString);

              //  mIp.setText("");
                if(connectStyle==0){
                    Toast.makeText(ConnectPCB.this, "请选择接入方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if(connectStyle==1) {
                        //获取连接到手机热点的硬件ip
//                        PCBIP = getConnectIp();
//                        Constants.PCBIP = PCBIP;
                        //启动后台service
                        Constants.IP=IP;
                        Intent startServiceIntent = new Intent(ConnectPCB.this, MinaClientService.class);
                        startService(startServiceIntent);
                        Constants.time=getTimeSec(0);
                        Constants.ID=ID;
                    }

                    Connect connect=new Connect();
                    connect.setConn(connectStyle);
                   Broadcast.sendBroadCast(ConnectPCB.this,ConstantValues.ConnectPCB,"connectPCB",connect);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        findViewById(R.id.getConnectPCB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.setequipmentID(Constants.ID);
                query.setFuncID((byte) 0x19);

                if (query != null) {
                    Broadcast.sendBroadCast(ConnectPCB.this,
                            ConstantValues.ConnectPCBQuery, "ConnectPCBQuery", query);
                }

           }
        });

        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectPCB.this.finish();
            }
        });


        findViewById(R.id.wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                connectStyle=1;
            }
        });

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                connectStyle=2;
            }
        });

        findViewById(R.id.usb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConnectPCB.this, "连接usb线", Toast.LENGTH_LONG).show();
                connectStyle=3;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ID=11;
                        IP="192.168.43.195";
                        break;
                    case 1:
                        ID=12;
                        IP="192.168.43.245";
                        break;
                    case 2:
                        ID=13;
                        IP="192.168.43.34";
                        break;
                    case 3:
                        ID=14;
                        IP="192.168.43.61";
                        break;
                    case 4:
                        ID=15;
                        IP="192.168.43.29";
                        break;
                }
                Log.d("FPGA","ID:"+ ID+"     IP:"+ IP);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(ConnectPCBReceiver);
        ConnectPCBReceiver = null;
        super.onDestroy();

    }
    private  void initspinnerSetting(){
        //1,设置数据源
        list = new ArrayList<String>();
        list.add("ID:11 /IP:192.168.43.195");
        list.add("ID:12 /IP:192.168.43.245");
        list.add("ID:13 /IP:192.168.43.34");
        list.add("ID:14 /IP:192.168.43.61");
        list.add("ID:15 /IP:192.168.43.29");

        //2.新建数组适配器
        adapter=new ArrayAdapter<String>(ConnectPCB.this,android.R.layout.simple_spinner_item,list);
        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner.setAdapter(adapter);
    }
    private String getTimeSec(int m) {
        //得到开始时刻
        Date date =new Date();
        long sec=date.getTime()/1000;
        if(m>0)
            sec=sec-m*60;
        return String.valueOf(sec);
    }
}
