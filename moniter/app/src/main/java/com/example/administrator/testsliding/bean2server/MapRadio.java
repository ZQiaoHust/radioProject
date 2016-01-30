package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapRadio implements Parcelable{
    private int equipmentID;
    private int centralFreq;
    private int band;
    private int radius;//半径
    private float dieta;//分辨率
    private int freshtime;//刷新间隔
    private byte[] startTime;//起始时间
    private byte[] endTime;

    public MapRadio(){

    }


    protected MapRadio(Parcel in) {
        equipmentID = in.readInt();
        centralFreq = in.readInt();
        band = in.readInt();
        radius = in.readInt();
        dieta = in.readFloat();
        freshtime = in.readInt();
        startTime = in.createByteArray();
        endTime = in.createByteArray();
    }

    public static final Creator<MapRadio> CREATOR = new Creator<MapRadio>() {
        @Override
        public MapRadio createFromParcel(Parcel in) {
            return new MapRadio(in);
        }

        @Override
        public MapRadio[] newArray(int size) {
            return new MapRadio[size];
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

    public float getDieta() {
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

    public void setDieta(float dieta) {
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
        dest.writeFloat(dieta);
        dest.writeInt(freshtime);
        dest.writeByteArray(startTime);
        dest.writeByteArray(endTime);
    }
}
