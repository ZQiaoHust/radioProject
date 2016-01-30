package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/18.
 */
public class Query implements Parcelable{

    private byte funcID;
    private int equipmentID;

    public Query(){

    }
    protected Query(Parcel in) {
        funcID = in.readByte();
        equipmentID = in.readInt();
    }

    public static final Creator<Query> CREATOR = new Creator<Query>() {
        @Override
        public Query createFromParcel(Parcel in) {
            return new Query(in);
        }

        @Override
        public Query[] newArray(int size) {
            return new Query[size];
        }
    };

    public void setFuncID(byte funcID) {
        this.funcID = funcID;
    }

    public byte getFuncID() {

        return funcID;
    }


    public void setequipmentID(int ID) {
        this.equipmentID = ID;
    }

    public int getequipmentID() {

        return equipmentID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(funcID);
        dest.writeInt(equipmentID);
    }
}
