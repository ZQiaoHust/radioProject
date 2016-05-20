package com.hust.radiofeeler.tab1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bigkoo.pickerview.TimePickerView;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.HistoryIQRequest;
import com.hust.radiofeeler.compute.ComputePara;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2015/10/26.
 */
public class Service_IQ extends Activity {
    private EditText startDateTime;
    private EditText endDateTime;
    private EditText et_ID;
    private Button btn_set;
    private ComputePara compute=new ComputePara();
    TimePickerView pvTime1 ,pvTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_iq);
//        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_servicestation);
//        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
//            @Override
//            public void leftclick() {
//                Service_IQ.this.finish();
//            }
//
//            @Override
//            public void rightclick() {
//
//            }
//        });

        initSetting();
        InitEvent();
    }
    private void initSetting() {

        et_ID = (EditText)findViewById(R.id.et_ID);
        // 两个输入框
        startDateTime = (EditText)findViewById(R.id.inputDate);
        endDateTime = (EditText)findViewById(R.id.inputDate2);
        btn_set= (Button) findViewById(R.id.btn_ID_time);
        //时间选择器
        pvTime1 = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY_HOURS_MINS);
        pvTime2 = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY_HOURS_MINS);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        pvTime1.setTime(new Date());
        pvTime1.setCyclic(false);
        pvTime1.setCancelable(true);
        //时间选择后回调
        pvTime1.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
//                startDateTime.setText(pvTime1.getTime());
                startDateTime.setText(date);
            }
        });
        pvTime2.setTime(new Date());
        pvTime2.setCyclic(false);
        pvTime2.setCancelable(true);
        //时间选择后回调
        pvTime2.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
                //endDateTime.setText(pvTime2.getTime());
                endDateTime.setText(date);
            }
        });

    }

    private void InitEvent() {
//        startDateTime.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil( Service_IQ.this);
//                dateTimePicKDialog.dateTimePicKDialog(startDateTime);
//
//            }
//        });
//
//        endDateTime.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//                        Service_IQ.this);
//                dateTimePicKDialog.dateTimePicKDialog(endDateTime);
//            }
//        });
        //弹出时间选择器
        startDateTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime1.show();
            }
        });
        endDateTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime2.show();
            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryIQRequest IQ=new HistoryIQRequest();
                try {
                    IQ.setEqiupmentID(Constants.ID);
                    if (!et_ID.getText().toString().equals("")) {
                        IQ.setIDcard(Integer.parseInt(et_ID.getText().toString()));
                    }
                    if (!startDateTime.getText().toString().equals("")) {
                        byte[] bytes = compute.Time2Bytes(startDateTime.getText().toString());
                        IQ.setStartTime(bytes);
                    }
                    if (!endDateTime.getText().toString().equals("")) {
                        byte[] bytes = compute.Time2Bytes(endDateTime.getText().toString());
                        IQ.setEndTime(bytes);
                    }
                    Broadcast.sendBroadCast(Service_IQ.this,
                            ConstantValues.SERVICE_IQ,"service_IQ",IQ);
                }catch (Exception e){

                }
            }
        });

    }

    //ActionBar菜单栏 模块
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_iq, menu);
        return true;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return format.format(date);
    }
}
