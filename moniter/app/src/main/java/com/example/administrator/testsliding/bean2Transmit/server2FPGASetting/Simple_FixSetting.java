package com.example.administrator.testsliding.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_FixSetting implements Parcelable{
    private byte[] content;

    public Simple_FixSetting(){

    }

    protected Simple_FixSetting(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_FixSetting> CREATOR = new Creator<Simple_FixSetting>() {
        @Override
        public Simple_FixSetting createFromParcel(Parcel in) {
            return new Simple_FixSetting(in);
        }

        @Override
        public Simple_FixSetting[] newArray(int size) {
            return new Simple_FixSetting[size];
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
