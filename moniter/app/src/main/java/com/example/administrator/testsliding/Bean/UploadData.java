package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/29.
 */
public class UploadData implements Parcelable {
    int func;

    public UploadData(){}

    public int getFunc() {
        return func;
    }

    public void setFunc(int func) {

        this.func = func;
    }

    protected UploadData(Parcel in) {
        func = in.readInt();
    }

    public static final Creator<UploadData> CREATOR = new Creator<UploadData>() {
        @Override
        public UploadData createFromParcel(Parcel in) {
            return new UploadData(in);
        }

        @Override
        public UploadData[] newArray(int size) {
            return new UploadData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(func);
    }
}
