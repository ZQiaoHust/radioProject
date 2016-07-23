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
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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

import java.io.File;
import java.net.InetSocketAddress;


/**
 * @ClassName: ToServerMinaService
 * @Description: TODO
 */
public class ToFileMinaService extends Service {

    private String IP = "27.17.8.142";
    private int PORT = 9988;
    private static IoSession session;
    private String TAG = "ToFileMinaService";
    private static IoConnector connector = new NioSocketConnector();
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    public static final String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/PowerSpectrumFile/";
    public static final String P0AFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/POA/";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        connector.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new ToFileProtocolCodeFactory()));

        connector.getSessionConfig().setReadBufferSize(1024);
        connector.setHandler(new DataHandler());

        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    // 这里是异步操作 连接后立即返回
                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            Constants.fileIP, Constants.filePort));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    if (session.isConnected()) {
                        Constants.FILEsession = session;
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), "连接成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();//
                    }
//                    session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
//                    connector.dispose();
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(getBaseContext(),"连接失败！",Toast.LENGTH_SHORT).show();
                    Looper.loop();//

                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
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
            while (true) {
                try {
                    Thread.sleep(3000);
                    // 这里是异步操作 连接后立即返回
                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            Constants.fileIP, Constants.filePort));
                    future.awaitUninterruptibly();// 等待连接创建完成
                    session = future.getSession();
                    if (session.isConnected()) {
                        Constants.FILEsession = session;
                        break;
                    }
                } catch (Exception e) {
                }
            }
            if (Constants.requestNetcontent != null) {
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
            if (message instanceof FileToServerReply) {
                FileToServerReply reply = (FileToServerReply) message;
                String name = reply.getFileName();
                ContentValues cvUpload = new ContentValues();
                cvUpload.put("upload", 2);
                    /* 取得扩展名 */
                String end = name
                        .substring(name.lastIndexOf(".") + 1, name.length())
                        .toLowerCase();
                if (end.equals("pwr")) {
                    db.update("localFile", cvUpload, "filename=?", new String[]{name});
                    Log.d("file", "上传成功" + name);
                    //删除已上传的文件
                    File file = new File(PSFILE_PATH, name);
                    if(file.exists())
                        deleteFile(file);
                    Log.d("file", "删除文件" + name);
                } else if (end.equals("iq")) {
                    db.update("iqFile", cvUpload, "filename=?", new String[]{name});
                    Log.d("file", "上传成功" + name);
                }else if (end.equals("poa")) {
                    db.update("poaFile", cvUpload, "filename=?", new String[]{name});
                    Log.d("file", "上传成功" + name);

                    //删除已上传的文件
                    File file = new File(P0AFILE_PATH, name);
                    if(file.exists())
                        deleteFile(file);
                    Log.d("file", "删除文件" + name);
                }
            }
        }

        @Override
        public void messageSent(IoSession session, Object message)
                throws Exception {

        }
    }

    /**
     * 删除文件
     * @param file
     */
    private void deleteFile(File file) {
        if (file.isFile()) {
            deleteFileSafely(file);
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                deleteFileSafely(file);
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            deleteFileSafely(file);
        }
    }


    /**
     * 安全删除文件.
     * @param file
     * @return
     */
    public static boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }
}
