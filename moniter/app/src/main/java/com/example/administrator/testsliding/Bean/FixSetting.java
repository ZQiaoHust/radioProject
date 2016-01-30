package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/24.
 */
public class FixSetting implements  Parcelable {
    private double IQwidth;
    private int blockNum;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public FixSetting(){}


    protected FixSetting(Parcel in) {
        IQwidth = in.readDouble();
        blockNum = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }

    public static final Creator<FixSetting> CREATOR = new Creator<FixSetting>() {
        @Override
        public FixSetting createFromParcel(Parcel in) {
            return new FixSetting(in);
        }

        @Override
        public FixSetting[] newArray(int size) {
            return new FixSetting[size];
        }
    };

    public void setIQwidth(double IQwidth) {
        this.IQwidth = IQwidth;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public double getIQwidth() {

        return IQwidth;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(IQwidth);
        dest.writeInt(blockNum);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
    }
}
