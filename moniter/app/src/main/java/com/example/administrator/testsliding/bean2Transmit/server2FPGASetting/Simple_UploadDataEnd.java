package com.example.administrator.testsliding.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_UploadDataEnd implements Parcelable{
    private byte[] content;

    public Simple_UploadDataEnd(){

    }

    protected Simple_UploadDataEnd(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_UploadDataEnd> CREATOR = new Creator<Simple_UploadDataEnd>() {
        @Override
        public Simple_UploadDataEnd createFromParcel(Parcel in) {
            return new Simple_UploadDataEnd(in);
        }

        @Override
        public Simple_UploadDataEnd[] newArray(int size) {
            return new Simple_UploadDataEnd[size];
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
