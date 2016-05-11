package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/22.
 */
public class MapRadioResult implements Parcelable{
    private int centralFreq;
    private int  band;
    private int freshNum;//刷新次数
    private int  freshTime;//刷新间隔
    private int Nx;
    private int Ny;
    private float dieta;//分辨率
    private ArrayList<MapRadioPointInfo> mapRadioPointInfoList;//每一时刻的信息

    public MapRadioResult(){

    }


    protected MapRadioResult(Parcel in) {
        centralFreq = in.readInt();
        band = in.readInt();
        freshNum = in.readInt();
        freshTime = in.readInt();
        Nx = in.readInt();
        Ny = in.readInt();
        dieta = in.readFloat();
        mapRadioPointInfoList = in.createTypedArrayList(MapRadioPointInfo.CREATOR);
    }

    public static final Creator<MapRadioResult> CREATOR = new Creator<MapRadioResult>() {
        @Override
        public MapRadioResult createFromParcel(Parcel in) {
            return new MapRadioResult(in);
        }

        @Override
        public MapRadioResult[] newArray(int size) {
            return new MapRadioResult[size];
        }
    };

    public int getCentralFreq() {
        return centralFreq;
    }

    public int getBand() {
        return band;
    }

    public int getFreshNum() {
        return freshNum;
    }

    public int getFreshTime() {
        return freshTime;
    }

    public int getNx() {
        return Nx;
    }

    public int getNy() {
        return Ny;
    }

    public double getDieta() {
        return dieta;
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

    public void setFreshNum(int freshNum) {
        this.freshNum = freshNum;
    }

    public void setFreshTime(int freshTime) {
        this.freshTime = freshTime;
    }

    public void setNx(int nx) {
        Nx = nx;
    }

    public void setNy(int ny) {
        Ny = ny;
    }

    public void setDieta(float dieta) {
        this.dieta = dieta;
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
        dest.writeInt(freshNum);
        dest.writeInt(freshTime);
        dest.writeInt(Nx);
        dest.writeInt(Ny);
        dest.writeFloat(dieta);
        dest.writeTypedList(mapRadioPointInfoList);
    }
}
