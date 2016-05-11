package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/19.
 */
public class FileToServerReply implements Parcelable {
    private String fileName;
    public FileToServerReply(){

    }
    protected FileToServerReply(Parcel in) {
        fileName = in.readString();
    }

    public static final Creator<FileToServerReply> CREATOR = new Creator<FileToServerReply>() {
        @Override
        public FileToServerReply createFromParcel(Parcel in) {
            return new FileToServerReply(in);
        }

        @Override
        public FileToServerReply[] newArray(int size) {
            return new FileToServerReply[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
    }
}
