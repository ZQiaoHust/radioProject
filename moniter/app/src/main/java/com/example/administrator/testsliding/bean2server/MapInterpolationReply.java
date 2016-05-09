package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**异常频点定位回应
 * Created by Administrator on 2015/11/28.
 */
public class MapInterpolationReply implements Parcelable{

    private double  abFreq;
    private float aBand;//带宽
    //中心点
    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;
    //左下角
    private String G1longtitudeStyle;//东西经
    private float G1longitude;
    private String G1latitudeStyle;//南北纬
    private float G1latitude;//纬度
    private int G1height;
    //右上角
    private String G2longtitudeStyle;//东西经
    private float G2longitude;
    private String G2latitudeStyle;//南北纬
    private float G2latitude;//纬度
    private int G2height;

    private float radius;//半径
    private float dieta;//分辨率
    private  int freNum;//图片总个数
    private  int freshtime;//刷新间隔

    public MapInterpolationReply(){

    }

    protected MapInterpolationReply(Parcel in) {
        abFreq = in.readDouble();
        aBand = in.readFloat();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
        G1longtitudeStyle = in.readString();
        G1longitude = in.readFloat();
        G1latitudeStyle = in.readString();
        G1latitude = in.readFloat();
        G1height = in.readInt();
        G2longtitudeStyle = in.readString();
        G2longitude = in.readFloat();
        G2latitudeStyle = in.readString();
        G2latitude = in.readFloat();
        G2height = in.readInt();
        radius = in.readFloat();
        dieta = in.readFloat();
        freNum = in.readInt();
        freshtime = in.readInt();
    }

    public static final Creator<MapInterpolationReply> CREATOR = new Creator<MapInterpolationReply>() {
        @Override
        public MapInterpolationReply createFromParcel(Parcel in) {
            return new MapInterpolationReply(in);
        }

        @Override
        public MapInterpolationReply[] newArray(int size) {
            return new MapInterpolationReply[size];
        }
    };

    public int getFreNum() {
        return freNum;
    }

    public void setFreNum(int freNum) {
        this.freNum = freNum;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDieta() {
        return dieta;
    }

    public void setDieta(float dieta) {
        this.dieta = dieta;
    }

    public int getFreshtime() {
        return freshtime;
    }

    public void setFreshtime(int freshtime) {
        this.freshtime = freshtime;
    }

    public double getAbFreq() {
        return abFreq;
    }

    public float getaBand() {
        return aBand;
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


    public void setAbFreq(double abFreq) {
        this.abFreq = abFreq;
    }

    public void setaBand(float aBand) {
        this.aBand = aBand;
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

    public String getG1longtitudeStyle() {
        return G1longtitudeStyle;
    }

    public void setG1longtitudeStyle(String g1longtitudeStyle) {
        G1longtitudeStyle = g1longtitudeStyle;
    }

    public float getG1longitude() {
        return G1longitude;
    }

    public void setG1longitude(float g1longitude) {
        G1longitude = g1longitude;
    }

    public String getG1latitudeStyle() {
        return G1latitudeStyle;
    }

    public void setG1latitudeStyle(String g1latitudeStyle) {
        G1latitudeStyle = g1latitudeStyle;
    }

    public float getG1latitude() {
        return G1latitude;
    }

    public void setG1latitude(float g1latitude) {
        G1latitude = g1latitude;
    }

    public int getG1height() {
        return G1height;
    }

    public void setG1height(int g1height) {
        G1height = g1height;
    }

    public String getG2longtitudeStyle() {
        return G2longtitudeStyle;
    }

    public void setG2longtitudeStyle(String g2longtitudeStyle) {
        G2longtitudeStyle = g2longtitudeStyle;
    }

    public float getG2longitude() {
        return G2longitude;
    }

    public void setG2longitude(float g2longitude) {
        G2longitude = g2longitude;
    }

    public String getG2latitudeStyle() {
        return G2latitudeStyle;
    }

    public void setG2latitudeStyle(String g2latitudeStyle) {
        G2latitudeStyle = g2latitudeStyle;
    }

    public float getG2latitude() {
        return G2latitude;
    }

    public void setG2latitude(float g2latitude) {
        G2latitude = g2latitude;
    }

    public int getG2height() {
        return G2height;
    }

    public void setG2height(int g2height) {
        G2height = g2height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(abFreq);
        dest.writeFloat(aBand);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
        dest.writeString(G1longtitudeStyle);
        dest.writeFloat(G1longitude);
        dest.writeString(G1latitudeStyle);
        dest.writeFloat(G1latitude);
        dest.writeInt(G1height);
        dest.writeString(G2longtitudeStyle);
        dest.writeFloat(G2longitude);
        dest.writeString(G2latitudeStyle);
        dest.writeFloat(G2latitude);
        dest.writeInt(G2height);
        dest.writeFloat(radius);
        dest.writeFloat(dieta);
        dest.writeInt(freNum);
        dest.writeInt(freshtime);
    }
}
