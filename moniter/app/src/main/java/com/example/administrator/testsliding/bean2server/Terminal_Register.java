package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**全部台站登记属性
 * Created by Administrator on 2015/11/23.
 */
public class Terminal_Register implements Parcelable{
    private int equipmentID;

    public Terminal_Register(){

    }


    protected Terminal_Register(Parcel in) {
        equipmentID = in.readInt();
    }

    public static final Creator<Terminal_Register> CREATOR = new Creator<Terminal_Register>() {
        @Override
        public Terminal_Register createFromParcel(Parcel in) {
            return new Terminal_Register(in);
        }

        @Override
        public Terminal_Register[] newArray(int size) {
            return new Terminal_Register[size];
        }
    };

    public void setEquipmentID(int eqiupmentID) {
        this.equipmentID = eqiupmentID;
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
    }
}
