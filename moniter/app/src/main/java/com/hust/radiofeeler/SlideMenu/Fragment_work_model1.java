package com.hust.radiofeeler.SlideMenu;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.radiofeeler.Bean.Query;
import com.hust.radiofeeler.Bean.SweepRange;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.SweepRangeInfo;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Fragment_work_model1 extends Fragment implements RadioGroup.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener {

    //文件上传模式按钮
    private RadioGroup rg_sendMode;
    private RadioGroup rg_sweep;

    private Spinner sp_autoSend;
    private List<String> list;
    private ArrayAdapter<String> adapter;

    private SeekBar seekBar_select;
    private TextView tv_select;

    private Button mSetButton;
    private Button mGetButton;
    private Button mFailed;




    private double[] v1 = null;
    private double[] v2 = null;

    private double zhidingStart;
    private double zhidingEnd;

    String s2=null;

    private BroadcastReceiver WorkModelReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SweepRange data=intent.getParcelableExtra("data");
            if (data==null){
                return;
            }

            int a =data.getaSweepMode();
            int c =data.getaTotalOfBands();
            int d=data.getaBandNumber();
            double e=data.getStartFrequence();
            double f=data.getEndFrequence();
            int g=data.getGate();
            int h =data.getaSelect();

            int b=data.getaSendMode();
            if(b==1){
                s2="手动传输";
            }else if(b==2){
                if(g==2){
                    s2="自动传输 判断门限是20dB";
                }else{
                    s2="自动传输 判断门限是10dB";
                }
            }else if(b==3){
                s2="抽取传输 抽取倍率是:"+String.valueOf(h);
            }
            if(a==1){
                Toast toast=Toast.makeText(getActivity(), "文件上传模式："+s2+"\n"
                        +"扫频模式： 全频段扫描", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 800);
                toast.show();
            }else if(a==2){
                Toast toast=Toast.makeText(getActivity(), "文件上传模式："+s2+"\n"+
                        "扫频模式： 指定频段扫描 范围 " +String.valueOf(e)+"到"+String.valueOf(f),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 800);
                toast.show();

            }else if(a==3){
                Toast toast=Toast.makeText(getActivity(), "文件上传模式："+s2+"\n"+
                        "多频段扫描"+"总扫描段数"+c +"当前是第"+d+"段"+ "起始频率"+String.valueOf(e)+"终止频率"
                                +String.valueOf(f),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP , 0, 400);
                toast.show();

            }

        }
    };





    /**
     * 扫频接收频率范围设置数据帧的数据域
     *
     * @param aSweepMode 扫频模式
     * @param aSendMode 功率谱数据上传模式
     * @param aTotalOfBands 多频段扫频模式的频段总数
     * @param aBandNumber 多频段扫频模式的频段序号
     * @param startFrequence 起止频率
     * @param endFrequence 终止频率
     * @param gate=3 功率谱数据变化的判定门限
     * @param aSelect=63 文件上传的抽取倍率
     * @return
     */


    private int uploadMode=1;//功率谱上传模式
    private int TotalOfBands;//多频段扫频模式的频段总数
    private byte gate=3;//功率谱数据变化的判定门限
    private int Select=63 ;//文件上传的抽取倍率
    private Boolean IsDUOSetting0K=false;
    private Boolean IsZHISetting0K=false;
    private Boolean IsQUANSetting0K=false;

    private final static String TAG="socket";


    //在fragment里设置点击事件，需要在OnActivity里面声明
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitSetting();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.SweepRangeQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(WorkModelReceiver, filter);
        initspinnerSetting();
        InitEvent();


    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_fragment_work_model1, container, false);
    }


    /**
     * 初始化参数
     */
    private void InitSetting() {
        gate=3;
        rg_sendMode = (RadioGroup) getActivity().findViewById(R.id.rg_sendMode);
        rg_sweep = (RadioGroup) getActivity().findViewById(R.id.rg_sweep);
        sp_autoSend = (Spinner) getActivity().findViewById(R.id.spinner_autoSend);
        seekBar_select = (SeekBar) getActivity().findViewById(R.id.seekBar_selectSend);
        tv_select = (TextView) getActivity().findViewById(R.id.tv_selectSend);
        mSetButton = (Button) getActivity().findViewById(R.id.bt_setoutgain);
        mGetButton = (Button) getActivity().findViewById(R.id.bt_getoutgain);
    }

    /**
     * spinner初始化
     */

    private void initspinnerSetting() {


        //1,设置数据源
        list = new ArrayList<String>();
        list.add("10");
        list.add("20");
        //2.新建数组适配器
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);

        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        sp_autoSend.setAdapter(adapter);
        sp_autoSend.setSelection(0, true);
    }


    private void InitEvent() {
        rg_sendMode.setOnCheckedChangeListener(this);
        rg_sweep.setOnCheckedChangeListener(this);
        //spin设置监听器
//        sp_autoSend.setOnItemSelectedListener(this);//spinner监听在fragment中失效
        seekBar_select.setOnSeekBarChangeListener(this);

        sp_autoSend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gate = 3;
                        break;
                    case 1:
                        gate = 2;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * 生成数据帧
         *
         */
        final SweepRange sweepRange =new SweepRange();
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweepRangeInfo sweepRangeInfo=new SweepRangeInfo();
                TotalOfBands = 0;
                if(IsQUANSetting0K){
                    IsQUANSetting0K=false;
                    sweepRange.setEquipmentId(Constants.ID);
                    sweepRange.setaSweepMode(1);
                    sweepRange.setaSendMode(uploadMode);
                    sweepRange.setaTotalOfBands(1);
                    sweepRange.setaBandNumber(1);
                    sweepRange.setStartFrequence(70);
                    sweepRange.setEndFrequence(6000);
                    sweepRange.setGate(gate);
                    sweepRange.setaSelect(Select);
                    sweepRangeInfo.setSegStart(70);
                    sweepRangeInfo.setSegEnd(6000);
                    sweepRangeInfo.setStartNum( 1);
                    sweepRangeInfo.setEndNum((int) ((6000-70)/25+1));
                    Constants.SweepParaList.clear();
                    Constants.SweepParaList.add(sweepRangeInfo);
                    Constants.sendMode=uploadMode;
                    Constants.selectRate=Select;
                    Constants.judgePower=gate;

                    if(sweepRange !=null)
                    {
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.SweepRangeSet, "SweepRangeSet", sweepRange);

                    }

                }else if(IsZHISetting0K){

                    IsZHISetting0K=false;
                    sweepRange.setEquipmentId(Constants.ID);
                    sweepRange.setaSweepMode(2);
                    sweepRange.setaSendMode(uploadMode);
                    sweepRange.setaTotalOfBands(1);
                    sweepRange.setaBandNumber(1);
                    sweepRange.setStartFrequence(zhidingStart);
                    sweepRange.setEndFrequence(zhidingEnd);
                    sweepRange.setGate(gate);
                    sweepRange.setaSelect(Select);


                    //扫描起始偏移
                    sweepRangeInfo.setSegStart(zhidingStart);
                    sweepRangeInfo.setSegEnd(zhidingEnd);
                    sweepRangeInfo.setStartNum((int) ((zhidingStart-70)/25+1));
                    sweepRangeInfo.setEndNum((int) ((zhidingEnd-70)/25+1));
                    Constants.SweepParaList.clear();
                    Constants.SweepParaList.add(sweepRangeInfo);
                    Constants.sendMode=uploadMode;
                    Constants.selectRate=Select;
                    Constants.judgePower=gate;
                    if(sweepRange !=null)
                    {
                        Broadcast.sendBroadCast(getActivity(),
                                ConstantValues.SweepRangeSet, "SweepRangeSet", sweepRange);


                    }
                }else if(IsDUOSetting0K){
                        IsDUOSetting0K = false;
                        for (int i = 0; i < 5; i++) {
                            if ((v1[i]) != 0) {
                                ++TotalOfBands;
                            }
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < TotalOfBands; i++) {
                                        sweepRange.setEquipmentId(Constants.ID);
                                        sweepRange.setaSweepMode(3);
                                        sweepRange.setaSendMode(uploadMode);
                                        sweepRange.setaTotalOfBands(TotalOfBands);
                                        sweepRange.setaBandNumber(i + 1);
                                        sweepRange.setStartFrequence(v1[i]);
                                        sweepRange.setEndFrequence(v2[i]);
                                        sweepRange.setGate(gate);
                                        sweepRange.setaSelect(Select);

                                        sweepRangeInfo.setSegStart(v1[i]);
                                        sweepRangeInfo.setSegEnd(v2[i]);
                                        sweepRangeInfo.setStartNum((int) ((v1[i]-70)/25+1));
                                        sweepRangeInfo.setEndNum((int) ((v2[i]-70)/25+1));
                                        Constants.SweepParaList.clear();
                                        Constants.SweepParaList.add(sweepRangeInfo);

                                        if(sweepRange !=null)
                                        {
                                            Broadcast.sendBroadCast(getActivity(),
                                                    ConstantValues.SweepRangeSet, "SweepRangeSet", sweepRange);

                                        }
                                    }
                                    Constants.sendMode=uploadMode;
                                    Constants.selectRate=Select;
                                    Constants.judgePower=gate;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i(TAG, "run: 发送异常");
                                }
                            }
                        }.start();
                }else
                Toast.makeText(getActivity(), "请输入扫频范围", Toast.LENGTH_SHORT).show();
            }


        });

        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=new Query();
                query.setequipmentID(Constants.ID);
                query.setFuncID((byte) 0x11);
                if(query!=null){
                    Broadcast.sendBroadCast(getActivity(),
                            ConstantValues.SweepRangeQuery,"SweepRangeQuery",query);
                }
            }
        });
    }

    /**
     * 查询扫频模式弹出框
     */
    private void getSweepModel() {

        View view = View.inflate(getActivity(), R.layout.dialoggetsweepmodel, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view);
        builder.setTitle("扫频工作模式：");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialog_Save1() {
        AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        bulider.setTitle("您是否确认保存以下参数：");
        bulider.setIcon(R.drawable.wo2);
        bulider.setMessage("确认设置扫频范围为70MHz-6GHz");
        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IsQUANSetting0K=true;
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
                IsZHISetting0K=true;
                zhidingStart= Double.
                        parseDouble(StartEditText.getText().toString());
                zhidingEnd= Double.
                        parseDouble(EndEditText.getText().toString());

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
                IsDUOSetting0K=true;
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
            case R.id.rbtn_sendHand:
                sp_autoSend.setEnabled(false);
                seekBar_select.setEnabled(false);
                uploadMode = 1;
                Select = 0;
                gate = 0;

                break;
            case R.id.rbtn_sendAuto:
                sp_autoSend.setEnabled(true);
                seekBar_select.setEnabled(false);
                uploadMode = 2;
                Select = 0;
                break;
            case R.id.rbtn_sendSelect:
                sp_autoSend.setEnabled(false);
                seekBar_select.setEnabled(true);
                uploadMode = 3;
                gate = 0;
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




    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tv_select.setText("当前值：" + progress);
        Select = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(WorkModelReceiver);
        WorkModelReceiver=null;

    }


}
