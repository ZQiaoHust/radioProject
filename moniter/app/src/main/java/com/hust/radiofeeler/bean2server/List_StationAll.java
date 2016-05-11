package com.hust.radiofeeler.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/24.
 */
public class List_StationAll implements Parcelable{
    private int num;//序号
    private String ASICII;
    private String IDcard;
    private String longtitudeStyle;//东西经
    private float longitude;
    private String latitudeStyle;//南北纬
    private float latitude;//纬度
    private int height;
    private String section;
    private float maxPower;
    private float aBand;
    private String modem;
    private float modemPara;
    private String work;
    private int ruleRadius;
    private float liveness;

    public List_StationAll(int num
            , String ASICII
            , String IDcard
            , String longtitudeStyle
            , float longitude
            , String latitudeStyle
            , float latitude
            , int height
            , String section
            , float maxPower
            , float aBand
            , String modem
            , float modemPara
            , String work
            , int ruleRadius
            , float liveness){
        this.num=num;
        this.ASICII=ASICII;
        this.IDcard=IDcard;
        this.longtitudeStyle=longtitudeStyle;
        this.longitude=longitude;
        this.latitudeStyle=latitudeStyle;
        this.latitude=latitude;
        this.height=height;
        this.section=section;
        this.maxPower=maxPower;
        this.aBand=aBand;
        this.modem=modem;
        this.modemPara=modemPara;
        this.work=work;
        this.ruleRadius=ruleRadius;
        this.liveness=liveness;

    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setASICII(String ASICII) {
        this.ASICII = ASICII;
    }

    public void setIDcard(String IDcard) {
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

    public void setSection(String section) {
        this.section = section;
    }

    public void setMaxPower(float maxPower) {
        this.maxPower = maxPower;
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

    public void setRuleRadius(int ruleRadius) {
        this.ruleRadius = ruleRadius;
    }

    public void setLiveness(float liveness) {
        this.liveness = liveness;
    }

    public int getNum() {

        return num;
    }

    public String getASICII() {
        return ASICII;
    }

    public String getIDcard() {
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

    public String getSection() {
        return section;
    }

    public float getMaxPower() {
        return maxPower;
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

    public int getRuleRadius() {
        return ruleRadius;
    }

    public float getLiveness() {
        return liveness;
    }




    protected List_StationAll(Parcel in) {
        num = in.readInt();
        ASICII = in.readString();
        IDcard = in.readString();
        longtitudeStyle = in.readString();
        longitude = in.readFloat();
        latitudeStyle = in.readString();
        latitude = in.readFloat();
        height = in.readInt();
        section = in.readString();
        maxPower = in.readFloat();
        aBand = in.readFloat();
        modem = in.readString();
        modemPara = in.readFloat();
        work = in.readString();
        ruleRadius = in.readInt();
        liveness = in.readFloat();
    }

    public static final Creator<List_StationAll> CREATOR = new Creator<List_StationAll>() {
        @Override
        public List_StationAll createFromParcel(Parcel in) {
            return new List_StationAll(in);
        }

        @Override
        public List_StationAll[] newArray(int size) {
            return new List_StationAll[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num);
        dest.writeString(ASICII);
        dest.writeString(IDcard);
        dest.writeString(longtitudeStyle);
        dest.writeFloat(longitude);
        dest.writeString(latitudeStyle);
        dest.writeFloat(latitude);
        dest.writeInt(height);
        dest.writeString(section);
        dest.writeFloat(maxPower);
        dest.writeFloat(aBand);
        dest.writeString(modem);
        dest.writeFloat(modemPara);
        dest.writeString(work);
        dest.writeInt(ruleRadius);
        dest.writeFloat(liveness);
    }
}
