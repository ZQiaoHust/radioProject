package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapRoute implements Parcelable{
    private int equipmentID;
    private int centralFreq;
    private int band;
    private byte isTPOA;
    private byte[] startTime;
    private byte[] endTime;
    public  MapRoute(){}


    protected MapRoute(Parcel in) {
        equipmentID = in.readInt();
        centralFreq = in.readInt();
        band = in.readInt();
        isTPOA = in.readByte();
        startTime = in.createByteArray();
        endTime = in.createByteArray();
    }

    public static final Creator<MapRoute> CREATOR = new Creator<MapRoute>() {
        @Override
        public MapRoute createFromParcel(Parcel in) {
            return new MapRoute(in);
        }

        @Override
        public MapRoute[] newArray(int size) {
            return new MapRoute[size];
        }
    };

    public int getEquipmentID() {
        return equipmentID;
    }

    public int getCentralFreq() {
        return centralFreq;
    }

    public int getBand() {
        return band;
    }

    public byte[] getStartTime() {
        return startTime;
    }

    public byte[] getEndTime() {
        return endTime;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setCentralFreq(int centralFreq) {
        this.centralFreq = centralFreq;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public void setStartTime(byte[] startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(byte[] endTime) {
        this.endTime = endTime;
    }

    public byte getIsTPOA() {
        return isTPOA;
    }

    public void setIsTPOA(byte isTPOA) {
        this.isTPOA = isTPOA;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(centralFreq);
        dest.writeInt(band);
        dest.writeByte(isTPOA);
        dest.writeByteArray(startTime);
        dest.writeByteArray(endTime);
    }
}
