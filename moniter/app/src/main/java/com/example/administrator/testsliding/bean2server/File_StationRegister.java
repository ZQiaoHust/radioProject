package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_StationRegister implements Parcelable {
    private byte[] fileContent;
    public File_StationRegister(){

    }

    protected File_StationRegister(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_StationRegister> CREATOR = new Creator<File_StationRegister>() {
        @Override
        public File_StationRegister createFromParcel(Parcel in) {
            return new File_StationRegister(in);
        }

        @Override
        public File_StationRegister[] newArray(int size) {
            return new File_StationRegister[size];
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
