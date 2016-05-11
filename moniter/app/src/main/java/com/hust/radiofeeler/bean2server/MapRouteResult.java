package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/22.
 */
public class MapRouteResult implements Parcelable{
    private int centralFreq;
    private int  band;
    //TPOA定位的结果
    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;

    private float equalPower;//等效发射功率
    private int CEPradius;//CEP半径

    private ArrayList<MapRadioPointInfo> mapRadioPointInfoList;//每一时刻的信息

    public MapRouteResult(){

    }


    protected MapRouteResult(Parcel in) {
        centralFreq = in.readInt();
        band = in.readInt();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
        equalPower = in.readFloat();
        CEPradius = in.readInt();
        mapRadioPointInfoList = in.createTypedArrayList(MapRadioPointInfo.CREATOR);
    }

    public static final Creator<MapRouteResult> CREATOR = new Creator<MapRouteResult>() {
        @Override
        public MapRouteResult createFromParcel(Parcel in) {
            return new MapRouteResult(in);
        }

        @Override
        public MapRouteResult[] newArray(int size) {
            return new MapRouteResult[size];
        }
    };

    public int getCentralFreq() {
        return centralFreq;
    }

    public int getBand() {
        return band;
    }


    public ArrayList<MapRadioPointInfo> getMapRadioPointInfoList() {
        return mapRadioPointInfoList;
    }

    public void setCentralFreq(int centralFreq) {
        this.centralFreq = centralFreq;
    }

    public void setBand(int band) {
        this.band = band;
    }



    public void setMapRadioPointInfoList(ArrayList<MapRadioPointInfo> mapRadioPointInfoList) {
        this.mapRadioPointInfoList = mapRadioPointInfoList;
    }

    public String getLongtitudeStyle() {
        return longtitudeStyle;
    }

    public void setLongtitudeStyle(String longtitudeStyle) {
        this.longtitudeStyle = longtitudeStyle;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getLatitudeStyle() {
        return latitudeStyle;
    }

    public void setLatitudeStyle(String latitudeStyle) {
        this.latitudeStyle = latitudeStyle;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getEqualPower() {
        return equalPower;
    }

    public void setEqualPower(float equalPower) {
        this.equalPower = equalPower;
    }

    public int getCEPradius() {
        return CEPradius;
    }

    public void setCEPradius(int CEPradius) {
        this.CEPradius = CEPradius;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(centralFreq);
        dest.writeInt(band);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
        dest.writeFloat(equalPower);
        dest.writeInt(CEPradius);
        dest.writeTypedList(mapRadioPointInfoList);
    }
}
