/**
 * @Company: Batways
 * @Project:Tnt
 * @Title: ToServerMinaService.java
 * @Package com.batways.tnt.service
 * @Description: TODO
 * @author victor_freedom (x_freedom_reddevil@126.com)
 * @date 2014年9月6日 下午2:16:08
 * @version V1.0
 */

package com.example.administrator.testsliding.Mina;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.testsliding.Bean.RequestNetworkAgain;
import com.example.administrator.testsliding.Bean.ToServerIQwaveFile;
import com.example.administrator.testsliding.Database.DatabaseHelper;
import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.HeartBeat.HEARTBEATREQUEST;
import com.example.administrator.testsliding.HeartBeat.HEARTBEATRESPONSE;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_Connect;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_FixCentralFreq;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_FixSetting;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_InGain;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_IsTerminalOnline;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_OutGain;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_Press;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_PressSetting;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_SweepRange;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_Threshold;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_UploadDataEnd;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_UploadDataStart;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_Connect;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_FixCentralFreq;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_FixSetting;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_InGain;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_OutGain;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_Press;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_PressSetting;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_StationState;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_SweepRange;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_Threshold;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_UploadDataEnd;
import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_UploadDataStart;
import com.example.administrator.testsliding.bean2server.File_MapInterpolation;
import com.example.administrator.testsliding.bean2server.File_ModifyAntenna;
import com.example.administrator.testsliding.bean2server.File_ModifyIngain;
import com.example.administrator.testsliding.bean2server.File_ServiceRadio;
import com.example.administrator.testsliding.bean2server.File_StationAll;
import com.example.administrator.testsliding.bean2server.File_StationRegister;
import com.example.administrator.testsliding.bean2server.File_TerminalOnline;
import com.example.administrator.testsliding.bean2server.File_TerminalRegister;
import com.example.administrator.testsliding.bean2server.HistoryIQRequest;
import com.example.administrator.testsliding.bean2server.HistorySpectrumRequest;
import com.example.administrator.testsliding.bean2server.InteractionFixmodeRequest;
import com.example.administrator.testsliding.bean2server.InteractionPressmodeRequest;
import com.example.administrator.testsliding.bean2server.InteractionSweepModeRequest;
import com.example.administrator.testsliding.bean2server.ListMap;
import com.example.administrator.testsliding.bean2server.List_StationAll;
import com.example.administrator.testsliding.bean2server.List_TerminalOnline;
import com.example.administrator.testsliding.bean2server.LocationAbnormalReply;
import com.example.administrator.testsliding.bean2server.LocationAbnormalRequest;
import com.example.administrator.testsliding.bean2server.MapInterpolation;
import com.example.administrator.testsliding.bean2server.MapInterpolationReply;
import com.example.administrator.testsliding.bean2server.MapRadio;
import com.example.administrator.testsliding.bean2server.MapRadioResult;
import com.example.administrator.testsliding.bean2server.MapRoute;
import com.example.administrator.testsliding.bean2server.MapRouteResult;
import com.example.administrator.testsliding.bean2server.ModifyAntenna;
import com.example.administrator.testsliding.bean2server.ModifyInGain;
import com.example.administrator.testsliding.bean2server.ModifyIngainView;
import com.example.administrator.testsliding.bean2server.RequstNetwork;
import com.example.administrator.testsliding.bean2server.RequstNetworkReply;
import com.example.administrator.testsliding.bean2server.Send_ServiceRadio;
import com.example.administrator.testsliding.bean2server.StationCurrentReply;
import com.example.administrator.testsliding.bean2server.Station_CurrentRequst;
import com.example.administrator.testsliding.bean2server.Station_RegisterRequst;
import com.example.administrator.testsliding.bean2server.TerminalAttributes_All;
import com.example.administrator.testsliding.bean2server.Terminal_Online;
import com.example.administrator.testsliding.bean2server.Terminal_Register;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.compute.ComputeParaInService;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;


/**
 * @ClassName: ToServerMinaService
 * @Description: TODO
 */
