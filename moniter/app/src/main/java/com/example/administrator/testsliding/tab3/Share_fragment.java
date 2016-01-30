package com.example.administrator.testsliding.tab3;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.testsliding.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.example.administrator.testsliding.Database.DatabaseHelper;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.GlobalConstants.MyApplication;
import com.example.administrator.testsliding.R;

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

    private SeekBar mSeekbar;

    private List mIQ;
    private FileOutputStream fos;

    FileInputStream fis;
    DataInputStream dis;


    public static final String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/PowerSpectrumFile/";

    public static final String IQFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/IQwaveFile/";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                int a = (int) msg.obj;
                mSeekbar.setProgress(a);

        }
    };

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSetting();
        InitEvent();
    }

    private void InitSetting() {
        myApplication = (MyApplication) getActivity().getApplication();
        dbHelper = new DatabaseHelper(getActivity());
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
                                int uploadFileCountPercent = 0;//它的取值区间是1到10
                                Looper.prepare();


//                        ArrayList fileName = GetFileName(PSFILE_PATH);
                                if (myApplication.getFileUploadMode() == 1) {
                                    int i = 0, fileNum = 0;
                                    Cursor c = db.rawQuery("SELECT filename from localFile  where  upload=0", null);
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
                                        try {
                                            fis = new FileInputStream(file);
//                                    dis = new DataInputStream(new FileInputStream(file));
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
                                    if (c != null) {

                                        c.close();
                                    }

                                } else if (myApplication.getFileUploadMode() == 2) {
                                    //自动门限上传
                                    Cursor c = db.rawQuery("SELECT filename from localFile  where isChanged=1 AND upload=0", null);
//                            Cursor c = db.query("loaclFile",null,null,null,null,null,null);//查询并获得游标
                                    while (c.moveToNext()) {//判断游标是否为空
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
                                    if (c != null) {

                                        c.close();
                                    }

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //创建IQ波形文件路径
                        File PSdir = new File(IQFILE_PATH);
                        if (!PSdir.exists()) {
                            PSdir.mkdir();
                        }

                        if (!Constants.Queue_IQwave.isEmpty()) {

                            for (int i = 0; i < Constants.Queue_IQwave.size(); i++) {
                                mIQ = Constants.Queue_IQwave.poll();

                                //取出时间
                                byte[] byte1 = (byte[]) mIQ.get(0);
                                int year = getYear(byte1);
                                int month = getMonth(byte1);
                                int day = getDay(byte1);
                                int hour = getHour(byte1);
                                int min = getMin(byte1);
                                int sec = getSecond(byte1);
                                //创建IQ文件
                                String IQname = String.format("%d_%d_%d_%d_%d_%d_%d.%s", year, month, day, hour, min, sec,
                                        Constants.ID, "iq");
                                File file = new File(IQFILE_PATH, IQname);
                                //向文件写入数据
                                try {
                                    //获取文件写入流
                                    fos = new FileOutputStream(file);
                                    fos.write((byte) 0x00);
                                    for (int j = 0; j < mIQ.size(); j++) {
//                                        fos.write((byte[]) mPowerSpectrum.get(j));
                                    }
                                    fos.write(0x00);
                                    fos.close();
                                } catch (IOException e) {
                                    try {
                                        fos.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }
                            }

                            Toast.makeText(getContext(), "IQ文件写入完毕", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).start();
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment, container, false);
    }

    private int getYear(byte[] bytes) {
        int year;
        year = (bytes[9] << 4) + (bytes[10] >> 4);
        return year;
    }

    private int getMonth(byte[] bytes) {
        int month;
        month = (bytes[10] & 0x0f);
        return month;
    }

    private int getDay(byte[] bytes) {
        int day;
        day = (bytes[11] & 0xf8);
        return day;
    }

    private int getHour(byte[] bytes) {
        int hour;
        hour = ((bytes[11] & 0x07) << 3) + (bytes[12] & 0x03);
        return hour;
    }

    private int getMin(byte[] bytes) {
        int min;
        min = (bytes[12] & 0xfc);
        return min;
    }

    private int getSecond(byte[] bytes) {
        int second;
        second = (bytes[13]) & 0xff;
        return second;
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

}




