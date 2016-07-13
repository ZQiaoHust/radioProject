package com.hust.radiofeeler.GlobalConstants;

import android.app.Application;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

/**
 * Created by jianghao on 16/1/19.
 */
public class MyApplication extends Application{

    //公共字段 ，全局数据共享
    // 扫频频率范围
    public static int sweepStart;
    public static int sweepEnd;



    //文件上传方式
    public static int fileUploadMode=1;
    //文件抽取倍率
    public static int upRate;

    public static int getUpRate() {
        return upRate;
    }

    public static void setUpRate(int upRate) {
        MyApplication.upRate = upRate;
    }






    public static void setFileUploadMode(int fileUploadMode) {
        MyApplication.fileUploadMode = fileUploadMode;
    }

    public static int getFileUploadMode() {
        return fileUploadMode;
    }

    public static Queue<List<byte[]>> AppQueue_RealtimeSpectrum;//实时功率谱数据,供写文件
    public static void setSweepStart(int sweepStart) {
        MyApplication.sweepStart = sweepStart;
    }

    public static void setSweepEnd(int sweepEnd) {
        MyApplication.sweepEnd = sweepEnd;
    }

    public static int getSweepStart() {

        return sweepStart;
    }

    public static int getSweepEnd() {
        return sweepEnd;
    }



    public static void setQueue_RealtimeSpectrum(Queue<List<byte[]>> queue_RealtimeSpectrum) {
        AppQueue_RealtimeSpectrum = queue_RealtimeSpectrum;
    }

    public static Queue<List<byte[]>> getQueue_RealtimeSpectrum() {
        return AppQueue_RealtimeSpectrum;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //setQueue_RealtimeSpectrum(new LinkedList<List<byte[]>>());
        saveLogcatToFile();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }
    private void saveLogcatToFile() {

        if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/radiofeeler" );
            File logDirectory = new File( appDirectory + "/log" );
            File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec( "logcat -c");
                // note *:D just save all debug log to log file. see logcat --help for how to filer log
                //process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:S com.example.a.tower.MainActivity:D");
                process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:D");
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else if ( isExternalStorageReadable() ) {
            // only readable
        } else {
            // not accessible
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }
}
