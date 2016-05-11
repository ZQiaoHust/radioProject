package com.hust.radiofeeler.tab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.Send_ServiceRadio;
import com.hust.radiofeeler.view.MyTopBar;


/**
 * Created by Administrator on 2015/10/26.
 */
public class Service_radio extends Activity {
    private EditText et_start,et_end;
    private MyTopBar topBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_radio);
        Init();
        InitEvent();

    }


    private void Init(){
       et_start= (EditText) findViewById(R.id.et_radioStart);
        et_end= (EditText) findViewById(R.id.et_radioEnd);
        topBar= (MyTopBar) findViewById(R.id.topbar_radio);
    }

    private void InitEvent() {
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_radio.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent(Service_radio.this, Service_radioResult.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_sendradio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取值并下发
                Send_ServiceRadio radio = new Send_ServiceRadio();
                radio.setEquipmentID(Constants.ID);


                if((et_start.getText().toString())!=null&&(et_end.getText().toString())!=null) {
                    try {
                        double  ss= Double.valueOf(et_start.getText().toString());
                        double ss2 = Double.valueOf(et_end.getText().toString());
                        if (ss<= ss2) {
                            int start = (int) Math.floor(ss);
                            int end = (int) Math.ceil(ss2);
                            radio.setStartFrequency(start);
                            radio.setEndFrequency(end);
                            // 点击发送消息到服务器
                            Broadcast.sendBroadCast(Service_radio.this,
                                    ConstantValues.WIRLESSPLAN, "wirlessplan", radio);
                            Intent intent = new Intent(Service_radio.this, Service_radioResult.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Service_radio.this, "输入数据有误!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch(Exception e){
                        Toast.makeText(Service_radio.this,"输入数据不能为空!",Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
    }


}
