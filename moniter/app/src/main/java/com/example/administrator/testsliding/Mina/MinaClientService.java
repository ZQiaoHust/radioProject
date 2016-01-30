package com.example.administrator.testsliding.Mina;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
import com.example.administrator.testsliding.Bean.UploadData;
import com.example.administrator.testsliding.Database.DatabaseHelper;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.GlobalConstants.MyApplication;
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jinaghao on 15/11/18.
 */
public class MinaClientService extends Service {
    private IoSession session;
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private MyApplication myApplication;
    ComputePara computePara = new ComputePara();
    private Boolean Ispsfull = false;//queshao

    List<byte[]> temp_powerSpectrum;
    List<byte[]> temp_abnormalPoint;
    List<float[]> temp_drawSpectrum;
    List<float[]> temp_drawWaterfall;

    List<byte[]> temp_IQwave = new ArrayList<>();
    int SweepParaList_length;

    private int total;
    private int h;
    private int y;
    private int z;
    private int fileIsChanged=0;


    public static final String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/PowerSpectrumFile/";


    private FileOutputStream fos;
    private DataOutputStream dos;

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
                    session.write(data);
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
                    session.write(data);
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
                    session.write(data);
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

                    session.write(data);

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
                    session.write(data);
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
                    session.write(data);
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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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

                    session.write(data);

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
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        myApplication = (MyApplication) getApplication();
        Log.d("service", "service运行次数" + Constants.sevCount);

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

        filter.addAction(ConstantValues.ConnectPCBQuery);

        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(ActivityReceiver, filter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //异常频点和频谱
//                    int size=Constants.Queue_SpectrumVSAbnormal.size();
//                    Log.d("qwr",String.valueOf(size));

                    IoConnector connector = new NioSocketConnector();

                    connector.setHandler(new MyClientHandler());

                    connector.getFilterChain().addLast("codec",
                            new ProtocolCodecFilter(new ToFPGAProtocolFactory()));

                    connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);
                    // connector.getSessionConfig().setReadBufferSize(1024);
//
//                    /**
//                     * 我的电脑的IP
//                     */
//                    ConnectFuture future=connector.connect
//                            (new InetSocketAddress("115.156.209.124",8080));

                    /**
                     * Fpga的IP
                     */
                    ConnectFuture future = connector.connect
                            (new InetSocketAddress("192.168.43.112", 8080));
                    /**
                     * Fpga的IP
                     */

//                    ArrayList ipList=getConnectIp();
//                    String FpgaIP= (String) ipList.get(1);
//                    ConnectFuture future = connector.connect
//                            (new InetSocketAddress(FpgaIP, 8080));
                    /**
                     * Fpga的IP
                     */
//                ConnectFuture future=connector.connect
//                        (new InetSocketAddress(Constants.PCBIP,8080));

                    future.awaitUninterruptibly();// 等待连接创建完成

                    session = future.getSession();

                    Constants.FPGAsession = session;

                    session.getCloseFuture().awaitUninterruptibly();//等待连接断开

                    connector.dispose();


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

