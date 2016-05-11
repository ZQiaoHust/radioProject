package com.hust.radiofeeler.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_PressSetting implements Parcelable{
    private byte[] content;

    public Simple_PressSetting(){

    }

    protected Simple_PressSetting(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_PressSetting> CREATOR = new Creator<Simple_PressSetting>() {
        @Override
        public Simple_PressSetting createFromParcel(Parcel in) {
            return new Simple_PressSetting(in);
        }

        @Override
        public Simple_PressSetting[] newArray(int size) {
            return new Simple_PressSetting[size];
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
