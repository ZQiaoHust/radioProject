package com.example.administrator.testsliding.Bean;

/**
 * Created by jinaghao on 15/11/30.
 */
public class PowerSpectrum {

    private byte functionID;//区分背景频谱和实时频谱
    private byte[] locationandTime;//地理位置坐标数组和时间
    private int sweepModel;
    private int fileSendmodel;
    private int IsChange;
    private int totalBand;
    private int bandNum;//频段序号
    private byte[] sweepModel2bandNum;//以上几个参数对应的字节数组，供写文件用
    private byte[] power;//功率值

    public PowerSpectrum(){}


    public byte getFunctionID() {
        return functionID;
    }

    public byte[] getLocationandTime() {
        return locationandTime;
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

    public byte[] getSweepModel2bandNum() {
        return sweepModel2bandNum;
    }

    public byte[] getPower() {
        return power;
    }

    public void setFunctionID(byte functionID) {
        this.functionID = functionID;
    }

    public void setLocationandTime(byte[] locationandTime) {
        this.locationandTime = locationandTime;
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

    public void setSweepModel2bandNum(byte[] sweepModel2bandNum) {
        this.sweepModel2bandNum = sweepModel2bandNum;
    }

    public void setPower(byte[] power) {
        this.power = power;
    }
}
