package com.hust.radiofeeler.tab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.Station_CurrentRequst;
import com.hust.radiofeeler.compute.ComputePara;
import com.hust.radiofeeler.view.DateTimePickDialogUtil2Mius;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/10/27.
 */
public class Stations_current extends Fragment  {

    private EditText et_ID;
    private EditText et_frequecy;

    private Spinner spinner_location, spinner_IQ;
    private List<String> list1, list2;
    private ArrayAdapter<String> adapter1, adapter2;
    TimePickerView pvTime;

    private EditText et_IQblock,et_inputtime;
    private ComputePara computePara =new ComputePara();
    private byte locationWay=0;
    private byte IQband_radio=0x11;//界面初始化对应
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stations_current, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        initspinnerSetting();
        InitEvent();
    }


    private void InitSetting() {

        et_frequecy = (EditText) getActivity().findViewById(R.id.et_radioPoint);
        et_ID = (EditText) getActivity().findViewById(R.id.et_ID);


        spinner_location = (Spinner) getActivity().findViewById(R.id.spinner_location);
        spinner_IQ = (Spinner) getActivity().findViewById(R.id.spinner_IQ);

        et_IQblock = (EditText) getActivity().findViewById(R.id.et_IQblock);
        et_inputtime= (EditText) getActivity().findViewById(R.id.inputDate_current);

        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.ALL);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
               // et_inputtime.setText(pvTime.getTime());
                et_inputtime.setText(date);
            }
        });

    }

    private void initspinnerSetting() {

        //1,设置数据源
        list2 = new ArrayList<String>();
        list2.add("5/5");
        list2.add("2.5/2.5");
        list2.add("1/1");
        list2.add("0.5/0.5");
        list2.add("0.1/0.1");

        //2.新建数组适配器
        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list2);

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
        adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list1);

        //adapter设置一个下拉列表样式
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner_location.setAdapter(adapter1);

    }

    private void InitEvent() {
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
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
                switch (position){
                    case 0:
                        IQband_radio=0x11;
                        break;
                    case 1:
                        IQband_radio=0x22;
                        break;
                    case 2:
                        IQband_radio=0x33;
                        break;
                    case 3:
                        IQband_radio=0x44;
                        break;
                    case 4:
                        IQband_radio=0x55;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        et_inputtime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DateTimePickDialogUtil2Mius dateTimePicKDialog = new DateTimePickDialogUtil2Mius(getActivity());
//                dateTimePicKDialog.dateTimePicKDialog(et_inputtime);
//            }
//        });
        et_inputtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        getActivity().findViewById(R.id.btn_querycurrent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Station_CurrentRequst curretnt=new Station_CurrentRequst();
                 try {
                     curretnt.setEquiomentID(Constants.ID);
                     if(!et_ID.getText().toString().equals("")) {
                         curretnt.setIDcard(Integer.parseInt(et_ID.getText().toString()));
                     }
                     curretnt.setLocationWay(locationWay);

                    if(!et_frequecy.getText().toString().equals("")) {
                        curretnt.setAbFreq(Double.valueOf(et_frequecy.getText().toString()));
                    }
                     curretnt.setIQband_radio(IQband_radio);
                     if(!et_IQblock.getText().toString().equals("")) {
                         curretnt.setBlockNum(Integer.parseInt(et_IQblock.getText().toString()));
                     }
                     if(!et_inputtime.getText().toString().equals("")) {
                         byte[] bytes = computePara.Time2Bytes(et_inputtime.getText().toString());
                         curretnt.setTime2min(bytes);
                     }

                     Broadcast.sendBroadCast(getActivity(),
                             ConstantValues.STATION_CURRENT, "station_current", curretnt);
                     Intent intent = new Intent(getActivity(), Stations_currentResult.class);
                     startActivity(intent);
                 }catch (Exception e){
                     Toast.makeText(getActivity(),"输入数据不能为空",Toast.LENGTH_SHORT).show();
                 }

            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return format.format(date);
    }

}
