package com.example.administrator.testsliding.GlobalConstants;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import java.util.LinkedList;
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

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }

}
