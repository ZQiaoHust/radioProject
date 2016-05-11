package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_ServiceRadio  implements Parcelable {
    private byte[] fileContent;
    public File_ServiceRadio(){

    }

    protected File_ServiceRadio(Parcel in) {
        fileContent = in.createByteArray();
    }

    public static final Creator<File_ServiceRadio> CREATOR = new Creator<File_ServiceRadio>() {
        @Override
        public File_ServiceRadio createFromParcel(Parcel in) {
            return new File_ServiceRadio(in);
        }

        @Override
        public File_ServiceRadio[] newArray(int size) {
            return new File_ServiceRadio[size];
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
