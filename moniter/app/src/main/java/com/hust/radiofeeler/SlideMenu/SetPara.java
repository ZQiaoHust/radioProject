package com.hust.radiofeeler.SlideMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.radiofeeler.Bean.InGain;
import com.hust.radiofeeler.Bean.OutGain;
import com.hust.radiofeeler.Bean.Query;
import com.hust.radiofeeler.Bean.Threshold;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/7/23.
 */
public class SetPara extends Activity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {


    //三个滑动条
    private SeekBar bar_recv;
    private SeekBar bar_send;
    private SeekBar bar_test;

    //滑动条对应的参数显示
    private TextView tv_recv;
    private TextView tv_send;
    private TextView tv_test;

    private RadioGroup rg;//检测门限系数的两个按钮

    //检测门限系数两种设置面板
    private LinearLayout reLay01;
    private RelativeLayout reLay02;

    //自适应门限用的下拉条
    private Spinner spin;
    private List<String> list;
    private ArrayAdapter<String> adapter;

    //设置和查询的Button
    private Button mInButton1;
    private Button mInButton2;
    private Button mOutButton1;
    private Button mOutButton2;
    private Button mSetThresholdButton;
    private Button mGetThresholdButton;


    private int recvGain;
    private int sendGain;
    private int testGate;
    private int ThresholdModel;
    private int autoThreshold;
    private int fixThreshold;

    private boolean isFixedGate;


    private BroadcastReceiver ServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ConstantValues.InGainQuery)) {
                InGain data = intent.getParcelableExtra("data");
                if (data == null) {
                    return;
                }
                int a = data.getIngain() - 3;

                Toast toast = Toast.makeText(SetPara.this, "接收通道增益：" + String.valueOf(a) + "dB",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 400);
                toast.show();
            }
            if (action.equals(ConstantValues.OutGainQuery)) {
                OutGain data = intent.getParcelableExtra("data");
                if (data == null) {
                    return;
                }
                int a = data.getOutGain();

                Toast toast = Toast.makeText(SetPara.this, "发射通道增益：" + String.valueOf(a) + "dB",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 800);
                toast.show();
            }

            if (action.equals(ConstantValues.ThresholdQuery)) {
                Threshold data = intent.getParcelableExtra("data");
                if (data == null) {
                    return;
                }
                int model = data.getThresholdModel();
                int fixTheshold = data.getFixThreshold();
                int autoTheshold = data.getAutoThreshold();

                if (model == 0) {
                    Toast toast = Toast.makeText(SetPara.this, "自适应门限检测：" + String.valueOf(findAutoThreshold(autoTheshold)) + "dB",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 1000);
                    toast.show();

                } else if (model == 1) {
                    Toast toast = Toast.makeText(SetPara.this, "固定门限检测：" + String.valueOf(fixTheshold) + "dB",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 1000);
                    toast.show();
                }
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpara);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.InGainQuery);
        filter.addAction(ConstantValues.OutGainQuery);
        filter.addAction(ConstantValues.FixCentralFreqQuery);
        filter.addAction(ConstantValues.FixSettingQuery);
        filter.addAction(ConstantValues.SweepRangeQuery);
        filter.addAction(ConstantValues.ThresholdQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(ServiceReceiver, filter);

        initSetting();
        initspinnerSetting();

        bar_recv.setOnSeekBarChangeListener(this);
        bar_send.setOnSeekBarChangeListener(this);
        bar_test.setOnSeekBarChangeListener(this);

        rg.setOnCheckedChangeListener(SetPara.this);
        //spin设置监听器
        spin.setOnItemSelectedListener(SetPara.this);

        initEvent();
    }

    /**
     * 初始化设置
     */
    private void initSetting() {

        //绑定滑动条
        bar_recv = (SeekBar) findViewById(R.id.seekBar_recv);
        bar_send = (SeekBar) findViewById(R.id.seekBar_send);
        bar_test = (SeekBar) findViewById(R.id.seekBar_test);
        //绑定显示的数值
        tv_recv = (TextView) findViewById(R.id.tv_recvGain);
        tv_send = (TextView) findViewById(R.id.tv_sendGain);
        tv_test = (TextView) findViewById(R.id.tv_testGate);

        //绑定两个门限设置面板
        reLay01 = (LinearLayout) findViewById(R.id.reLayout_testGate1);
        reLay02 = (RelativeLayout) findViewById(R.id.reLayout_testGate2);

        //绑定button
        mInButton1 = (Button) findViewById(R.id.bt_setingain);
        mInButton2 = (Button) findViewById(R.id.bt_getingain);
        mOutButton1 = (Button) findViewById(R.id.bt_setoutgain);
        mOutButton2 = (Button) findViewById(R.id.bt_getoutgain);
        mSetThresholdButton = (Button) findViewById(R.id.bt_Setgate);
        mGetThresholdButton = (Button) findViewById(R.id.bt_Getgate);

        //绑定监听按钮
        rg = (RadioGroup) findViewById(R.id.radioGroup1);

        spin = (Spinner) findViewById(R.id.spinner);

        //弹出框查询

        isFixedGate = true;


    }

    /**
     * spinner初始化
     */

    private void initspinnerSetting() {
        //1,设置数据源
        list = new ArrayList<String>();
        list.add("3");
        list.add("10");
        list.add("20");
        list.add("25");
        list.add("30");
        list.add("40");
        //2.新建数组适配器
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        //adapter设置一个下拉列表样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spin加载适配器
        spin.setAdapter(adapter);
    }


    private void initEvent() {
        /**
         * 接收通道增益
         */
        final InGain inGain = new InGain();
        mInButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inGain.setIngain(recvGain + 3);
                if (inGain == null) {
                    return;
                }
                // 点击发送数据到service然后发送至服务器
                Broadcast.sendBroadCast(SetPara.this,
                        ConstantValues.InGainSet, "InGainSet", inGain);
            }
        });

        mInButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x14);

