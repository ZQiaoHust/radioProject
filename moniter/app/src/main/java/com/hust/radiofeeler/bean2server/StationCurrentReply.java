package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/27.
 */
public class StationCurrentReply implements Parcelable {
    private  String ASICIIId;
    private  int IDcard;
    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;
    private double centralFreq;
    private float equalPower;//等效发射功率
    private float rPara;//损耗指数
    private float aBand;//带宽
    private String modem;//调制方式
    private float modemPara;//调制参数
    private String work;//规定业务属性
    private float liveness;//活跃度
    private String  rule;//是否合法

    public  StationCurrentReply(){

    }

    protected StationCurrentReply(Parcel in) {
        ASICIIId = in.readString();
        IDcard = in.readInt();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
        centralFreq = in.readDouble();
        equalPower = in.readFloat();
        rPara = in.readFloat();
        aBand = in.readFloat();
        modem = in.readString();
        modemPara = in.readFloat();
        work = in.readString();
        liveness = in.readFloat();
        rule = in.readString();
    }

    public static final Creator<StationCurrentReply> CREATOR = new Creator<StationCurrentReply>() {
        @Override
        public StationCurrentReply createFromParcel(Parcel in) {
            return new StationCurrentReply(in);
        }

        @Override
        public StationCurrentReply[] newArray(int size) {
            return new StationCurrentReply[size];
        }
    };

    public float getrPara() {
        return rPara;
    }
    public void setrPara(float rPara) {
        this.rPara = rPara;
    }
    public String getASICIIId() {
        return ASICIIId;
    }

    public int getIDcard() {
        return IDcard;
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

    public double getCentralFreq() {
        return centralFreq;
    }

    public float getEqualPower() {
        return equalPower;
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

    public String getWork() {
        return work;
    }

    public float getLiveness() {
        return liveness;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setASICIIId(String ASICIIId) {
        this.ASICIIId = ASICIIId;
    }

    public void setIDcard(int IDcard) {
        this.IDcard = IDcard;
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

    public void setCentralFreq(double centralFreq) {
        this.centralFreq = centralFreq;
    }

    public void setEqualPower(float equalPower) {
        this.equalPower = equalPower;
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

    public void setWork(String work) {
        this.work = work;
    }

    public void setLiveness(float liveness) {
        this.liveness = liveness;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ASICIIId);
        dest.writeInt(IDcard);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
        dest.writeDouble(centralFreq);
        dest.writeFloat(equalPower);
        dest.writeFloat(rPara);
        dest.writeFloat(aBand);
        dest.writeString(modem);
        dest.writeFloat(modemPara);
        dest.writeString(work);
        dest.writeFloat(liveness);
        dest.writeString(rule);
    }
}
