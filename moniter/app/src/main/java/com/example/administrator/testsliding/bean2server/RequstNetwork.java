package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetwork implements Parcelable{
    private  int equipmentID;
    private  int FPGAID;//硬件型号
    private byte[] styleAndLocation;//终端类型和位置信息


    public RequstNetwork(){}


    protected RequstNetwork(Parcel in) {
        equipmentID = in.readInt();
        FPGAID = in.readInt();
        styleAndLocation = in.createByteArray();
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

    public void setFPGAID(int FPGAID) {
        this.FPGAID = FPGAID;
    }

    public int getFPGAID() {
        return FPGAID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public byte[] getStyleAndLocation() {
        return styleAndLocation;
    }

    public void setStyleAndLocation(byte[] styleAndLocation) {
        this.styleAndLocation = styleAndLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(FPGAID);
        dest.writeByteArray(styleAndLocation);
    }
}