                final PowerSpectrumAndAbnormalPonit PSAP = (PowerSpectrumAndAbnormalPonit) message;
                if (PSAP != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            total = PSAP.getTotalBand();

                            if (PSAP.getFunctionID() == 0x0D) {//区分功率谱数据类型
                                /**
                                 * 扫频范围只跨越一个25MHz
                                 */
                                if (firstart == lastend) {
                                    temp_powerSpectrum = new ArrayList<>();
                                    temp_abnormalPoint = new ArrayList<>();
                                    temp_drawSpectrum = new ArrayList<float[]>();
                                    temp_drawWaterfall = new ArrayList<float[]>();
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

                                    writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                    Constants.Queue_DrawRealtimeSpectrum.offer(temp_drawSpectrum);
                                    Constants.Queue_DrawRealtimewaterfall.offer(temp_drawWaterfall);

                                } else {
                                    /**
                                     * 扫频跨越多个25MHz
                                     */
                                    if (firstart == PSAP.getPSbandNum()) {
                                        //判断起始段
                                        temp_powerSpectrum = new ArrayList<>();
                                        temp_abnormalPoint = new ArrayList<>();
                                        temp_drawSpectrum = new ArrayList<float[]>();
                                        temp_drawWaterfall = new ArrayList<float[]>();
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
                                    }
                                    else {
                                        if ((firstart + Constants.spectrumCount) == PSAP.getPSbandNum()) {
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
                                            if(temp_powerSpectrum!=null) {
                                                temp_powerSpectrum.clear();
                                            }
                                            if(temp_abnormalPoint!=null) {
                                                temp_abnormalPoint.clear();
                                            }
                                            if(temp_drawSpectrum!=null) {
                                                temp_drawSpectrum.clear();
                                            }
                                            if(temp_drawWaterfall!=null) {
                                                temp_drawWaterfall.clear();
                                            }
                                            fileIsChanged = 0;
                                            Constants.spectrumCount = 0;
                                        }
                                    }

                                    if ((Constants.spectrumCount+firstart )== lastend + 1) {
                                        //结束
                                        writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                        Constants.Queue_DrawRealtimeSpectrum.offer(temp_drawSpectrum);
                                        Constants.Queue_DrawRealtimewaterfall.offer(temp_drawWaterfall);

                                        fileIsChanged = 0;
                                        Constants.spectrumCount = 0;
                                    }
                                }


                            } else {//背景功率只画图
                                float[] pow = new float[1025];
                                pow[0] = PSAP.getPSbandNum();//输入段序号
                                float[] f1 = computePara.Bytes2Power(PSAP.getPSpower());
                                System.arraycopy(f1, 0, pow, 1, 1024);//填入功率谱值
                                Constants.Queue_BackgroundSpectrum.offer(pow);
                            }

                            //=============================异常频点=================================================
                            ////////////////存入显示列表
                            int length = PSAP.getAPnum() * 3;
                            if (length != 0) {
                                byte[] abnormalList = new byte[length + 1];
                                abnormalList[0] = (byte) PSAP.getAPbandNum();//段序号
                                byte[] allPow = PSAP.getAPpower();
                                System.arraycopy(allPow, 0, abnormalList, 1, length);
                                Constants.Queue_AbnormalFreq_List.offer(abnormalList);
                            }
                        }

                    }).start();
                }
                Log.d("abcd", "写文件结束时间：" + String.valueOf(System.currentTimeMillis()));


            }

            if (message instanceof InGain) {
                InGain data = (InGain) message;
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.InGainQuery, "data", data);
            }

            if (message instanceof SweepRange) {
                SweepRange data = (SweepRange) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.SweepRangeQuery, "data", data);

            }

            if (message instanceof OutGain) {
                OutGain data = (OutGain) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.OutGainQuery, "data", data);

            }

            if (message instanceof Threshold) {
                Threshold data = (Threshold) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.ThresholdQuery, "data", data);

            }

            if (message instanceof FixCentralFreq) {
                FixCentralFreq data = (FixCentralFreq) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.FixCentralFreqQuery, "data", data);

            }

            if (message instanceof FixSetting) {
                FixSetting data = (FixSetting) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.FixSettingQuery, "data", data);

            }
            if (message instanceof Press) {
                Press data = (Press) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.PressQuery, "data", data);

            }
            if (message instanceof PressSetting) {
                PressSetting data = (PressSetting) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.PressSettingQuery, "data", data);

            }
            if (message instanceof StationState) {
                StationState data = (StationState) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.StationStateQuery, "data", data);

            }

            if (message instanceof UploadData) {
                UploadData data = (UploadData) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.uploadQuery, "data", data);

            }

            if (message instanceof Connect) {
                Connect data = (Connect) message;
                Broadcast.sendBroadCast(getBaseContext(), ConstantValues.ConnectPCBQuery, "data", data);

            }


//============================IQ波形文件生成==================================================

            if (message instanceof IQwave) {
                IQwave iQwave = new IQwave();
                iQwave = (IQwave) message;
                if (iQwave != null) {
                    if (iQwave.getNowNum() == 1) {
                        Constants.IQCount++;
                        byte[] byte1 = new byte[6020];
                        System.arraycopy(iQwave.getLocation(), 0, byte1, 0, 10);
                        System.arraycopy(iQwave.getIQpara(), 0, byte1, 11, 5);
                        System.arraycopy(iQwave.getIQwave(), 0, byte1, 16, 6001);
                        temp_IQwave.add(byte1);

                    } else if (Constants.IQCount == iQwave.getNowNum()) {
                        Constants.IQCount++;
                        byte[] byte1 = new byte[6001];
                        System.arraycopy(iQwave.getIQwave(), 0, byte1, 0, 6001);
                        temp_IQwave.add(byte1);

                    } else if (Constants.IQCount != iQwave.getNowNum()) {
                        int need = iQwave.getTotalBands() - Constants.IQCount;
                        for (int i = 0; i < need; i++) {
                            Constants.IQCount++;
                            byte[] byte1 = new byte[6001];
                            System.arraycopy(iQwave.getIQwave(), 0, byte1, 0, 6001);
                            temp_IQwave.add(byte1);
                        }
                    } else if (iQwave.getNowNum() == Constants.IQCount && iQwave.getNowNum() == iQwave.getTotalBands()) {
                        byte[] byte1 = new byte[6001];
                        System.arraycopy(iQwave.getIQwave(), 0, byte1, 0, 6001);
                        temp_IQwave.add(byte1);
                        Constants.Queue_IQwave.offer(temp_IQwave);
                        temp_IQwave.clear();
                        Constants.IQCount = 0;
                    }
                }
            }
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
            super.sessionClosed(session);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
            ReceiveWrong mReceiveWrong = new ReceiveWrong();
            ReceiveRight mReceiveRight = new ReceiveRight();
            //频谱数据超时重传
