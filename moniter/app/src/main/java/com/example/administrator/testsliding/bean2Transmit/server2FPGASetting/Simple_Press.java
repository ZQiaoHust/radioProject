package com.example.administrator.testsliding.bean2Transmit.server2FPGASetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_Press implements Parcelable{
    private byte[] content;

    public Simple_Press(){

    }

    protected Simple_Press(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Simple_Press> CREATOR = new Creator<Simple_Press>() {
        @Override
        public Simple_Press createFromParcel(Parcel in) {
            return new Simple_Press(in);
        }

        @Override
        public Simple_Press[] newArray(int size) {
            return new Simple_Press[size];
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
