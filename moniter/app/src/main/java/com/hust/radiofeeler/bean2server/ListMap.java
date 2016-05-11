package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/19.
 */
public class ListMap implements Parcelable {
    private String Num;
    private String section;
    private String sectionAttributes;
    public ListMap(String Num,String section,String sectionAttributes){
        this.Num=Num;
        this.section=section;
        this.sectionAttributes=sectionAttributes;

    }

    protected ListMap(Parcel in) {
        Num = in.readString();
        section = in.readString();
        sectionAttributes = in.readString();
    }

    public static final Creator<ListMap> CREATOR = new Creator<ListMap>() {
        @Override
        public ListMap createFromParcel(Parcel in) {
            return new ListMap(in);
        }

        @Override
        public ListMap[] newArray(int size) {
            return new ListMap[size];
        }
    };

    public String getNum() {
        return Num;
    }

    public String getSection() {
        return section;
    }

    public String getSectionAttributes() {
        return sectionAttributes;
    }

    public void setNum(String num) {
        Num = num;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSectionAttributes(String sectionAttributes) {
        this.sectionAttributes = sectionAttributes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Num);
        dest.writeString(section);
        dest.writeString(sectionAttributes);
    }
}
