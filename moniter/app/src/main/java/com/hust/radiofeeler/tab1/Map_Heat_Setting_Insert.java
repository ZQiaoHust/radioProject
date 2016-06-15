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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapInterpolation;
import com.hust.radiofeeler.compute.ComputePara;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 */
public class Map_Heat_Setting_Insert extends Fragment implements RadioGroup.OnCheckedChangeListener{

    private EditText et_bandwidth,et_centerfreq,et_radius,et_dieta,et_fresh;
    private Button btn_setting;
    private Spinner FreqSpinner,RadiumSpinner,DietaSpinner,PicDietaSpinner;
    private ArrayAdapter<String> Spinner_freq,Spinner_radium,Spinner_dieta,Spinner_pic_dieta;

    private List<String> Freq_List = new ArrayList<String>();
    private List<String> Radium_List = new ArrayList<String>();
    private List<String> Dieta_List = new ArrayList<String>();
    private List<String> PicDieta_List = new ArrayList<String>();
    private  RadioGroup select_mode;
    private EditText iuputtime1,inputtime2;
    private TimePickerView pvTime1 ,pvTime2;
    ///组帧参数
    private int centralFreq=98,band=20,radium = 20,PicDieta = 0x01;//中心频率和带宽(初始化界面同步)
    private double dieta = 1.0;
    private boolean Ishand=false,Ischoose=false;
    private ComputePara computePara=new ComputePara();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_base_setting_insert, container, false);
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        InitFreqSpinner();
        InitRadiumSpinner();
        InitPicDietaSpinner();
        //InitDietaSpinner();
        InitEvent();

    }

    private void InitSetting(){
        FreqSpinner = (Spinner)getActivity().findViewById(R.id.Spinner_base_radio_insert);
        RadiumSpinner = (Spinner)getActivity().findViewById(R.id.Spinner_radium_insert);
        DietaSpinner = (Spinner)getActivity().findViewById(R.id.Spinner_dieta_insert);
        PicDietaSpinner = (Spinner)getActivity().findViewById(R.id.Picture_Dieta);
        et_bandwidth= (EditText)getActivity(). findViewById(R.id.bandwidth_interpolation);
        et_centerfreq= (EditText)getActivity(). findViewById(R.id.edit_center_interpolation);
        et_fresh=(EditText)getActivity(). findViewById(R.id.et_freshTime_interpolation);

        iuputtime1= (EditText)getActivity(). findViewById(R.id.inputTime1_insert);
        inputtime2= (EditText)getActivity(). findViewById(R.id.inputTime2_insert);
        select_mode = (RadioGroup)getActivity().findViewById(R.id.select_model_interpolation);
        btn_setting= (Button) getActivity().findViewById(R.id.btn_mapbase_setting_insert);

        //时间选择器
        pvTime1 = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY_HOURS_MINS);
        pvTime2 = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY_HOURS_MINS);
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
               iuputtime1.setText(date);
                //iuputtime1.setText(pvTime1.getTime());
            }
        });
        pvTime2.setTime(new Date());
        pvTime2.setCyclic(false);
        pvTime2.setCancelable(true);
        //时间选择后回调
        pvTime2.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String date) {
                inputtime2.setText(date);
               // inputtime2.setText(pvTime2.getTime());
            }
        });

    }


    private void InitFreqSpinner(){
        Freq_List.add("FM调频广播频段（88-108MHz）");
        Freq_List.add("GSM下行频段I（935-960MHz）");
        Freq_List.add("GSM下行频段II（1805-1880MHz）");
        Freq_List.add("IS95CDMA下行频段（870-880MHz）");
        Freq_List.add("TD 3G频段（1880-1900MHz）");
        Freq_List.add("TD LTE频段I（2320-2370MHz）");
        Freq_List.add("TD LTE频段II（2575-2635MHz）");
        Freq_List.add("WCDMA下行频段（2130-2145MHz）");
        Freq_List.add("联通TD LTE频段I（2300-2320MHz）");
        Freq_List.add("联通TD LTE频段II（2555-2575MHz）");
        Freq_List.add("电信TD LTE频段I（2370-2390MHz）");
        Freq_List.add("电信TD LTE频段II（2635-26550MHz）");
        Freq_List.add("cdma2000 下行频段（2110-2125MHz）");
        Freq_List.add("LTE FDD频段I（1850-1880MHz）");
        Freq_List.add("LTE FDD频段II（2145-2170MHz）");
        Freq_List.add("ISM 433M频段（433.05-434.790MHz）");
        Freq_List.add("ISM 工业频段（902-9280MHz）");
        Freq_List.add("ISM 科学研究频段（2420-2483.50MHz）");
        Freq_List.add("ISM 医疗频段（5725-5850MHz）");
        Spinner_freq = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item_custom, Freq_List);
        Spinner_freq.setDropDownViewResource(R.layout.simple_spinner_item_custom);
        FreqSpinner.setAdapter(Spinner_freq);
    }

    private void InitRadiumSpinner(){
        Radium_List.add("5km");
        Radium_List.add("10km");
        Radium_List.add("20km");
        Radium_List.add("50km");
        Radium_List.add("100km");
        Radium_List.add("200km");
        Spinner_radium = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item_custom, Radium_List);
        Spinner_radium.setDropDownViewResource(R.layout.simple_spinner_item_custom);
        RadiumSpinner.setAdapter(Spinner_radium);
    }


    private void InitPicDietaSpinner(){
        PicDieta_List.add("240x320");
        PicDieta_List.add("270x360");
        PicDieta_List.add("300x400");
        PicDieta_List.add("600x800");
        PicDieta_List.add("300x400");

        Spinner_pic_dieta = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item_custom, PicDieta_List);
        Spinner_pic_dieta.setDropDownViewResource(R.layout.simple_spinner_item_custom);
        PicDietaSpinner.setAdapter(Spinner_pic_dieta);
    }
    private void SetDietaSpinner(int Radium){
        Dieta_List.clear();
        switch (Radium)
        {
            case 5:
                Dieta_List.add("0.1(网格边长约185m)");
                Dieta_List.add("0.2(网格边长约370m)");
                break;
            case 10:
                Dieta_List.add("0.2(网格边长约370m)");
                Dieta_List.add("0.5(网格边长约925m)");
                break;
            case 20:
                Dieta_List.add("0.5(网格边长约925m)");
                Dieta_List.add("1.0(网格边长约1.85km)");
                break;
            case 50:
                Dieta_List.add("1.0(网格边长约1.85km)");
                Dieta_List.add("2.0(网格边长约3.70km)");
                break;
            case 100:
                Dieta_List.add("2.0(网格边长约3.70km)");
                Dieta_List.add("5.0(网格边长约9.25km)");
                break;
            case 200:
                Dieta_List.add("5.0(网格边长约9.25km)");
                Dieta_List.add("10.0(网格边长约18.5km)");
                break;
        }

        Spinner_dieta = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item_custom, Dieta_List);
        Spinner_dieta.setDropDownViewResource(R.layout.simple_spinner_item_custom);
        DietaSpinner.setAdapter(Spinner_dieta);
    }
    private void InitEvent(){
        select_mode.setOnCheckedChangeListener(this);
        //////////////////////
        FreqSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                           case 0:
                               centralFreq=98;
                               band=20;
                               break;
                           case 1:
                               centralFreq= 947;//947.5
                               band=25;
                               break;
                           case 2:
                               centralFreq=1842;//1842.5
                               band=75;
                               break;
                           case 3:
                               centralFreq=875;
                               band=19;
                               break;
                           case 4:
                               centralFreq=1890;
                               band=20;
                               break;
                           case 5:
                               centralFreq=2345;
                               band=50;
                               break;
                           case 6:
                               centralFreq=2605;
                               band=60;
                               break;
                           case 7:
                               centralFreq=2137;//2137.5
                               band=15;
                               break;
                           case 8:
                               centralFreq=2310;
                               band=20;
                               break;
                           case 9:
                               centralFreq=2565;
                               band=20;
                               break;
                           case 10:
                               centralFreq=2117;//2117.5
                               band=15;
                               break;
                           case 11:
                               centralFreq=2380;
                               band=20;
                               break;
                           case 12:
                               centralFreq=2645;
                               band=20;
                               break;
                           case 13:
                               centralFreq=1865;
                               band=30;
                               break;
                           case 14:
                               centralFreq=2157;//2157.5
                               band=25;
                               break;
                           case 15:
                               centralFreq=434;//433.92
                               band=2;//1.74
                               break;
                           case 16:
                               centralFreq=915;
                               band=26;
                               break;
                           case 17:
                               centralFreq=2452;//2451.75
                               band=63;//63.5
                               break;
                           case 18:
                               centralFreq=5785;//5787.5
                               band=125;
                               break;
                       }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadiumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        radium = 5;
                        break;
                    case 1:
                        radium = 10;
                        break;
                    case 2:
                        radium = 20;
                        break;
                    case 3:
                        radium = 50;
                        break;
                    case 4:
                        radium = 100;
                        break;
                    case 5:
                        radium = 200;
                        break;
                    default:
                        radium = 10;
                        break;

                }
                SetDietaSpinner(radium);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PicDietaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        PicDieta = 0x01;
                        break;
                    case 1:
                        PicDieta= 0x02;
                        break;
                    case 2:
                        PicDieta= 0x03;
                        break;
                    case 3:
                        PicDieta= 0x4;
                        break;
                    case 4:
                        PicDieta= 0x06;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DietaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        if(radium == 5)
                            dieta =0.1;
                        else if(radium == 10)
                            dieta = 0.2;
                        else if(radium == 20)
                            dieta = 0.5;
                        else if(radium ==50)
                            dieta = 1.0;
                        else if(radium == 100)
                            dieta = 2.0;
                        else if(radium == 200)
                            dieta = 5.0;
                        break;
                    case 1:
                        if(radium == 5)
                            dieta =0.2;
                        else if(radium == 10)
                            dieta = 0.5;
                        else if(radium == 20)
                            dieta = 1.0;
                        else if(radium ==50)
                            dieta = 2.0;
                        else if(radium == 100)
                            dieta = 5.0;
                        else if(radium == 200)
                            dieta = 10.0;
                        break;

                    default:
                        break;

                }
                //SetDietaSpinner(radium);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //弹出时间选择器
        iuputtime1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime1.show();
            }
        });
        inputtime2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime2.show();
            }
        });

        /////////////////////////
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapInterpolation inter=new MapInterpolation();
                inter.setEquipmentID(Constants.ID);

                try {
                    if (Ishand && (!Ischoose)) {
                        //手动
                        if (!et_centerfreq.getText().toString().equals("")) {
                            inter.setCentralFreq((int) Float.parseFloat(et_centerfreq.getText().toString()));
                        }
                        if (!et_bandwidth.getText().toString().equals("")) {
                            inter.setBand((int) Float.parseFloat(et_bandwidth.getText().toString()));
                        }

                    } else if ((!Ishand) && Ischoose) {
                        inter.setCentralFreq(centralFreq);
                        inter.setBand(band);
                    } else {
                        Toast.makeText(getContext(), "请正确输入！", Toast.LENGTH_SHORT).show();
                    }
                    inter.setRadius(radium);
                    inter.setDieta(dieta);

                    if (!et_fresh.getText().toString().equals("")) {
                        inter.setFreshtime((int) Float.parseFloat(et_fresh.getText().toString()));
                    }
                    if (!iuputtime1.getText().toString().equals("")) {
                        byte[] bytes = computePara.Time2Bytes(iuputtime1.getText().toString());
                        inter.setStartTime(bytes);
                    }
                    if (!inputtime2.getText().toString().equals("")) {
                        byte[] bytes = computePara.Time2Bytes(inputtime2.getText().toString());
                        inter.setEndTime(bytes);
                    }
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.MAPINTERPOLATION, "map_inter",inter);

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Map_interpolationResult.class);
                    startActivity(intent);

                }catch (Exception e){
                    Toast.makeText(getContext(), "请正确输入！", Toast.LENGTH_SHORT).show();
                }


                }
            });

        }


    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.munual_select_interpolation:
                FreqSpinner.setEnabled(false);
                et_bandwidth.setFocusable(true);
                et_bandwidth.setFocusableInTouchMode(true);
                et_bandwidth.requestFocus();
                et_centerfreq.setFocusable(true);
                et_centerfreq.setFocusableInTouchMode(true);
                et_centerfreq.requestFocus();
                Ishand=true;
                Ischoose=false;
                break;
            case R.id.straight_select_interpolation:
                et_bandwidth.setText("");
                et_centerfreq.setText("");
                et_bandwidth.setFocusableInTouchMode(false);
                et_centerfreq.setFocusableInTouchMode(false);
                et_bandwidth.setFocusable(false);
                et_centerfreq.setFocusable(false);
                FreqSpinner.setEnabled(true);
                Ishand=false;
                Ischoose=true;
                break;
            default:
                break;

        }
    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return format.format(date);
    }
}
