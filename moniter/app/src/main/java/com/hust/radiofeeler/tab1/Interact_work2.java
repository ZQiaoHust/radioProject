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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.InteractionFixmodeRequest;
import com.hust.radiofeeler.compute.ComputePara;
import com.hust.radiofeeler.view.DateTimePickDialogUtil2Mius;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Interact_work2 extends Fragment {
    private EditText et_ID;
    private SeekBar seekBar01;
    private  TextView textView;
    private Button btn_gone1,btn_gone2,btn_set,btn_send;


    private EditText et_frequency01;
    private EditText et_frequency02;
    private EditText et_frequency03;

    private Spinner spinner_IQ;
    private List<String> list;
    private ArrayAdapter<String> adapter;

    private EditText et_IQblock;
    private EditText inputDate;
    ArrayList<EditText> editList;
    private double[] v1=null;
    private int FreqNumber;

    private int recvGain=7;//初始化的值对应，没拖动就没有触发事件
    private byte IQband_radio=0x11;//初始化对应
    private ComputePara computePara=new ComputePara();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.interact_work2,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        initspinnerSetting();
        InitEvent();
    }

    private void InitSetting(){
        et_ID= (EditText) getActivity().findViewById(R.id.et_work2_ID);
        seekBar01= (SeekBar) getActivity().findViewById(R.id.seekBar_recv_interact);
        textView= (TextView) getActivity().findViewById(R.id.tv_recvGain_interact);

        View work=getActivity().findViewById(R.id.work2);

        et_frequency01=(EditText)work.findViewById(R.id.et_frequency01);
        et_frequency02=(EditText)work.findViewById(R.id.et_frequency02);
        et_frequency03=(EditText)work.findViewById(R.id.et_frequency03);
        //两个按钮多余，隐藏
        btn_gone1= (Button) work.findViewById(R.id.bt_setCentralFreq);
        btn_gone2= (Button) work.findViewById(R.id.bt_getCentralFreq);
        btn_gone1.setVisibility(View.INVISIBLE);
        btn_gone2.setVisibility(View.INVISIBLE);

        spinner_IQ=(Spinner)work.findViewById(R.id.spinner_IQ);
        inputDate= (EditText) work.findViewById(R.id.inputDate);

        et_IQblock=(EditText)work.findViewById(R.id.et_IQblock);

        btn_set= (Button) work.findViewById(R.id.bt_setIQ);
        btn_send= (Button) work.findViewById(R.id.bt_getIQ);

        editList=new ArrayList<>();
        editList.add(et_frequency01);
        editList.add(et_frequency02);
        editList.add(et_frequency03);


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
        list.add("1/1");
        list.add("0.5/0.5");
        list.add("0.1/0.1");

        //2.新建数组适配器
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);

        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner_IQ.setAdapter(adapter);
        spinner_IQ.setSelection(0,true);
    }
    private void InitEvent(){
        seekBar01.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                       textView.setText("当前值："+progress);
                recvGain=progress;//接受通道增益

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

        inputDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DateTimePickDialogUtil2Mius dateTimePicKDialog = new DateTimePickDialogUtil2Mius(getActivity());
                dateTimePicKDialog.dateTimePicKDialog(inputDate);

            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreqNumber = 0;
                v1 = new double[3];
                try {

                    for (int i = 0; i < 3; i++) {

                        if (!editList.get(i).getText().toString().equals("")) {
                            double mm = Double.parseDouble(editList.get(i).getText().toString());
                            v1[i] = mm;
                            FreqNumber++;
                        }
                    }
                    InteractionFixmodeRequest fix = new InteractionFixmodeRequest();
                    fix.setEqiupmentID(Constants.ID);
                    if (!et_ID.getText().toString().equals("")) {
                        fix.setIDcard(Integer.parseInt(et_ID.getText().toString()));
                    }


                    fix.setRecvGain(recvGain);
                    fix.setFreqNum(FreqNumber);
                    if (FreqNumber != 0) {
                        fix.setFix1(v1[0]);
                        fix.setFix2(v1[1]);
                        fix.setFix3(v1[2]);
                    }
                    fix.setIQband_ratio(IQband_radio);
                    if (!et_IQblock.getText().toString().equals("")) {
                        fix.setBlockNum(Integer.parseInt(et_IQblock.getText().toString()));
                    } else
                        fix.setBlockNum(0);

                    //shijian
                    if (!inputDate.getText().toString().equals("")) {
                        byte[] bytes = computePara.Time2Bytes(inputDate.getText().toString());
                        fix.setTime(bytes);
                    }
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.INTERACTION_WORKMODEL02, "interaction_workmodel02", fix);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"输入数据不为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
