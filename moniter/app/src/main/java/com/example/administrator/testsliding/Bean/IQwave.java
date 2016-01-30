package com.example.administrator.testsliding.Bean;

/**
 * Created by jinaghao on 15/12/1.
 */
public class IQwave  {

    private byte[] location;//地理位置坐标数组
    private byte[] time;//时间p
    private int totalBands;//iq上传总段数
    private int nowNum;//序号
    private byte[] IQpara;//IQ的参数
    private byte[] IQwave;//IQ路2000个点


    public IQwave(){}

    public void setLocation(byte[] location) {
        this.location = location;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }

    public void setIQpara(byte[] IQpara) {
        this.IQpara = IQpara;
    }

    public void setIQwave(byte[] IQwave) {
        this.IQwave = IQwave;
    }

    public byte[] getLocation() {

        return location;
    }

    public byte[] getTime() {
        return time;
    }

    public byte[] getIQpara() {
        return IQpara;
    }

    public byte[] getIQwave() {
        return IQwave;
    }

    public void setTotalBands(int totalBands) {
        this.totalBands = totalBands;
    }

    public void setNowNum(int nowNum) {
        this.nowNum = nowNum;
    }

    public int getTotalBands() {

        return totalBands;
    }

    public int getNowNum() {
        return nowNum;
    }
}
