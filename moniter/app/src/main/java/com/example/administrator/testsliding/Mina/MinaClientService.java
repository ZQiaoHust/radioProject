package com.example.administrator.testsliding.Mina;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.testsliding.Bean.BackgroundPowerSpectrum;
import com.example.administrator.testsliding.Bean.Connect;
import com.example.administrator.testsliding.Bean.FixCentralFreq;
import com.example.administrator.testsliding.Bean.FixSetting;
import com.example.administrator.testsliding.Bean.IQwave;
import com.example.administrator.testsliding.Bean.InGain;
import com.example.administrator.testsliding.Bean.OutGain;
import com.example.administrator.testsliding.Bean.PowerSpectrumAndAbnormalPonit;
import com.example.administrator.testsliding.Bean.Press;
import com.example.administrator.testsliding.Bean.PressSetting;
import com.example.administrator.testsliding.Bean.Query;
import com.example.administrator.testsliding.Bean.ReceiveRight;
import com.example.administrator.testsliding.Bean.ReceiveWrong;
import com.example.administrator.testsliding.Bean.StationState;
import com.example.administrator.testsliding.Bean.SweepRange;
import com.example.administrator.testsliding.Bean.Threshold;
import com.example.administrator.testsliding.Bean.ToServerIQwaveFile;
import com.example.administrator.testsliding.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.example.administrator.testsliding.Bean.UploadData;
import com.example.administrator.testsliding.Database.DatabaseHelper;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.GlobalConstants.MyApplication;
import com.example.administrator.testsliding.HeartBeat.HEARTBEATREQUEST;
import com.example.administrator.testsliding.HeartBeat.HEARTBEATRESPONSE;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jinaghao on 15/11/18.
 */
public class MinaClientService extends Service {
    private  static IoSession session=null;
    private static  IoConnector connector = new NioSocketConnector();
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private MyApplication myApplication;
    ComputePara computePara = new ComputePara();

    private Boolean Ispsfull = false;//queshao
    /*************Fpga的IP*************/
   private static String IP="192.168.43.135"; //HUAWEIAP5  ID=15
    //private static String IP="192.168.43.228"; //HUAWEIAP4,ID为14
//    private static String IP="192.168.43.73";//HUAWEIAP3,ID为13
    //private static String IP="192.168.43.99";//HUAWEIAP2  ID=12

    private static int PORT=8899;
    private String FpgaIP;

    private float firstMax = 0, secMax = 0;//需要压制的频点
    List<byte[]> temp_powerSpectrum;
    List<byte[]> temp_abnormalPoint;
    List<float[]> temp_drawSpectrum;
    List<float[]> temp_drawWaterfall;
    List<float[]> temp_drawBackSpectrum;
    Map<Float, Float> map_abnormal;

    List<byte[]> temp_IQwave;
    int SweepParaList_length;

    private int total;
    private int num;
    private int h;
    private int y;
    private int z;
    private int fileIsChanged = 0;
    private Timer timer = new Timer();
    private TimerTask task;


    public static final String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/PowerSpectrumFile/";
    public static final String IQFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/IQwaveFile/";

