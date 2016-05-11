package com.hust.radiofeeler.Bean;

/**
 * Created by jinaghao on 15/11/23.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 扫频接收频率范围设置数据帧的数据域
 * aSweepMode 扫频模式
 * aSendMode 功率谱数据上传模式
 * aTotalOfBands 多频段扫频模式的频段总数
 * aBandNumber 多频段扫频模式的频段序号
 * startFrequence 起止频率
 * endFrequence 终止频率
 *  gate 功率谱数据变化的判定门限
 * aSelect 文件上传的抽取倍率
 * @return
 */
public class SweepRange implements Parcelable{
private byte packetHead;
    private  int aSweepMode;
    private  int equipmentId;
    private  int aSendMode;
    private int aTotalOfBands;
    private int aBandNumber;
    private double startFrequence;
    private double endFrequence;
    private int gate;
    private int aSelect;
    private byte[] content;

    public SweepRange(){

    }

    protected SweepRange(Parcel in) {
        packetHead = in.readByte();
        aSweepMode = in.readInt();
        equipmentId = in.readInt();
        aSendMode = in.readInt();
        aTotalOfBands = in.readInt();
        aBandNumber = in.readInt();
        startFrequence = in.readDouble();
        endFrequence = in.readDouble();
        gate = in.readInt();
        aSelect = in.readInt();
        content = in.createByteArray();
    }

    public static final Creator<SweepRange> CREATOR = new Creator<SweepRange>() {
        @Override
        public SweepRange createFromParcel(Parcel in) {
            return new SweepRange(in);
        }

        @Override
        public SweepRange[] newArray(int size) {
            return new SweepRange[size];
        }
    };

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setaSweepMode(int aSweepMode) {
        this.aSweepMode = aSweepMode;
    }

    public void setaSendMode(int aSendMode) {
        this.aSendMode = aSendMode;
    }

    public void setaTotalOfBands(int aTotalOfBands) {
        this.aTotalOfBands = aTotalOfBands;
    }

    public void setaBandNumber(int aBandNumber) {
        this.aBandNumber = aBandNumber;
    }

    public void setStartFrequence(double startFrequence) {
        this.startFrequence = startFrequence;
    }

    public void setEndFrequence(double endFrequence) {
        this.endFrequence = endFrequence;
    }

    public void setGate(int gate) {
        this.gate = gate;
    }

    public void setaSelect(int aSelect) {
        this.aSelect = aSelect;
    }

    public int getaSweepMode() {

        return aSweepMode;
    }

    public int getaSendMode() {
        return aSendMode;
    }

    public int getaTotalOfBands() {
        return aTotalOfBands;
    }

    public int getaBandNumber() {
        return aBandNumber;
    }

    public double getStartFrequence() {
        return startFrequence;
    }

    public double getEndFrequence() {
        return endFrequence;
    }

    public int getGate() {
        return gate;
    }

    public int getaSelect() {
        return aSelect;
    }

    public byte getPacketHead() {
        return packetHead;
    }

    public void setPacketHead(byte packetHead) {
        this.packetHead = packetHead;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(packetHead);
        dest.writeInt(aSweepMode);
        dest.writeInt(equipmentId);
        dest.writeInt(aSendMode);
        dest.writeInt(aTotalOfBands);
        dest.writeInt(aBandNumber);
        dest.writeDouble(startFrequence);
        dest.writeDouble(endFrequence);
        dest.writeInt(gate);
        dest.writeInt(aSelect);
        dest.writeByteArray(content);
    }
}
