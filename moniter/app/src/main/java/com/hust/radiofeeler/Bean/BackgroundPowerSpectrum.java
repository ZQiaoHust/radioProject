package com.hust.radiofeeler.Bean;

/**
 * Created by jinaghao on 15/12/24.
 */
public class BackgroundPowerSpectrum {

    private int totalBand;//扫频的频段总数
    private int numN;//扫频总数据帧的序号
    private int PSbandNum;//频段序号
    private float[] PSpower;//功率值

    public BackgroundPowerSpectrum(){}

    public void setTotalBand(int totalBand) {
        this.totalBand = totalBand;
    }
    public void setPSbandNum(int PSbandNum) {
        this.PSbandNum = PSbandNum;
    }
    public int getNumN() {
        return numN;
    }
    public void setNumN(int numN) {
        this.numN = numN;
    }

    public void setPSpower(float[] PSpower) {
        this.PSpower = PSpower;
    }



    public int getTotalBand() {
        return totalBand;
    }

    public int getPSbandNum() {
        return PSbandNum;
    }



    public float[] getPSpower() {
        return PSpower;
    }


}
