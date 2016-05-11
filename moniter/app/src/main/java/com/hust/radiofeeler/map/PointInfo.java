package com.hust.radiofeeler.map;

/**
 * Created by Administrator on 2015/11/14.
 */
public class PointInfo {
    //需要计算的参数，返回的信息
    public double latitude;
    public double longtitude;
    public double xheight;
    public double power;//功率值

    public PointInfo() {

    }
    public PointInfo(double latitude, double longtitude, double xheight, double power) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.xheight = xheight;
        this.power = power;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getXheight() {
        return xheight;
    }

    public double getPower() {
        return power;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setXheight(double xheight) {
        this.xheight = xheight;
    }

    public void setPower(double power) {
        this.power = power;
    }
}
