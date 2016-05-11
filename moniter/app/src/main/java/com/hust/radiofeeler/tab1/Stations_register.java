package com.hust.radiofeeler.tab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.Station_RegisterRequst;


/**
 * Created by Administrator on 2015/10/27.
 */
public class Stations_register extends Fragment {
    private EditText et_start,et_end;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stations_register,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et_start= (EditText) getActivity().findViewById(R.id.et_registerStart);
        et_end= (EditText) getActivity().findViewById(R.id.et_registerEnd);
        InitEvent();
    }

    private void  InitEvent(){

        getActivity().findViewById(R.id.btn_queryregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Station_RegisterRequst register = new Station_RegisterRequst();
                register.setEquipmentID(Constants.ID);


                try {
                    double ss = Double.valueOf(et_start.getText().toString());
                    double ss2 = Double.valueOf(et_end.getText().toString());
                    if (ss <= ss2) {
                        int start = (int) Math.floor(ss);
                        int end = (int) Math.ceil(ss2);
                        register.setStartFreq(start);
                        register.setEndFreq(end);
                        // 点击发送消息到服务器
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.STATION_REGISTER, "station_register", register);
                        Intent intent = new Intent(getActivity(), Stations_registerResult.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "输入数据有误!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "输入数据不能为空!", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

}
