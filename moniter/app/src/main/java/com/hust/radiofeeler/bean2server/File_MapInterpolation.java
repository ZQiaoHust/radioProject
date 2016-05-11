package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class File_MapInterpolation implements Parcelable {
    private byte[] showContent;//供表显示
    private byte[] fileContent;//存图片
    public File_MapInterpolation(){

    }

    protected File_MapInterpolation(Parcel in) {
        showContent = in.createByteArray();
        fileContent = in.createByteArray();
    }

    public static final Creator<File_MapInterpolation> CREATOR = new Creator<File_MapInterpolation>() {
        @Override
        public File_MapInterpolation createFromParcel(Parcel in) {
            return new File_MapInterpolation(in);
        }

        @Override
        public File_MapInterpolation[] newArray(int size) {
            return new File_MapInterpolation[size];
        }
    };

    public byte[] getShowContent() {
        return showContent;
    }

    public void setShowContent(byte[] showContent) {
        this.showContent = showContent;
    }


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
        dest.writeByteArray(showContent);
        dest.writeByteArray(fileContent);
    }
}
