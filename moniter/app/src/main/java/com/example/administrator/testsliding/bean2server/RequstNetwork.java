package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetwork implements Parcelable{
    private  int equipmentID;
    private  int FPGAID;//硬件型号
    private byte style;
    private byte[] longtitude;
    private byte[] latitude;
    private byte[] height;

    public RequstNetwork(){}
    protected RequstNetwork(Parcel in) {
        equipmentID = in.readInt();
        FPGAID = in.readInt();
        style = in.readByte();
        longtitude = in.createByteArray();
        latitude = in.createByteArray();
        height = in.createByteArray();
    }

    public static final Creator<RequstNetwork> CREATOR = new Creator<RequstNetwork>() {
        @Override
        public RequstNetwork createFromParcel(Parcel in) {
            return new RequstNetwork(in);
        }

        @Override
        public RequstNetwork[] newArray(int size) {
            return new RequstNetwork[size];
        }
    };

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

    public byte[] getLatitude() {

        return latitude;
    }

    public void setFPGAID(int FPGAID) {
        this.FPGAID = FPGAID;
    }

    public int getFPGAID() {
        return FPGAID;
    }

    public byte[] getLongtitude() {
        return longtitude;
    }

    public byte getStyle() {
        return style;
    }

    public byte[] getHeight() {
        return height;
    }


    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(FPGAID);
        dest.writeByte(style);
        dest.writeByteArray(longtitude);
        dest.writeByteArray(latitude);
        dest.writeByteArray(height);
    }
}
