package com.hust.radiofeeler.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_Threshold implements Parcelable{
    private byte[] content;

    public Query_Threshold(){

    }

    protected Query_Threshold(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_Threshold> CREATOR = new Creator<Query_Threshold>() {
        @Override
        public Query_Threshold createFromParcel(Parcel in) {
            return new Query_Threshold(in);
        }

        @Override
        public Query_Threshold[] newArray(int size) {
            return new Query_Threshold[size];
        }
    };

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {

        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(content);
    }
}
