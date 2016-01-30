package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/30.
 */
public class InteractionPressmodeRequest implements Parcelable{
    private int eqiupmentID;
    private int IDcard;
    private  int sendGain;
    private int freqNum;
    private double pressFreq1;
    private double pressFreg2;
    private byte sendModel;//压制发射模式
    private byte signalStyle;//信号类型
    private byte signalBand;//信号带宽
    private int t1;
    private int t2;
    private int t3;
    private int t4;

public InteractionPressmodeRequest(){

}


    protected InteractionPressmodeRequest(Parcel in) {
        eqiupmentID = in.readInt();
        IDcard = in.readInt();
        sendGain = in.readInt();
        freqNum = in.readInt();
        pressFreq1 = in.readDouble();
        pressFreg2 = in.readDouble();
        sendModel = in.readByte();
        signalStyle = in.readByte();
        signalBand = in.readByte();
        t1 = in.readInt();
        t2 = in.readInt();
        t3 = in.readInt();
        t4 = in.readInt();
    }

    public static final Creator<InteractionPressmodeRequest> CREATOR = new Creator<InteractionPressmodeRequest>() {
        @Override
        public InteractionPressmodeRequest createFromParcel(Parcel in) {
            return new InteractionPressmodeRequest(in);
        }

        @Override
        public InteractionPressmodeRequest[] newArray(int size) {
            return new InteractionPressmodeRequest[size];
        }
    };

    public void setEqiupmentID(int eqiupmentID) {
        this.eqiupmentID = eqiupmentID;
    }

    public void setIDcard(int IDcard) {
        this.IDcard = IDcard;
    }

    public void setSendGain(int sendGain) {
        this.sendGain = sendGain;
    }

    public void setPressFreq1(double pressFreq1) {
        this.pressFreq1 = pressFreq1;
    }

    public void setPressFreg2(double pressFreg2) {
        this.pressFreg2 = pressFreg2;
    }

    public void setSignalStyle(byte signalStyle) {
        this.signalStyle = signalStyle;
    }

    public void setSendModel(byte sendModel) {
        this.sendModel = sendModel;
    }

    public void setSignalBand(byte signalBand) {
        this.signalBand = signalBand;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public void setT4(int t4) {
        this.t4 = t4;
    }

    public int getEqiupmentID() {
        return eqiupmentID;
    }

    public int getIDcard() {
        return IDcard;
    }

    public int getSendGain() {
        return sendGain;
    }

    public double getPressFreq1() {
        return pressFreq1;
    }

    public double getPressFreg2() {
        return pressFreg2;
    }

    public byte getSendModel() {
        return sendModel;
    }

    public byte getSignalStyle() {
        return signalStyle;
    }

    public byte getSignalBand() {
        return signalBand;
    }

    public int getT1() {
        return t1;
    }

    public int getT2() {
        return t2;
    }

    public int getT3() {
        return t3;
    }

    public int getT4() {
        return t4;
    }

    public void setFreqNum(int freqNum) {
        this.freqNum = freqNum;
    }

    public int getFreqNum() {

        return freqNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eqiupmentID);
        dest.writeInt(IDcard);
        dest.writeInt(sendGain);
        dest.writeInt(freqNum);
        dest.writeDouble(pressFreq1);
        dest.writeDouble(pressFreg2);
        dest.writeByte(sendModel);
        dest.writeByte(signalStyle);
        dest.writeByte(signalBand);
        dest.writeInt(t1);
        dest.writeInt(t2);
        dest.writeInt(t3);
        dest.writeInt(t4);
    }
}
