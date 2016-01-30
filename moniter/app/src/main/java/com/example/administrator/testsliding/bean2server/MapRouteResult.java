package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/22.
 */
public class MapRouteResult implements Parcelable{
    private int centralFreq;
    private int  band;
    private ArrayList<MapRadioPointInfo> mapRadioPointInfoList;//每一时刻的信息

    public MapRouteResult(){

    }


    protected MapRouteResult(Parcel in) {
        centralFreq = in.readInt();
        band = in.readInt();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(centralFreq);
        dest.writeInt(band);
        dest.writeTypedList(mapRadioPointInfoList);
    }
}
