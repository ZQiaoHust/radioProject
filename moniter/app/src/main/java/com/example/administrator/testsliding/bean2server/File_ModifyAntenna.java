package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_ModifyAntenna implements Parcelable {
    private byte[] fileContent;
    public File_ModifyAntenna(){

    }

    protected File_ModifyAntenna(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_ModifyAntenna> CREATOR = new Creator<File_ModifyAntenna>() {
        @Override
        public File_ModifyAntenna createFromParcel(Parcel in) {
            return new File_ModifyAntenna(in);
        }

        @Override
        public File_ModifyAntenna[] newArray(int size) {
            return new File_ModifyAntenna[size];
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
