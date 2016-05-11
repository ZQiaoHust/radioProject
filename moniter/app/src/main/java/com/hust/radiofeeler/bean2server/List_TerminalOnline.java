package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**所有在网终端列表项
 * Created by Administrator on 2015/11/25.
 */
public class List_TerminalOnline implements Parcelable {

   private int num;//序号
    private int IDnum;
    private int style;
    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;

    public List_TerminalOnline(int num
            ,int IDnum
            ,int style
            ,String longtitudeStyle
            , float longitude
            ,String latitudeStyle
            ,float latitude
            ,int height){
        this.num=num;
        this.IDnum=IDnum;
        this.style=style;
        this.longtitudeStyle=longtitudeStyle;
        this.longitude=longitude;
        this.latitudeStyle=latitudeStyle;
        this.latitude=latitude;
        this.height=height;

    }

    public int getNum() {
        return num;
    }

    public int getIDnum() {
        return IDnum;
    }

    public int getStyle() {
        return style;
    }

    public String getLongtitudeStyle() {
        return longtitudeStyle;
    }

    public String getLatitudeStyle() {
        return latitudeStyle;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getHeight() {
        return height;
    }

    protected List_TerminalOnline(Parcel in) {
        num = in.readInt();
        IDnum = in.readInt();
        style = in.readInt();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
    }

    public static final Creator<List_TerminalOnline> CREATOR = new Creator<List_TerminalOnline>() {
        @Override
        public List_TerminalOnline createFromParcel(Parcel in) {
            return new List_TerminalOnline(in);
        }

        @Override
        public List_TerminalOnline[] newArray(int size) {
            return new List_TerminalOnline[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num);
        dest.writeInt(IDnum);
        dest.writeInt(style);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
    }
}
