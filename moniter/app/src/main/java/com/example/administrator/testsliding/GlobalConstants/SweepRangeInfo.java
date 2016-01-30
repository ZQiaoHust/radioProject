package com.example.administrator.testsliding.GlobalConstants;

/**
 * Created by Administrator on 2015/12/3.
 */
public class SweepRangeInfo {
    private double segStart;
    private double segEnd;
    private int startNum;
    private int endNum;

    public SweepRangeInfo(double segStart, double segEnd, int startNum, int endNum){
        this.segStart=segStart;
        this.segEnd=segEnd;
        this.startNum=startNum;
        this.endNum=endNum;

    }
    public SweepRangeInfo(){}

    public void setSegStart(double segStart) {
        this.segStart = segStart;
    }

    public void setSegEnd(double segEnd) {
        this.segEnd = segEnd;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public double getSegStart() {

        return segStart;
    }

    public double getSegEnd() {
        return segEnd;
    }

    public int getStartNum() {
        return startNum;
    }

    public int getEndNum() {
        return endNum;
    }
}
