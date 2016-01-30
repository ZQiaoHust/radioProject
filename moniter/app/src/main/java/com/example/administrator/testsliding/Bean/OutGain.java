package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class OutGain implements Parcelable{
    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setOutGain(int outGain) {
        OutGain = outGain;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public int getOutGain() {
        return OutGain;
    }

    private int equipmentID;

    private int OutGain;

    public OutGain(){}

    protected OutGain(Parcel in) {
        equipmentID = in.readInt();
        OutGain = in.readInt();
    }

    public static final Creator<com.example.administrator.testsliding.Bean.OutGain> CREATOR = new Creator<com.example.administrator.testsliding.Bean.OutGain>() {
        @Override
        public com.example.administrator.testsliding.Bean.OutGain createFromParcel(Parcel in) {
            return new OutGain(in);
        }

        @Override
        public com.example.administrator.testsliding.Bean.OutGain[] newArray(int size) {
            return new OutGain[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(OutGain);
    }
}
