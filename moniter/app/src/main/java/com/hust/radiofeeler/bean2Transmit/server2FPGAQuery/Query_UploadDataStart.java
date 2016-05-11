package com.hust.radiofeeler.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_UploadDataStart implements Parcelable{
    private byte[] content;

    public Query_UploadDataStart(){

    }

    protected Query_UploadDataStart(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_UploadDataStart> CREATOR = new Creator<Query_UploadDataStart>() {
        @Override
        public Query_UploadDataStart createFromParcel(Parcel in) {
            return new Query_UploadDataStart(in);
        }

        @Override
        public Query_UploadDataStart[] newArray(int size) {
            return new Query_UploadDataStart[size];
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
