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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.ModifyInGain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Service_InGainModify extends Fragment {
    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private Button btn_query;
    private int ID=0;//硬件的型号编码
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.modify_ingain,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner=(Spinner)getActivity().findViewById(R.id.spinner_FPGA);
        btn_query= (Button) getActivity().findViewById(R.id.btn_queryingain);
        initspinnerSetting();
        InitEvent();
    }
    /**
     * spinner初始化
     *
     */

    private  void initspinnerSetting(){

        //1,设置数据源
        list = new ArrayList<String>();
        list.add("");
        list.add("SRF201");
        list.add("SRF301");

        //2.新建数组适配器
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);

        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spinner.setAdapter(adapter);
    }

    private void InitEvent(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   ID=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyInGain modify=new ModifyInGain();
                modify.setEquipmentID(Constants.ID);
                if(ID==0){
                    Toast.makeText(getActivity(),"请输入硬件型号！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    modify.setFPGAID(ID);
                   // ID=0;
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.MODIFYINGAIN,"modifyIngain",modify);

                    Intent mintent=new Intent(getActivity(),ModifyIngainChart.class);
                    startActivity(mintent);
                }

            }
        });
    }
}
