package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetworkReply implements Parcelable{
    private byte Isagreen;


    public RequstNetworkReply(){

    }
    protected RequstNetworkReply(Parcel in) {
        Isagreen = in.readByte();
    }
    public static final Creator<RequstNetworkReply> CREATOR = new Creator<RequstNetworkReply>() {
        @Override
        public RequstNetworkReply createFromParcel(Parcel in) {
            return new RequstNetworkReply(in);
        }

        @Override
        public RequstNetworkReply[] newArray(int size) {
            return new RequstNetworkReply[size];
        }
    };


    public void setIsagreen(byte isagreen) {
        Isagreen = isagreen;
    }

    public byte getIsagreen() {

        return Isagreen;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(Isagreen);

    }
}
