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
import com.hust.radiofeeler.bean2server.ModifyAntenna;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Service_AntennaModify extends Fragment{
    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private Button btn_query;

    private int ID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.modify_antenna,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner=(Spinner)getActivity().findViewById(R.id.spinner_antenna);
        btn_query= (Button) getActivity().findViewById(R.id.btn_queryAntenna);
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
        list.add("超短套筒宽带吸盘天线");
        list.add("超短螺旋窄带吸盘天线");
        list.add("单鞭螺旋加载宽带天线");
        list.add("平面双锥宽带天线");
        list.add("AH-8000宽带盘锥天线");
        list.add("AH-7000宽带盘锥天线");
        list.add("TQJ-1000宽带盘锥天线");
        list.add("国人对数周期天线");
        list.add("汇讯通对数周期天线");
        list.add("BTA-BicoLOG20100双锥天线");
        list.add("LX-520背腔阿螺宽带天线");
        list.add("LX-840背腔阿螺宽带天线");
        list.add("LX-1080背腔阿螺宽带天线");
        list.add("PZ-100800/P盘锥天线");

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
                ModifyAntenna modify=new ModifyAntenna();
                modify.setEquipmentID(Constants.ID);
                if(ID==0){
                    Toast.makeText(getActivity(), "请输入天线型号！", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    modify.setFPGAID(ID);
                    //ID=0;
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.MODIFYANTENNA, "modifyAntenna", modify);
                    Intent mintent=new Intent(getActivity(),ModifyAntennaChart.class);
                    startActivity(mintent);

                }

            }
        });
    }
}
