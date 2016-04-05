package com.example.administrator.testsliding.SlideMenu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.testsliding.Bean.Press;
import com.example.administrator.testsliding.Bean.PressSetting;
import com.example.administrator.testsliding.Bean.Query;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */
public class Fragment_work_model3  extends Fragment implements
        RadioGroup.OnCheckedChangeListener {


    //两种压制模式选择面板
    private LinearLayout lilay_single;
    private LinearLayout lilay_double;

    private RadioGroup rg_choose;//压制的频点是否自定义
    private RadioGroup rg_press;//压制模式的两个按钮
    private RadioGroup rg_way;//压制方式的两个按钮
    //自定义模式下的选中框
    private CheckBox cb_firstPoint;
    private CheckBox cb_secondPoint;

    private LinearLayout lilay_defined;
    //自定义模式下的编辑框
    private EditText edit_firstPoint;
    private EditText edit_secondPoint;

    //单信号模式下的参数
    private EditText et_T1;//压制时间
    private EditText et_T2;//扫频时间

    //双信号模式下的参数
    private EditText et_doubleT1;//总压制时间
    private EditText et_doubleT2;//扫频时间
    private EditText et_doubleT3;//分别压制时长
    private EditText et_doubleT4;//分别压制时长

    private Spinner spin1,spin2;
    private List<String> list1,list2;
    private ArrayAdapter<String> adapter1,adapter2;

    private Button mSetPressFreq;
    private Button mGetPressFreq;
    private ArrayList<EditText> editList;
    private double[] v1=null;
    private int FreqNumber;


    //压制的三个按钮
    private Button btn_stopPress;
    private Button btn_startPress;
    private Button mGetPressSetting;

    //全局变量
    private int pressMode;
    private int style=1;//与界面一致
    private int band=1;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private Boolean isSingle;
    private Boolean isAuto;

    private BroadcastReceiver PressReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(ConstantValues.PressQuery)){
                Press data=intent.getParcelableExtra("data");
                if(data==null){
                    return;
                }
                int pressNum=data.getNumber();
                double freq1=data.getFix1();
                double freq2=data.getFix2();
                if(pressNum==1){
                    Toast toast=Toast.makeText(getActivity(), "压制发射频率："+String.valueOf(freq1)+"MHz"
                            , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP , 0, 400);
                    toast.show();
                }
                if(pressNum==2){
                    Toast toast=Toast.makeText(getActivity(), "压制发射频率："+String.valueOf(freq1)
                            +"MHz 和 "+String.valueOf(freq2)+"MHz", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP , 0, 400);
                    toast.show();

                }
            }

            if(action.equals(ConstantValues.PressSettingQuery)){
                PressSetting data=intent.getParcelableExtra("data");
                if(data==null){
                    return;
                }
                int pressMode=data.getPressMode();
                int style=data.getStyle();
                int band=data.getBand();
                int t1= data.getT1();
                int t2= data.getT2();
                int t3= data.getT3();
                int t4= data.getT4();

                String s1mode=null;
                String s2style=null;
                String s3band=null;
                String s4time=null;
                String s5double;
                switch (pressMode){
                    case 0x01:
                        s1mode="压制发射模式是只对一个频点进行自动压制";
                        break;
                    case 0x02:
                        s1mode="压制发射模式是只对一个频点进行手动压制";
                        break;
                    case 0x03:
                        s1mode="压制发射模式是对两个频点进行自动循环压制";
                        break;
                    case 0x04:
                        s1mode="压制发射模式是对两个频点进行手动循环压制";
                        break;
                    case 0x05:
                        s1mode="压制发射模式是结束压制发射";
                        break;
                    default:
                        break;
                }
                switch (style){
                    case 0x1:
                        s2style="压制发射信号是单频正弦波信号";
                        break;
                    case 0x2:
                        s2style="压制发射信号是等幅多频信号";
                        break;
                    case 0x3:
                        s2style="压制发射信号是噪声调频信号";
                        break;
                    case 0x4:
                        s2style="压制发射信号是数字射频存储DRM信号";
                        break;
                    default:
                        break;
                }

                switch (band){
                    case 0x1:
                        s3band="压制发射的信号带宽是单谱线";
                        break;
                    case 0x2:
                        s3band="压制发射的信号带宽是0.5MHz";
                        break;
                    case 0x3:
                        s3band="压制发射的信号带宽是直接IQ调制发射";
                        break;
                    default:
                        break;
                }

                if(pressMode==0x01||pressMode==0x02){
                    s4time="压制时长是"+String.valueOf(t1)+"扫频时长是"+String.valueOf(t2);

                }else if(pressMode==0x03||pressMode==0x04){
                    s4time="总压制时长是"+String.valueOf(t1)+"扫频时长是"+String.valueOf(t2)
                    +"单次循环中压制第一个频点的时长是"+String.valueOf(t3)
                            +"单次循环中压制第二个频点的时长是"+String.valueOf(t4);

                }


                Toast toast=Toast.makeText(getActivity(),s1mode+s2style+s3band+s4time, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 1000);
                toast.show();


            }
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstantValues.PressQuery);
        filter.addAction(ConstantValues.PressSettingQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(PressReceiver,filter);
        initspinnerSetting();
        InitEvent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_work_model3,container,false);
    }

    /**
     * 初始化参数
     */

    private void InitSetting(){
        //单双信号 和 手动自动压制全局变量初始化
        isSingle=true;
        isAuto=true;
        pressMode=0x01;
        t1=0;
        t2=0;
        t3=0;
        t4=0;
        //绑定groupRadio
        rg_choose= (RadioGroup)getActivity().findViewById(R.id.radioGroup_choose);
        rg_press= (RadioGroup)getActivity().findViewById(R.id.radioGroup_press);
        rg_way= (RadioGroup) getActivity().findViewById(R.id.radioGroup_way);
        //自定义压制频率
        lilay_defined= (LinearLayout) getActivity().findViewById(R.id.liLay_defined);

        edit_firstPoint= (EditText)getActivity().findViewById(R.id.et_firstPressPoint);
        edit_secondPoint= (EditText)getActivity().findViewById(R.id.et_secondPressPoint);
        //绑定两种扫频模式的面板
        lilay_single= (LinearLayout)getActivity().findViewById(R.id.reLay_singlePress);
        lilay_double= (LinearLayout)getActivity().findViewById(R.id.reLay_doublePress);


        //压制的三个按钮
        btn_startPress= (Button)getActivity().findViewById(R.id.bt_startPress);
        btn_stopPress= (Button)getActivity().findViewById(R.id.bt_stopPress);
        mGetPressSetting= (Button) getActivity().findViewById(R.id.bt_getPressSetting);

        spin1= (Spinner)getActivity().findViewById(R.id.spinner_radiostyle);
        spin2= (Spinner)getActivity().findViewById(R.id.spinner_radioband);

        mSetPressFreq= (Button) getActivity().findViewById(R.id.bt_setPressFreq);
        mGetPressFreq= (Button) getActivity().findViewById(R.id.bt_getPressFreq);
        //输入的文本框
        et_T1= (EditText) getActivity().findViewById(R.id.et_T1);
        et_T2= (EditText) getActivity().findViewById(R.id.et_T2);

        et_doubleT1= (EditText) getActivity().findViewById(R.id.et_doubleT1);
        et_doubleT2= (EditText) getActivity().findViewById(R.id.et_doubleT2);
        et_doubleT3= (EditText) getActivity().findViewById(R.id.et_doubleT3);
        et_doubleT4= (EditText) getActivity().findViewById(R.id.et_doubleT4);
    }

    private  void initspinnerSetting(){

        //1,设置数据源
        list1 = new ArrayList<String>();
        list1.add("单频正弦波");
        list1.add("等幅多频信号");
        list1.add("噪声低频信号");
        list1.add("数字射频DRM");
        //2.新建数组适配器
        adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list1);

        //adapter设置一个下拉列表样式
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spin1.setAdapter(adapter1);

        list2 = new ArrayList<String>();
        list2.add("单谱线");
        list2.add("0.5MHz");
        list2.add("IQ调制发射");

        //2.新建数组适配器
        adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list2);

        //adapter设置一个下拉列表样式
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spin2.setAdapter(adapter2);


        editList=new ArrayList<>();
        v1=new double[2];
        editList.add(edit_firstPoint);
        editList.add(edit_secondPoint);
    }
    /**
     * 初始化事件
     */
    private void InitEvent(){
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        style=0x1;
                        break;
                    case 1:
                        style=0x2;
                        break;
                    case 2:
                        style=0x3;
                        break;
                    case 3:
                        style=0x4;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        band=0x1;
                        break;
                    case 1:
                        band=0x2;
                        break;
                    case 2:
                        band=0x3;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        rg_choose.setOnCheckedChangeListener(this);
        rg_press.setOnCheckedChangeListener(this);
        rg_way.setOnCheckedChangeListener(this);
        /**
         * 压制频率
         */
        mSetPressFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreqNumber=0;
                Press press = new Press();
                for (int i = 0; i < 2; i++) {

                    if (!editList.get(i).getText().toString().equals("")) {
                        double mm = Double.parseDouble(editList.get(i).getText().toString());
                        v1[i] = mm;
                        FreqNumber++;
                    }
                }
                if(FreqNumber!=0){
                    press.setNumber(FreqNumber);
                    press.setFix1(v1[0]);
                    press.setFix2(v1[1]);
                    Constants.press=press;
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.PressSet, "PressSet", press);
                }else {
                    Toast.makeText(getActivity(),"请填写压制频点",Toast.LENGTH_SHORT).show();

                }




            }
        });

        mGetPressFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x13);

                if(query!=null){
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.PressQuery,"PressQuery",query);
                }

            }
        });

        /**
         * 压制参数设置
         */

        btn_startPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    PressSetting pressSetting=new PressSetting();
                    if(isAuto&&isSingle){
                        //自动单频点
                        Constants.pressModel=1;
                        pressMode=0x01;
                        pressSetting.setPressMode(pressMode);
                        t1=Integer.parseInt(et_T1.getText().toString());
                        t2=Integer.parseInt(et_T2.getText().toString());
                        pressSetting.setStyle(style);
                        pressSetting.setBand(band);
                        pressSetting.setT1(t1);
                        pressSetting.setT2(t2);
                        pressSetting.setT3(t3);
                        pressSetting.setT4(t4);
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.PressSettingSet,"PressSettingSet",pressSetting);
                    }else if(isSingle&&!isAuto){
                        pressMode=0x02;
                        Constants.pressModel=0;
                        pressSetting.setPressMode(pressMode);
                        t1=Integer.parseInt(et_T1.getText().toString());
                        t2=Integer.parseInt(et_T2.getText().toString());
                        pressSetting.setStyle(style);
                        pressSetting.setBand(band);
                        pressSetting.setT1(t1);
                        pressSetting.setT2(t2);
                        pressSetting.setT3(t3);
                        pressSetting.setT4(t4);
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.PressSettingSet,"PressSettingSet",pressSetting);
                    }else if(!isSingle&&isAuto){
                        //双频点自动压制
                        Constants.pressModel=3;
                        pressMode=0x03;
                        pressSetting.setPressMode(pressMode);
                        t1=Integer.parseInt(et_doubleT1.getText().toString());
                        t2=Integer.parseInt(et_doubleT2.getText().toString());
                        t3=Integer.parseInt(et_doubleT3.getText().toString());
                        t4=Integer.parseInt(et_doubleT4.getText().toString());
                        pressSetting.setStyle(style);
                        pressSetting.setBand(band);
                        pressSetting.setT1(t1);
                        pressSetting.setT2(t2);
                        pressSetting.setT3(t3);
                        pressSetting.setT4(t4);
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.PressSettingSet, "PressSettingSet", pressSetting);
                    }else if(!isSingle&&!isAuto){
                        pressMode=0x04;
                        Constants.pressModel=0;
                        pressSetting.setPressMode(pressMode);
                        t1=Integer.parseInt(et_doubleT1.getText().toString());
                        t2=Integer.parseInt(et_doubleT2.getText().toString());
                        t3=Integer.parseInt(et_doubleT3.getText().toString());
                        t4=Integer.parseInt(et_doubleT4.getText().toString());
                        pressSetting.setStyle(style);
                        pressSetting.setBand(band);
                        pressSetting.setT1(t1);
                        pressSetting.setT2(t2);
                        pressSetting.setT3(t3);
                        pressSetting.setT4(t4);
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.PressSettingSet, "PressSettingSet", pressSetting);
                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(),"请设置参数",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_stopPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 下发停止压制的包，并且使当前工作状态发生改变
                 */
                PressSetting pressSetting=new PressSetting();
                pressSetting.setPressMode(0x05);
                if(pressSetting !=null)
                {
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.PressSettingSet, "PressSettingSet", pressSetting);
                }
            }
        });


        mGetPressSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x18);

                if(query!=null){
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.PressSettingQuery,"PressSettingQuery",query);
                }
            }
        });



    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(group.getId()){

            case R.id.radioGroup_press:
                switch(checkedId) {
                    case R.id.rbtn_single:
                        /**
                         * 单信号模式显示
                         */
                        isSingle=true;
                        lilay_single.setVisibility(View.VISIBLE);
                        lilay_double.setVisibility(View.GONE);
                        break;
                    case R.id.rbtn_double:
                        /**
                         * 双信号模式显示
                         */
                        isSingle=false;
                        lilay_single.setVisibility(View.GONE);
                        lilay_double.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.radioGroup_way:
                switch(checkedId) {
                    case R.id.rbtn_autoPress:
                        /**
                         * 下发自动压制指令和参数设置的包
                         */
                        isAuto=true;
                        break;
                    case R.id.rbtn_handPress:
                        /**
                         * 下发手动压制指令和参数设置的包
                         */
                        isAuto=false;
                        break;
                }
                break;
            default:
                break;

        }

    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(PressReceiver);
        PressReceiver=null;
    }

}
