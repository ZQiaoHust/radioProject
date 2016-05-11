package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/26.
 */
public class Station_CurrentRequst implements Parcelable {
    private int equiomentID;
    private int IDcard;
    private byte locationWay;
    private double abFreq;
    private byte IQband_radio;
    private int blockNum;
    private byte[] time2min;//到分

    public Station_CurrentRequst(){

    }
    protected Station_CurrentRequst(Parcel in) {
        equiomentID = in.readInt();
        IDcard = in.readInt();
        locationWay = in.readByte();
        abFreq = in.readDouble();
        IQband_radio = in.readByte();
        blockNum = in.readInt();
        time2min = in.createByteArray();
    }

    public static final Creator<Station_CurrentRequst> CREATOR = new Creator<Station_CurrentRequst>() {
        @Override
        public Station_CurrentRequst createFromParcel(Parcel in) {
            return new Station_CurrentRequst(in);
        }

        @Override
        public Station_CurrentRequst[] newArray(int size) {
            return new Station_CurrentRequst[size];
        }
    };

    public int getEquiomentID() {
        return equiomentID;
    }

    public int getIDcard() {
        return IDcard;
    }

    public byte getLocationWay() {
        return locationWay;
    }

    public double getAbFreq() {
        return abFreq;
    }

    public byte getIQband_radio() {
        return IQband_radio;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public byte[] getTime2min() {
        return time2min;
    }



    public void setEquiomentID(int equiomentID) {
        this.equiomentID = equiomentID;
    }

    public void setIDcard(int IDcard) {
        this.IDcard = IDcard;
    }

    public void setLocationWay(byte locationWay) {
        this.locationWay = locationWay;
    }

    public void setAbFreq(double abFreq) {
        this.abFreq = abFreq;
    }

    public void setIQband_radio(byte IQband_radio) {
        this.IQband_radio = IQband_radio;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public void setTime2min(byte[] time2min) {
        this.time2min = time2min;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equiomentID);
        dest.writeInt(IDcard);
        dest.writeByte(locationWay);
        dest.writeDouble(abFreq);
        dest.writeByte(IQband_radio);
        dest.writeInt(blockNum);
        dest.writeByteArray(time2min);

    }
}
