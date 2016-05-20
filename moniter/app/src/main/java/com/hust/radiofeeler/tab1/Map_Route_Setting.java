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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapRoute;
import com.hust.radiofeeler.compute.ComputePara;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/6.
 */
public class Map_Route_Setting extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private Spinner mySpinner;
    private ArrayAdapter<String> Spinner_adapter;
    private List<String> FreqSelectList = new ArrayList<String>();

    private EditText iuputtime1,inputtime2;
    private Button btn_setting;
    private EditText et_bandwidth,et_centerfreq;
    private  RadioGroup select_mode,radio_selectData;
    TimePickerView pvTime1 ,pvTime2;
    private CheckBox cb_TPOA;

    ///组帧参数
    private int centralFreq=98,band=20;//中心频率和带宽(初始化界面同步)
    private boolean Ishand=false,Ischoose=false,IsFromCenter=false,IsFromLocal=false;
    private byte isTPOA=0;//TPOA定位
    private ComputePara computePara=new ComputePara();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_geometry_setting, container, false);
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        InitSpinnerSetting();
        InitEvent();
    }


    private void InitSetting(){
        et_bandwidth= (EditText)getActivity(). findViewById(R.id.bandwidth);
        et_centerfreq= (EditText)getActivity(). findViewById(R.id.edit_center);
        select_mode = (RadioGroup)getActivity().findViewById(R.id.radioGroup_selectModel);
        mySpinner = (Spinner)getActivity().findViewById(R.id.Spinner_route);
        iuputtime1= (EditText) getActivity().findViewById(R.id.inputTime1_start);
        inputtime2= (EditText)getActivity().findViewById(R.id.inputTime2_end);
        btn_setting= (Button) getActivity().findViewById(R.id.btn_maproute_setting);
        cb_TPOA= (CheckBox) getActivity().findViewById(R.id.chBox_TPOA);
        radio_selectData=(RadioGroup)getActivity().findViewById(R.id.radioGroup_selectCoordinates);
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
               // iuputtime1.setText(pvTime1.getTime());
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
                //inputtime2.setText(pvTime2.getTime());
            }
        });
    }
    private void InitSpinnerSetting(){
        FreqSelectList.add("FM调频广播频段（88-108MHz）");
        FreqSelectList.add("GSM下行频段I（35-960MHz）");
        FreqSelectList.add("GSM下行频段II（1804-1880MHz）");
        FreqSelectList.add("IS95CDMA下行频段（2555-25750MHz）");
        FreqSelectList.add("TD 3G频段（1880-1900MHz）");
        FreqSelectList.add("TD LTE频段I（2320-2370MHz）");
        FreqSelectList.add("TD LTE频段II（2575-2635MHz）");
        FreqSelectList.add("WCDMA下行频段（2130-2145MHz）");
        FreqSelectList.add("联通TD LTE频段I（2300-2320MHz）");
        FreqSelectList.add("联通TD LTE频段II（2555-2575MHz）");
        FreqSelectList.add("电信TD LTE频段I（2370-2390MHz）");
        FreqSelectList.add("电信TD LTE频段II（2635-26550MHz）");
        FreqSelectList.add("cdma2000 下行频段（2110-2125MHz）");
        FreqSelectList.add("LTE FDD频段I（1850-1880MHz）");
        FreqSelectList.add("LTE FDD频段II（2145-2170MHz）");
        FreqSelectList.add("ISM 433M频段（433.05-434.790MHz）");
        FreqSelectList.add("ISM 工业频段（902-9280MHz）");
        FreqSelectList.add("ISM 科学研究频段（2420-2483.50MHz）");
        FreqSelectList.add("ISM 医疗频段（5725-5850MHz）");
        Spinner_adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item_custom, FreqSelectList);
        Spinner_adapter.setDropDownViewResource(R.layout.simple_spinner_item_custom);
        mySpinner.setAdapter(Spinner_adapter);

    }

    private void InitEvent(){
        select_mode.setOnCheckedChangeListener(this);
        radio_selectData.setOnCheckedChangeListener(this);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        cb_TPOA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isTPOA= (byte) 0xff;
                }
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
//

       btn_setting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MapRoute route = new MapRoute();
               route.setEquipmentID(Constants.ID);
               try {
                   if (Ishand && (!Ischoose)) {
                       //手动
                       if (!et_centerfreq.getText().toString().equals("")) {
                           route.setCentralFreq(Integer.parseInt(et_centerfreq.getText().toString()));
                       }
                       if (!et_bandwidth.getText().toString().equals("")) {
                           route.setBand(Integer.parseInt(et_bandwidth.getText().toString()));
                       }

                   } else if ((!Ishand) && Ischoose) {
                       route.setCentralFreq(centralFreq);
                       route.setBand(band);
                   } else {
                       Toast.makeText(getContext(), "请正确输入！", Toast.LENGTH_SHORT).show();
                   }
                   if (!iuputtime1.getText().toString().equals("")) {
                       byte[] bytes = computePara.Time2Bytes(iuputtime1.getText().toString());
                       route.setStartTime(bytes);
                   }
                   if (!inputtime2.getText().toString().equals("")) {
                       byte[] bytes = computePara.Time2Bytes(inputtime2.getText().toString());
                       route.setEndTime(bytes);
                   }
                   route.setIsTPOA(isTPOA);
                 if(IsFromCenter&&(!IsFromLocal)) {
                       //从中心站获取数据
                       Broadcast.sendBroadCast(getActivity(),
                               ConstantValues.MAPROUTE, "map_route", route);
                       getActivity(). findViewById(R.id.selectCoordinates01).setEnabled(false);

                  }else if(IsFromLocal&&(!IsFromCenter)) {
                      //本地取
                   }

                   Intent intent = new Intent(getActivity(), Map_Route_Result.class);
                   startActivity(intent);
               } catch (Exception e) {
                   Toast.makeText(getContext(), "请正确输入！", Toast.LENGTH_SHORT).show();
               }
           }
       });

    }



    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.munual_select:
                mySpinner.setEnabled(false);
                et_bandwidth.setFocusable(true);
                et_bandwidth.setFocusableInTouchMode(true);
                et_bandwidth.requestFocus();
                et_centerfreq.setFocusable(true);
                et_centerfreq.setFocusableInTouchMode(true);
                et_centerfreq.requestFocus();
                Ishand=true;
                Ischoose=false;
                break;
            case R.id.straight_select:
                mySpinner.setEnabled(true);
                et_bandwidth.setText("");
                et_centerfreq.setText("");
                et_bandwidth.setFocusableInTouchMode(false);
                et_centerfreq.setFocusableInTouchMode(false);
                et_bandwidth.setFocusable(false);
                et_centerfreq.setFocusable(false);
                Ishand=false;
                Ischoose=true;
                break;
            case R.id.radio_local:
                IsFromCenter=false;
                IsFromLocal=true;
                break;
            case R.id.radio_centerstation:
                IsFromCenter=true;
                IsFromLocal=false;
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

