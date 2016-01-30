package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_SweepRange implements Parcelable{
    private byte[] content;

    public Query_SweepRange(){

    }

    protected Query_SweepRange(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_SweepRange> CREATOR = new Creator<Query_SweepRange>() {
        @Override
        public Query_SweepRange createFromParcel(Parcel in) {
            return new Query_SweepRange(in);
        }

        @Override
        public Query_SweepRange[] newArray(int size) {
            return new Query_SweepRange[size];
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
