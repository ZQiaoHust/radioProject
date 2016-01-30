package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/28.
 */
public class InteractionFixmodeRequest implements Parcelable {
    private int eqiupmentID;
    private int IDcard;
    private int recvGain;
    private int freqNum;
    private double fix1;
    private double fix2;
    private double fix3;
    private byte IQband_ratio;//带宽、数据率
    private int blockNum;
    private byte[] time;//到秒
    public InteractionFixmodeRequest(){

    }

    protected InteractionFixmodeRequest(Parcel in) {
        eqiupmentID = in.readInt();
        IDcard = in.readInt();
        recvGain = in.readInt();
        freqNum = in.readInt();
        fix1 = in.readDouble();
        fix2 = in.readDouble();
        fix3 = in.readDouble();
        IQband_ratio = in.readByte();
        blockNum = in.readInt();
        time = in.createByteArray();
    }

    public static final Creator<InteractionFixmodeRequest> CREATOR = new Creator<InteractionFixmodeRequest>() {
        @Override
        public InteractionFixmodeRequest createFromParcel(Parcel in) {
            return new InteractionFixmodeRequest(in);
        }

        @Override
        public InteractionFixmodeRequest[] newArray(int size) {
            return new InteractionFixmodeRequest[size];
        }
    };

    public void setFreqNum(int freqNum) {
        this.freqNum = freqNum;
    }

    public int getFreqNum() {

        return freqNum;
    }

    public int getEqiupmentID() {
        return eqiupmentID;
    }

    public void setEqiupmentID(int eqiupmentID) {
        this.eqiupmentID = eqiupmentID;
    }

    public int getIDcard() {
        return IDcard;
    }

    public int getRecvGain() {
        return recvGain;
    }

    public double getFix1() {
        return fix1;
    }

    public double getFix2() {
        return fix2;
    }

    public double getFix3() {
        return fix3;
    }

    public byte getIQband_ratio() {
        return IQband_ratio;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public byte[] getTime() {
        return time;
    }

    public void setIDcard(int IDcard) {
        this.IDcard = IDcard;
    }

    public void setRecvGain(int recvGain) {
        this.recvGain = recvGain;
    }

    public void setFix1(double fix1) {
        this.fix1 = fix1;
    }

    public void setFix2(double fix2) {
        this.fix2 = fix2;
    }

    public void setIQband_ratio(byte IQband_ratio) {
        this.IQband_ratio = IQband_ratio;
    }

    public void setFix3(double fix3) {
        this.fix3 = fix3;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eqiupmentID);
        dest.writeInt(IDcard);
        dest.writeInt(recvGain);
        dest.writeInt(freqNum);
        dest.writeDouble(fix1);
        dest.writeDouble(fix2);
        dest.writeDouble(fix3);
        dest.writeByte(IQband_ratio);
        dest.writeInt(blockNum);
        dest.writeByteArray(time);
    }
}
