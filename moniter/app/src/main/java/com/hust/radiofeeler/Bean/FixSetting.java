package com.hust.radiofeeler.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/24.
 */
public class FixSetting implements Parcelable  {
    private byte packetHead;
    private double IQwidth;
    private int blockNum;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String timeString;
    private byte[] content;

    public FixSetting(){}


    protected FixSetting(Parcel in) {
        packetHead = in.readByte();
        IQwidth = in.readDouble();
        blockNum = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
        timeString = in.readString();
        content = in.createByteArray();
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

    public byte getPacketHead() {
        return packetHead;
    }

    public void setPacketHead(byte packetHead) {
        this.packetHead = packetHead;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

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

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(packetHead);
        dest.writeDouble(IQwidth);
        dest.writeInt(blockNum);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
        dest.writeString(timeString);
        dest.writeByteArray(content);
    }
}
