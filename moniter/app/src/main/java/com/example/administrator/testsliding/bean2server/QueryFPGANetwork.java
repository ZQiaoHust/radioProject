package com.example.administrator.testsliding.bean2server;

/**
 * Created by Administrator on 2015/11/18.
 */
public class QueryFPGANetwork {
    private int equipmentID;
    private byte  IsOnline;
    private byte style;
    private byte[] longtitude;
    private byte[] latitude;
    private byte[] height;

    public int getEquipmentID() {
        return equipmentID;
    }

    public byte getIsOnline() {
        return IsOnline;
    }

    public byte getStyle() {
        return style;
    }

    public byte[] getLongtitude() {
        return longtitude;
    }

    public byte[] getLatitude() {
        return latitude;
    }

    public byte[] getHeight() {
        return height;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setIsOnline(byte isOnline) {
        IsOnline = isOnline;
    }

    public void setStyle(byte style) {
        this.style = style;
    }

    public void setLongtitude(byte[] longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(byte[] latitude) {
        this.latitude = latitude;
    }

    public void setHeight(byte[] height) {
        this.height = height;
    }
}
