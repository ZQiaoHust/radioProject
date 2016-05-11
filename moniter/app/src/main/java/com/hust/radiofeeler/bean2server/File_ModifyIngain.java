package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_ModifyIngain implements Parcelable {
    private byte[] fileContent;
    public File_ModifyIngain(){

    }

    protected File_ModifyIngain(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_ModifyIngain> CREATOR = new Creator<File_ModifyIngain>() {
        @Override
        public File_ModifyIngain createFromParcel(Parcel in) {
            return new File_ModifyIngain(in);
        }

        @Override
        public File_ModifyIngain[] newArray(int size) {
            return new File_ModifyIngain[size];
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
