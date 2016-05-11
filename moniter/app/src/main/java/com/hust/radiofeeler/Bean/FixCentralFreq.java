package com.hust.radiofeeler.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class FixCentralFreq implements Parcelable{
    private byte packetHead;
    private int number;
    private double fix1;
    private double fix2;
    private double fix3;
    private byte[] content;

    public FixCentralFreq(){}


    protected FixCentralFreq(Parcel in) {
        packetHead = in.readByte();
        number = in.readInt();
        fix1 = in.readDouble();
        fix2 = in.readDouble();
        fix3 = in.readDouble();
        content = in.createByteArray();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(packetHead);
        dest.writeInt(number);
        dest.writeDouble(fix1);
        dest.writeDouble(fix2);
        dest.writeDouble(fix3);
        dest.writeByteArray(content);
    }
}
