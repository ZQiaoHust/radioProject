package com.example.administrator.testsliding.bean2server;

import android.os.Parcel;
import android.os.Parcelable;

/**接收通道增益表的显示
 * Created by Administrator on 2015/12/16.
 */

public class ModifyIngainView implements Parcelable{
    private String FPGANum;//硬件型号
    private String section;//工作的频率范围
    private String[] value;//修正值
    private byte[] content;//直接用于下发给FPGA的值

    public ModifyIngainView(){

    }


    protected ModifyIngainView(Parcel in) {
        FPGANum = in.readString();
        section = in.readString();
        value = in.createStringArray();
        content = in.createByteArray();
    }

    public static final Creator<ModifyIngainView> CREATOR = new Creator<ModifyIngainView>() {
        @Override
        public ModifyIngainView createFromParcel(Parcel in) {
            return new ModifyIngainView(in);
        }

        @Override
        public ModifyIngainView[] newArray(int size) {
            return new ModifyIngainView[size];
        }
    };

    public String getFPGANum() {
        return FPGANum;
    }

    public String getSection() {
        return section;
    }

    public String[] getValue() {
        return value;
    }

    public void setFPGANum(String FPGANum) {
        this.FPGANum = FPGANum;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(FPGANum);
        dest.writeString(section);
        dest.writeStringArray(value);
        dest.writeByteArray(content);
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {

        return content;
    }

}