    private FileOutputStream fos;
    private DataOutputStream dos;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //定时上传IQ文件，每5秒扫描一次
                uploadIQFile();
            }
        }
    };


    private BroadcastReceiver ActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ConstantValues.InGainSet)) {
                InGain data = intent.getParcelableExtra("InGainSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;

            }
            if (action.equals(ConstantValues.InGainQuery)) {
                Query data = intent.getParcelableExtra("InGainQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.SweepRangeQuery)) {
                Query data = intent.getParcelableExtra("SweepRangeQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.SweepRangeSet)) {
                SweepRange data = intent.getParcelableExtra("SweepRangeSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {

                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.OutGainSet)) {
                OutGain data = intent.getParcelableExtra("OutGainSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.OutGainQuery)) {
                Query data = intent.getParcelableExtra("OutGainQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.ThresholdSet)) {
                Threshold data = intent.getParcelableExtra("ThresholdSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.ThresholdQuery)) {
                Query data = intent.getParcelableExtra("ThresholdQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.FixCentralFreqSet)) {
                FixCentralFreq data = intent.getParcelableExtra("FixCentralFreqSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.FixCentralFreqQuery)) {
                Query data = intent.getParcelableExtra("FixCentralFreqQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.FixSettingSet)) {
                FixSetting data = intent.getParcelableExtra("FixSettingSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {

                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.FixSettingQuery)) {
                Query data = intent.getParcelableExtra("FixSettingQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.PressSet)) {
                Press data = intent.getParcelableExtra("PressSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.PressQuery)) {
                Query data = intent.getParcelableExtra("PressQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.PressSettingSet)) {
                PressSetting data = intent.getParcelableExtra("PressSettingSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.PressSettingQuery)) {
                Query data = intent.getParcelableExtra("PressSettingQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {

                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.StationStateQuery)) {
                Query data = intent.getParcelableExtra("StationStateQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {

                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.uploadQuery)) {
                Query data = intent.getParcelableExtra("uploadQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (action.equals(ConstantValues.uploadDataSet)) {
                UploadData data = intent.getParcelableExtra("uploadDataSet");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {

                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.ConnectPCB)) {
                Connect data = intent.getParcelableExtra("connectPCB");
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.ConnectPCBQuery)) {
                Query data = intent.getParcelableExtra("ConnectPCBQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {

                    Constants.FPGAsession.write(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (action.equals(ConstantValues.IsOnlieQuery)) {
                Query data = intent.getParcelableExtra("IsOnlieQuery");
                /**
                 * 在这里把activity的消息转发给服务器
                 */
                if (data == null) {
                    return;
                }
                try {
                    Constants.FPGAsession.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    };


    @Override
    public void onCreate() {
        Constants.sevCount++;
        dbHelper=DatabaseHelper.getInstance(this);//单例模式
        db = dbHelper.getWritableDatabase();
        myApplication = (MyApplication) getApplication();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.InGainSet);
        filter.addAction(ConstantValues.InGainQuery);

        filter.addAction(ConstantValues.OutGainSet);
        filter.addAction(ConstantValues.OutGainQuery);

        filter.addAction(ConstantValues.FixCentralFreqSet);
        filter.addAction(ConstantValues.FixCentralFreqQuery);

        filter.addAction(ConstantValues.FixSettingSet);
        filter.addAction(ConstantValues.FixSettingQuery);

        filter.addAction(ConstantValues.SweepRangeSet);
        filter.addAction(ConstantValues.SweepRangeQuery);

        filter.addAction(ConstantValues.ThresholdSet);
        filter.addAction(ConstantValues.ThresholdQuery);

        filter.addAction(ConstantValues.PressSet);
        filter.addAction(ConstantValues.PressQuery);

        filter.addAction(ConstantValues.PressSettingSet);
        filter.addAction(ConstantValues.PressSettingQuery);

        filter.addAction(ConstantValues.StationStateQuery);

        filter.addAction(ConstantValues.uploadDataSet);
        filter.addAction(ConstantValues.uploadQuery);
        filter.addAction(ConstantValues.ConnectPCB);
        filter.addAction(ConstantValues.ConnectPCBQuery);
        filter.addAction(ConstantValues.IsOnlieQuery);

        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(ActivityReceiver, filter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connector.setHandler(new MyClientHandler());
                    connector.getFilterChain().addLast("codec",
                            new ProtocolCodecFilter(new ToFPGAProtocolFactory()));
                    connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);

//                    ArrayList<String> ipList = getConnectIp();
//                    FpgaIP = (String) ipList.get(1);
//                    ConnectFuture future = connector.connect
//                            (new InetSocketAddress(FpgaIP, PORT));

                    ConnectFuture future = connector.connect
                            (new InetSocketAddress(IP, PORT));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    Constants.FPGAsession = session;
//                    session.getCloseFuture().awaitUninterruptibly();//等待连接断开
//                    connector.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(ActivityReceiver);
        ActivityReceiver = null;
        db.close();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class MyClientHandler extends IoHandlerAdapter {
        @Override
        public void messageReceived(IoSession session, final Object message) throws Exception {
            //处理从服务端接收到的消息，这个消息已经经过decode解码器把消息从字节流转化为对象

            //==========================================功率谱解析
            if (message instanceof PowerSpectrumAndAbnormalPonit) {
                SweepParaList_length = Constants.SweepParaList.size();
                final int firstart = Constants.SweepParaList.get(0).getStartNum();//输入扫频范围第一组的起点对应的段号
                final int lastend = Constants.SweepParaList.get(SweepParaList_length - 1).getEndNum();//输入扫频范围最后
                myApplication.setSweepStart((firstart-1)*25+70);
                myApplication.setSweepEnd(lastend*25+70);
                final PowerSpectrumAndAbnormalPonit PSAP = (PowerSpectrumAndAbnormalPonit) message;
                if (PSAP != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            total = PSAP.getTotalBand();
                            num = PSAP.getNumN();
                            /**
                             * 扫频范围只跨越一个25MHz
                             */
                            if (total == 1) {
                                temp_powerSpectrum = new ArrayList<>();
                                temp_abnormalPoint = new ArrayList<>();
                                temp_drawSpectrum = new ArrayList<float[]>();
                                temp_drawWaterfall = new ArrayList<float[]>();
                                map_abnormal = new HashMap<Float, Float>();
                                //判断是否为有变化的文件
                                if (PSAP.getIsChange() == 0x0f) {
                                    fileIsChanged = 1;
                                }
                                //存数据
                                byte[] byte1 = powerSpec2File(true, PSAP);//填入频段序号和功率谱值
                                temp_powerSpectrum.add(byte1);
                                //异常频点存入写文件
                                byte[] byteAb1 = aP2File(PSAP);
                                temp_abnormalPoint.add(byteAb1);
                                //存入画频谱图图
                                float[] pow = new float[1026];
                                pow[0] = PSAP.getTotalBand();//填入总帧数
                                pow[1] = PSAP.getPSbandNum();//输入第一个频率段序号
                                float[] f1 = computePara.Bytes2Power(PSAP.getPSpower());
                                System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                                temp_drawSpectrum.add(pow);
                                //存入画瀑布图
                                float[] water = new float[1025];
                                water[0] = PSAP.getTotalBand();//填入总段数
                                System.arraycopy(f1, 0, water, 1, 1024);//填入功率谱值
                                temp_drawWaterfall.add(water);
                                //异常频点
                                ////////////////存入显示列表
                                Map<Float, Float> map = abnormalToList(PSAP, firstMax, secMax);
                                map_abnormal.putAll(map);
                                Constants.Queue_Abnormal.offer(map_abnormal);
                                //下发自动压制帧
                                if (Constants.pressModel == 1) {
                                    //单频点
                                    if (Constants.press != null && firstMax != 0) {
                                        Constants.press.setFix1(firstMax);
                                        Constants.press.setFix2(0);
                                        Constants.press.setNumber(1);
                                        Constants.FPGAsession.write(Constants.press);
                                    }
                                }
                                if (Constants.pressModel == 3) {
                                    //双频点
                                    if (Constants.press != null && firstMax != 0 && secMax != 0) {
                                        Constants.press.setFix1(firstMax);
                                        Constants.press.setFix2(secMax);
                                        Constants.press.setNumber(2);
                                        Constants.FPGAsession.write(Constants.press);
                                    }
                                }

                                writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                Constants.Queue_DrawRealtimeSpectrum.offer(temp_drawSpectrum);
                                Constants.Queue_DrawRealtimewaterfall.offer(temp_drawWaterfall);

                            } else {
                                /**
                                 * 扫频跨越多个25MHz
                                 */
                                if (num == 1) {
                                    //判断起始段
                                    temp_powerSpectrum = new ArrayList<>();
                                    temp_abnormalPoint = new ArrayList<>();
                                    temp_drawSpectrum = new ArrayList<float[]>();
                                    temp_drawWaterfall = new ArrayList<float[]>();
                                    map_abnormal = new HashMap<Float, Float>();
                                    if (PSAP.getIsChange() == 0x0f) {
                                        fileIsChanged = 1;
                                    }
                                    //==========第一段存入数据不一样，要单独列================
                                    //存数据
                                    byte[] byte1 = powerSpec2File(true, PSAP);//填入频段序号和功率谱值
                                    temp_powerSpectrum.add(byte1);
                                    //异常频点存入写文件
                                    byte[] byteAb1 = aP2File(PSAP);
                                    temp_abnormalPoint.add(byteAb1);
                                    //存入画频谱图图
                                    float[] pow = new float[1026];
                                    pow[0] = PSAP.getTotalBand();//填入总段数
                                    pow[1] = PSAP.getPSbandNum();//输入段序号
                                    float[] f1 = computePara.Bytes2Power(PSAP.getPSpower());
                                    System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                                    temp_drawSpectrum.add(pow);
                                    //存入画瀑布图
                                    float[] water = new float[1025];
                                    water[0] = PSAP.getTotalBand();//填入总段数
                                    System.arraycopy(f1, 0, water, 1, 1024);//填入功率谱值
                                    temp_drawWaterfall.add(water);
                                    Constants.spectrumCount++;
                                    //异常频点
                                    ////////////////存入显示列表
                                    Map<Float, Float> map = abnormalToList(PSAP, firstMax, secMax);
                                    map_abnormal.putAll(map);
                                } else {
                                    if (num == (Constants.spectrumCount + 1)) {
                                        //===========从第二段开始===============
                                        if (PSAP.getIsChange() == 0x0f) {
                                            fileIsChanged = 1;
                                        }
                                        //频谱数据存入写文件
                                        byte[] byte1 = powerSpec2File(false, PSAP);//填入频段序号和功率谱值
                                        temp_powerSpectrum.add(byte1);
                                        //异常频点存入写文件
                                        byte[] byteAb1 = aP2File(PSAP);
                                        temp_abnormalPoint.add(byteAb1);
                                        //异常频点
                                        ////////////////存入显示列表
                                        Map<Float, Float> map = abnormalToList(PSAP, firstMax, secMax);
                                        map_abnormal.putAll(map);

                                        //存入画频谱图图
                                        float[] pow = new float[1026];
                                        pow[0] = PSAP.getTotalBand();//填入总段数
                                        pow[1] = PSAP.getPSbandNum();//输入段序号
                                        float[] f1 = computePara.Bytes2Power(PSAP.getPSpower());
                                        System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                                        temp_drawSpectrum.add(pow);
                                        //瀑布图
                                        temp_drawWaterfall.add(f1);
                                        Constants.spectrumCount++;
                                    } else {
                                        if (temp_powerSpectrum != null) {
                                            temp_powerSpectrum.clear();
                                        }
                                        if (temp_abnormalPoint != null) {
                                            temp_abnormalPoint.clear();
                                        }
                                        if (temp_drawSpectrum != null) {
                                            temp_drawSpectrum.clear();
                                        }
                                        if (temp_drawWaterfall != null) {
                                            temp_drawWaterfall.clear();
                                        }
                                        fileIsChanged = 0;
                                        Constants.spectrumCount = 0;
                                    }
                                }
                                if ((num == total) && (Constants.spectrumCount == total)) {
                                    //结束
                                    if ((temp_powerSpectrum.size() == total) && (temp_abnormalPoint.size() == total)) {
                                        writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                        Constants.Queue_DrawRealtimeSpectrum.offer(temp_drawSpectrum);
                                        Constants.Queue_DrawRealtimewaterfall.offer(temp_drawWaterfall);
                                    }
                                    Constants.Queue_Abnormal.offer(map_abnormal);
                                    //下发自动压制帧
                                    if (Constants.pressModel == 1) {
                                        //单频点
                                        if (Constants.press != null && firstMax != 0) {
                                            Constants.press.setFix1(firstMax);
                                            Constants.press.setFix2(0);
                                            Constants.press.setNumber(1);
                                            Constants.FPGAsession.write(Constants.press);
                                        }
                                    }
                                    if (Constants.pressModel == 3) {
                                        //双频点
                                        if (Constants.press != null && firstMax != 0 && secMax != 0) {
                                            Constants.press.setFix1(firstMax);
                                            Constants.press.setFix2(secMax);
                                            Constants.press.setNumber(2);
                                            Constants.FPGAsession.write(Constants.press);
                                        }
                                    }
                                    fileIsChanged = 0;
                                    Constants.spectrumCount = 0;
                                }
                            }
                            //=============================异常频点=================================================
                        }
                    }).start();
                }
            }
            /**
             * 背景功率谱
             */
            if (message instanceof BackgroundPowerSpectrum) {
                BackgroundPowerSpectrum back = (BackgroundPowerSpectrum) message;

                if (back.getTotalBand() == 1) {
                    temp_drawBackSpectrum = new ArrayList<>();
                    float[] pow = new float[1026];
                    pow[0] = back.getTotalBand();//总段数
                    pow[1] = back.getPSbandNum();//输入段序号
                    float[] f1 = back.getPSpower();
                    System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                    temp_drawBackSpectrum.add(pow);
                    Constants.Queue_BackgroundSpectrum.offer(temp_drawBackSpectrum);
                } else {
                    if (back.getNumN() == 1) {
                        temp_drawBackSpectrum = new ArrayList<>();
                        float[] pow = new float[1026];
                        pow[0] = back.getTotalBand();//总段数
                        pow[1] = back.getPSbandNum();//输入段序号
                        float[] f1 = back.getPSpower();
                        System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                        temp_drawBackSpectrum.add(pow);
                        Constants.BackgroundCount++;
                    } else {
                        if (back.getNumN() == (Constants.BackgroundCount + 1)) {
                            float[] pow = new float[1026];
                            pow[0] = back.getTotalBand();//总段数
                            pow[1] = back.getPSbandNum();//输入段序号
                            float[] f1 = back.getPSpower();
                            System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                            temp_drawBackSpectrum.add(pow);
                            Constants.BackgroundCount++;
                        } else {
                            if (temp_drawBackSpectrum != null) {
                                temp_drawBackSpectrum.clear();
                            }
                        }
                    }
                    if ((back.getNumN() == back.getTotalBand()) && (Constants.BackgroundCount == total)) {
                        //结束
                        Constants.Queue_BackgroundSpectrum.offer(temp_drawBackSpectrum);
                        Constants.BackgroundCount = 0;
                    }

                }

            }

            if (message instanceof InGain) {
                InGain data = (InGain) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(),
                            ConstantValues.InGainQuery, "data", data);
                }
            }

            if (message instanceof SweepRange) {
                SweepRange data = (SweepRange) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.SweepRangeQuery, "data", data);
                }
            }

            if (message instanceof OutGain) {
                OutGain data = (OutGain) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.OutGainQuery, "data", data);
                }
            }

            if (message instanceof Threshold) {
                Threshold data = (Threshold) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.ThresholdQuery, "data", data);
                }
            }

            if (message instanceof FixCentralFreq) {
                FixCentralFreq data = (FixCentralFreq) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.FixCentralFreqQuery, "data", data);
                }
            }

            if (message instanceof FixSetting) {
                FixSetting data = (FixSetting) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.FixSettingQuery, "data", data);
                }
            }
            if (message instanceof Press) {
                Press data = (Press) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.PressQuery, "data", data);
                }
            }
            if (message instanceof PressSetting) {
                PressSetting data = (PressSetting) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.PressSettingQuery, "data", data);
                }
            }
            if (message instanceof StationState) {
                StationState data = (StationState) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.StationStateQuery, "data", data);
                }
            }

            if (message instanceof UploadData) {
                UploadData data = (UploadData) message;
                if (data.getPacketHead() == 0x66) {
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.uploadQuery, "data", data);
                }
            }

            if (message instanceof Connect) {
                Connect data = (Connect) message;
                if (data.getPacketHead() == 0x66) {
                    //zhuanfa
                    Constants.SERVERsession.write(data);
                } else {
                    Broadcast.sendBroadCast(getBaseContext(), ConstantValues.ConnectPCBQuery, "data", data);
                }
            }


