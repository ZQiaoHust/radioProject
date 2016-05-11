package com.hust.radiofeeler.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_StationState implements Parcelable{
    private byte[] content;

    public Simple_StationState(){

    }

    protected Simple_StationState(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_StationState> CREATOR = new Creator<Simple_StationState>() {
        @Override
        public Simple_StationState createFromParcel(Parcel in) {
            return new Simple_StationState(in);
        }

        @Override
        public Simple_StationState[] newArray(int size) {
            return new Simple_StationState[size];
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
