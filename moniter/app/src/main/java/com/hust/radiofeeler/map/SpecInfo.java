package com.hust.radiofeeler.map;

/**
 * Created by Administrator on 2015/11/12.
 */

/**
 * Created by Administrator on 2015/11/7.
 */
public class SpecInfo {

    /**
     * 经度
     */
    private double latitude;
    /**
     * 纬度
     */
    private double longitude;
    /**
     * 功率谱值
     */
    private int spectrum;




    public SpecInfo()
    {
    }
    public SpecInfo(double latitude, double longitude, int spectrum)
    {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.spectrum = spectrum;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getSpectrum()
    {
        return spectrum;
    }

    public void setspectrum(int spectrum)
    {
        this.spectrum = spectrum;
    }

}