public class ToServerMinaService extends Service {
    public static final String IQFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/IQwaveFile/";
    public static final String INTERPOLATION_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/interpolationFile/";
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private DataOutputStream dos;
    /**
     * 30秒后超时
     */
    private static final int IDELTIMEOUT = 30;
    /**
     * 15秒发送一次心跳包
     */
    private static final int HEARTBEATRATE = 15;
    private static HEARTBEATREQUEST heartbeatrequest = null;
    private static HEARTBEATRESPONSE heartbeatresponse = null;
    private static int i=0;
    private String IP = "27.17.8.142";
    private int PORT = 9988;
    private DataHandler dataHandler;
    private static IoSession session=null;
    private String TAG = "ToServerMinaService";
    private ComputeParaInService compute = new ComputeParaInService();
    private ComputePara computePara = new ComputePara();
    private static NioSocketConnector connector = new NioSocketConnector();

    private BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            /************************************************************************************/
            String action = intent.getAction();

            switch (action) {
//                case ConnectivityManager.CONNECTIVITY_ACTION:
//                    /**监听网络变化的广播**/
//                    ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                    NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//                    NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//                    if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//                        Toast.makeText(getBaseContext(), "网络不可以用", Toast.LENGTH_SHORT).show();
//                        // BSToast.showLong(context, "网络不可以用");
//                        //改变背景或者 处理网络的全局变量
//                    }else {
//                        //改变背景或者 处理网络的全局变量
//                    }
//                    break;
                case ConstantValues.REQUSTNETWORK:
                    RequstNetwork net = intent.getParcelableExtra("network");
                    if (net == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(net);
                        Constants.FILEsession.write(net);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.MAPRADIO:
                    MapRadio radio = intent.getParcelableExtra("map_radio");
                    if (radio == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(radio);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.MAPINTERPOLATION:
                    MapInterpolation interpolation=intent.getParcelableExtra("map_inter");
                    if (interpolation == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(interpolation);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.MAPROUTE:
                    MapRoute route = intent.getParcelableExtra("map_route");
                    if (route == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(route);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.ABNORMAL_LOCATION:
                    LocationAbnormalRequest locate = intent.getParcelableExtra("abnormal_location");
                    if (locate == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(locate);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case ConstantValues.STATION_REGISTER:
                    Station_RegisterRequst reg = intent.getParcelableExtra("station_register");
                    if (reg == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(reg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.STATION_CURRENT:
                    Station_CurrentRequst cur = intent.getParcelableExtra("station_current");
                    if (cur == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(cur);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ConstantValues.WIRLESSPLAN:
                    Send_ServiceRadio data = intent.getParcelableExtra("wirlessplan");
                    if (data == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.TERMINAL_ALL:
                    TerminalAttributes_All ter = intent.getParcelableExtra("station_all");
                    if (ter == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(ter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.TERMINAL_ONLINE:
                    Terminal_Online online = intent.getParcelableExtra("terminal_online");
                    if (online == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(online);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.TERMINAL_REGISTER:
                    Terminal_Register register = intent.getParcelableExtra("terminal_register");
                    if (register == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(register);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.SERVICE_SPECTRUM:
                    HistorySpectrumRequest spec = intent.getParcelableExtra("service_spectrum");
                    if (spec == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(spec);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.SERVICE_IQ:
                    HistoryIQRequest IQ = intent.getParcelableExtra("service_IQ");
                    if (IQ == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(IQ);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.INTERACTION_WORKMODEL01:
                    InteractionSweepModeRequest sweep = intent.getParcelableExtra("interaction_workmodel01");
                    if (sweep == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(sweep);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.INTERACTION_WORKMODEL02:
                    InteractionFixmodeRequest fix = intent.getParcelableExtra("interaction_workmodel02");
                    if (fix == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(fix);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.INTERACTION_WORKMODEL03:
                    InteractionPressmodeRequest press = intent.getParcelableExtra("interaction_workmodel03");
                    if (press == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(press);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.MODIFYINGAIN:
                    ModifyInGain modify = intent.getParcelableExtra("modifyIngain");
                    if (modify == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(modify);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ConstantValues.MODIFYANTENNA:
                    ModifyAntenna antenna = intent.getParcelableExtra("modifyAntenna");
                    if (antenna == null) {
                        return;
                    }
                    try {
                        Constants.SERVERsession.write(antenna);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

        }
    };
    /******************************锁屏后CPU唤醒状态**********************************************/
    PowerManager.WakeLock wakeLock = null;
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK| PowerManager.ON_AFTER_RELEASE,  getClass().getCanonicalName());
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
        }
    }
    //释放设备电源锁
    private void releaseWakeLock()
    {
        if (null != wakeLock)
        {
            wakeLock.release();
            wakeLock = null;
        }
    }
/*******************************************************************************************/
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dbHelper=DatabaseHelper.getInstance(this);//单例模式
        db = dbHelper.getWritableDatabase();
        heartbeatrequest = new HEARTBEATREQUEST();
        heartbeatresponse = new HEARTBEATRESPONSE();
        byte[] request = {(byte) 0x55, (byte) 0x66};
        byte[] response = {(byte) 0x77, (byte) 0x88};
        heartbeatrequest.setContent(request);
        heartbeatresponse.setContent(response);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.REQUSTNETWORK);
        filter.addAction(ConstantValues.MAPRADIO);
        filter.addAction(ConstantValues.MAPROUTE);
        filter.addAction(ConstantValues.MAPINTERPOLATION);

        filter.addAction(ConstantValues.ABNORMAL_LOCATION);
        filter.addAction(ConstantValues.STATION_REGISTER);
        filter.addAction(ConstantValues.STATION_CURRENT);
        filter.addAction(ConstantValues.WIRLESSPLAN);
        filter.addAction(ConstantValues.SERVICE_IQ);
        filter.addAction(ConstantValues.SERVICE_SPECTRUM);

        filter.addAction(ConstantValues.INTERACTION_WORKMODEL01);
        filter.addAction(ConstantValues.INTERACTION_WORKMODEL02);
        filter.addAction(ConstantValues.INTERACTION_WORKMODEL03);

        filter.addAction(ConstantValues.TERMINAL_ALL);
        filter.addAction(ConstantValues.TERMINAL_ONLINE);
        filter.addAction(ConstantValues.TERMINAL_REGISTER);
        filter.addAction(ConstantValues.MODIFYINGAIN);
        filter.addAction(ConstantValues.MODIFYANTENNA);

        //网络变化
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        filter.addCategory(Intent.CATEGORY_DEFAULT);

        dataHandler = new DataHandler();
        registerReceiver(sendMessage, filter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                serviceConnect(dataHandler);
            }
        }).start();
        acquireWakeLock();//电源
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(sendMessage);
        sendMessage = null;
        releaseWakeLock();//释放锁
        super.onDestroy();
    }

    private class DataHandler extends IoHandlerAdapter {

        @Override
        public void sessionCreated(IoSession session) throws Exception {
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {

        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            Log.d("session","连接断开");
            i=0;
            while(true) {
                try {
                    Thread.sleep(3000);
                    // 这里是异步操作 连接后立即返回
                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            "27.17.8.142", 9000));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    if(session.isConnected()) {
                        Constants.SERVERsession = session;
                        break;
                    }else{
                        Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
            if(Constants.requestNetcontent!=null) {
                RequestNetworkAgain request = new RequestNetworkAgain();
                request.setContent(Constants.requestNetcontent);
                Constants.SERVERsession.write(request);
            }
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status)
                throws Exception {
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause)
                throws Exception {
            cause.printStackTrace();
        }

        @Override
        public void messageReceived(IoSession session, Object message)
                throws Exception {
            if (message instanceof RequstNetworkReply) {
                RequstNetworkReply networkReply = (RequstNetworkReply) message;
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.RREQUSTNETWORK, "requstNet", networkReply);
            }
            //处理从服务端接收到的消息
            if (message instanceof MapRadioResult) {
                MapRadioResult map = (MapRadioResult) message;
                Thread.sleep(1500);
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.RMAPRADIO, "map_radio", map);

            }
            if (message instanceof MapRouteResult) {
                MapRouteResult map = (MapRouteResult) message;
                Thread.sleep(1000);
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.RMAPROUTE, "map_route", map);

            }
            if (message instanceof File_MapInterpolation) {
                final File_MapInterpolation map = (File_MapInterpolation) message;
                MapInterpolationReply inter = compute.File2InterpolationResult(map);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        file2InterpolationPNG(map);
                    }
                }).start();
                Thread.sleep(1000);
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.RMAPINTERPOLATION, "map_interpolation", inter);

            }
            if (message instanceof File_ServiceRadio) {
                ArrayList<ListMap> listItems = new ArrayList<>();
                File_ServiceRadio radio = new File_ServiceRadio();
                radio = (File_ServiceRadio) message;
                listItems.clear();
                listItems = compute.Radio2ListItem(radio);
                Thread.sleep(1000);
                Broadcast.sendBroadCastRadioList(getBaseContext(),
                        ConstantValues.RWIRLESSPLAN, "wirlessplan", listItems);

            }

            if (message instanceof File_StationAll) {
                File_StationAll all = new File_StationAll();
                ArrayList<List_StationAll> list_stationAlls = new ArrayList<>();
                all = (File_StationAll) message;
                list_stationAlls.clear();
                list_stationAlls = compute.StationALL2ListItem(all);
                Thread.sleep(1000);
                if (list_stationAlls != null) {
                    Broadcast.sendBroadCastTerminalAllList(getBaseContext(),
                            ConstantValues.RTERMINAL_ALL, "station_allresult", list_stationAlls);
                } else {
                    Toast.makeText(getBaseContext(), "您所查询的范围内没有相关记录！", Toast.LENGTH_SHORT).show();
                }
            }


            if (message instanceof File_TerminalOnline) {
                File_TerminalOnline online = new File_TerminalOnline();
                ArrayList<List_TerminalOnline> list_terminalOnlines = new ArrayList<>();
                online = (File_TerminalOnline) message;
                list_terminalOnlines.clear();
                list_terminalOnlines = compute.TerminalOnline2ListItem(online);
                Thread.sleep(1000);
                if (list_terminalOnlines != null) {
                    Broadcast.sendBroadCastTerminalOnlineList(getBaseContext(),
                            ConstantValues.RTERMINAL_ONLINE, "terminal_onlineresult", list_terminalOnlines);
                } else {
                    Toast.makeText(getBaseContext(), "您所查询的范围内没有相关记录！", Toast.LENGTH_SHORT).show();
                }
            }


            if (message instanceof File_TerminalRegister) {
                File_TerminalRegister register = new File_TerminalRegister();
                //与在线终端的表一样
                ArrayList<List_TerminalOnline> list_terminalRegister = new ArrayList<>();
                register = (File_TerminalRegister) message;
                list_terminalRegister.clear();
                list_terminalRegister = compute.TerminalRegister2ListItem(register);
                Thread.sleep(1000);
                if (list_terminalRegister != null) {
                    Broadcast.sendBroadCastTerminalOnlineList(getBaseContext(),
                            ConstantValues.RTERMINAL_REGISTER, "terminal_registerresult", list_terminalRegister);
                } else {
                    Toast.makeText(getBaseContext(), "您所查询的范围内没有相关记录！", Toast.LENGTH_SHORT).show();
                }

            }
            //==============================================================================

            if (message instanceof File_StationRegister) {
                File_StationRegister register1 = new File_StationRegister();
                //与全部台站属性的列表一样
                ArrayList<List_StationAll> list_StayionRegister = new ArrayList<>();
                register1 = (File_StationRegister) message;
                list_StayionRegister.clear();
                list_StayionRegister = compute.TerminalALL2ListItem(register1);
                Thread.sleep(1000);
                if (list_StayionRegister != null) {
                    Broadcast.sendBroadCastTerminalAllList(getBaseContext(),
                            ConstantValues.RSTATION_REGISTER, "station_register", list_StayionRegister);
                } else {
                    Toast.makeText(getBaseContext(), "您所查询的范围内没有相关记录！", Toast.LENGTH_SHORT).show();
                }

            }

            //=======================================================================

            if (message instanceof StationCurrentReply) {
                StationCurrentReply reply = new StationCurrentReply();
                reply = (StationCurrentReply) message;
                Thread.sleep(1000);
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.RSTATION_CURRENT, "station_current", reply);
            }

            //=======================================================================

            if (message instanceof LocationAbnormalReply) {
                LocationAbnormalReply abnormal = new LocationAbnormalReply();
                abnormal = (LocationAbnormalReply) message;
                Broadcast.sendBroadCast(getBaseContext(),
                        ConstantValues.RABNORMAL_LOCATION, "abnormal_location", abnormal);
            }
            //========================增益表==============================
            if (message instanceof File_ModifyIngain) {
                File_ModifyIngain modify = (File_ModifyIngain) message;
                byte[] bytes = modify.getFileContent();
                if (bytes[0] == 0x55) {
                    ModifyIngainView view = compute.File2InGain(modify);
                    Broadcast.sendBroadCast(getBaseContext(),
                            ConstantValues.RMODIFYINGAIN, "modifyIngian", view);
                }
                Constants.FPGAsession.write(modify);
            }
            if (message instanceof File_ModifyAntenna) {
                File_ModifyAntenna modify = (File_ModifyAntenna) message;
                byte[] bytes = modify.getFileContent();
                if (bytes[0] == 0x55) {
                    //表相同
                    ModifyIngainView view = compute.File2Antenna(modify);
                    Broadcast.sendBroadCast(getBaseContext(),
                            ConstantValues.RMODIFYANTENNA, "modifyAntenna", view);
                }
                Constants.FPGAsession.write(modify);
            }
            //==============================================================================
//=============================================数据转发(设置指令)=============================================

            if (message instanceof Simple_Connect) {
                Simple_Connect connect = new Simple_Connect();
                connect = (Simple_Connect) message;
                Constants.FPGAsession.write(connect);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(connect.getContent()));
            }

            //================================================

            if (message instanceof Simple_FixCentralFreq) {
                Simple_FixCentralFreq fixCentral = new Simple_FixCentralFreq();
                fixCentral = (Simple_FixCentralFreq) message;
                Constants.FPGAsession.write(fixCentral);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(fixCentral.getContent()));
            }
            /////////////////////////////////
            if (message instanceof Simple_FixSetting) {
                Simple_FixSetting fixSetting = new Simple_FixSetting();
                fixSetting = (Simple_FixSetting) message;
                Constants.FPGAsession.write(fixSetting);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(fixSetting.getContent()));
            }

            ///////////////////////////////////////////////////

            if (message instanceof Simple_InGain) {
                Simple_InGain inGain = new Simple_InGain();
                inGain = (Simple_InGain) message;
                Constants.FPGAsession.write(inGain);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(inGain.getContent()));
            }
            /////////////////////////////////////////////////////
            if (message instanceof Simple_OutGain) {
                Simple_OutGain outGain = new Simple_OutGain();
                outGain = (Simple_OutGain) message;
                Constants.FPGAsession.write(outGain);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(outGain.getContent()));
            }
            //////////////////////////////////////
            if (message instanceof Simple_Press) {
                Simple_Press press = new Simple_Press();
                press = (Simple_Press) message;
                Constants.FPGAsession.write(press);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(press.getContent()));
            }
            ///////////////////////////////////////////

            if (message instanceof Simple_PressSetting) {
                Simple_PressSetting pressSetting = new Simple_PressSetting();
                pressSetting = (Simple_PressSetting) message;
                Constants.FPGAsession.write(pressSetting);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(pressSetting.getContent()));
            }
            ////////////////////////////////////////////////

            if (message instanceof Simple_StationState) {
                Simple_StationState simple_stationState = new Simple_StationState();
                simple_stationState = (Simple_StationState) message;
                Constants.FPGAsession.write(simple_stationState);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(simple_stationState.getContent()));
            }
            ///////////////////////////////////////////////////