//============================IQ波形文件生成==================================================

            if (message instanceof IQwave) {
                IQwave iQwave = (IQwave) message;
                if (iQwave != null) {
                    if (iQwave.getTotalBands() == 1) {
                        //只有一个数据块
                        temp_IQwave = new ArrayList<>();
                        byte[] byte1 = new byte[6015];
                        System.arraycopy(iQwave.getLocation(), 0, byte1, 0, 9);
                        System.arraycopy(iQwave.getIQpara(), 0, byte1, 9, 5);
                        System.arraycopy(iQwave.getIQwave(), 0, byte1, 14, 6001);
                        temp_IQwave.add(byte1);
                        writeIQFlie(iQwave, temp_IQwave);
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 1000, 5000);

                    } else {
                        if (iQwave.getNowNum() == 1) {
                            temp_IQwave = new ArrayList<>();
                            Constants.IQCount = 0;
                            Constants.IQCount++;
                            byte[] byte1 = new byte[6015];
                            System.arraycopy(iQwave.getLocation(), 0, byte1, 0, 9);
                            System.arraycopy(iQwave.getIQpara(), 0, byte1, 9, 5);
                            System.arraycopy(iQwave.getIQwave(), 0, byte1, 14, 6001);
                            temp_IQwave.add(byte1);
                        } else {

                            if (iQwave.getNowNum() == (Constants.IQCount + 1)) {
                                //从第二块开始
                                Constants.IQCount++;
                                byte[] byte1 = new byte[6001];
                                System.arraycopy(iQwave.getIQwave(), 0, byte1, 0, 6001);
                                temp_IQwave.add(byte1);
                            } else {
                                if (temp_IQwave != null) {
                                    temp_IQwave.clear();
                                }
                                Constants.IQCount = 0;
                            }

                        }
                        if ((iQwave.getNowNum() == iQwave.getTotalBands()) && (Constants.IQCount == iQwave.getTotalBands())) {
                            //结束
                            if (temp_IQwave.size() == iQwave.getTotalBands()) {
                                writeIQFlie(iQwave, temp_IQwave);
                                task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                    }
                                };
                                timer.schedule(task, 1000, 5000);
                            }
                            Constants.IQCount = 0;
                        }

                    }

                }
            }
            //==========================数据转发=======================

        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            Log.d("session","与FPGA断开！");
            while(true) {
                try {
                    Thread.sleep(3000);
                    // 这里是异步操作 连接后立即返回
                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            IP, PORT));
