package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_Connect implements Parcelable{
    private byte[] content;

    public Query_Connect(){

    }

    protected Query_Connect(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_Connect> CREATOR = new Creator<Query_Connect>() {
        @Override
        public Query_Connect createFromParcel(Parcel in) {
            return new Query_Connect(in);
        }

        @Override
        public Query_Connect[] newArray(int size) {
            return new Query_Connect[size];
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
