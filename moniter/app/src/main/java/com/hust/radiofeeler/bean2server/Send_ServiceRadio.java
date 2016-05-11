package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/14.
 */
public class Send_ServiceRadio implements Parcelable{

    private int equipmentID;
    private int startFrequency;
   private int endFrequency;



    public Send_ServiceRadio(){

    }
    protected Send_ServiceRadio(Parcel in) {
        equipmentID = in.readInt();
        startFrequency = in.readInt();
        endFrequency = in.readInt();
    }

    public static final Creator<Send_ServiceRadio> CREATOR = new Creator<Send_ServiceRadio>() {
        @Override
        public Send_ServiceRadio createFromParcel(Parcel in) {
            return new Send_ServiceRadio(in);
        }

        @Override
        public Send_ServiceRadio[] newArray(int size) {
            return new Send_ServiceRadio[size];
        }
    };

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setStartFrequency(int startFrequency) {
        this.startFrequency = startFrequency;
    }

    public void setEndFrequency(int endFrequency) {
        this.endFrequency = endFrequency;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public int getStartFrequency() {
        return startFrequency;
    }

    public int getEndFrequency() {
        return endFrequency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(startFrequency);
        dest.writeInt(endFrequency);
    }
}