//                if(query!=null){
                Broadcast.sendBroadCast(SetPara.this,
                        ConstantValues.InGainQuery, "InGainQuery", query);
//                }


            }
        });
        /**
         * 发射通道增益
         */
        final OutGain outGain = new OutGain();
        mOutButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outGain.setOutGain(sendGain);
                if (outGain == null) {
                    return;
                }
                // 点击发送数据到service然后发送至服务器
                Broadcast.sendBroadCast(SetPara.this,
                        ConstantValues.OutGainSet, "OutGainSet", outGain);
            }

        });

        mOutButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x15);

                if (query != null) {
                    Broadcast.sendBroadCast(SetPara.this,
                            ConstantValues.OutGainQuery, "OutGainQuery", query);
                }

            }

        });

        /**
         * 检测门限设置
         */

        final Threshold threshold = new Threshold();
        mSetThresholdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                threshold.setEquipmentId(0);
                threshold.setAutoThreshold(autoThreshold);
                threshold.setFixThreshold(fixThreshold);
                threshold.setThresholdModel(ThresholdModel);
                if (threshold == null) {
                    return;
                }
                Broadcast.sendBroadCast(SetPara.this,
                        ConstantValues.ThresholdSet, "ThresholdSet", threshold);

            }
        });


        mGetThresholdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x16);

                if (query != null) {
                    Broadcast.sendBroadCast(SetPara.this,
                            ConstantValues.ThresholdQuery, "ThresholdQuery", query);
                }

            }
        });

        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPara.this.finish();
            }
        });


    }


    /**
     * 查询接受通道增益弹出框
     */
    private void getIngain() {

        View view = View.inflate(getApplicationContext(), R.layout.dialogsetara1, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        builder.setTitle("接受通道增益：");
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    /**
     * 查询接受通道增益弹出框
     */
    private void getOutgain() {

        View view = View.inflate(getApplicationContext(), R.layout.dialogsetara1, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        builder.setTitle("发射通道增益：");
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    /**
     * 点击查询检测门限弹出对话框
     */
    private void getThreshold() {


        View view = View.inflate(getApplicationContext(), R.layout.dialoggetthreshold, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        builder.setTitle("检测门限：");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 查询按钮的toast
     */
    private void toastCheck() {

        if (isFixedGate) {
            Toast toast = Toast.makeText(this, "当前查询到的参数：" + "\n" + "接受通道增益：" + "\n" + "发射通道衰减：" + "\n" + "固定门限值：",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
        if (!isFixedGate) {
            Toast toast = Toast.makeText(this, "当前查询到的参数：" + "\n" + "接受通道增益：" + "\n" + "发射通道衰减：" + "\n" + "自适应门限值：",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rbtn_guding:
                /**
                 * 固定门限显示
                 */
                ThresholdModel = 0x01;
                isFixedGate = true;
                reLay01.setVisibility(View.VISIBLE);
                reLay02.setVisibility(View.GONE);
                break;
            case R.id.rbtn_zidingyi:
                /**
                 * 自定义门限显示
                 */
                ThresholdModel = 0x00;
                isFixedGate = false;
                reLay01.setVisibility(View.GONE);
                reLay02.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                autoThreshold = 3;
                break;
            case 1:
                autoThreshold = 10;
                break;
            case 2:
                autoThreshold = 20;
                break;
            case 3:
                autoThreshold = 25;
                break;
            case 4:
                autoThreshold = 30;
                break;
            case 5:
                autoThreshold = 40;
                break;
            default:
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar_recv:
                tv_recv.setText("当前值：" + progress);
                recvGain = progress;
                break;
            case R.id.seekBar_send:
                tv_send.setText("当前值：" + progress);
                sendGain = progress;
                break;
            case R.id.seekBar_test:
                tv_test.setText("当前值：" + progress);
                fixThreshold = progress;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(ServiceReceiver);
        ServiceReceiver = null;
        super.onDestroy();
    }

    private int findAutoThreshold(int index) {
        int shold = 0;
        switch (index) {
            case 0:
                shold = 3;
                break;
            case 1:
                shold = 10;
                break;
            case 2:
                shold = 20;
                break;
            case 3:
                shold = 25;
                break;
            case 4:
                shold = 30;
                break;
            case 5:
                shold = 40;
                break;
            default:
                break;
        }
        return shold;

    }

}
