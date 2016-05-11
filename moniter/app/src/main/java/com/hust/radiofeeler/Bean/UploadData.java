package com.hust.radiofeeler.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/29.
 */
public class UploadData implements Parcelable {
    int func;
    private byte packetHead;
    private byte[] content;

    public UploadData(){}

    protected UploadData(Parcel in) {
        func = in.readInt();
        packetHead = in.readByte();
        content = in.createByteArray();
    }

    public static final Creator<UploadData> CREATOR = new Creator<UploadData>() {
        @Override
        public UploadData createFromParcel(Parcel in) {
            return new UploadData(in);
        }

        @Override
        public UploadData[] newArray(int size) {
            return new UploadData[size];
        }
    };

    public int getFunc() {
        return func;
    }

    public void setFunc(int func) {

        this.func = func;
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
        dest.writeInt(func);
        dest.writeByte(packetHead);
        dest.writeByteArray(content);
    }
}