//            if (((System.currentTimeMillis() - Constants.startTime) > 300)) {
            if (Constants.NotFill) {
                Constants.FPGAsession.write(mReceiveWrong);
                Constants.NotFill = false;
                Constants.ctx.reset();
                h++;
                Log.d("abcd", "重传次数：" + h);


            }
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            super.exceptionCaught(session, cause);

        }
    }


    private int getYear(byte[] bytes) {
        int year;
        year = ((bytes[9] & 0xff) << 4) + ((bytes[10] >> 4) & 0xff);
        return year;
    }

    private int getMonth(byte[] bytes) {
        int month;
        month = (bytes[10] & 0x0f);
        return month;
    }

    private int getDay(byte[] bytes) {
        int day;
        day = ((bytes[11] >> 3) & 0xff);
        return day;
    }

    private int getHour(byte[] bytes) {
        int hour;
        hour = (((bytes[11] & 0x07) << 2) & 0xff) + ((bytes[12] & 0x03) & 0xff);
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
        int hour = getHour(byte6);
        int min = getMin(byte6);
        int sec = getSecond(byte6);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sdf.format(new Date());
        String fname = time + "-" + String.format("%d-%d-%s.%s", 0, Constants.ID, "fine", "pwr");
        //String name = null;
        //创建文件
//        if (PASP.getStyle() == 0) {
//        name = String.format("%d-%d-%d-%d-%d-%d-%d-%d-%s.%s", year, month, day, hour, min, sec,
//                0, Constants.ID, "fine", "pwr");
        int count = 0;


        //判断是否是一秒内的文件，如果是，需要加上1s序号
        File[] PSFile = PSdir.listFiles();
        if (PSFile.length > 0) {
            for (int j = 0; j < PSFile.length; j++) {
                if (fname.equals(PSFile[j].getName())) {
                    count++;
//                    name = String.format("%d-%d-%d-%d-%d-%d-%d-%d-%s.%s", year, month, day,
//                            hour, min, sec, count, Constants.ID, "fine", "pwr");
                    fname = time + "-" + String.format("%d-%d-%s.%s", count, Constants.ID, "fine", "pwr");
                }
            }
        }


        // }
//        else if (PASP.getStyle() == 1) {
//            name = String.format("%d-%d-%d-%d-%d-%d-%d-%d-%s.%s", year, month, day, hour, min, sec,
//                    0, Constants.ID, "coarse", "pwr");
//
//            //判断是否是一秒内的文件，如果是，需要加上1s序号
//            File[] PSFile = PSdir.listFiles();
//            if (PSFile.length > 0) {
//                for (int j = 0; j < PSFile.length; j++) {
//                    if (name == PSFile[j].getName()) {
//                        int count = 0;
//                        count++;
//                        name = String.format("%d-%d-%d-%d-%d-%d-%d-%d-%s.%s", year, month, day,
//                                hour, min, sec, count, Constants.ID, "coarse", "pwr");
//                    }
//                }
//            }
//        }
        if (fname != null) {
            File file = new File(PSdir, fname);
//                                        if (!file.exists()) {
            //获取文件写入流
            try {
                dos = new DataOutputStream(new FileOutputStream(file));
//                                                fos = new FileOutputStream(file);
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
                Log.d("abcde", "写文件个数：" + y);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //在此将文件的信息插入数据库===================
            ContentValues cv = new ContentValues();
            cv.put("filename", fname);
            cv.put("start", myApplication.getSweepStart());
            cv.put("end", myApplication.getSweepEnd());
            cv.put("isChanged", fileIsChanged);
            cv.put("upload", 0);
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
}





