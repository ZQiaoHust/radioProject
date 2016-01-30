package com.example.administrator.testsliding.mina2FPGA.Encode;

/**
 * Created by Administrator on 2015/11/14.
 */
public class Send_ServiceRadio {

    public int equipmentID;
    public int startFrequency;
    public int endFrequency;

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setStartFrequency(int startFrequency) {
        this.startFrequency = startFrequency;
    }

    public void setEndFrequency(int endFrequency) {
        this.endFrequency = endFrequency;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public int getStartFrequency() {
        return startFrequency;
    }

    public int getEndFrequency() {
        return endFrequency;
    }
}
