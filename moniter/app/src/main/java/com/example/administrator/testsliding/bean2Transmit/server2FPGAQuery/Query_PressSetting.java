package com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_PressSetting implements Parcelable{
    private byte[] content;

    public Query_PressSetting(){

    }

    protected Query_PressSetting(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_PressSetting> CREATOR = new Creator<Query_PressSetting>() {
        @Override
        public Query_PressSetting createFromParcel(Parcel in) {
            return new Query_PressSetting(in);
        }

        @Override
        public Query_PressSetting[] newArray(int size) {
            return new Query_PressSetting[size];
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
