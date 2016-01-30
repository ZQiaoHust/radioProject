package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class Threshold implements Parcelable {
    private int equipmentId;
    private int autoThreshold;
    private int thresholdModel;
    private int fixThreshold;

    public Threshold(){}


    protected Threshold(Parcel in) {
        equipmentId = in.readInt();
        autoThreshold = in.readInt();
        thresholdModel = in.readInt();
        fixThreshold = in.readInt();
    }

    public static final Creator<Threshold> CREATOR = new Creator<Threshold>() {
        @Override
        public Threshold createFromParcel(Parcel in) {
            return new Threshold(in);
        }

        @Override
        public Threshold[] newArray(int size) {
            return new Threshold[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentId);
        dest.writeInt(autoThreshold);
        dest.writeInt(thresholdModel);
        dest.writeInt(fixThreshold);
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setAutoThreshold(int autoThreshold) {
        this.autoThreshold = autoThreshold;
    }

    public void setThresholdModel(int thresholdModel) {
        this.thresholdModel = thresholdModel;
    }

    public void setFixThreshold(int fixThreshold) {
        this.fixThreshold = fixThreshold;
    }

    public int getEquipmentId() {

        return equipmentId;
    }

    public int getAutoThreshold() {
        return autoThreshold;
    }

    public int getThresholdModel() {
        return thresholdModel;
    }

    public int getFixThreshold() {
        return fixThreshold;
    }
}
