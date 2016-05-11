package com.hust.radiofeeler.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class RequestNetworkAgain implements Parcelable{
    private byte[] content;

    public RequestNetworkAgain(){

    }
    protected RequestNetworkAgain(Parcel in) {
        content = in.createByteArray();
    }

    public static final Creator<RequestNetworkAgain> CREATOR = new Creator<RequestNetworkAgain>() {
        @Override
        public RequestNetworkAgain createFromParcel(Parcel in) {
            return new RequestNetworkAgain(in);
        }

        @Override
        public RequestNetworkAgain[] newArray(int size) {
            return new RequestNetworkAgain[size];
        }
    };

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
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
