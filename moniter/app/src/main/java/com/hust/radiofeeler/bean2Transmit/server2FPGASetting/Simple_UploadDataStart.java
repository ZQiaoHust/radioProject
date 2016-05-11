package com.hust.radiofeeler.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_UploadDataStart implements Parcelable{
    private byte[] content;

    public Simple_UploadDataStart(){

    }

    protected Simple_UploadDataStart(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_UploadDataStart> CREATOR = new Creator<Simple_UploadDataStart>() {
        @Override
        public Simple_UploadDataStart createFromParcel(Parcel in) {
            return new Simple_UploadDataStart(in);
        }

        @Override
        public Simple_UploadDataStart[] newArray(int size) {
            return new Simple_UploadDataStart[size];
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
