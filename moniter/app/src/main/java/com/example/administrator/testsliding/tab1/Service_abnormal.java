package com.example.administrator.testsliding.tab1;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.LocationAbnormalRequest;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.map.Service_abnormal_map;
import com.example.administrator.testsliding.view.DateTimePickDialogUtil2Mius;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/26.
 */
public class Service_abnormal extends Activity {

    private EditText et_locationpoint;
    private MyTopBar topbar;
    private Spinner spinner_location,spinner_IQ;
    private List<String> list1,list2;
    private ArrayAdapter<String> adapter1,adapter2;

    private EditText et_IQblock,et_inputtime,et_miao;
    private byte locationWay=0;
    private byte IQband_radio=0x11;//界面初始化对应
    private ComputePara computePara =new ComputePara();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_abnormal);

        InitSetting();
        initspinnerSetting();
        InitEvent();

    }

    private void InitSetting(){
        topbar= (MyTopBar) findViewById(R.id.topBar);
        et_locationpoint=(EditText)findViewById(R.id.et_radioPoint);
        spinner_location=(Spinner)findViewById(R.id.spinner_location);
        spinner_IQ=(Spinner)findViewById(R.id.spinner_IQ);

        et_IQblock=(EditText)findViewById(R.id.et_IQblock);

        et_inputtime= (EditText) findViewById(R.id.inputDate_abnormal);
    }

    private  void initspinnerSetting() {

        //1,设置数据源
        list2 = new ArrayList<String>();
        list2.add("5/5");
        list2.add("2.5/2.5");
        list2.add("1/1");
        list2.add("0.5/0.5");
        list2.add("0.1/0.1");

        //2.新建数组适配器
        adapter2 = new ArrayAdapter<String>(Service_abnormal.this, android.R.layout.simple_spinner_item, list2);

        //adapter设置一个下拉列表样式
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner_IQ.setAdapter(adapter2);
        spinner_IQ.setSelection(0, true);

        //1,设置数据源
        list1 = new ArrayList<String>();
        list1.add("POA");
        list1.add("TDOA/POA");


        //2.新建数组适配器
        adapter1 = new ArrayAdapter<String>(Service_abnormal.this, android.R.layout.simple_spinner_item, list1);

        //adapter设置一个下拉列表样式
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner_location.setAdapter(adapter1);

    }

    private void InitEvent() {
        topbar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_abnormal.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent(Service_abnormal.this, Service_abnormal_map.class);
                startActivity(intent);
            }
        });
        et_inputtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil2Mius dateTimePicKDialog = new DateTimePickDialogUtil2Mius(Service_abnormal.this);
                dateTimePicKDialog.dateTimePicKDialog(et_inputtime);
            }
        });
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        locationWay = 0x00;
                        break;
                    case 1:
                        locationWay = 0x0F;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_IQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        IQband_radio = 0x11;
                        break;
                    case 1:
                        IQband_radio = 0x22;
                        break;
                    case 2:
                        IQband_radio = 0x33;
                        break;
                    case 3:
                        IQband_radio = 0x44;
                        break;
                    case 4:
                        IQband_radio = 0x55;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.btn_setLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationAbnormalRequest abnormal=new LocationAbnormalRequest();
                try {
                    abnormal.setEquiomentID(Constants.ID);
                    abnormal.setLocationWay(locationWay);
                    if(!et_locationpoint.getText().toString().equals("")) {
                        abnormal.setAbFreq(Double.parseDouble(et_locationpoint.getText().toString()));
                    }
                    abnormal.setIQband_radio(IQband_radio);
                    if(!et_IQblock.getText().toString().equals("")) {
                        abnormal.setBlockNum(Integer.parseInt(et_IQblock.getText().toString()));
                    }
                    if(!et_inputtime.getText().toString().equals("")) {
                        byte[] bytes = computePara.Time2Bytes(et_inputtime.getText().toString());
                        abnormal.setTime2min(bytes);
                    }
                    Broadcast.sendBroadCast(Service_abnormal.this,
                            ConstantValues.ABNORMAL_LOCATION, "abnormal_location", abnormal);
                    Intent intent = new Intent(Service_abnormal.this, Service_locationResult.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(Service_abnormal.this,"请设置参数",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
