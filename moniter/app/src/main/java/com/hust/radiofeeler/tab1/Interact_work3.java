package com.hust.radiofeeler.tab1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.InteractionPressmodeRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Interact_work3 extends Fragment implements RadioGroup.OnCheckedChangeListener
,SeekBar.OnSeekBarChangeListener{
    private EditText et_ID;
    private SeekBar seekBar01;
    private TextView textView;

    //两种压制模式选择面板
    private LinearLayout lilay_single;
    private LinearLayout lilay_double;


    private RadioGroup rg_press;//压制模式的两个按钮
    private RadioGroup rg_way;//压制方式的两个按钮


    private LinearLayout lilay_setPressFreq;//要隐藏的多余按钮
    //自定义模式下的编辑框
    private EditText edit_firstPoint;
    private EditText edit_secondPoint;

    //单信号模式下的参数
    private EditText edit_siglePreTime;//压制时间
    private EditText edit_sigleSweTime;//扫频时间

    //双信号模式下的参数
    private EditText edit_doubleFirstTime;//第一个压制时间
    private EditText edit_doubleSecTime;//第二个压制时间
    private EditText edit_doubleSweTime;//扫频时间
    private EditText edit_totalTime;//压制总时间

    private Button btn_stop,btn_set,btn_get;

    private Spinner spin1,spin2;
    private List<String> list1,list2;
    private ArrayAdapter<String> adapter1,adapter2;
    private ArrayList<EditText> editList;
    private double[] v1=null;
    private int FreqNumber;
    //全局变量
    private int sendGain;
    private int pressMode;
    private int style=1;//与界面初始化对应
    private int band=1;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private Boolean isSingle=true;//与界面初始化对应
    private Boolean isAuto=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.interact_work3,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        initspinnerSetting();
        InitEvent();
    }

    private void InitSetting() {
        et_ID = (EditText) getActivity().findViewById(R.id.et_work3_ID);
        seekBar01 = (SeekBar) getActivity().findViewById(R.id.seekBar_send_interact3);
        textView = (TextView) getActivity().findViewById(R.id.tv_sendGain_interact3);

        View work=getActivity().findViewById(R.id.work3);
//        lilay_setPressFreq= (LinearLayout) work.findViewById(R.id.lilay_setPressFreq);
//        lilay_setPressFreq.setVisibility(View.INVISIBLE);
        //绑定groupRadio
        rg_press= (RadioGroup)work.findViewById(R.id.radioGroup_press);
        rg_way= (RadioGroup)work.findViewById(R.id.radioGroup_way);

        edit_firstPoint= (EditText)work.findViewById(R.id.et_firstPressPoint);
        edit_secondPoint= (EditText)work.findViewById(R.id.et_secondPressPoint);
        //绑定两种扫频模式的面板
        lilay_single= (LinearLayout)work.findViewById(R.id.reLay_singlePress);
        lilay_double= (LinearLayout)work.findViewById(R.id.reLay_doublePress);

        //单信号模式参数绑定
        edit_siglePreTime= (EditText)work.findViewById(R.id.et_T1);
        edit_sigleSweTime= (EditText)work.findViewById(R.id.et_T2);
        //双信号模式参数绑定
        edit_doubleFirstTime= (EditText)work.findViewById(R.id.et_doubleT3);
        edit_doubleSecTime= (EditText)work.findViewById(R.id.et_doubleT4);
        edit_totalTime= (EditText) work.findViewById(R.id.et_doubleT1);
        edit_doubleSweTime= (EditText)work.findViewById(R.id.et_doubleT2);

        spin1= (Spinner)work.findViewById(R.id.spinner_radiostyle);
        spin2= (Spinner)work.findViewById(R.id.spinner_radioband);

        btn_stop= (Button) work.findViewById(R.id.bt_stopPress);
        btn_set=(Button) work.findViewById(R.id.bt_startPress);
        btn_get=(Button) work.findViewById(R.id.bt_getPressSetting);
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
        spin1.setSelection(0, true);

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
        spin2.setSelection(0, true);
        editList=new ArrayList<>();
        v1=new double[2];
        editList.add(edit_firstPoint);
        editList.add(edit_secondPoint);

    }

    private void InitEvent(){
        seekBar01.setOnSeekBarChangeListener(this);
        rg_press.setOnCheckedChangeListener(this);
        rg_way.setOnCheckedChangeListener(this);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                style = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                band=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/**
 * 设置按钮
 */
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreqNumber = 0;
                InteractionPressmodeRequest press = new InteractionPressmodeRequest();
                try {
                    press.setEqiupmentID(Constants.ID);
                    if (!et_ID.getText().toString().equals("")) {
                        press.setIDcard(Integer.parseInt(et_ID.getText().toString()));
                    }
                    press.setSendGain(sendGain);
                    //两个压制点
                    for (int i = 0; i < 2; i++) {

                        if (!editList.get(i).getText().toString().equals("")) {
                            double mm = Double.parseDouble(editList.get(i).getText().toString());
                            v1[i] = mm;
                            FreqNumber++;
                        }
                    }
                    if (FreqNumber != 0) {
                        press.setFreqNum(FreqNumber);
                        press.setPressFreq1(v1[0]);
                        press.setPressFreg2(v1[1]);
                    } else {
                        Toast.makeText(getActivity(), "请输入压制点", Toast.LENGTH_SHORT).show();
                    }
                    /////
                    press.setSignalStyle((byte) style);
                    press.setSignalBand((byte) band);
                    //压制方式
                    if (isSingle) {
                        if (isAuto) {
                            //单信号自动压制
                            press.setSendModel((byte) 0x01);
                        } else {
                            //单信号手动
                            press.setSendModel((byte) 0x02);
                        }
                        press.setT1(Integer.parseInt(edit_siglePreTime.getText().toString()));
                        press.setT2(Integer.parseInt(edit_sigleSweTime.getText().toString()));
                    } else {
                        if (isAuto) {
                            //双信号自动压制
                            press.setSendModel((byte) 0x03);
                        } else {
                            //双信号手动压制
                            press.setSendModel((byte) 0x04);
                        }
                        press.setT1(Integer.parseInt(edit_totalTime.getText().toString()));
                        press.setT2(Integer.parseInt(edit_doubleSweTime.getText().toString()));
                        press.setT3(Integer.parseInt(edit_doubleFirstTime.getText().toString()));
                        press.setT4(Integer.parseInt(edit_doubleSecTime.getText().toString()));
                    }
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.INTERACTION_WORKMODEL03, "interaction_workmodel03", press);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"请设置参数",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 下发停止压制的包，并且使当前工作状态发生改变
                 */
                InteractionPressmodeRequest press=new InteractionPressmodeRequest();
                press.setEqiupmentID(Constants.ID);
                if (!et_ID.getText().toString().equals("")) {
                    press.setIDcard(Integer.parseInt(et_ID.getText().toString()));
                }
                press.setSendModel((byte)0x05);
                Broadcast.sendBroadCast(getActivity(),
                        ConstantValues.INTERACTION_WORKMODEL03, "interaction_workmodel03", press);

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText("当前值："+progress);
        sendGain=progress;


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
