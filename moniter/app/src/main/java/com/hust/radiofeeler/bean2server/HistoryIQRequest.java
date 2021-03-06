package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/30.
 */
public class HistoryIQRequest implements Parcelable {
    private int eqiupmentID;
    private int IDcard;
    private byte[] startTime;
    private byte[] endTime;

    public HistoryIQRequest(){

    }
    protected HistoryIQRequest(Parcel in) {
        eqiupmentID = in.readInt();
        IDcard = in.readInt();
        startTime = in.createByteArray();
        endTime = in.createByteArray();
    }

    public static final Creator<HistoryIQRequest> CREATOR = new Creator<HistoryIQRequest>() {
        @Override
        public HistoryIQRequest createFromParcel(Parcel in) {
            return new HistoryIQRequest(in);
        }

        @Override
        public HistoryIQRequest[] newArray(int size) {
            return new HistoryIQRequest[size];
        }
    };

    public void setEqiupmentID(int eqiupmentID) {
        this.eqiupmentID = eqiupmentID;
    }

    public void setIDcard(int IDcard) {
        this.IDcard = IDcard;
    }

    public void setStartTime(byte[] startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(byte[] endTime) {
        this.endTime = endTime;
    }

    public byte[] getEndTime() {
        return endTime;
    }

    public int getEqiupmentID() {
        return eqiupmentID;
    }

    public int getIDcard() {
        return IDcard;
    }

    public byte[] getStartTime() {
        return startTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eqiupmentID);
        dest.writeInt(IDcard);
        dest.writeByteArray(startTime);
        dest.writeByteArray(endTime);
    }
}
