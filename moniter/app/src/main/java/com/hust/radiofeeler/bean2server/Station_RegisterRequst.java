package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/26.
 */
public class Station_RegisterRequst implements Parcelable {
    private int equipmentID;
    private int startFreq;
    private int endFreq;

    public Station_RegisterRequst(){

    }
    protected Station_RegisterRequst(Parcel in) {
        equipmentID = in.readInt();
        startFreq = in.readInt();
        endFreq = in.readInt();
    }

    public static final Creator<Station_RegisterRequst> CREATOR = new Creator<Station_RegisterRequst>() {
        @Override
        public Station_RegisterRequst createFromParcel(Parcel in) {
            return new Station_RegisterRequst(in);
        }

        @Override
        public Station_RegisterRequst[] newArray(int size) {
            return new Station_RegisterRequst[size];
        }
    };

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setStartFreq(int startFreq) {
        this.startFreq = startFreq;
    }

    public void setEndFreq(int endFreq) {
        this.endFreq = endFreq;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public int getStartFreq() {
        return startFreq;
    }

    public int getEndFreq() {
        return endFreq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(startFreq);
        dest.writeInt(endFreq);
    }
}
