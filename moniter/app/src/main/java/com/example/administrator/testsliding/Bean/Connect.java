package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/30.
 */
public class Connect implements Parcelable {
    int conn;
    public Connect(){}

    protected Connect(Parcel in) {
        conn = in.readInt();
    }

    public static final Creator<Connect> CREATOR = new Creator<Connect>() {
        @Override
        public Connect createFromParcel(Parcel in) {
            return new Connect(in);
        }

        @Override
        public Connect[] newArray(int size) {
            return new Connect[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(conn);
    }

    public int getConn() {
        return conn;
    }

    public void setConn(int conn) {

        this.conn = conn;
    }
}
