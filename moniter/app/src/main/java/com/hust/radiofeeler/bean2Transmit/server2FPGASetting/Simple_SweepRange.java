package com.hust.radiofeeler.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_SweepRange implements Parcelable{
    private byte[] content;

    public Simple_SweepRange(){

    }

    protected Simple_SweepRange(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_SweepRange> CREATOR = new Creator<Simple_SweepRange>() {
        @Override
        public Simple_SweepRange createFromParcel(Parcel in) {
            return new Simple_SweepRange(in);
        }

        @Override
        public Simple_SweepRange[] newArray(int size) {
            return new Simple_SweepRange[size];
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
