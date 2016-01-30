package com.example.administrator.testsliding.tab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.TerminalAttributes_All;


/**
 * 全部台站登记属性数据的强求服务
 * Created by Administrator on 2015/11/23.
 */
public class Station_All extends Fragment {
    private EditText et_start;
    private EditText et_end;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_all, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        et_start = (EditText) getActivity().findViewById(R.id.et_terminalStart01);
        et_end = (EditText) getActivity().findViewById(R.id.et_terminalEnd01);

        getActivity().findViewById(R.id.btn_queryTerminal01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////同时发送所有在网终端和注册终端的属性查询

                TerminalAttributes_All terminal = new TerminalAttributes_All();
                terminal.setEquipmentID(Constants.ID);
                try {
                    int start = Integer.valueOf(et_start.getText().toString());
                    int end = Integer.valueOf(et_end.getText().toString());
                    if (start < end) {
                        terminal.setStartFreq(start);
                        terminal.setEndFreq(end);
                        // 点击发送消息到服务器
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.TERMINAL_ALL, "station_all", terminal);
                        Intent intent = new Intent(getActivity(), Station_AllResult.class);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
