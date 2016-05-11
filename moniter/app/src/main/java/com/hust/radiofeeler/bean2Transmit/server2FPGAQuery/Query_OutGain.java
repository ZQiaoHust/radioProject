package com.hust.radiofeeler.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_OutGain implements Parcelable{
    private byte[] content;

    public Query_OutGain(){

    }

    protected Query_OutGain(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_OutGain> CREATOR = new Creator<Query_OutGain>() {
        @Override
        public Query_OutGain createFromParcel(Parcel in) {
            return new Query_OutGain(in);
        }

        @Override
        public Query_OutGain[] newArray(int size) {
            return new Query_OutGain[size];
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
