package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/27.
 */
public class LocationAbnormalRequest implements Parcelable {
    private int equiomentID;
    private byte locationWay;
    private double abFreq;
    private byte IQband_radio;
    private int blockNum;
    private byte[] time2min;

    public LocationAbnormalRequest(){

    }
    protected LocationAbnormalRequest(Parcel in) {
        equiomentID = in.readInt();
        locationWay = in.readByte();
        abFreq = in.readDouble();
        IQband_radio = in.readByte();
        blockNum = in.readInt();
        time2min = in.createByteArray();

    }

    public static final Creator<LocationAbnormalRequest> CREATOR = new Creator<LocationAbnormalRequest>() {
        @Override
        public LocationAbnormalRequest createFromParcel(Parcel in) {
            return new LocationAbnormalRequest(in);
        }

        @Override
        public LocationAbnormalRequest[] newArray(int size) {
            return new LocationAbnormalRequest[size];
        }
    };

    public void setEquiomentID(int equiomentID) {
        this.equiomentID = equiomentID;
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



    public int getEquiomentID() {

        return equiomentID;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equiomentID);
        dest.writeByte(locationWay);
        dest.writeDouble(abFreq);
        dest.writeByte(IQband_radio);
        dest.writeInt(blockNum);
        dest.writeByteArray(time2min);
    }
}

