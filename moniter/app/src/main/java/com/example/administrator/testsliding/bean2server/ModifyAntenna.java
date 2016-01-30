package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyAntenna implements Parcelable {
    private int equipmentID;
    private int FPGAID;//硬件的型号

    public ModifyAntenna(){}
    protected ModifyAntenna(Parcel in) {
        equipmentID = in.readInt();
        FPGAID = in.readInt();
    }

    public static final Creator<ModifyAntenna> CREATOR = new Creator<ModifyAntenna>() {
        @Override
        public ModifyAntenna createFromParcel(Parcel in) {
            return new ModifyAntenna(in);
        }

        @Override
        public ModifyAntenna[] newArray(int size) {
            return new ModifyAntenna[size];
        }
    };

    public int getFPGAID() {
        return FPGAID;
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setFPGAID(int FPGAID) {
        this.FPGAID = FPGAID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(FPGAID);
    }
}
