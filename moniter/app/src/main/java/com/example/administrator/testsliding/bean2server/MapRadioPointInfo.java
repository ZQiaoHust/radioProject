package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapRadioPointInfo implements Parcelable {
    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;

    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;

    private float equalPower;//等效发射功率
    private float rPara;//损耗指数

    public MapRadioPointInfo() {
    }

    public MapRadioPointInfo(int month, int year, int date, int hour, String longtitudeStyle, int min, float longitude, String latitudeStyle, float latitude, int height, float equalPower, float rPara) {
        this.month = month;
        this.year = year;
        this.date = date;
        this.hour = hour;
        this.longtitudeStyle = longtitudeStyle;
        this.min = min;
        this.longitude = longitude;
        this.latitudeStyle = latitudeStyle;
        this.latitude = latitude;
        this.height = height;
        this.equalPower = equalPower;
        this.rPara = rPara;
    }

    protected MapRadioPointInfo(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        date = in.readInt();
        hour = in.readInt();
        min = in.readInt();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
        equalPower = in.readFloat();
        rPara = in.readFloat();
    }

    public static final Creator<MapRadioPointInfo> CREATOR = new Creator<MapRadioPointInfo>() {
        @Override
        public MapRadioPointInfo createFromParcel(Parcel in) {
            return new MapRadioPointInfo(in);
        }

        @Override
        public MapRadioPointInfo[] newArray(int size) {
            return new MapRadioPointInfo[size];
        }
    };

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public String getLongtitudeStyle() {
        return longtitudeStyle;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getLatitudeStyle() {
        return latitudeStyle;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getHeight() {
        return height;
    }

    public float getEqualPower() {
        return equalPower;
    }

    public float getrPara() {
        return rPara;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }



    public void setLongtitudeStyle(String longtitudeStyle) {
        this.longtitudeStyle = longtitudeStyle;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitudeStyle(String latitudeStyle) {
        this.latitudeStyle = latitudeStyle;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setEqualPower(float equalPower) {
        this.equalPower = equalPower;
    }

    public void setrPara(float rPara) {
        this.rPara = rPara;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(date);
        dest.writeInt(hour);
        dest.writeInt(min);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
        dest.writeFloat(equalPower);
        dest.writeFloat(rPara);
    }
}
