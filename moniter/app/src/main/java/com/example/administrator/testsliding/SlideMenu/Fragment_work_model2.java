package com.example.administrator.testsliding.SlideMenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.administrator.testsliding.Bean.FixCentralFreq;
import com.example.administrator.testsliding.Bean.FixSetting;
import com.example.administrator.testsliding.Bean.Query;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.view.DateTimePickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by H on 2015/9/10.
 */
public class Fragment_work_model2 extends Fragment {

    private EditText et_frequency01;
    private EditText et_frequency02;
    private EditText et_frequency03;

    private Spinner spinner_IQ;
    private List<String> list;
    private ArrayAdapter<String> adapter;

    private EditText et_IQblock;
    private EditText inputDate;

    private Button mSetCentralFreq;
    private Button mGetCentralFreq;
    private Button mSetIQ;
    private Button mGetIQ;
    TimePickerView pvTime;

    ArrayList<EditText> editList;
    private double[] v1=null;
    private int FreqNumber;
    //全局变量
    private double IQwidth=5;//与界面默认值一致
    private int blockNum;

    private ComputePara computePara=new ComputePara();


    private BroadcastReceiver fixFreqReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if(action.equals(ConstantValues.FixCentralFreqQuery)){
                FixCentralFreq data=intent.getParcelableExtra("data");
                if (data==null){
                    return;
                }
                int number=data.getNumber();
                double fix1=data.getFix1();
                double fix2=data.getFix2();
                double fix3=data.getFix3();


                Toast toast=Toast.makeText(getActivity(), "定频接受中心频率："+number+"个"+"\n"+
                                 String.valueOf(fix1)+" MHz, "
                                +String.valueOf(fix2)+" MHz, "+String.valueOf(fix3)+"MHz", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 600);
                toast.show();
            }

            if(action.equals(ConstantValues.FixSettingQuery)){
                FixSetting data=intent.getParcelableExtra("data");
                if (data==null){
                    return;
                }

                double IQwidth=data.getIQwidth();

                int year=data.getYear();
                int month=data.getMonth();
                int day=data.getDay();
                int hour=data.getHour();//UTC时间转北京时间
                int min=data.getMinute();
                int second=data.getSecond();


                Toast toast=Toast.makeText(getActivity(), "IQ复信号带宽为:"+String.valueOf(IQwidth)
                       +"MHz"+"\n" +
                        "数据率:"+String.valueOf(IQwidth)+"Msps"+"\n"+
                        "起始时间是:"+String.valueOf(year)+"年"
                        +String.valueOf(month)+"月"+String.valueOf(day)+" 日 "+String.valueOf(hour)+
                        ":"+String.valueOf(min)+ ":"+String.valueOf(second), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 800);
                toast.show();
            }


        }
    };


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitSetting();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.FixSettingQuery);
        filter.addAction(ConstantValues.FixCentralFreqQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(fixFreqReceiver, filter);
        initspinnerSetting();
        InitEvent();


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_fragment_work_model2, container, false);
    }

    private void InitSetting(){
        et_frequency01=(EditText)getActivity().findViewById(R.id.et_frequency01);
        et_frequency02=(EditText)getActivity().findViewById(R.id.et_frequency02);
        et_frequency03=(EditText)getActivity().findViewById(R.id.et_frequency03);
        spinner_IQ=(Spinner)getActivity().findViewById(R.id.spinner_IQ);
        inputDate= (EditText) getActivity().findViewById(R.id.inputDate);
        et_IQblock=(EditText)getActivity().findViewById(R.id.et_IQblock);
        mSetCentralFreq= (Button) getActivity().findViewById(R.id.bt_setCentralFreq);
        mGetCentralFreq= (Button) getActivity().findViewById(R.id.bt_getCentralFreq);
        mSetIQ= (Button) getActivity().findViewById(R.id.bt_setIQ);
        mGetIQ= (Button) getActivity().findViewById(R.id.bt_getIQ);
        editList=new ArrayList<>();
        editList.add(et_frequency01);
        editList.add(et_frequency02);
        editList.add(et_frequency03);

        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.ALL);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                inputDate.setText(getTime(date));
            }
        });
    }
    /**
     * spinner初始化
     *
     */

    private  void initspinnerSetting(){

        //1,设置数据源
        list = new ArrayList<String>();
        list.add("5/5");
        list.add("2.5/2.5");
        list.add("1.25/1.25");
        list.add("0.625/0.625");
        list.add("0.125/0.125");

        //2.新建数组适配器
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);

        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner_IQ.setAdapter(adapter);
        spinner_IQ.setSelection(0,true);
    }
    private void InitEvent(){

        spinner_IQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        IQwidth = 5;
                        break;
                    case 1:
                        IQwidth = 2.5;
                        break;
                    case 2:
                        IQwidth = 1;
                        break;
                    case 3:
                        IQwidth = 0.5;
                        break;
                    case 4:
                        IQwidth = 0.1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * 设置定频中心频率
         */
        mSetCentralFreq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FreqNumber=0;
                v1=new double[3];
                for(int i=0;i<3;i++){

                    if(!editList.get(i).getText().toString().equals("")){
                        double mm=Double.parseDouble(editList.get(i).getText().toString());
                        v1[i]=mm;
                        FreqNumber++;
                    }
                }
                if(FreqNumber!=0){
                    FixCentralFreq fixCentralFreq=new FixCentralFreq();
                    fixCentralFreq.setNumber(FreqNumber);
                    fixCentralFreq.setFix1(v1[0]);
                    fixCentralFreq.setFix2(v1[1]);
                    fixCentralFreq.setFix3(v1[2]);
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.FixCentralFreqSet, "FixCentralFreqSet", fixCentralFreq);
                }else {
                    Toast.makeText(getActivity(),"请填写中心频率",Toast.LENGTH_SHORT).show();

                }

            }
        });

        mGetCentralFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x12);

                if (query != null) {
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.FixCentralFreqQuery, "FixCentralFreqQuery", query);
                }

            }
        });

        /**
         * 设置定频参数
         */

        mSetIQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    blockNum=Integer.parseInt(et_IQblock.getText().toString());
                    FixSetting fixSetting=new FixSetting();
                    fixSetting.setIQwidth(IQwidth);
                    fixSetting.setBlockNum(blockNum);
                    if(!inputDate.getText().toString().equals("")){
                        fixSetting.setTimeString(inputDate.getText().toString());
                    }

                    if(fixSetting!=null){
                        Broadcast.sendBroadCast(getActivity(),ConstantValues.FixSettingSet,
                                "FixSettingSet",fixSetting);
                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(),"请填写参数",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mGetIQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x17);

                if(query!=null){
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.FixSettingQuery,"FixSettingQuery",query);
                }
            }
        });
        inputDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(fixFreqReceiver);
        fixFreqReceiver=null;
        super.onDestroy();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return format.format(date);
    }

}
