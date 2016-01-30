package com.example.administrator.testsliding.tab1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.GlobalConstants.SweepRangeInfo;
import com.example.administrator.testsliding.Mina.Broadcast;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.InteractionSweepModeRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Interact_work1 extends Fragment implements RadioGroup.OnCheckedChangeListener{
    //四种参数设置条
    private EditText et_ID, et_testRecv, et_testSend, et_testGate;
    private Button btn_set,btn_query;

    private RadioGroup rg;//检测门限系数的两个按钮

    //检测门限系数两种设置面板
    private LinearLayout reLay01;
    private RelativeLayout reLay02;

    //自适应门限用的下拉条
    private Spinner spin;
    private List<String> list1;
    private ArrayAdapter<String> adapter1;

    //文件上传模式按钮
    private RadioGroup rg_sendMode;
    private  RadioGroup rg_sweep;
    //
    private Spinner  sp_autoSend;
    private List<String> list2;
    private ArrayAdapter<String> adapter2;
    private EditText et_select;

    private int recvGain;
    private int sendGain;
    private int testGate;
    private int ThresholdModel=1;//默认是固定门限显示
    private int autoThreshold;
    private int fixThreshold;
    private boolean isFixedGate=true;//

    private int sweepMode;//扫频模式
    private int uploadMode;//功率谱上传模式
    private int TotalOfBands;//多频段扫频模式的频段总数
    private byte gate = 3;//功率谱数据变化的判定门限
    private int Select = 63;//文件上传的抽取倍率
    private Boolean IsDUOSetting0K=false;
    private Boolean IsZHISetting0K=false;
    private Boolean IsQUANSetting0K=false;
    private Boolean isUPSettingOK=false;

    private double zhidingStart;
    private double zhidingEnd;
    private double[] v1 = null;
    private double[] v2 = null;

    private final static String TAG="socket";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.interact_work1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSetting();
        initspinnerSetting();
        InitEvent();

    }

    /**
     * 初始化设置
     */
    private void initSetting() {
        et_ID = (EditText) getActivity().findViewById(R.id.et_work1_ID);
        et_testRecv = (EditText) getActivity().findViewById(R.id.et_testRecv);
        et_testSend = (EditText) getActivity().findViewById(R.id.et_testSend);
        et_testGate = (EditText) getActivity().findViewById(R.id.et_testGate1);
        //绑定两个门限设置面板
        reLay01 = (LinearLayout) getActivity().findViewById(R.id.reLayout_testGate1);
        reLay02 = (RelativeLayout) getActivity().findViewById(R.id.reLayout_testGate2);
        //绑定监听按钮
        rg = (RadioGroup) getActivity().findViewById(R.id.radioGroup1);
        spin = (Spinner) getActivity().findViewById(R.id.spinner);
        rg_sendMode=(RadioGroup)getActivity().findViewById(R.id.rg_sendMode);
        rg_sweep=(RadioGroup)getActivity().findViewById(R.id.rg_sweep);
        sp_autoSend= (Spinner)getActivity().findViewById(R.id.spinner_autoSend);
        et_select=(EditText)getActivity().findViewById(R.id.et_select);


        btn_set= (Button) getActivity().findViewById(R.id.bt_setoutgain);
        btn_query= (Button) getActivity().findViewById(R.id.bt_getoutgain);

    }

    /**
     * spinner初始化
     */

    private void initspinnerSetting() {
        //1,设置数据源
        list1 = new ArrayList<String>();
        list1.add("3");
        list1.add("10");
        list1.add("20");
        list1.add("25");
        list1.add("30");
        list1.add("40");
        //2.新建数组适配器
        adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list1);
        //adapter设置一个下拉列表样式
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spin.setAdapter(adapter1);

        //1,设置数据源
        list2 = new ArrayList<String>();
        list2.add("10");
        list2.add("20");
        //2.新建数组适配器
        adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list2);

        //adapter设置一个下拉列表样式
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        sp_autoSend.setAdapter(adapter2);
        sp_autoSend.setSelection(0,true);
    }

    private void InitEvent() {
        rg.setOnCheckedChangeListener(this);
        rg_sendMode.setOnCheckedChangeListener(this);
        rg_sweep.setOnCheckedChangeListener(this);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        autoThreshold=position;//填入对应的编码
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_autoSend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        gate=3;
                        break;
                    case 1:
                        gate=2;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InteractionSweepModeRequest sweep=new InteractionSweepModeRequest();
                TotalOfBands = 0;
                try {
                    sweep.setEquipmentID(Constants.ID);
                    if(!et_ID.getText().toString().equals("")) {
                        sweep.setIDcard(Integer.parseInt(et_ID.getText().toString()));
                    }

                    sweep.setRecvGain(Integer.parseInt(et_testRecv.getText().toString()));
                    sweep.setSendGain(Integer.parseInt(et_testSend.getText().toString()));
                    sweep.setThresholdStyle(ThresholdModel);
                        //如果选择了固定门限，自适应门限填0
                    if(isFixedGate) {
                        if (!et_testGate.getText().toString().equals(""))
                            sweep.setFixThreshold(Integer.parseInt(et_testGate.getText().toString()));
                        sweep.setAdapThreshold(0);
                    }else {
                        sweep.setFixThreshold(0);
                        sweep.setAdapThreshold(autoThreshold);//自适应门限
                    }

                    if(uploadMode==0){
                        Toast.makeText(getActivity(), "请选择文件上传模式", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sweep.setSendFilemode(uploadMode);//文件上传模式
                    }
                    sweep.setJudgeThreshold(gate);//判定门限

                    if(!et_select.getText().toString().equals(""))
                    sweep.setExtractionRatio(Integer.parseInt(et_select.getText().toString()));//抽取倍率


                    if (sweepMode==1) {
                        IsQUANSetting0K = false;
                        sweep.setSweepMode(1);
                        sweep.setTotalBand(1);
                        sweep.setBandNum(1);
                        sweep.setStartFreq(70);
                        sweep.setEndFreq(6000);
                        if (sweep != null) {
                            Broadcast.sendBroadCast(getActivity(),
                                    ConstantValues.INTERACTION_WORKMODEL01, "interaction_workmodel01", sweep);
                        }

                    } else if (sweepMode==2) {
                        IsZHISetting0K = false;
                        sweep.setSweepMode(2);
                        sweep.setTotalBand(1);
                        sweep.setBandNum(1);
                        sweep.setStartFreq(zhidingStart);
                        sweep.setEndFreq(zhidingEnd);
                        if (sweep != null) {
                            Broadcast.sendBroadCast(getActivity(),
                                    ConstantValues.INTERACTION_WORKMODEL01, "interaction_workmodel01", sweep);
                        }

                    } else if (sweepMode==3) {
                        IsDUOSetting0K = false;
                        Constants.SweepParaList.clear();
                        for (int i = 0; i < 5; i++) {
                            if ((v1[i]) != 0) {
                                ++TotalOfBands;
                            }
                            new Thread() {
                                @Override
                                public void run() {
                                    try {

                                        for (int i = 0; i < TotalOfBands; i++) {
                                            sweep.setSweepMode(3);
                                            sweep.setTotalBand(TotalOfBands);
                                            sweep.setBandNum(i + 1);
                                            sweep.setStartFreq(v1[i]);
                                            sweep.setEndFreq(v2[i]);
                                            if (sweep != null) {
                                                Broadcast.sendBroadCast(getActivity(),
                                                        ConstantValues.INTERACTION_WORKMODEL01, "interaction_workmodel01", sweep);
                                            }
                                            int num1= (int) ((v1[i]-70)/25+1);
                                            int num2= (int) ((v2[i]-70)/25+1);
                                            Constants.SweepParaList.add(new SweepRangeInfo(v1[i], v2[i], num1, num2));

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i(TAG, "run: 发送异常");
                                    }
                                }
                            }.start();
                        }

                    } else {
                        Toast.makeText(getActivity(), "请输入扫频范围", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    Toast.makeText(getActivity(), "输入数据不能为空", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    private void showDialog_Save1() {

        AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        bulider.setTitle("您是否确认保存以下参数：");
        bulider.setIcon(R.drawable.wo2);
        bulider.setMessage("确认设置扫频范围为70MHz-6GHz");
        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sweepMode=1;
                Constants.SweepParaList.clear();
                Constants.SweepParaList.add(new SweepRangeInfo(70,6000,1,237));
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = bulider.create();
        dialog.show();
    }

    private void showDialog_Save2() {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.mydialogzhidingpinduan, null);
        final EditText StartEditText = (EditText) view.findViewById(R.id.edit1);
        final EditText EndEditText = (EditText) view.findViewById(R.id.edit2);

        AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        bulider.setTitle("请输入扫频范围70MHz-6GHz");
        bulider.setIcon(R.drawable.wo2);

        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               sweepMode=2;
                zhidingStart= Double.
                        parseDouble(StartEditText.getText().toString());
                zhidingEnd= Double.
                        parseDouble(EndEditText.getText().toString());
                //存起来
                int num1= (int) ((zhidingStart-70)/25+1);
                int num2= (int) ((zhidingEnd-70)/25+1);
                Constants.SweepParaList.clear();
                Constants.SweepParaList.add(new SweepRangeInfo(zhidingStart,zhidingEnd,num1,num2));
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();


            }
        });

        bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
        bulider.setView(view);


        AlertDialog dialog = bulider.create();
        dialog.show();
    }


    private void showDialog_Save3() {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.mydialogduopinduan, null);

        EditText StartEditText1 = (EditText) view.findViewById(R.id.tvStart1_sweepModel3);
        EditText StartEditText2 = (EditText) view.findViewById(R.id.tvStart2_sweepModel3);
        EditText StartEditText3 = (EditText) view.findViewById(R.id.tvStart3_sweepModel3);
        EditText StartEditText4 = (EditText) view.findViewById(R.id.tvStart4_sweepModel3);
        EditText StartEditText5 = (EditText) view.findViewById(R.id.tvStart5_sweepModel3);

        EditText EndEditText1 = (EditText) view.findViewById(R.id.tvEnd1_sweepModel3);
        EditText EndEditText2 = (EditText) view.findViewById(R.id.tvEnd2_sweepModel3);
        EditText EndEditText3 = (EditText) view.findViewById(R.id.tvEnd3_sweepModel3);
        EditText EndEditText4 = (EditText) view.findViewById(R.id.tvEnd4_sweepModel3);
        EditText EndEditText5 = (EditText) view.findViewById(R.id.tvEnd5_sweepModel3);


        final ArrayList<EditText> StartEditTextArrayList = new ArrayList<>();
        final ArrayList<EditText> EndEditTextArrayList = new ArrayList<>();

        StartEditTextArrayList.add(StartEditText1);
        StartEditTextArrayList.add(StartEditText2);
        StartEditTextArrayList.add(StartEditText3);
        StartEditTextArrayList.add(StartEditText4);
        StartEditTextArrayList.add(StartEditText5);

        EndEditTextArrayList.add(EndEditText1);
        EndEditTextArrayList.add(EndEditText2);
        EndEditTextArrayList.add(EndEditText3);
        EndEditTextArrayList.add(EndEditText4);
        EndEditTextArrayList.add(EndEditText5);

        AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        bulider.setTitle("请输入所选频段70MHz-6GHz");
        bulider.setIcon(R.drawable.wo2);

        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sweepMode=3;
                v1 = new double[5];
                v2 = new double[5];
                //mVector=new Vector();


                for (int i = 0; i < StartEditTextArrayList.size(); i++) {
                    if (!StartEditTextArrayList.get(i).getText().toString().equals("")) {
                        double mm = Double.
                                parseDouble(StartEditTextArrayList.get(i).getText().toString());
                        v1[i] = mm;


                    }
                }
                for (int i = 0; i < EndEditTextArrayList.size(); i++) {
                    if (!EndEditTextArrayList.get(i).getText().toString().equals("")) {
                        double cc = Double.
                                parseDouble(EndEditTextArrayList.get(i).getText().toString());
                        v2[i] = cc;


                    }
                }

                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
        bulider.setView(view);
        AlertDialog dialog = bulider.create();
        dialog.show();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_guding:
                ThresholdModel=1;
                isFixedGate=true;
                /**
                 * 固定门限显示
                 */
                reLay01.setVisibility(View.VISIBLE);
                reLay02.setVisibility(View.GONE);
                break;
            case R.id.rbtn_zidingyi:
                ThresholdModel=0;
                isFixedGate=false;
                /**
                 *
                 * 自定义门限显示
                 */
                reLay01.setVisibility(View.GONE);
                reLay02.setVisibility(View.VISIBLE);
                // initspinnerSetting();
                break;
            case R.id.rbtn_sendHand:
                isUPSettingOK=true;
                uploadMode = 1;
                Select = 0;
                gate = 0;
                sp_autoSend.setEnabled(false);
                et_select.setEnabled(false);
                break;
            case R.id.rbtn_sendAuto:
                isUPSettingOK=true;
                uploadMode = 2;
                Select = 0;
                sp_autoSend.setEnabled(true);
                et_select.setEnabled(false);
                break;
            case R.id.rbtn_sendSelect:
                isUPSettingOK=true;
                uploadMode = 3;
                gate = 0;
                sp_autoSend.setEnabled(false);
                et_select.setEnabled(true);
                et_select.setText("63");
                break;
            case R.id.rbtn_whole:
                showDialog_Save1();
                break;
            case R.id.rbtn_specify:
                showDialog_Save2();
                break;
            case R.id.rbtn_many:
                showDialog_Save3();
                break;
            default:
                break;
        }
    }





}
