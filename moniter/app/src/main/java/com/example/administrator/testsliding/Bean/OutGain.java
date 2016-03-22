package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/23.
 */
public class OutGain implements Parcelable {
    private byte packetHead;
    private byte[] content;

    protected OutGain(Parcel in) {
        packetHead = in.readByte();
        content = in.createByteArray();
        equipmentID = in.readInt();
        OutGain = in.readInt();
    }

    public static final Creator<OutGain> CREATOR = new Creator<OutGain>() {
        @Override
        public OutGain createFromParcel(Parcel in) {
            return new OutGain(in);
        }

        @Override
        public OutGain[] newArray(int size) {
            return new OutGain[size];
        }
    };

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setOutGain(int outGain) {
        OutGain = outGain;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public int getOutGain() {
        return OutGain;
    }

    private int equipmentID;

    private int OutGain;

    public OutGain(){}

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
        dest.writeByteArray(content);
        dest.writeInt(equipmentID);
        dest.writeInt(OutGain);
    }
}
