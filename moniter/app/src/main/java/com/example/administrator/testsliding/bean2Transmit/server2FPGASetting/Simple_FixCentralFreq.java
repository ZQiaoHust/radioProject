package com.example.administrator.testsliding.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_FixCentralFreq implements Parcelable{
    private byte[] content;

    public Simple_FixCentralFreq(){

    }

    protected Simple_FixCentralFreq(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_FixCentralFreq> CREATOR = new Creator<Simple_FixCentralFreq>() {
        @Override
        public Simple_FixCentralFreq createFromParcel(Parcel in) {
            return new Simple_FixCentralFreq(in);
        }

        @Override
        public Simple_FixCentralFreq[] newArray(int size) {
            return new Simple_FixCentralFreq[size];
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
