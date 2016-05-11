package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapInterpolation implements Parcelable{
    private int equipmentID;
    private int centralFreq;
    private int band;
    private int radius;//半径
    private double dieta;//分辨率
    private int freshtime;//刷新间隔
    private byte[] startTime;//起始时间
    private byte[] endTime;

    public MapInterpolation(){

    }


    protected MapInterpolation(Parcel in) {
        equipmentID = in.readInt();
        centralFreq = in.readInt();
        band = in.readInt();
        radius = in.readInt();
        dieta = in.readDouble();
        freshtime = in.readInt();
        startTime = in.createByteArray();
        endTime = in.createByteArray();
    }

    public static final Creator<MapInterpolation> CREATOR = new Creator<MapInterpolation>() {
        @Override
        public MapInterpolation createFromParcel(Parcel in) {
            return new MapInterpolation(in);
        }

        @Override
        public MapInterpolation[] newArray(int size) {
            return new MapInterpolation[size];
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

    public int getRadius() {
        return radius;
    }

    public double getDieta() {
        return dieta;
    }

    public int getFreshtime() {
        return freshtime;
    }

    public byte[] getStartTime() {
        return startTime;
    }

    public byte[] getEndTime() {
        return endTime;
    }

    public void setEndTime(byte[] endTime) {
        this.endTime = endTime;
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

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setDieta(double dieta) {
        this.dieta = dieta;
    }

    public void setFreshtime(int freshtime) {
        this.freshtime = freshtime;
    }

    public void setStartTime(byte[] startTime) {
        this.startTime = startTime;
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
        dest.writeInt(radius);
        dest.writeDouble(dieta);
        dest.writeInt(freshtime);
        dest.writeByteArray(startTime);
        dest.writeByteArray(endTime);
    }
}