//                    ConnectFuture future = connector.connect
//                            (new InetSocketAddress(FpgaIP, PORT));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    if(session.isConnected()) {
                        Constants.FPGAsession = session;
                        break;
                    }
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
            final ReceiveWrong mReceiveWrong = new ReceiveWrong();
            final ReceiveRight mReceiveRight = new ReceiveRight();
            //频谱数据超时重传
            if (Constants.NotFill) {
                Constants.FPGAsession.write(mReceiveWrong);
                Constants.NotFill = false;
                Constants.ctx.reset();
                Constants.failCount++;
                Log.d("fail", "重传次数：" + Constants.failCount);
            }
            if (Constants.Backfail) {
                Constants.FPGAsession.write(mReceiveWrong);
                Constants.Backfail = false;
                Constants.IsJump = true;
                Constants.ctxBack.reset();
            }
            if (Constants.Isstop) {
                Constants.FPGAsession.write(mReceiveWrong);
                Constants.Isstop = false;
            }
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            super.exceptionCaught(session, cause);

        }
    }


    private int getYear(byte[] bytes) {
        int year;
        year = ((bytes[9] & 0xff) << 4) + ((bytes[10] >> 4) & 0x0f);
        return year;
    }

    private int getMonth(byte[] bytes) {
        int month;
        month = (bytes[10] & 0x0f);
        return month;
    }

    private int getDay(byte[] bytes) {
        int day;
        day = ((bytes[11] >> 3) & 0x1f);
        return day;
    }

    private int getHour(byte[] bytes) {
        int hour;
        hour = ((bytes[11] & 0x07) << 2) + (bytes[12] & 0x03);
        return hour;
    }

    private int getMin(byte[] bytes) {
        int min;
        min = (bytes[12] >> 2) & 0xff;
        return min;
    }

    private int getSecond(byte[] bytes) {
        int second;
        second = (bytes[13]) & 0xff;
        return second;
    }

    private String getFileName(String path) {

        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        } else {
            return null;
        }

    }

    /**
     * 写功率谱文件
     *
     * @param PASP
     * @param mlist
     * @param ablist
     */
    private void writeFlie(PowerSpectrumAndAbnormalPonit PASP, List<byte[]> mlist, List<byte[]> ablist) {
        File PSdir = new File(PSFILE_PATH);
        if (!PSdir.exists()) {
            PSdir.mkdir();
        }
        //取出时间
        byte[] byte6 = PASP.getLocationandTime();
        int year = getYear(byte6);
        int month = getMonth(byte6);
        int day = getDay(byte6);
        int hour = getHour(byte6) + 8;
        int min = getMin(byte6);
        int sec = getSecond(byte6);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sdf.format(new Date());
        String fname = null;
        //String name = null;
        int count = 0;
        //创建文件
        if (PASP.getStyle() == 0) {
//        name = String.format("%d-%d-%d-%d-%d-%d-%d-%d-%s.%s", year, month, day, hour, min, sec,
//                0, Constants.ID, "fine", "pwr");
            fname = time + "-" + String.format("%d-%d-%s.%s", 0, Constants.ID, "fine", "pwr");

            //判断是否是一秒内的文件，如果是，需要加上1s序号
//            File[] PSFile = PSdir.listFiles();
//            int fileNum=PSFile.length;
//            if (fileNum> 0) {
//                for (int j = 0; j < fileNum; j++) {
//                    if (fname.equals(PSFile[j].getName())) {
//                        count++;
//                        fname = time + "-" + String.format("%d-%d-%s.%s", count, Constants.ID, "fine", "pwr");
//                    }
//                }
//            }


        } else if (PASP.getStyle() == 1) {
            fname = time + "-" + String.format("%d-%d-%s.%s", 0, Constants.ID, "coarse", "pwr");
            //判断是否是一秒内的文件，如果是，需要加上1s序号
//            File[] PSFile = PSdir.listFiles();
//            int fileNum=PSFile.length;
//            if (fileNum > 0) {
//                for (int j = 0; j < fileNum; j++) {
//                    if (fname.equals(PSFile[j].getName())) {
//                        count++;
//                        fname = time + "-" + String.format("%d-%d-%s.%s", count, Constants.ID, "coarse", "pwr");
//                    }
//                }
//            }
        }
        if (fname != null) {
            File file = new File(PSdir, fname);
            //获取文件写入流
            try {
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write((byte) 0x00);
                for (int j = 0; j < mlist.size(); j++) {
                    dos.write(mlist.get(j));
                }
                dos.write(0xff);
                for (int k = 0; k < ablist.size(); k++) {
                    dos.write(ablist.get(k));
                }
                dos.write(0x00);
                dos.close();
                y++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            //在此将文件的信息插入数据库===================
            ContentValues cv = new ContentValues();
            cv.put("filename", fname);
            cv.put("start", myApplication.getSweepStart());
            cv.put("end", myApplication.getSweepEnd());
            if(PASP.getIsChange()==0x0f)
                fileIsChanged=1;
            else
                fileIsChanged=0;

            cv.put("isChanged", fileIsChanged);
            cv.put("upload", 0);
            cv.put("times", 0);
            db.insert("localFile", null, cv);
        }
        count = 0;
    }

    /**
     * 解析供写功率谱文件的数据帧
     *
     * @param PSAP
     * @return
     */
    private byte[] powerSpec2File(boolean Isstart, PowerSpectrumAndAbnormalPonit PSAP) {
        byte[] byte1 = new byte[1549];//
        byte[] byte2 = new byte[1537];
        byte2[0] = (byte) PSAP.getPSbandNum();
        byte[] b3 = PSAP.getPSpower();
        System.arraycopy(b3, 0, byte2, 1, 1536);//填入功率谱值
        if (Isstart) {
            //如果是起始段，需要填入经纬度等信息
            byte[] b1 = PSAP.getLocationandTime();
            System.arraycopy(b1, 0, byte1, 0, 9);//填入经纬度

            byte[] b2 = new byte[3];
            b2[0] = (byte) (((PSAP.getSweepModel() & 0xff) << 2) + (PSAP.getFileSendmodel() & 0x03));
            b2[1] = (byte) (((Constants.judgePower & 0xff) << 6) + (Constants.selectRate & 0xff));
            b2[2] = (byte) PSAP.getTotalBand();
            System.arraycopy(b2, 0, byte1, 9, 3);//填入扫频模式，文件上传模式的信息
            byte1[12] = byte2[0];
            System.arraycopy(b3, 0, byte1, 13, 1536);

            return byte1;
        } else {
            return byte2;
        }
    }

    private byte[] aP2File(PowerSpectrumAndAbnormalPonit PSAP) {
        byte[] bytes = new byte[32];
        bytes[0] = (byte) PSAP.getAPbandNum();
        bytes[1] = (byte) PSAP.getAPnum();
        byte[] ap = PSAP.getAPpower();
        System.arraycopy(ap, 0, bytes, 2, 30);
        return bytes;
    }

    private ArrayList<String> getConnectIp() throws Exception {
        ArrayList<String> connectIpList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] splitted = line.split(" +");
            if (splitted != null && splitted.length >= 4) {
                String ip = splitted[0];
                connectIpList.add(ip);
            }
        }
        return connectIpList;
    }

    /**
     * IQ博存文件
     *
     * @param iQwave
     * @param mlist
     */
    private void writeIQFlie(IQwave iQwave, List<byte[]> mlist) {
        ToServerIQwaveFile ToWave = new ToServerIQwaveFile();
        File PSdir = new File(IQFILE_PATH);
        if (!PSdir.exists()) {
            PSdir.mkdir();
        }
        //取出时间
//        byte[] byte6 = iQwave.getTime();
//        int year = getYear(byte6);
//        int month = getMonth(byte6);
//        int day = getDay(byte6);
//        int hour = getHour(byte6)+8;//UTC转北京时间
//        int min = getMin(byte6);
//        int sec = getSecond(byte6);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sdf.format(new Date());
        String fname = null;
//        String name = null;
        int count = 0;
        //创建文件
//        name = String.format("%d-%d-%d-%d-%d-%d-%d.%s", year, month, day, hour, min, sec,
//                 Constants.ID, "Niq");
        fname = time + "-" + String.format("%d-%d.%s", Constants.ID, Constants.sequenceID, "iq");

        if (fname != null) {
            File file = new File(PSdir, fname);
            //获取文件写入流
            try {
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write((byte) 0x00);
                for (int j = 0; j < mlist.size(); j++) {
                    dos.write(mlist.get(j));
                }
                dos.write(0x00);
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //在此将文件的信息插入数据库===================
            ContentValues cv = new ContentValues();
            cv.put("filename", fname);
            cv.put("upload", 0);
            cv.put("_times", 0);
            db.insert("iqFile", null, cv);
        }
    }

    private void uploadIQFile() {
      /*********************************************************************************/
        Cursor cursor = db.rawQuery("SELECT * FROM iqFile WHERE upload=1", null);
        while (cursor.moveToNext()) {
            int times = cursor.getInt(cursor.getColumnIndex("_times"));
            String name = cursor.getString(cursor.getColumnIndex("fileName"));
            ContentValues cvUpload = new ContentValues();
            if(times==3){
                //如果轮询3次，依旧没有上传成功，则将是否上传成功标志位置为0；
                cvUpload.put("upload", 0);
                db.update("iqFile", cvUpload, "filename=?", new String[]{name});
            }else{
                //在这里更新数据库，将没有确认上传成功的文件对应上传次数加1
                times=times+1;
                cvUpload.put("_times", times);
                db.update("iqFile", cvUpload, "filename=?", new String[]{name});
            }
        }
        cursor.close();
        /******************************************************************************/
        Cursor c = db.rawQuery("SELECT filename from iqFile  where  upload=0", null);
        while (c.moveToNext()) {
            //上传文件
            String name = c.getString(c.getColumnIndex("fileName"));
            File file = new File(IQFILE_PATH, name);
            if(!file.exists()){
                //如果文件不存在，从数据库删除记录
                db.delete("iqFile","filename=?", new String[]{name});
            }else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    byte[] content = new byte[fis.available()];
                    byte[] buffer = new byte[content.length];
                    while ((fis.read(buffer)) != -1) {
                        content = buffer;
                    }
                    //将文件里的内容转化为对象
                    ToServerIQwaveFile ToWave = new ToServerIQwaveFile();
                    ToWave.setContent(content);
                    ToWave.setContentLength(content.length);
                    ToWave.setFileName(name);
                    ToWave.setFileNameLength((short) name.getBytes(Charset.forName("UTF-8")).length);
                    try {
                        Constants.FILEsession.write(ToWave);
                        ContentValues cv = new ContentValues();
                        cv.put("upload", 1);
                        db.update("iqFile", cv, "filename=?", new String[]{name});
                        Log.d("file", "上传iq" + name);
                    } catch (Exception e) {

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (c != null) {
            c.close();
        }
    }



    private Map<Float, Float> abnormalToList(PowerSpectrumAndAbnormalPonit PSAP, float fisMax, float secMax) {
        Map<Float, Float> map = new HashMap<>();
        List<Float> mlist = new ArrayList<>();
        if (PSAP.getAPnum() > 0 && PSAP.getAPnum() <= 10) {
            int start = (PSAP.getAPbandNum() - 1) * 25 + 70;
            int length = PSAP.getAPnum() * 3;
            if (length != 0) {
                byte[] data = new byte[length];
                byte[] allPow = PSAP.getAPpower();
                System.arraycopy(allPow, 0, data, 0, length);
                float power = 0;
                for (int i = 0; i < (length) / 3; i++) {
                    float freq = (float) (((((data[i * 3] >> 4) & 0x0f) << 8) + data[i * 3 + 1] & 0xff) * 15 / 1024.0 + start);
                    float f1 = (((data[i * 3] & 0x0f) << 8) + (data[i * 3 + 2] & 0xff));
                    if (((data[i * 3] >> 3) & 0x01) == 0) {
                        power = (float) (f1 / 8.0);
                    } else {
                        power = (float) ((f1 - Math.pow(2, 12)) / 8.0);
                    }
                    map.put(freq, power);
                    mlist.add(power);
                }
            }
            //找出压制点
            Collections.sort(mlist);
            int size = mlist.size();
            if (size >= 1) {
                float f1 = mlist.get(size - 1);
                if (f1 > fisMax) {
                    fisMax = f1;
                }
            }
            if (size >= 2) {
                float f1 = mlist.get(size - 1);
                float f2 = mlist.get(size - 2);
                if (f1 > fisMax) {
                    fisMax = f1;
                }
                if (f2 > secMax) {
                    secMax = f2;
                }
            }

        }
        return map;
    }

}





