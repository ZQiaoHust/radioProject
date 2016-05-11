package com.hust.radiofeeler.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_Connect implements Parcelable{
    private byte[] content;

    public Simple_Connect(){

    }

    protected Simple_Connect(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_Connect> CREATOR = new Creator<Simple_Connect>() {
        @Override
        public Simple_Connect createFromParcel(Parcel in) {
            return new Simple_Connect(in);
        }

        @Override
        public Simple_Connect[] newArray(int size) {
            return new Simple_Connect[size];
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
