package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_InGain implements Parcelable{
    private byte[] content;

    public Query_InGain(){

    }

    protected Query_InGain(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_InGain> CREATOR = new Creator<Query_InGain>() {
        @Override
        public Query_InGain createFromParcel(Parcel in) {
            return new Query_InGain(in);
        }

        @Override
        public Query_InGain[] newArray(int size) {
            return new Query_InGain[size];
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
