package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_StationState implements Parcelable{
    private byte[] content;

    public Query_StationState(){

    }

    protected Query_StationState(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_StationState> CREATOR = new Creator<Query_StationState>() {
        @Override
        public Query_StationState createFromParcel(Parcel in) {
            return new Query_StationState(in);
        }

        @Override
        public Query_StationState[] newArray(int size) {
            return new Query_StationState[size];
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
