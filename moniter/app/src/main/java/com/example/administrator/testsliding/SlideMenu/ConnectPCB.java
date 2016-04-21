package com.example.administrator.testsliding.SlideMenu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.testsliding.Bean.Connect;
import com.example.administrator.testsliding.Bean.Query;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.Mina.MinaClientService;
import com.example.administrator.testsliding.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by H on 2015/10/24.
 */
public class ConnectPCB extends Activity {
    private TextView mIp;
    StringBuilder resultList;
    ArrayList<String> connectedIP;
    private String PCBIP;
    private int connectStyle;

    private BroadcastReceiver ConnectPCBReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantValues.ConnectPCBQuery)) {


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

       // mIp = (TextView) findViewById(R.id.et_ID);
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
                        Intent startServiceIntent = new Intent(ConnectPCB.this, MinaClientService.class);
                        startService(startServiceIntent);
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
                query.setequipmentID(0);
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
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(ConnectPCBReceiver);
        ConnectPCBReceiver = null;
        super.onDestroy();

    }
}
