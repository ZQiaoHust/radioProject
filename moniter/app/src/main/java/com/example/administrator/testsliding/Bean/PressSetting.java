package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressSetting implements Parcelable {
    private int pressMode;
    private int style;
    private int band;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    public PressSetting(){}


    protected PressSetting(Parcel in) {
        pressMode = in.readInt();
        style = in.readInt();
        band = in.readInt();
        t1 = in.readInt();
        t2 = in.readInt();
        t3 = in.readInt();
        t4 = in.readInt();
    }

    public static final Creator<PressSetting> CREATOR = new Creator<PressSetting>() {
        @Override
        public PressSetting createFromParcel(Parcel in) {
            return new PressSetting(in);
        }

        @Override
        public PressSetting[] newArray(int size) {
            return new PressSetting[size];
        }
    };

    public void setPressMode(int pressMode) {
        this.pressMode = pressMode;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public void setT4(int t4) {
        this.t4 = t4;
    }

    public int getPressMode() {

        return pressMode;
    }

    public int getStyle() {
        return style;
    }

    public int getBand() {
        return band;
    }

    public int getT1() {
        return t1;
    }

    public int getT2() {
        return t2;
    }

    public int getT3() {
        return t3;
    }

    public int getT4() {
        return t4;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pressMode);
        dest.writeInt(style);
        dest.writeInt(band);
        dest.writeInt(t1);
        dest.writeInt(t2);
        dest.writeInt(t3);
        dest.writeInt(t4);
    }
}
