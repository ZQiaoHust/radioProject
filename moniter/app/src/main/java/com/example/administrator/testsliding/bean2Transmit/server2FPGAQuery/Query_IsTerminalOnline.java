package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_IsTerminalOnline implements Parcelable{
    private byte[] content;

    public Query_IsTerminalOnline(){

    }

    protected Query_IsTerminalOnline(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_IsTerminalOnline> CREATOR = new Creator<Query_IsTerminalOnline>() {
        @Override
        public Query_IsTerminalOnline createFromParcel(Parcel in) {
            return new Query_IsTerminalOnline(in);
        }

        @Override
        public Query_IsTerminalOnline[] newArray(int size) {
            return new Query_IsTerminalOnline[size];
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
