package com.hust.radiofeeler.Mina;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.hust.radiofeeler.Bean.BackgroundPowerSpectrum;
import com.hust.radiofeeler.Bean.Connect;
import com.hust.radiofeeler.Bean.FixCentralFreq;
import com.hust.radiofeeler.Bean.FixSetting;
import com.hust.radiofeeler.Bean.IQwave;
import com.hust.radiofeeler.Bean.InGain;
import com.hust.radiofeeler.Bean.OutGain;
import com.hust.radiofeeler.Bean.POAdata;
import com.hust.radiofeeler.Bean.PowerSpectrumAndAbnormalPonit;
import com.hust.radiofeeler.Bean.Press;
import com.hust.radiofeeler.Bean.PressSetting;
import com.hust.radiofeeler.Bean.Query;
import com.hust.radiofeeler.Bean.ReceiveRight;
import com.hust.radiofeeler.Bean.ReceiveWrong;
import com.hust.radiofeeler.Bean.StationState;
import com.hust.radiofeeler.Bean.SweepRange;
import com.hust.radiofeeler.Bean.TDOAdata;
import com.hust.radiofeeler.Bean.Threshold;
import com.hust.radiofeeler.Bean.ToServerIQwaveFile;
import com.hust.radiofeeler.Bean.ToServerPOAFile;
import com.hust.radiofeeler.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.hust.radiofeeler.Bean.ToServerTDOAFile;
import com.hust.radiofeeler.Bean.UploadData;
import com.hust.radiofeeler.Database.DatabaseHelper;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.MyApplication;
import com.hust.radiofeeler.compute.ComputePara;

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
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static final String TAG = "MinaClientService";
    private  static IoSession session=null;
    private static  IoConnector connector = new NioSocketConnector();
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private MyApplication myApplication;
    ComputePara computePara = new ComputePara();
    private int hasfile_count = 0;

    private static int PORT=8899;

    private float firstMax = 0, secMax = 0;//需要压制的频点
    List<byte[]> temp_powerSpectrum;
    List<byte[]> temp_abnormalPoint;
    List<float[]> temp_drawSpectrum;
    List<float[]> temp_drawBackSpectrum;
    ArrayMap<Float, Float> map_abnormal;
    List<byte[]> temp_IQwave;


    private int total;
    private int num;
    private int fileIsChanged = 0;
    private Timer timer = new Timer();
    private TimerTask task;
    private int sec_count = 0;

    public static final String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/PowerSpectrumFile/";
    public static final String IQFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/IQwaveFile/";
    public static final String POA_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/POA/";
    public static final String TDOA_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/TDOA/";
    private DataOutputStream dos;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                //定时上传IQ文件，每5秒扫描一次
                uploadIQFile();
            }else if(msg.what==4){
                //定时上传TDOA文件，每5秒扫描一次
                uploadTDOAFile();
            }
        }
    };

    private Handler hand= new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        uploadPowerSpectrumFile();
                    }else if(msg.what==3){
                        //定时上传POA文件，每5秒扫描一次
                        uploadPOAFile();
                    }
                }
            };

    private BroadcastReceiver ActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action){
                case ConstantValues.InGainSet:
                    InGain data = intent.getParcelableExtra("InGainSet");
                    if (data == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.InGainQuery:
                    Query query = intent.getParcelableExtra("InGainQuery");

                    if (query == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(query);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                break;
                case ConstantValues.SweepRangeQuery:
                    Query see = intent.getParcelableExtra("SweepRangeQuery");
                    if (see == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(see);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                break;
                case ConstantValues.SweepRangeSet:
                    SweepRange set = intent.getParcelableExtra("SweepRangeSet");
                    if (set == null) {
                        return;
                    }
                    try {

                        Constants.FPGAsession.write(set);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ConstantValues.OutGainSet:
                    OutGain out = intent.getParcelableExtra("OutGainSet");
                    if (out == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.OutGainQuery:
                    Query gain = intent.getParcelableExtra("OutGainQuery");
                    if (gain == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(gain);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.ThresholdSet:
                    Threshold thre = intent.getParcelableExtra("ThresholdSet");
                    if (thre == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(thre);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.ThresholdQuery:
                    Query thresholdQuery = intent.getParcelableExtra("ThresholdQuery");
                    if (thresholdQuery == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(thresholdQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.FixCentralFreqSet:
                    FixCentralFreq fix = intent.getParcelableExtra("FixCentralFreqSet");

                    if (fix == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(fix);
                        Toast.makeText(getBaseContext(), "定频模式已设定", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.FixCentralFreqQuery:
                    Query fixq = intent.getParcelableExtra("FixCentralFreqQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (fixq == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(fixq);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.FixSettingSet:
                    FixSetting fixs = intent.getParcelableExtra("FixSettingSet");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (fixs == null) {
                        return;
                    }
                    try {

                        Constants.FPGAsession.write(fixs);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.FixSettingQuery:
                    Query fixSettingQuery = intent.getParcelableExtra("FixSettingQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (fixSettingQuery == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(fixSettingQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.PressSet:
                    Press press = intent.getParcelableExtra("PressSet");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (press == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(press);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.PressQuery:
                    Query pressQuery = intent.getParcelableExtra("PressQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (pressQuery == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(pressQuery);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ConstantValues.PressSettingSet:
                    PressSetting pressSetting = intent.getParcelableExtra("PressSettingSet");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (pressSetting == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(pressSetting);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ConstantValues.PressSettingQuery:
                    Query pressSettingQuery = intent.getParcelableExtra("PressSettingQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (pressSettingQuery == null) {
                        return;
                    }
                    try {

                        Constants.FPGAsession.write(pressSettingQuery);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.StationStateQuery:
                    Query stationStateQuery = intent.getParcelableExtra("StationStateQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (stationStateQuery == null) {
                        return;
                    }
                    try {

                        Constants.FPGAsession.write(stationStateQuery);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.uploadQuery:
                    Query uploadQuery = intent.getParcelableExtra("uploadQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (uploadQuery == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(uploadQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.uploadDataSet:
                    UploadData uploadData = intent.getParcelableExtra("uploadDataSet");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (uploadData == null) {
                        return;
                    }
                    try {

                        Constants.FPGAsession.write(uploadData);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.ConnectPCB:
                    Connect connect = intent.getParcelableExtra("connectPCB");
                    if (connect == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(connect);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.ConnectPCBQuery:
                    Query connectPCBQuery = intent.getParcelableExtra("ConnectPCBQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (connectPCBQuery == null) {
                        return;
                    }
                    try {

                        Constants.FPGAsession.write(connectPCBQuery);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.IsOnlieQuery:
                    Query onlieQuery = intent.getParcelableExtra("IsOnlieQuery");
                    /**
                     * 在这里把activity的消息转发给服务器
                     */
                    if (onlieQuery == null) {
                        return;
                    }
                    try {
                        Constants.FPGAsession.write(onlieQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请连接硬件", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };


    @Override
    public void onCreate() {
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

        connector.setHandler(new MyClientHandler());
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ToFPGAProtocolFactory()));
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while(true) {
                        try {
                            Thread.sleep(300);
                            // 这里是异步操作 连接后立即返回
                            ConnectFuture future = connector.connect(new InetSocketAddress(
                                    Constants.IP, PORT));
                            future.awaitUninterruptibly();// 等待连接创建完成
                            session = future.getSession();

                            if(session.isConnected()) {
                                Constants.FPGAsession = session;
                                Looper.prepare();
                                Toast.makeText(getBaseContext(),"连接成功！",Toast.LENGTH_SHORT).show();
                                Constants.ID=getId(Constants.IP);
                                Looper.loop();//
                                break;
                            }
                        } catch (Exception e) {
                            Looper.prepare();
                            Toast.makeText(getBaseContext(),"连接失败！",Toast.LENGTH_SHORT).show();
                            Looper.loop();//
                            e.printStackTrace();

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }




    public class MyClientHandler extends IoHandlerAdapter {
        @Override
        public void messageReceived(IoSession session, final Object message) throws Exception {
            //处理从服务端接收到的消息，这个消息已经经过decode解码器把消息从字节流转化为对象

            //==========================================功率谱解析
            if (message instanceof PowerSpectrumAndAbnormalPonit) {

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

                                //存数据
                                if(Constants.isWriteFile) {
                                    byte[] byte1 = powerSpec2File(true, PSAP);//填入频段序号和功率谱值
                                    temp_powerSpectrum.add(byte1);
                                    //异常频点存入写文件
                                    byte[] byteAb1 = aP2File(PSAP);
                                    temp_abnormalPoint.add(byteAb1);
                                }
                                //存入画频谱图图
                                float[] pow = new float[1026];
                                pow[0] = PSAP.getTotalBand();//填入总帧数
                                pow[1] = PSAP.getPSbandNum();//输入第一个频率段序号
                                float[] f1 = computePara.Bytes2Power(PSAP.getPSpower());
                                System.arraycopy(f1, 0, pow, 2, 1024);//填入功率谱值
                                temp_drawSpectrum.add(pow);
                                //存入画瀑布图
//                                float[] water = new float[1025];
//                                water[0] = PSAP.getTotalBand();//填入总段数
//                                System.arraycopy(f1, 0, water, 1, 1024);//填入功率谱值
//                                temp_drawWaterfall.add(water);
                                //异常频点
                                ////////////////存入显示列表

                                map_abnormal= abnormalToList(PSAP, firstMax, secMax);
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

                                    if (Constants.sendMode == 1) {
                                        //手动上传
                                        writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                    } else if ((Constants.sendMode == 2) && (PSAP.getIsChange() == 0x0f)) {
                                        fileIsChanged = 1;
                                        //自动抽取上传模式，有变化就存文件
                                        //判断是否为有变化的文件
                                        writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                    } else if (Constants.sendMode == 3 && Constants.SELECT_COUNT == 1) {
                                        //抽取上传
                                        writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                    }


                                Lock lock = new ReentrantLock(); //锁对象
                                lock.lock();
                                try {
                                    Constants.Queue_DrawRealtimeSpectrum.offer(temp_drawSpectrum);
                                    // Constants.Queue_DrawRealtimewaterfall.offer(temp_drawWaterfall);
                                }catch (Exception e) {

                                } finally {
                                    lock.unlock();
                                }

                            } else {
                                /**
                                 * 扫频跨越多个25MHz
                                 */
                                if (num == 1) {
                                    //判断起始段
                                    temp_powerSpectrum = new ArrayList<>();
                                    temp_abnormalPoint = new ArrayList<>();
                                    temp_drawSpectrum = new ArrayList<float[]>();
                                   // temp_drawWaterfall = new ArrayList<float[]>();
                                    map_abnormal = new ArrayMap<Float, Float>();
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
//                                    float[] water = new float[1025];
//                                    water[0] = PSAP.getTotalBand();//填入总段数
//                                    System.arraycopy(f1, 0, water, 1, 1024);//填入功率谱值
//                                    temp_drawWaterfall.add(water);
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
//
                                        fileIsChanged = 0;
                                        Constants.spectrumCount = 0;
                                    }
                                }
                                if ((num == total) && (Constants.spectrumCount == total)) {
                                    //结束
                                    if ((temp_powerSpectrum.size() == total) && (temp_abnormalPoint.size() == total)) {
                                        long t1= System.currentTimeMillis();
                                        if(Constants.sendMode==1){
                                            //手动上传
                                            writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                        }else if ((Constants.sendMode==2)&&(PSAP.getIsChange() == 0x0f)) {
                                            fileIsChanged = 1;
                                            //自动抽取上传模式，有变化就存文件
                                            //判断是否为有变化的文件
                                            writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                        }else if(Constants.sendMode==3&&Constants.SELECT_COUNT==1){
                                            //抽取上传
                                            writeFlie(PSAP, temp_powerSpectrum, temp_abnormalPoint);//写文件
                                        }

                                            Constants.Queue_DrawRealtimeSpectrum.offer(temp_drawSpectrum);

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
                            //定时清除缓存
                            Log.d("MainClient","shishi："+Constants.Queue_DrawRealtimeSpectrum.size());
                            if(Constants.Queue_DrawRealtimeSpectrum.size()>10){
                                Constants.Queue_DrawRealtimeSpectrum.clear();
                            }
//                            if(Constants.Queue_DrawRealtimewaterfall.size()>30){
//                                Constants.Queue_DrawRealtimewaterfall.clear();
//                            }
                            //=============================异常频点=================================================
                        }
                    }).start();


                    //开启文件上传，自动传输和抽取上传
                    if(Constants.FILEsession!=null&&(Constants.sendMode==2||Constants.sendMode==3)){
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                hand.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 1000, 30000);
                    }

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
                    Lock lock = new ReentrantLock(); //锁对象
                    lock.lock();
                    try {
                        Constants.Queue_BackgroundSpectrum.offer(temp_drawBackSpectrum);
                    }catch (Exception e) {

                    } finally {
                        lock.unlock();
                    }
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
                    if ((back.getNumN() == back.getTotalBand()) && (Constants.BackgroundCount == back.getTotalBand() )) {

                            Constants.Queue_BackgroundSpectrum.offer(temp_drawBackSpectrum);
                        Constants.BackgroundCount = 0;
                    }

                }
                //清除缓存
                Log.d("MainClient","背景："+Constants.Queue_BackgroundSpectrum.size());
                if(Constants.Queue_BackgroundSpectrum.size()>10){
                    Constants.Queue_BackgroundSpectrum.clear();
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

                            }
                            Constants.IQCount = 0;
                        }

                    }
                    if(Constants.FILEsession!=null){
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 1000, 5000);
                    }
                }
            }
            //辐射源数据帧,数据解析类似于扫频，因此部分变量公用
            if (message instanceof POAdata) {
                final POAdata poa= (POAdata) message;
                if (poa != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            total = poa.getTotalBand();
                            num = poa.getNumN();
                            /**
                             * 扫频范围只跨越一个25MHz
                             */
                            if (total == 1) {
                                temp_abnormalPoint = new ArrayList<>();
                                if(poa.getAblNum()==0) {
                                    //没有异常频点
                                    return;
                                }
                                Constants.POAabNum=poa.getAblNum();
                                temp_abnormalPoint.add(poa.getAbnormalInfo());
                                writePOAfile(poa,temp_abnormalPoint);
                            } else {
                                /**
                                 * 扫频跨越多个25MHz
                                 */
                                if (num == 1) {
                                    //判断起始段
                                    temp_abnormalPoint = new ArrayList<>();
                                    if(poa.getAblNum()!=0){
                                        //有异常频点
                                        temp_abnormalPoint.add(poa.getAbnormalInfo());
                                        Constants.POAabNum=poa.getAblNum();
                                    }
                                    Constants.spectrumCount++;

                                } else {
                                    if (num == (Constants.spectrumCount + 1)) {
                                        if(poa.getAbnormalInfo()!=null) {
                                            //有异常频点
                                            temp_abnormalPoint.add(poa.getAbnormalInfo());
                                            Constants.POAabNum+=poa.getAblNum();
                                        }
                                        Constants.spectrumCount++;
                                    } else {
                                        if (temp_abnormalPoint != null) {
                                            temp_abnormalPoint.clear();
                                        }
                                        Constants.spectrumCount = 0;
                                    }
                                }
                                if ((num == total) && (Constants.spectrumCount == total)) {
                                    //结束
                                    if (temp_abnormalPoint.size() != 0) {
                                        writePOAfile(poa,temp_abnormalPoint);
                                    }
                                    Constants.spectrumCount = 0;
                                }
                            }
                        }
                    }).start();

                    if(Constants.FILEsession!=null){
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 3;
                                hand.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 1000, 30000);
                    }
                }
            }

            //TDOA数据帧，与IQ数据帧一样
            if (message instanceof TDOAdata) {
                TDOAdata td = (TDOAdata) message;
                if (td != null) {

                        //一个数据块一个文件
                        byte[] byte1 = new byte[6015];
                        System.arraycopy(td.getLocation(), 0, byte1, 0, 9);
                        System.arraycopy(td.getIQpara(), 0, byte1, 9, 5);
                        System.arraycopy(td.getIQwave(), 0, byte1, 14, 6001);

                        writeTDOAFlie(td, byte1);

                    if(Constants.FILEsession!=null){
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 4;
                                handler.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 1000, 5000);
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
           Log.d("MinaClientService","FPGA  SESSION CLOSE");
            while(true) {
                try {
                    Thread.sleep(3000);
                    // 这里是异步操作 连接后立即返回
                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            Constants.IP, PORT));
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
            //频谱数据超时重传
            if (Constants.NotFill) {
                Constants.FPGAsession.write(mReceiveWrong);
                Constants.NotFill = false;
                Constants.ctx.reset();
                Constants.failCount++;
                Log.d("trans", "重传次数：" + Constants.failCount);
            }
            if (Constants.Backfail) {
                Constants.FPGAsession.write(mReceiveWrong);
                Constants.Backfail = false;
                Constants.IsJump = true;
                Constants.ctx.reset();
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


    /**
     * 写功率谱文件
     *
     * @param PASP
     * @param mlist
     * @param ablist
     */
    private void writeFlie(PowerSpectrumAndAbnormalPonit PASP, List<byte[]> mlist, List<byte[]> ablist) {
        int times=0;//辅助传输

             //取出时间
             byte[] bytes = PASP.getLocationandTime();
             //location
             String location = null;
             if (bytes[0] == 0) {
                 location = "E";
             } else {
                 location = "W";
             }
             float longtitude = (float) ((bytes[1] & 0xff) + ((bytes[2] >> 2) & 0x3f) / 60.0 +
                     (((bytes[2] & 0x03) << 8) + (bytes[3] & 0xff)) / 60000.0);

             float latitude = (float) ((bytes[4] & 0x7f) + ((bytes[5] >> 2) & 0x3f) / 60.0 +
                     (((bytes[5] & 0x03) << 8) + (bytes[6] & 0xff)) / 60000.0);

             java.text.DecimalFormat df = new java.text.DecimalFormat("0.000000");
             if (longtitude != 0) {
                 location += df.format(longtitude);//截小数点后6位，返回为String
             }
             if ((bytes[4] >> 7) == 0) {
                 location += ",N";
             } else {
                 location += ",S";
             }
             if (latitude != 0) {
                 location += df.format(latitude);//截小数点后6位，返回为String
             }
             Date date = new Date();
             long timeSec = date.getTime() / 1000;//存储到秒

             int year = ((bytes[9] & 0xff) << 4) + ((bytes[10] >> 4) & 0x0f);
             int month = (bytes[10] & 0x0f);
             int day = ((bytes[11] >> 3) & 0x1f);
             int hour = ((bytes[11] & 0x07) << 2) + (bytes[12] & 0x03) + 8;//UTC转北京时间
             int min = (bytes[12] >> 2) & 0x3f;
             int sec = (bytes[13]) & 0xff;
             StringBuilder sb = new StringBuilder();
             sb.append(year);
             sb.append("-");
             sb.append((month < 10 ? "0" + month : month));
             sb.append("-");
             sb.append((day < 10 ? "0" + day : day));
             sb.append("-");
             sb.append((hour < 10 ? "0" + hour : hour));
             sb.append("-");
             sb.append((min < 10 ? "0" + min : min));
             sb.append("-");
             sb.append((sec < 10 ? "0" + sec : sec));
             if (sec != sec_count) {
                 sec_count = sec;
                 hasfile_count = 0;
             } else {
                 hasfile_count++;
             }
             sb.append(hasfile_count);
             sb.append(Constants.ID);

             //创建文件
             if (PASP.getStyle() == 0) {
                 sb.append("fine");
             } else if (PASP.getStyle() == 1) {
                 sb.append("coarse");
             }
             sb.append("pwr");
             String name = sb.toString();
             if (name == null)
                 return;
        if(mlist.size()!=0) {
             if (Constants.isUpload != 0) {
                 //上传关闭
                 //本地路径入库，不生成文件
                 times = 2;
             } else {
                 File PSdir = new File(PSFILE_PATH);
                 if (!PSdir.exists()) {
                     PSdir.mkdirs();//mkdir()不能创建多个目录
                 }
                 File file = new File(PSdir, name);
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


                 } catch (Exception e) {
                     e.printStackTrace();
                 } finally {
                     try {
                         if (dos != null) {
                             dos.close();
                         }
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }
         }
        try {
            //在此将文件的信息插入数据库===================
            ContentValues cv = new ContentValues();
            cv.put("filename", name);
            cv.put("start", myApplication.getSweepStart());
            cv.put("end", myApplication.getSweepEnd());
            cv.put("location", location);
            cv.put("isShow", 0);
            if (PASP.getIsChange() == 0x0f)
                fileIsChanged = 1;
            else
                fileIsChanged = 0;

            cv.put("isChanged", fileIsChanged);
            cv.put("upload", 0);
            cv.put("times", times);
            cv.put("fileTime",timeSec);
            db.insert("localFile", null, cv);
        }catch(Exception e){
            e.printStackTrace();
        }

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
            PSdir.mkdirs();
        }
        //取出时间
        byte[] bytes = iQwave.getTime();
        int year =((bytes[0]&0xff)<<4)+((bytes[1]>>4)&0x0f);
        int month =(bytes[1]& 0x0f);
        int day =((bytes[2] >> 3) & 0x1f);
        int hour =((bytes[2] & 0x07) << 2) + (bytes[3] & 0x03)+8;//UTC转北京时间
        int min = (bytes[3] >> 2) & 0x3f;
        int sec = (bytes[4]) & 0xff;
        String Syear = String.valueOf(year);
        String smonth = String.valueOf(month< 10 ? "0" + month :month);
        String Sday = String.valueOf(day< 10 ? "0" + day : day);

        String Shour = String.valueOf(hour< 10 ? "0" + hour : hour);
        String Smin = String.valueOf(min< 10 ? "0" + min : min);
        String SEC = String.valueOf(sec< 10 ? "0" + sec : sec);
        String name = null;

        //创建文件
        name =Syear + "-" + smonth + "-" + Sday + "-" + Shour + "-" + Smin+"-"+SEC + "-" +
                String.format("%d-%d.%s", Constants.ID, Constants.sequenceID, "iq");

//        fname = time + "-" + String.format("%d-%d.%s", Constants.ID, Constants.sequenceID, "iq");

        if (name != null) {
            File file = new File(PSdir, name);
            //获取文件写入流
            try {
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write((byte) 0x00);
                for (int j = 0; j < mlist.size(); j++) {
                    dos.write(mlist.get(j));
                }
                dos.write(0x00);
                dos.close();

                //在此将文件的信息插入数据库===================
                ContentValues cv = new ContentValues();
                cv.put("filename", name);
                cv.put("upload", 0);
                cv.put("_times", 0);
                db.insert("iqFile", null, cv);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (dos != null) {
                        dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
        if(cursor!=null)
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
                }finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (c != null) {
            c.close();
        }
    }



    private ArrayMap<Float, Float> abnormalToList(PowerSpectrumAndAbnormalPonit PSAP, float fisMax, float secMax) {
        ArrayMap<Float, Float> map = new ArrayMap<>();
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
                    float freq = (float) (((((data[i * 3] >> 4) & 0x0f) << 8) + (data[i * 3 + 1] & 0xff)) * 25 / 1024.0 + start);
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
  private int getId(String IP){
      int id=0;
      switch(IP){
          case "192.168.43.195":
              id=11;
              break;
          case "192.168.43.245":
              id=12;
              break;
          case "192.168.43.34":
              id=13;
              break;
          case "192.168.43.61":
              id=14;
              break;
          case "192.168.43.233":
              id=15;
              break;
          default:
              break;
      }
      return id;
  }

    private void uploadPowerSpectrumFile(){

        /*********************************************************************************/
        Cursor cursor = db.rawQuery("SELECT * FROM localFile WHERE upload=1", null);
        while (cursor.moveToNext()) {
            int times = cursor.getInt(cursor.getColumnIndex("times"));
            String name = cursor.getString(cursor.getColumnIndex("fileName"));
            ContentValues cvUpload = new ContentValues();
            if(times==3){
                //如果轮询3次，依旧没有上传成功，则将是否上传成功标志位置为0；
                cvUpload.put("upload", 0);
                cvUpload.put("times", 0);
                db.update("localFile", cvUpload, "filename=?", new String[]{name});
            }else{
                //在这里更新数据库，将没有确认上传成功的文件对应上传次数加1
                times=times+1;
                cvUpload.put("times", times);
                db.update("localFile", cvUpload, "filename=?", new String[]{name});
            }
        }
        if(cursor!=null)
            cursor.close();
        /*****************************************************************************/
        Cursor c = db.rawQuery("SELECT filename from localFile  where  upload=0", null);
        while (c.moveToNext()) {

            //上传文件
            String name = c.getString(c.getColumnIndex("fileName"));
            File file = new File(PSFILE_PATH, name);
            if(!file.exists()){
                //如果文件不存在，从数据库删除记录
                db.delete("localFile","filename=?", new String[]{name});
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
                    ToServerPowerSpectrumAndAbnormalPoint ToPS = new ToServerPowerSpectrumAndAbnormalPoint();
                    ToPS.setContent(content);
                    ToPS.setContentLength(content.length);
                    ToPS.setFileName(name);
                    ToPS.setFileNameLength((short) name.getBytes(Charset.forName("UTF-8")).length);
                    //将功率谱对象用服务器的session发出去
                    Constants.FILEsession.write(ToPS);
                    Log.d("file", "上传：" + name);
                    //在这里更新数据库，将文件是否上传的标志位置为1
                    ContentValues cvUpload = new ContentValues();
                    cvUpload.put("upload", 1);
                    db.update("localFile", cvUpload, "filename=?", new String[]{name});
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "文件上传较多，请稍等", Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "请连接服务器", Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        if (c != null) {
            c.close();
        }

    }

    private void writePOAfile(POAdata poa, List<byte[]> mlist) {
        if(mlist==null||mlist.size()==0)
            return;
        ToServerIQwaveFile ToWave = new ToServerIQwaveFile();
        File PSdir = new File(POA_PATH);
        if (!PSdir.exists()) {
            PSdir.mkdirs();
        }
        //取出时间
        byte[] bytes = poa.getTime();
        int year =((bytes[0]&0xff)<<4)+((bytes[1]>>4)&0x0f);
        int month =(bytes[1]& 0x0f);
        int day =((bytes[2] >> 3) & 0x1f);
        int hour =((bytes[2] & 0x07) << 2) + (bytes[3] & 0x03)+8;//UTC转北京时间
        int min = (bytes[3] >> 2) & 0x3f;
        int sec = (bytes[4]) & 0xff;
        int count=((bytes[5]&0xff)<<24)+((bytes[6]&0xff)<<16)+((bytes[7]&0xff)<<8)+(bytes[8]&0xff);
        String Syear = String.valueOf(year);
        String smonth = String.valueOf(month< 10 ? "0" + month :month);
        String Sday = String.valueOf(day< 10 ? "0" + day : day);

        String Shour = String.valueOf(hour< 10 ? "0" + hour : hour);
        String Smin = String.valueOf(min< 10 ? "0" + min : min);
        String SEC = String.valueOf(sec< 10 ? "0" + sec : sec);
        String Scount = String.valueOf(count< 10 ? "0" + count : count);


        //创建文件
        String name =Syear + "-" + smonth + "-" + Sday + "-" + Shour + "-" + Smin+"-"+SEC + "-" +Scount + "-" +
                String.format("%d.%s", Constants.ID, "poa");

        if (name != null) {
            File file = new File(PSdir, name);
            //获取文件写入流
            try {
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write((byte) 0x00);
                dos.write(poa.getLocation());
                dos.write((byte)Constants.POAabNum);//异常信号的总个数
                for (int j = 0; j < mlist.size(); j++) {
                    dos.write(mlist.get(j));
                }
                dos.write(0x00);
                dos.close();
                Constants.POAabNum=0;
                //在此将文件的信息插入数据库===================
                ContentValues cv = new ContentValues();
                cv.put("filename", name);
                cv.put("upload", 0);
                cv.put("_times", 0);
                db.insert("poaFile", null, cv);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (dos != null) {
                        dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    /**
     * TDOA存文件
     *
     * @param td
     * @param
     */
    private void writeTDOAFlie(TDOAdata td, byte[] mbyte) {

        File PSdir = new File(TDOA_PATH);
        if (!PSdir.exists()) {
            PSdir.mkdirs();
        }
        //取出时间
        byte[] bytes = td.getTime();
        int year =((bytes[0]&0xff)<<4)+((bytes[1]>>4)&0x0f);
        int month =(bytes[1]& 0x0f);
        int day =((bytes[2] >> 3) & 0x1f);
        int hour =((bytes[2] & 0x07) << 2) + (bytes[3] & 0x03)+8;//UTC转北京时间
        int min = (bytes[3] >> 2) & 0x3f;
        int sec = (bytes[4]) & 0xff;
        int count=((bytes[5]&0xff)<<24)+((bytes[6]&0xff)<<16)+((bytes[7]&0xff)<<8)+(bytes[8]&0xff);
        String Syear = String.valueOf(year);
        String smonth = String.valueOf(month< 10 ? "0" + month :month);
        String Sday = String.valueOf(day< 10 ? "0" + day : day);
        String Shour = String.valueOf(hour< 10 ? "0" + hour : hour);
        String Smin = String.valueOf(min< 10 ? "0" + min : min);
        String SEC = String.valueOf(sec< 10 ? "0" + sec : sec);
        String Scount = String.valueOf(count< 10 ? "0" + count : count);
        String name = null;

        //创建文件
        name =Syear + "-" + smonth + "-" + Sday + "-" + Shour + "-" + Smin+"-"+SEC + "-" +Scount + "-" +
                String.format("%d.%s", Constants.ID, "tdoa");


        if (name != null) {
            File file = new File(PSdir, name);

            //获取文件写入流
            try {
                dos = new DataOutputStream(new FileOutputStream(file));
                dos.write((byte) 0x00);
                dos.write(mbyte);
                dos.write(0x00);
                dos.close();

                //在此将文件的信息插入数据库===================
                ContentValues cv = new ContentValues();
                cv.put("filename", name);
                cv.put("upload", 0);
                cv.put("_times", 0);
                db.insert("tdoaFile", null, cv);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (dos != null) {
                        dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
//poa\上传
    private void uploadPOAFile() {
        /*********************************************************************************/
        Cursor cursor = db.rawQuery("SELECT * FROM poaFile WHERE upload=1", null);
        while (cursor.moveToNext()) {
            int times = cursor.getInt(cursor.getColumnIndex("_times"));
            String name = cursor.getString(cursor.getColumnIndex("fileName"));
            ContentValues cvUpload = new ContentValues();
            if(times==3){
                //如果轮询3次，依旧没有上传成功，则将是否上传成功标志位置为0；
                cvUpload.put("upload", 0);
                db.update("poaFile", cvUpload, "filename=?", new String[]{name});
            }else{
                //在这里更新数据库，将没有确认上传成功的文件对应上传次数加1
                times=times+1;
                cvUpload.put("_times", times);
                db.update("poaFile", cvUpload, "filename=?", new String[]{name});
            }
        }
        if(cursor!=null)
            cursor.close();
        /******************************************************************************/
        Cursor c = db.rawQuery("SELECT filename from poaFile  where  upload=0", null);
        while (c.moveToNext()) {
            //上传文件
            String name = c.getString(c.getColumnIndex("fileName"));
            File file = new File(POA_PATH, name);
            if(!file.exists()){
                //如果文件不存在，从数据库删除记录
                db.delete("poaFile","filename=?", new String[]{name});
            }else {
//                long bufferszie = Constants.FILEsession.getScheduledWriteBytes();
//                System.out.println("内存缓冲区大小：" + bufferszie);
//                if (bufferszie > 50* 1024 * 1024)
//                    try {
//                        System.out.println("内存缓冲区的数据过多");
//                        while (true) {
//                            Thread.sleep(2 * 1000);
//                            System.out.println(
//                                    "内存缓冲区大小：" + Constants.FILEsession.getScheduledWriteBytes());
//                            if (Constants.FILEsession.getScheduledWriteBytes() < 5 * 1024 * 1024)
//                                break;
//                        }
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    byte[] content = new byte[fis.available()];
                    byte[] buffer = new byte[content.length];
                    while ((fis.read(buffer)) != -1) {
                        content = buffer;
                    }
                    //将文件里的内容转化为对象
                    ToServerPOAFile ToWave = new ToServerPOAFile();
                    ToWave.setContent(content);
                    ToWave.setContentLength(content.length);
                    ToWave.setFileName(name);
                    ToWave.setFileNameLength((short) name.getBytes(Charset.forName("UTF-8")).length);
                    try {
                        Constants.FILEsession.write(ToWave);
                        ContentValues cv = new ContentValues();
                        cv.put("upload", 1);
                        db.update("poaFile", cv, "filename=?", new String[]{name});
                        Log.d("file", "上传POA" + name);
                    } catch (Exception e) {

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (c != null) {
            c.close();
        }
    }
    //TDOA\上传
    private void uploadTDOAFile() {
        /*********************************************************************************/
        Cursor cursor = db.rawQuery("SELECT * FROM tdoaFile WHERE upload=1", null);
        while (cursor.moveToNext()) {
            int times = cursor.getInt(cursor.getColumnIndex("_times"));
            String name = cursor.getString(cursor.getColumnIndex("fileName"));
            ContentValues cvUpload = new ContentValues();
            if(times==3){
                //如果轮询3次，依旧没有上传成功，则将是否上传成功标志位置为0；
                cvUpload.put("upload", 0);
                db.update("tdoaFile", cvUpload, "filename=?", new String[]{name});
            }else{
                //在这里更新数据库，将没有确认上传成功的文件对应上传次数加1
                times=times+1;
                cvUpload.put("_times", times);
                db.update("tdoaFile", cvUpload, "filename=?", new String[]{name});
            }
        }
        if(cursor!=null)
            cursor.close();
        /******************************************************************************/
        Cursor c = db.rawQuery("SELECT filename from tdoaFile  where  upload=0", null);
        while (c.moveToNext()) {
            //上传文件
            String name = c.getString(c.getColumnIndex("fileName"));
            File file = new File(TDOA_PATH, name);
            if(!file.exists()){
                //如果文件不存在，从数据库删除记录
                db.delete("tdoaFile","filename=?", new String[]{name});
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
                    ToServerTDOAFile ToWave = new ToServerTDOAFile();
                    ToWave.setContent(content);
                    ToWave.setContentLength(content.length);
                    ToWave.setFileName(name);
                    ToWave.setFileNameLength((short) name.getBytes(Charset.forName("UTF-8")).length);
                    try {
                        Constants.FILEsession.write(ToWave);
                        ContentValues cv = new ContentValues();
                        cv.put("upload", 1);
                        db.update("tdoaFile", cv, "filename=?", new String[]{name});
                        Log.d("file", "上传TDOA" + name);
                    } catch (Exception e) {

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (c != null) {
            c.close();
        }
    }
}