            if (message instanceof Simple_SweepRange) {
                Simple_SweepRange sweep = new Simple_SweepRange();
                sweep = (Simple_SweepRange) message;
                Constants.FPGAsession.write(sweep);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(sweep.getContent()));
            }
            ////////////////////////////////////////////////

            if (message instanceof Simple_Threshold) {
                Simple_Threshold threshold = new Simple_Threshold();
                threshold = (Simple_Threshold) message;
                Constants.FPGAsession.write(threshold);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(threshold.getContent()));
            }

            ////////////////////////////////////////////////

            if (message instanceof Simple_UploadDataStart) {
                Simple_UploadDataStart data = new Simple_UploadDataStart();
                data = (Simple_UploadDataStart) message;
                Constants.FPGAsession.write(data);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(data.getContent()));
            }
            /////////////////////////////////////////////////////

            if (message instanceof Simple_UploadDataEnd) {
                Simple_UploadDataEnd dataend = new Simple_UploadDataEnd();
                dataend = (Simple_UploadDataEnd) message;
                Constants.FPGAsession.write(dataend);
                Log.d("trans", "SeverSession 转发设置" + Arrays.toString(dataend.getContent()));
            }

            //======================================数据转发（查询）===================

            if (message instanceof Query_Connect) {
                Query_Connect query_connect = new Query_Connect();
                query_connect = (Query_Connect) message;
                Constants.FPGAsession.write(query_connect);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_connect.getContent()));
            }

            ////////////////////////////

            if (message instanceof Query_FixCentralFreq) {
                Query_FixCentralFreq query_fixCentralFreq = new Query_FixCentralFreq();
                query_fixCentralFreq = (Query_FixCentralFreq) message;
                Constants.FPGAsession.write(query_fixCentralFreq);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_fixCentralFreq.getContent()));
            }
            //////////////////////////

            if (message instanceof Query_FixSetting) {
                Query_FixSetting query_fixSetting = new Query_FixSetting();
                query_fixSetting = (Query_FixSetting) message;
                Constants.FPGAsession.write(query_fixSetting);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_fixSetting.getContent()));
            }

            ///////////////////

            if (message instanceof Query_InGain) {
                Query_InGain query_inGain = new Query_InGain();
                query_inGain = (Query_InGain) message;
                Constants.FPGAsession.write(query_inGain);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_inGain.getContent()));
            }

            ///////////////////////////////////

            if (message instanceof Query_OutGain) {
                Query_OutGain query_outGain = new Query_OutGain();
                query_outGain = (Query_OutGain) message;
                Constants.FPGAsession.write(query_outGain);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_outGain.getContent()));
            }

            ///////////////////////////////////////

            if (message instanceof Query_IsTerminalOnline) {
                Query_IsTerminalOnline query_isTerminalOnline = new Query_IsTerminalOnline();
                query_isTerminalOnline = (Query_IsTerminalOnline) message;
                Constants.FPGAsession.write(query_isTerminalOnline);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_isTerminalOnline.getContent()));
            }
            //////////////////////////////////////////

            if (message instanceof Query_Press) {
                Query_Press query_press = new Query_Press();
                query_press = (Query_Press) message;
                Constants.FPGAsession.write(query_press);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_press.getContent()));
            }
            /////////////////////////////////////////////////////

            if (message instanceof Query_PressSetting) {
                Query_PressSetting query_pressSetting = new Query_PressSetting();
                query_pressSetting = (Query_PressSetting) message;
                Constants.FPGAsession.write(query_pressSetting);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_pressSetting.getContent()));
            }

            //////////////////////////////////////////

            if (message instanceof Query_SweepRange) {
                Query_SweepRange query_sweepRange = new Query_SweepRange();
                query_sweepRange = (Query_SweepRange) message;
                Constants.FPGAsession.write(query_sweepRange);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_sweepRange.getContent()));
            }

            /////////////////////////////////////////

            if (message instanceof Query_Threshold) {
                Query_Threshold query_threshold = new Query_Threshold();
                query_threshold = (Query_Threshold) message;
                Constants.FPGAsession.write(query_threshold);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_threshold.getContent()));
            }

            ////////////////////////////////////////////

            if (message instanceof Query_UploadDataStart) {
                Query_UploadDataStart uploadData = new Query_UploadDataStart();
                uploadData = (Query_UploadDataStart) message;
                Constants.FPGAsession.write(uploadData);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(uploadData.getContent()));
            }
            ////////////////////////////////////////////

            if (message instanceof Query_UploadDataEnd) {
                Query_UploadDataEnd query_uploadDataEnd = new Query_UploadDataEnd();
                query_uploadDataEnd = (Query_UploadDataEnd) message;
                Constants.FPGAsession.write(query_uploadDataEnd);
                Log.d("trans", "SeverSession 转发查询" + Arrays.toString(query_uploadDataEnd.getContent()));
            }

            //======================================================================================================
        }

        @Override
        public void messageSent(IoSession session, Object message)
                throws Exception {

        }
    }

    private static class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

        /*
         * (non-Javadoc)
         *
         * @see
         * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getRequest
         * (org.apache.mina.core.session.IoSession)
         */
        @Override
        public Object getRequest(IoSession session) {
//            i++;
//            Log.d("session",i+"");
//			if (i > 5) {
//				try {
//                    Log.d("session","睡眠");
//					Thread.sleep(40000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
            return heartbeatrequest;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#getResponse
         * (org.apache.mina.core.session.IoSession, java.lang.Object)
         */
        @Override
        public Object getResponse(IoSession session, Object request) {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isRequest
         * (org.apache.mina.core.session.IoSession, java.lang.Object)
         */
        @Override
        public boolean isRequest(IoSession session, Object message) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.apache.mina.filter.keepalive.KeepAliveMessageFactory#isResponse
         * (org.apache.mina.core.session.IoSession, java.lang.Object)
         */
        @Override
        public boolean isResponse(IoSession session, Object message) {
            if (message instanceof HEARTBEATRESPONSE) {
                Log.d("session","是响应心跳包");
                return true;
            }
            return false;
        }

    }

    /***
     * @ClassName: KeepAliveRequestTimeoutHandlerImpl
     * @Description: 当心跳超时时的处理，也可以用默认处理 这里like
     */
    private static class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {

        /*
         * (non-Javadoc)
         *
         * @seeorg.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler#
         * keepAliveRequestTimedOut
         * (org.apache.mina.filter.keepalive.KeepAliveFilter,
         * org.apache.mina.core.session.IoSession)
         */
        @Override
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            ((Logger) LOG).info("心跳超时！");
        }

    }

    private void serviceConnect(DataHandler dataHandler) {
        while(true) {

            try {
                Thread.sleep(3000);
                connector.setConnectTimeoutMillis(30000); //设置连接超时
                connector.getFilterChain().addLast(
                        "codec",
                        new ProtocolCodecFilter(new ToServerProtocolCodecFactory()));
                connector.getSessionConfig().setReadBufferSize(1024);
                connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDELTIMEOUT);
                /** 主角登场 */
                KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
                KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory);
                /** 是否回发 */
                heartBeat.setForwardEvent(true);
                /** 发送频率 */
                heartBeat.setRequestInterval(HEARTBEATRATE);
                //connector.getSessionConfig().setKeepAlive(true);
                connector.getFilterChain().addLast("heartbeat", heartBeat);
                /****************************/
                connector.setHandler(dataHandler);
                // 这里是异步操作 连接后立即返回
                ConnectFuture future = connector.connect(new InetSocketAddress(
                        "27.17.8.142", 9000));
// ConnectFuture future = connector.connect(new InetSocketAddress(
//                            "115.156.208.51",9123));
//                    ConnectFuture future = connector.connect(new InetSocketAddress(
//                            Constants.IPValue, Constants.PORTValue));
                future.awaitUninterruptibly();// 等待连接创建完成

                session = future.getSession();
                if(session.isConnected()) {
                    Constants.SERVERsession = session;
                    break;
                }
//             session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
//             connector.dispose();
            } catch (Exception e) {
            }
        }

    }


    private void file2InterpolationPNG(File_MapInterpolation interfile){
        byte[] b=interfile.getFileContent();
        File PSdir = new File(INTERPOLATION_PATH);
        if (!PSdir.exists()) {
            PSdir.mkdir();
        }
        int matchCount=0;//每次解析完一张图片的长度
        while(matchCount<b.length) {
            /*****************解析第一张图片**************************************/
            int year = ((b[0+matchCount] & 0xff) << 4) + ((b[1+matchCount] >> 4) & 0x0f);
            int month = b[1+matchCount] & 0x0f;
            int day = (b[2+matchCount] >> 3) & 0x1f;
            int hour = ((b[2+matchCount] & 0x07) << 2) + (b[3+matchCount] & 0x03);
            int min = ((b[3+matchCount] >> 2) & 0x3f);
            //文件长度
            int fileLength = ((b[4+matchCount] & 0xff) << 16) + ((b[5+matchCount] & 0xff) << 8) + (b[6+matchCount] & 0xff);

            byte[] content = new byte[fileLength];
            System.arraycopy(b, 7+matchCount, content, 0, fileLength);

            String name = null;
            //创建文件
            name = String.format("%d-%d-%d-%d-%d.%s", year, month, day, hour, min, "png");
            if (name != null) {
                File file = new File(PSdir, name);
                //获取文件写入流
                try {
                    dos = new DataOutputStream(new FileOutputStream(file));
                    dos.write(content);
                    dos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            /********************************************************************/

            matchCount+=fileLength+7;
        }

    }
}


