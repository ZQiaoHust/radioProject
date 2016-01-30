package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class FixCentralFreq implements Parcelable{
    private int number;
    private double fix1;
    private double fix2;
    private double fix3;

    public FixCentralFreq(){}
    protected FixCentralFreq(Parcel in) {
        number = in.readInt();
        fix1 = in.readDouble();
        fix2 = in.readDouble();
        fix3 = in.readDouble();
    }

    public static final Creator<FixCentralFreq> CREATOR = new Creator<FixCentralFreq>() {
        @Override
        public FixCentralFreq createFromParcel(Parcel in) {
            return new FixCentralFreq(in);
        }

        @Override
        public FixCentralFreq[] newArray(int size) {
            return new FixCentralFreq[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeDouble(fix1);
        dest.writeDouble(fix2);
        dest.writeDouble(fix3);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFix1(double fix1) {
        this.fix1 = fix1;
    }

    public void setFix2(double fix2) {
        this.fix2 = fix2;
    }

    public void setFix3(double fix3) {
        this.fix3 = fix3;
    }

    public int getNumber() {

        return number;
    }

    public double getFix1() {
        return fix1;
    }

    public double getFix2() {
        return fix2;
    }

    public double getFix3() {
        return fix3;
    }
}
