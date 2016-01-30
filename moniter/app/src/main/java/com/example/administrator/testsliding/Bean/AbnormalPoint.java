package com.example.administrator.testsliding.Bean;

/**
 * Created by Administrator on 2015/12/4.
 */
public class AbnormalPoint {
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
