package com.hust.radiofeeler.bean2Transmit.server2FPGAQuery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_Press implements Parcelable{
    private byte[] content;

    public Query_Press(){

    }

    protected Query_Press(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<Query_Press> CREATOR = new Creator<Query_Press>() {
        @Override
        public Query_Press createFromParcel(Parcel in) {
            return new Query_Press(in);
        }

        @Override
        public Query_Press[] newArray(int size) {
            return new Query_Press[size];
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
