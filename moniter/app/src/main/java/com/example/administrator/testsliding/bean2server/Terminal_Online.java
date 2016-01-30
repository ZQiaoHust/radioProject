package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**全部台站登记属性
 * Created by Administrator on 2015/11/23.
 */
public class Terminal_Online implements Parcelable{
    private int equipmentID;
//    private int startFreq;
//    private int endFreq;
    public Terminal_Online(){

    }


    protected Terminal_Online(Parcel in) {
        equipmentID = in.readInt();
    }

    public static final Creator<Terminal_Online> CREATOR = new Creator<Terminal_Online>() {
        @Override
        public Terminal_Online createFromParcel(Parcel in) {
            return new Terminal_Online(in);
        }

        @Override
        public Terminal_Online[] newArray(int size) {
            return new Terminal_Online[size];
        }
    };

    public void setEquipmentID(int eqiupmentID) {
        this.equipmentID = eqiupmentID;
    }

//    public void setStartFreq(int startFreq) {
//        this.startFreq = startFreq;
//    }
//
//    public void setEndFreq(int endFreq) {
//        this.endFreq = endFreq;
//    }

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
    }
}
