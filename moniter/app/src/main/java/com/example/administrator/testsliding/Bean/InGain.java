package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class InGain implements Parcelable{
    private int eqipmentID;

    private int Ingain;

    public InGain(){}


    protected InGain(Parcel in) {
        eqipmentID = in.readInt();
        Ingain = in.readInt();
    }

    public static final Creator<InGain> CREATOR = new Creator<InGain>() {
        @Override
        public InGain createFromParcel(Parcel in) {
            return new InGain(in);
        }

        @Override
        public InGain[] newArray(int size) {
            return new InGain[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eqipmentID);
        dest.writeInt(Ingain);
    }

    public void setEqipmentID(int eqipmentID) {
        this.eqipmentID = eqipmentID;
    }

    public void setIngain(int ingain) {
        Ingain = ingain;
    }

    public int getEqipmentID() {

        return eqipmentID;
    }

    public int getIngain() {
        return Ingain;
    }
}
