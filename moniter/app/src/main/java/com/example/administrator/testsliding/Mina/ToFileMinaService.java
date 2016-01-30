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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.GlobalConstants.Constants;
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
import com.example.administrator.testsliding.bean2server.MapRadio;
import com.example.administrator.testsliding.bean2server.MapRadioResult;
import com.example.administrator.testsliding.bean2server.MapRoute;
import com.example.administrator.testsliding.bean2server.MapRouteResult;
import com.example.administrator.testsliding.bean2server.ModifyAntenna;
import com.example.administrator.testsliding.bean2server.ModifyInGain;
import com.example.administrator.testsliding.bean2server.ModifyIngainView;
import com.example.administrator.testsliding.bean2server.RequstNetwork;
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
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * @ClassName: ToServerMinaService
 * @Description: TODO
 */
public class ToFileMinaService extends Service {

    private String IP = "27.17.8.142";
    private int PORT = 9988;
    private IoSession session;
    private String TAG = "ToFileMinaService";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    IoConnector connector = new NioSocketConnector();
                    connector.getFilterChain().addLast(
                            "codec",
                            new ProtocolCodecFilter(new ToFileProtocolCodeFactory()));

                    connector.getSessionConfig().setReadBufferSize(1024);
                    connector.setHandler(new DataHandler());
                    // 这里是异步操作 连接后立即返回
			ConnectFuture future = connector.connect(new InetSocketAddress(
						"27.17.8.142",9988));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    Constants.FILEsession=session;
                    session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
                    connector.dispose();
                } catch (Exception e) {

                }
            }
        }).start();

        super.onCreate();
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

                //======================================================================================================
        }

        @Override
        public void messageSent(IoSession session, Object message)
                throws Exception {

        }
    }


}
