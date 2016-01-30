package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_FixCentralFreq implements Parcelable{
    private byte[] content;

    public Query_FixCentralFreq(){

    }

    protected Query_FixCentralFreq(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_FixCentralFreq> CREATOR = new Creator<Query_FixCentralFreq>() {
        @Override
        public Query_FixCentralFreq createFromParcel(Parcel in) {
            return new Query_FixCentralFreq(in);
        }

        @Override
        public Query_FixCentralFreq[] newArray(int size) {
            return new Query_FixCentralFreq[size];
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
