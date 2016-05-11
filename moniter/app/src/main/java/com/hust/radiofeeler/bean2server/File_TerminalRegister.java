package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_TerminalRegister implements Parcelable {
    private byte[] fileContent;
    public File_TerminalRegister(){

    }

    protected File_TerminalRegister(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_TerminalRegister> CREATOR = new Creator<File_TerminalRegister>() {
        @Override
        public File_TerminalRegister createFromParcel(Parcel in) {
            return new File_TerminalRegister(in);
        }

        @Override
        public File_TerminalRegister[] newArray(int size) {
            return new File_TerminalRegister[size];
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
