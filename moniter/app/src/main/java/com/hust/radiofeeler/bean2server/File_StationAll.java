package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_StationAll implements Parcelable {
    private byte[] fileContent;
    public File_StationAll(){

    }

    protected File_StationAll(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_StationAll> CREATOR = new Creator<File_StationAll>() {
        @Override
        public File_StationAll createFromParcel(Parcel in) {
            return new File_StationAll(in);
        }

        @Override
        public File_StationAll[] newArray(int size) {
            return new File_StationAll[size];
        }
    };

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(fileContent);
    }
}
