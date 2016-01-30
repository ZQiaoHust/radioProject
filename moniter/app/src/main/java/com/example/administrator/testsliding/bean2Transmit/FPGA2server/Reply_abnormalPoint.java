package com.example.administrator.testsliding.bean2Transmit.FPGA2server;

/**
 * Created by Administrator on 2015/12/4.
 */
public class Reply_abnormalPoint {
    private int bandNum;//所在段序号
    private int num;
    private byte[] power;

    public int getBandNum() {
        return bandNum;
    }

    public int getNum() {
        return num;
    }

    public byte[] getPower() {
        return power;
    }

    public void setBandNum(int bandNum) {
        this.bandNum = bandNum;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPower(byte[] power) {
        this.power = power;
    }
}
