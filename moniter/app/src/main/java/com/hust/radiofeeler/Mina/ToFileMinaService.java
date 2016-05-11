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

package com.hust.radiofeeler.Mina;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.hust.radiofeeler.Bean.RequestNetworkAgain;
import com.hust.radiofeeler.Database.DatabaseHelper;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.bean2server.FileToServerReply;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;


/**
 * @ClassName: ToServerMinaService
 * @Description: TODO
 */
public class ToFileMinaService extends Service {

    private String IP = "27.17.8.142";
    private int PORT = 9988;
    private  static IoSession session;
    private String TAG = "ToFileMinaService";
    private  static  IoConnector connector = new NioSocketConnector();
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

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
//                    session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
//                    connector.dispose();
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
            while(true) {
                try {
                    Thread.sleep(3000);
                    // 这里是异步操作 连接后立即返回
                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            "27.17.8.142", 9988));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    if(session.isConnected()) {
                        Constants.FILEsession = session;
                        break;
                    }
                } catch (Exception e) {
                }
            }
            if(Constants.requestNetcontent!=null) {
                RequestNetworkAgain request = new RequestNetworkAgain();
                request.setContent(Constants.requestNetcontent);
                Constants.FILEsession.write(request);
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

                //======================================================================================================
               if(message instanceof FileToServerReply){
                   FileToServerReply reply= (FileToServerReply) message;
                   String name=reply.getFileName();
                   ContentValues cvUpload = new ContentValues();
                   cvUpload.put("upload", 2);
                    /* 取得扩展名 */
                   String end = name
                           .substring(name.lastIndexOf(".") + 1, name.length())
                           .toLowerCase();
                   if(end.equals("pwr")) {
                       db.update("localFile", cvUpload, "filename=?", new String[]{name});
                       Log.d("file","上传成功"+name);
                   } else  if(end.equals("iq")) {
                       db.update("iqFile", cvUpload, "filename=?", new String[]{name});
                       Log.d("file","上传成功"+name);
                   }
               }
        }

        @Override
        public void messageSent(IoSession session, Object message)
                throws Exception {

        }
    }


}
