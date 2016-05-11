package com.hust.radiofeeler.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_InGain implements Parcelable{
    private byte[] content;

    public Simple_InGain(){

    }

    protected Simple_InGain(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_InGain> CREATOR = new Creator<Simple_InGain>() {
        @Override
        public Simple_InGain createFromParcel(Parcel in) {
            return new Simple_InGain(in);
        }

        @Override
        public Simple_InGain[] newArray(int size) {
            return new Simple_InGain[size];
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
