package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**全部台站登记属性
 * Created by Administrator on 2015/11/23.
 */
public class TerminalAttributes_All implements Parcelable{
    private int equipmentID;
    private int startFreq;
    private int endFreq;
    public TerminalAttributes_All(){

    }

    protected TerminalAttributes_All(Parcel in) {
        equipmentID = in.readInt();
        startFreq = in.readInt();
        endFreq = in.readInt();
    }

    public static final Creator<TerminalAttributes_All> CREATOR = new Creator<TerminalAttributes_All>() {
        @Override
        public TerminalAttributes_All createFromParcel(Parcel in) {
            return new TerminalAttributes_All(in);
        }

        @Override
        public TerminalAttributes_All[] newArray(int size) {
            return new TerminalAttributes_All[size];
        }
    };

    public void setEquipmentID(int eqiupmentID) {
        this.equipmentID = eqiupmentID;
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
