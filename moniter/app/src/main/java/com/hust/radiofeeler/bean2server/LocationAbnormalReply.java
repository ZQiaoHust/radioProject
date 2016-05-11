package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**异常频点定位回应
 * Created by Administrator on 2015/11/28.
 */
public class LocationAbnormalReply implements Parcelable{

    private double  abFreq;
    private float aBand;//带宽
    private String modem;//调制方式
    private float modemPara;//调制参数
    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;
    private float equalPower;//等效发射功率
    private float rPara;//损耗指数
    private float liveness;//活跃度(时间占用度)
    private String work;//业务属性
    private int isLegal;//是否合法
    private String organizer;//归属单位

    public LocationAbnormalReply(){

    }

    protected LocationAbnormalReply(Parcel in) {
        abFreq = in.readDouble();
        aBand = in.readFloat();
        modem = in.readString();
        modemPara = in.readFloat();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
        equalPower = in.readFloat();
        rPara = in.readFloat();
        liveness = in.readFloat();
        work = in.readString();
        isLegal = in.readInt();
        organizer = in.readString();
    }

    public static final Creator<LocationAbnormalReply> CREATOR = new Creator<LocationAbnormalReply>() {
        @Override
        public LocationAbnormalReply createFromParcel(Parcel in) {
            return new LocationAbnormalReply(in);
        }

        @Override
        public LocationAbnormalReply[] newArray(int size) {
            return new LocationAbnormalReply[size];
        }
    };

    public double getAbFreq() {
        return abFreq;
    }

    public float getaBand() {
        return aBand;
    }

    public String getModem() {
        return modem;
    }

    public float getModemPara() {
        return modemPara;
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

    public float getLiveness() {
        return liveness;
    }

    public String getWork() {
        return work;
    }

    public int getIsLegal() {
        return isLegal;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setAbFreq(double abFreq) {
        this.abFreq = abFreq;
    }

    public void setaBand(float aBand) {
        this.aBand = aBand;
    }

    public void setModem(String modem) {
        this.modem = modem;
    }

    public void setModemPara(float modemPara) {
        this.modemPara = modemPara;
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

    public void setLiveness(float liveness) {
        this.liveness = liveness;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setIsLegal(int isLegal) {
        this.isLegal = isLegal;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(abFreq);
        dest.writeFloat(aBand);
        dest.writeString(modem);
        dest.writeFloat(modemPara);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
        dest.writeFloat(equalPower);
        dest.writeFloat(rPara);
        dest.writeFloat(liveness);
        dest.writeString(work);
        dest.writeInt(isLegal);
        dest.writeString(organizer);
    }
}
