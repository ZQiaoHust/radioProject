package com.hust.radiofeeler.tab3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.hust.radiofeeler.Bean.ToServerIQwaveFile;
import com.hust.radiofeeler.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.hust.radiofeeler.Database.DatabaseHelper;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.MyApplication;
import com.hust.radiofeeler.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/7/21.
 */
public class Share_fragment extends Fragment {

    private MyApplication myApplication;
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;
    private Button mUpload;
    private Button mDownload;
    private Button mCreateIQ;
    private int uploadFileCountPercent = 0;//它的取值区间是1到10
    private   Cursor c=null;

    private SeekBar mSeekbar;

    private List mIQ;
    private FileOutputStream fos;

    FileInputStream fis;
    DataInputStream dis;
    private Timer timer = new Timer();
    private TimerTask task;

    public static final String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/PowerSpectrumFile/";

    public static final String IQFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/IQwaveFile/";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                int a = (int) msg.obj;
                mSeekbar.setProgress(a);

        }
    };
    private Handler handlerIQ = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //定时上传IQ文件，每5秒扫描一次
                uploadIQFile();
            }
        }
    };

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        InitEvent();
    }

    private void InitSetting() {
        myApplication = (MyApplication) getActivity().getApplication();
        dbHelper = DatabaseHelper.getInstance(getActivity());
        db = dbHelper.getReadableDatabase();
        mUpload = (Button) getActivity().findViewById(R.id.upload);
        mDownload = (Button) getActivity().findViewById(R.id.download);
        mCreateIQ = (Button) getActivity().findViewById(R.id.iq_localsave);
        mSeekbar = (SeekBar) getActivity().findViewById(R.id.progress_seekbar);
    }

    public void InitEvent() {
        /**
         * 上传文件按钮
         */
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setVisibility(View.VISIBLE);
                mSeekbar.setProgress(0);
                mSeekbar.setMax(10);
                TimerTask task = new TimerTask(){
                    public void run(){
                        //实现自己的延时执行任务
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFileCountPercent = 0;//它的取值区间是1到10
                                Looper.prepare();

                                if (myApplication.getFileUploadMode() == 1) {
                                     c = db.rawQuery("SELECT filename from localFile  where  upload=0", null);
                                    int pace=0;
                                    uploadPowerSpectrumFile(c,pace);

                                } else if (myApplication.getFileUploadMode() == 2) {
                                    //自动门限上传
                                    c = db.rawQuery("SELECT filename from localFile  where isChanged=1 AND upload=0", null);
                                   int pace=0;
                                    uploadPowerSpectrumFile(c,pace);
                                } else if (myApplication.getFileUploadMode() == 3) {
                                    //抽取上传
                                    int pace = myApplication.getUpRate();
                                    Cursor c = db.rawQuery("SELECT filename from localFile  where  upload=0", null);
                                    int i = 0;
                                    int fileNum = c.getCount();
                                    while (c.moveToNext()) {
                                        if (i % pace == 0) {

                                            String name = c.getString(c.getColumnIndex("fileName"));
                                            //上传文件
                                            File file = new File(PSFILE_PATH, name);
                                            try {
                                                fis = new FileInputStream(file);
//                                        dis = new DataInputStream(fis);
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
                                                //在这里更新数据库，将文件是否上传的标志位置为1
//                                                ContentValues cvUpload = new ContentValues();
//                                                cvUpload.put("upload", 1);
//                                                db.update("localFile", cvUpload, "filename=?", new String[]{name});
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getActivity(), "请连接服务器", Toast.LENGTH_SHORT).show();
                                                Looper.loop();// 进入loop中的循环，查看消息队列
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getActivity(), "请连接服务器", Toast.LENGTH_SHORT).show();
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
                                        i++;
                                        //上传文件达到十分之一时候更新进度条
                                        if (i % (fileNum / 10) == 0) {
                                            uploadFileCountPercent++;
                                            handler.obtainMessage(1, uploadFileCountPercent).sendToTarget();
                                        }
                                    }
                                    if (c != null) {

                                        c.close();
                                    }

                                }
                                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }).start();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task,1000,60000);


            }

        });

        /**
         * 从服务器下载历史文件
         */
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int a = 1;
                        int b = 1;
                        int c = a + b;


                    }
                }).start();
            }
        });


        mCreateIQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handlerIQ.sendMessage(message);
                    }
                };
                timer.schedule(task, 1000, 5000);
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment, container, false);
    }

    //获取当前目录下所有的功率谱文件
    public ArrayList<String> GetFileName(String fileAbsolutePath) {
        ArrayList<String> Filename = new ArrayList<>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String name = subFile[iFileLength].getName();
                // 判断是否为pwr结尾
                if (name.trim().toLowerCase().endsWith(".pwr")) {
                    Filename.add(name);
                }
            }
        }
        return Filename;
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
//    private void uploadIQFile() {
//        File file = new File(IQFILE_PATH);
//        if (!file.exists()) {
//            return;
//        }
//        String[] tempList = file.list();
//        for (int i = 0; i < tempList.length; i++) {
//            String name=tempList[i];
//            File f = new File(IQFILE_PATH, name);
//        /* 取得扩展名 */
//            String end = name
//                    .substring(name.lastIndexOf(".") + 1, name.length())
//                    .toLowerCase();
//            String befoe=name.substring(0,name.lastIndexOf(".") );
//            if(end.equals("uniq")){
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(f);
//                    byte[] content = new byte[fis.available()];
//                    byte[] buffer = new byte[content.length];
//                    while ((fis.read(buffer)) != -1) {
//                        content = buffer;
//                    }
//                    //将文件里的内容转化为对象
//                    ToServerIQwaveFile ToWave = new ToServerIQwaveFile();
//                    ToWave.setContent(content);
//                    ToWave.setContentLength(content.length);
//                     String upname= befoe  + ".iq";
//                    ToWave.setFileName(upname);
//                    ToWave.setFileNameLength((short) upname.getBytes(Charset.forName("UTF-8")).length);
//                    try {
//                        Constants.FILEsession.write(ToWave);
//                        if(tempList.length>10) {
//                            f.delete();//上传后删除
//                        }else{
//                            File f2=new File(IQFILE_PATH, upname);
//                            f.renameTo(f2);
//                        }
//                    }catch(Exception e){
//
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private void uploadPowerSpectrumFile(Cursor c,int pace){
        if(Constants.isUpload!=0)//上传关闭
            return ;
        int i = 0, fileNum = 0;
       if(c==null)
           return ;
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
        fileNum = c.getCount();
        while (c.moveToNext()) {
            i++;
            //上传文件达到十分之一时候更新进度条
            if (fileNum >= 10) {
                if (i % (fileNum / 10) == 0) {
                    uploadFileCountPercent++;
                    handler.obtainMessage(1, uploadFileCountPercent).sendToTarget();
                }
            } else {
                handler.obtainMessage(1, 10).sendToTarget();
            }
            //上传文件
            String name = c.getString(c.getColumnIndex("fileName"));
            File file = new File(PSFILE_PATH, name);
            if(!file.exists()){
                //如果文件不存在，从数据库删除记录
                db.delete("localFile","filename=?", new String[]{name});
            }else {

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
                    Toast.makeText(getActivity(), "请连接服务器", Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "请连接服务器", Toast.LENGTH_SHORT).show();
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
}




