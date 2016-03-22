package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/30.
 */
public class Connect implements Parcelable {

    private byte packetHead;
    private int conn;
    private byte[] content;
    public Connect(){}


    protected Connect(Parcel in) {
        packetHead = in.readByte();
        conn = in.readInt();
        content = in.createByteArray();
    }

    public static final Creator<Connect> CREATOR = new Creator<Connect>() {
        @Override
        public Connect createFromParcel(Parcel in) {
            return new Connect(in);
        }

        @Override
        public Connect[] newArray(int size) {
            return new Connect[size];
        }
    };

    public byte getPacketHead() {
        return packetHead;
    }

    public void setPacketHead(byte packetHead) {
        this.packetHead = packetHead;
    }


    public int getConn() {
        return conn;
    }

    public void setConn(int conn) {

        this.conn = conn;
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
        dest.writeInt(conn);
        dest.writeByteArray(content);
    }
}
