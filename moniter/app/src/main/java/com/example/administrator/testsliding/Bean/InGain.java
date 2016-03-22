package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class InGain implements Parcelable{
    private byte packetHead;
    private int eqipmentID;
    private int Ingain;
    private byte[] content;
    public InGain(){}

    protected InGain(Parcel in) {
        packetHead = in.readByte();
        eqipmentID = in.readInt();
        Ingain = in.readInt();
        content = in.createByteArray();
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte getPacketHead() {
        return packetHead;
    }
    public void setPacketHead(byte packetHead) {
        this.packetHead = packetHead;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(packetHead);
        dest.writeInt(eqipmentID);
        dest.writeInt(Ingain);
        dest.writeByteArray(content);
    }
}
