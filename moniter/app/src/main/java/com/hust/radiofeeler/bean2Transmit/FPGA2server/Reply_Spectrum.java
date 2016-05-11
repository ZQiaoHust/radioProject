package com.hust.radiofeeler.bean2Transmit.FPGA2server;

/**
 * Created by Administrator on 2015/12/3.
 */
public class Reply_Spectrum {
    private byte functionID;//区分背景频谱和实时频谱
    private byte[] locationandTime;//地理位置坐标数组和时间
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minite;
    private int mius;
    private int sweepModel;
    private int fileSendmodel;
    private int IsChange;
    private int totalBand;
    private int bandNum;
    private byte[] sweepModel2bandNum;//以上几个参数对应的字节数组，供写文件用
    private byte[] power;//功率值

    public void setSweepModel2bandNum(byte[] sweepModel2bandNum) {
        this.sweepModel2bandNum = sweepModel2bandNum;
    }

    public byte[] getSweepModel2bandNum() {

        return sweepModel2bandNum;
    }

    public void setFunctionID(byte functionID) {
        this.functionID = functionID;
    }

    public void setLocation(byte[] location) {
        this.locationandTime = location;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinite(int minite) {
        this.minite = minite;
    }

    public void setMius(int mius) {
        this.mius = mius;
    }

    public void setSweepModel(int sweepModel) {
        this.sweepModel = sweepModel;
    }

    public void setFileSendmodel(int fileSendmodel) {
        this.fileSendmodel = fileSendmodel;
    }

    public void setIsChange(int isChange) {
        IsChange = isChange;
    }

    public void setTotalBand(int totalBand) {
        this.totalBand = totalBand;
    }

    public void setBandNum(int bandNum) {
        this.bandNum = bandNum;
    }

    public void setPower(byte[] power) {
        this.power = power;
    }

    public byte getFunctionID() {

        return functionID;
    }

    public byte[] getLocation() {
        return locationandTime;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinite() {
        return minite;
    }

    public int getMius() {
        return mius;
    }

    public int getSweepModel() {
        return sweepModel;
    }

    public int getFileSendmodel() {
        return fileSendmodel;
    }

    public int getIsChange() {
        return IsChange;
    }

    public int getTotalBand() {
        return totalBand;
    }

    public int getBandNum() {
        return bandNum;
    }

    public byte[] getPower() {
        return power;
    }
}
