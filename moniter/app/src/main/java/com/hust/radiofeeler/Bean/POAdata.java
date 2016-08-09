package com.hust.radiofeeler.Bean;

/**
 * 异常辐射源POA数据帧
 * Created by zq on 2016/7/12.
 */
public class POAdata {
    private byte[] time; //时间
    private byte[] location;//地理位置坐标数组
    private int totalBand;//扫频的频段总数
    private int numN;//扫频总数据帧的序号
    private int ablNum;//异常频点的个数
    private byte[] abnormalInfo;//有效的异常频点信息，去除为0的部分

    public byte[] getTime() {
        return time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }

    public byte[] getLocation() {
        return location;
    }

    public void setLocation(byte[] location) {
        this.location = location;
    }

    public int getTotalBand() {
        return totalBand;
    }

    public void setTotalBand(int totalBand) {
        this.totalBand = totalBand;
    }

    public int getNumN() {
        return numN;
    }

    public void setNumN(int numN) {
        this.numN = numN;
    }

    public int getAblNum() {
        return ablNum;
    }

    public void setAblNum(int ablNum) {
        this.ablNum = ablNum;
    }

    public byte[] getAbnormalInfo() {
        return abnormalInfo;
    }

    public void setAbnormalInfo(byte[] abnormalInfo) {
        this.abnormalInfo = abnormalInfo;
    }
}
