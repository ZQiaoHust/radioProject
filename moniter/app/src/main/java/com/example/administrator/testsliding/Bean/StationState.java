package com.example.administrator.testsliding.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinaghao on 15/11/25.
 */
public class StationState  implements Parcelable{
   private int equipmentID;
    private  int onNet;
    private  int model;
    private  int eastORwest;
    private  int northORsouth;
    private  double longtitude;
    private  double latitude;
    private int isAboveHrizon;
    private  int atitude;
    public StationState (){}


    protected StationState(Parcel in) {
        equipmentID = in.readInt();
        onNet = in.readInt();
        model = in.readInt();
        eastORwest = in.readInt();
        northORsouth = in.readInt();
        longtitude = in.readDouble();
        latitude = in.readDouble();
        isAboveHrizon = in.readInt();
        atitude = in.readInt();
    }

    public static final Creator<StationState> CREATOR = new Creator<StationState>() {
        @Override
        public StationState createFromParcel(Parcel in) {
            return new StationState(in);
        }

        @Override
        public StationState[] newArray(int size) {
            return new StationState[size];
        }
    };

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }
    public void setOnNet(int onNet) {
        this.onNet = onNet;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setEastORwest(int eastORwest) {
        this.eastORwest = eastORwest;
    }

    public void setNorthORsouth(int northORsouth) {
        this.northORsouth = northORsouth;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setIsAboveHrizon(int isAboveHrizon) {
        this.isAboveHrizon = isAboveHrizon;
    }

    public void setAtitude(int atitude) {
        this.atitude = atitude;
    }

    public int getOnNet() {
        return onNet;
    }

    public int getModel() {
        return model;
    }

    public int getEastORwest() {
        return eastORwest;
    }

    public int getNorthORsouth() {
        return northORsouth;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getIsAboveHrizon() {
        return isAboveHrizon;
    }

    public int getAtitude() {
        return atitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(equipmentID);
        dest.writeInt(onNet);
        dest.writeInt(model);
        dest.writeInt(eastORwest);
        dest.writeInt(northORsouth);
        dest.writeDouble(longtitude);
        dest.writeDouble(latitude);
        dest.writeInt(isAboveHrizon);
        dest.writeInt(atitude);
    }
}
