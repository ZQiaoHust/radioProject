package com.example.administrator.testsliding.SlideMenu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.testsliding.Bean.StationState;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.QueryFPGANetwork;
import com.example.administrator.testsliding.bean2server.RequstNetwork;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2015/10/24.
 */
public class FinalStationState extends Activity {
    private Button  btn_requestNet,btn_getpcb;
    private TextView tv_ID,tv_style,tv_location;
    private LinearLayout mLinearLayout;

    private QueryFPGANetwork fpgaInfo=null;

    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private int ID=0;//硬件的型号编码
    private BroadcastReceiver StationStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantValues.StationStateQuery)) {

                StationState data = intent.getParcelableExtra("data");
                if (data == null) {
                    return;
                }

                String s1net;

                String s2model=null;
                Constants.ID=data.getEquipmentID();//将ID号存下
                int onNet=data.getOnNet();
                int model=data.getModel();
                int eastORwest=data.getEastORwest();
                int northORsouth=data.getNorthORsouth();
                double longtitude=data.getLongtitude();
                double latitude=data.getLatitude();
                int isAboveHrizon=data.getIsAboveHrizon();
                int atitude=data.getAtitude();

                if(onNet==0x0f){
                    s1net="在网用户";
                }else{
                    s1net="不在网";
                }

                switch(model){
                    case 0x00:
                        s2model="专业用户终端";
                        break;
                    case 0x01:
                        s2model="普通用户终端";
                        break;
                    case 0x02:
                        s2model="专业查询终端";
                        break;
                    case 0x03:
                        s2model="普通查询终端";
                        break;
                }

                Toast toast=Toast.makeText(FinalStationState.this, "该终端是："
                        +s1net+"的"+s2model, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 400);
                toast.show();


            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station);
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstantValues.StationStateQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(StationStateReceiver, filter);
        Initing();
        initspinnerSetting();
        InitEvent();

    }
    private void Initing(){
        btn_requestNet= (Button) findViewById(R.id.btn_requestNetwork);
        btn_getpcb= (Button) findViewById(R.id.btn_getpcbinfo);
        mLinearLayout= (LinearLayout) findViewById(R.id.pcbinfo);
        tv_ID= (TextView) findViewById(R.id.tv_ID);
        tv_style= (TextView) findViewById(R.id.tv_style);
        tv_location= (TextView) findViewById(R.id.tv_location);
        spinner= (Spinner) findViewById(R.id.spinner_FPGA);
    }
    private  void initspinnerSetting(){

        //1,设置数据源
        list = new ArrayList<String>();
        list.add("");
        list.add("SRF201");
        list.add("SRF301");

        //2.新建数组适配器
        adapter=new ArrayAdapter<String>(FinalStationState.this,android.R.layout.simple_spinner_item,list);

        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner.setAdapter(adapter);
    }
    private void InitEvent(){
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalStationState.this.finish();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ID=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_getpcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout.setVisibility(View.VISIBLE);
                fpgaInfo=new QueryFPGANetwork();

            }
        });

        btn_requestNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequstNetwork net=new RequstNetwork();
                if(fpgaInfo!=null){
//                    net.setEquipmentID(fpgaInfo.getEquipmentID());
//                    net.setStyle(fpgaInfo.getStyle());
//                    net.setLongtitude(fpgaInfo.getLongtitude());
//                    net.setLatitude(fpgaInfo.getLatitude());
//                    net.setHeight(fpgaInfo.getHeight());
                    net.setEquipmentID(Constants.ID);
                    net.setStyle((byte) 1);

                    byte[] bytes=new byte[4];
                    bytes[0]=0;

                    bytes[1]=30;
                    bytes[2]= (byte) (132&0xff);
                    bytes[3]=95;
                    net.setLatitude(bytes);
                    byte[] bytes1=new byte[3];
                    bytes1[0]=114&0xff;
                    bytes1[1]=108;
                    bytes1[2]= (byte) (174&0xff);
                    net.setLongtitude(bytes1);
                    byte[] bytes2=new byte[2];
                    net.setHeight(bytes2);
                    Broadcast.sendBroadCast(FinalStationState.this,
                            ConstantValues.REQUSTNETWORK, "network", net);
                    //启动与中心站连接的service
//                    Intent intent = new Intent(FinalStationState.this, ToServerMinaService.class);
//                    startService(intent);

                }
                else {
                    Toast.makeText(FinalStationState.this,"请先查询FPGA的信息",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(StationStateReceiver);
        StationStateReceiver=null;
        super.onDestroy();
    }
}
