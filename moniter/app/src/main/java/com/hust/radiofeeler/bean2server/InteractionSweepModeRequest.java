package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/28.
 */
public class InteractionSweepModeRequest implements Parcelable {
    private int equipmentID;
    private int IDcard;
    private int recvGain;
    private int sendGain;
    private  int thresholdStyle;
    private int fixThreshold;
    private int adapThreshold;//自适应门限
    private int  sendFilemode;
    private int judgeThreshold;
    private int extractionRatio;//抽取倍率
    private int sweepMode;
    private int totalBand;
    private int bandNum;//xuhao
    private double startFreq;
    private double endFreq;
    public InteractionSweepModeRequest(){

    }


    protected InteractionSweepModeRequest(Parcel in) {
        equipmentID = in.readInt();
        IDcard = in.readInt();
        recvGain = in.readInt();
        sendGain = in.readInt();
        thresholdStyle = in.readInt();
        fixThreshold = in.readInt();
        adapThreshold = in.readInt();
        sendFilemode = in.readInt();
        judgeThreshold = in.readInt();
        extractionRatio = in.readInt();
        sweepMode = in.readInt();
        totalBand = in.readInt();
        bandNum = in.readInt();
        startFreq = in.readDouble();
        endFreq = in.readDouble();
    }

    public static final Creator<InteractionSweepModeRequest> CREATOR = new Creator<InteractionSweepModeRequest>() {
        @Override
        public InteractionSweepModeRequest createFromParcel(Parcel in) {
            return new InteractionSweepModeRequest(in);
        }

        @Override
        public InteractionSweepModeRequest[] newArray(int size) {
            return new InteractionSweepModeRequest[size];
        }
    };

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public int getIDcard() {
        return IDcard;
    }

    public int getRecvGain() {
        return recvGain;
    }

    public int getSendGain() {
        return sendGain;
    }

    public int getThresholdStyle() {
        return thresholdStyle;
    }

    public int getFixThreshold() {
        return fixThreshold;
    }

    public int getAdapThreshold() {
        return adapThreshold;
    }

    public int getSendFilemode() {
        return sendFilemode;
    }

    public int getJudgeThreshold() {
        return judgeThreshold;
    }

    public int getExtractionRatio() {
        return extractionRatio;
    }

    public int getSweepMode() {
        return sweepMode;
    }

    public int getTotalBand() {
        return totalBand;
    }

    public int getBandNum() {
        return bandNum;
    }

    public double getStartFreq() {
        return startFreq;
    }

    public double getEndFreq() {
        return endFreq;
    }

    public void setIDcard(int IDcard) {
        this.IDcard = IDcard;
    }

    public void setRecvGain(int recvGain) {
        this.recvGain = recvGain;
    }

    public void setSendGain(int sendGain) {
        this.sendGain = sendGain;
    }

    public void setThresholdStyle(int thresholdStyle) {
        this.thresholdStyle = thresholdStyle;
    }

    public void setAdapThreshold(int adapThreshold) {
        this.adapThreshold = adapThreshold;
    }

    public void setFixThreshold(int fixThreshold) {
        this.fixThreshold = fixThreshold;
    }

    public void setSendFilemode(int sendFilemode) {
        this.sendFilemode = sendFilemode;
    }

    public void setJudgeThreshold(int judgeThreshold) {
        this.judgeThreshold = judgeThreshold;
    }

    public void setExtractionRatio(int extractionRatio) {
        this.extractionRatio = extractionRatio;
    }

    public void setSweepMode(int sweepMode) {
        this.sweepMode = sweepMode;
    }

    public void setTotalBand(int totalBand) {
        this.totalBand = totalBand;
    }

    public void setBandNum(int bandNum) {
        this.bandNum = bandNum;
    }

    public void setStartFreq(double startFreq) {
        this.startFreq = startFreq;
    }

    public void setEndFreq(double endFreq) {
        this.endFreq = endFreq;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(IDcard);
        dest.writeInt(recvGain);
        dest.writeInt(sendGain);
        dest.writeInt(thresholdStyle);
        dest.writeInt(fixThreshold);
        dest.writeInt(adapThreshold);
        dest.writeInt(sendFilemode);
        dest.writeInt(judgeThreshold);
        dest.writeInt(extractionRatio);
        dest.writeInt(sweepMode);
        dest.writeInt(totalBand);
        dest.writeInt(bandNum);
        dest.writeDouble(startFreq);
        dest.writeDouble(endFreq);
    }
}
