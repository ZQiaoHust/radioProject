package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetworkReply implements Parcelable{
    private  int equipmentID;
    private byte Isagreen;
    private byte style;
    private byte[] longtitude;
    private byte[] latitude;
    private byte[] height;

    public RequstNetworkReply(){

    }
    protected RequstNetworkReply(Parcel in) {
        equipmentID = in.readInt();
        Isagreen = in.readByte();
        style = in.readByte();
        longtitude = in.createByteArray();
        latitude = in.createByteArray();
        height = in.createByteArray();
    }

    public static final Creator<RequstNetworkReply> CREATOR = new Creator<RequstNetworkReply>() {
        @Override
        public RequstNetworkReply createFromParcel(Parcel in) {
            return new RequstNetworkReply(in);
        }

        @Override
        public RequstNetworkReply[] newArray(int size) {
            return new RequstNetworkReply[size];
        }
    };

    public void setHeight(byte[] height) {
        this.height = height;
    }

    public byte[] getHeight() {

        return height;
    }

    public void setIsagreen(byte isagreen) {
        Isagreen = isagreen;
    }

    public byte getIsagreen() {

        return Isagreen;
    }
    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
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

    public int getEquipmentID() {

        return equipmentID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeByte(Isagreen);
        dest.writeByte(style);
        dest.writeByteArray(longtitude);
        dest.writeByteArray(latitude);
        dest.writeByteArray(height);
    }
}
