package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_UploadDataEnd implements Parcelable{
    private byte[] content;

    public Query_UploadDataEnd(){

    }

    protected Query_UploadDataEnd(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_UploadDataEnd> CREATOR = new Creator<Query_UploadDataEnd>() {
        @Override
        public Query_UploadDataEnd createFromParcel(Parcel in) {
            return new Query_UploadDataEnd(in);
        }

        @Override
        public Query_UploadDataEnd[] newArray(int size) {
            return new Query_UploadDataEnd[size];
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
