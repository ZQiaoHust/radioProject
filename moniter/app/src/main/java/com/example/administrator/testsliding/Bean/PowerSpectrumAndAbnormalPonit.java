package com.example.administrator.testsliding.Bean;

/**
 * Created by jinaghao on 15/12/24.
 */
public class PowerSpectrumAndAbnormalPonit {

    private byte functionID;//区分背景频谱和实时频谱
    private byte[] locationandTime;//地理位置坐标数组和时间
    private int sweepModel;
    private int fileSendmodel;
    private int IsChange;
    private int totalBand;
    private int PSbandNum;//频段序号
    private byte[] PSpower;//功率值


    //异常频点的内容
    private int APbandNum;//所在段序号
    private int APnum;//异常频点总个数 [0,10]
    private byte[] APpower;

    public PowerSpectrumAndAbnormalPonit(){}

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

    public void setPSbandNum(int PSbandNum) {
        this.PSbandNum = PSbandNum;
    }



    public void setPSpower(byte[] PSpower) {
        this.PSpower = PSpower;
    }

    public void setAPbandNum(int APbandNum) {
        this.APbandNum = APbandNum;
    }

    public void setAPnum(int APnum) {
        this.APnum = APnum;
    }

    public void setAPpower(byte[] APpower) {
        this.APpower = APpower;
    }

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

    public int getPSbandNum() {
        return PSbandNum;
    }



    public byte[] getPSpower() {
        return PSpower;
    }

    public int getAPbandNum() {
        return APbandNum;
    }

    public int getAPnum() {
        return APnum;
    }

    public byte[] getAPpower() {
        return APpower;
    }
}
