package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_TerminalOnline implements Parcelable {
    private byte[] fileContent;
    public File_TerminalOnline(){

    }

    protected File_TerminalOnline(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_TerminalOnline> CREATOR = new Creator<File_TerminalOnline>() {
        @Override
        public File_TerminalOnline createFromParcel(Parcel in) {
            return new File_TerminalOnline(in);
        }

        @Override
        public File_TerminalOnline[] newArray(int size) {
            return new File_TerminalOnline[size];
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
