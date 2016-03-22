package com.example.administrator.testsliding.compute;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**所有编码和解码中用到的计算
 * Created by Administrator on 2015/11/25.
 */
public class ComputePara {

    /**
     * 将文本框的String转换为时间字节
     * @param str
     * @return
     */
    public byte[] Time2Bytes(String str){
        byte[] bytes=new byte[5];
        //从字符串中取出时间，分别是年，月，日，时，分,miao
        String regex="\\d*";
        List<Integer> digitList =new ArrayList<>();
        Pattern p=Pattern.compile(regex);

        Matcher m=p.matcher(str);
        while (m.find()){
            if(!"".equals(m.group())){
                digitList.add(Integer.valueOf(m.group()));
            }
        }
        if(digitList.size()==5){
            //只有到分
            bytes[0]= (byte) ((digitList.get(0)>>4)&0xff);//年高八位
            bytes[1]= (byte) (((digitList.get(0)&0x0f)<<4)+(digitList.get(1)&0x0f));//年+月
            bytes[2]= (byte) (((digitList.get(2)&0x1f)<<3)+((digitList.get(3)&0x1f)>>2));//日+时高三位
            bytes[3]= (byte) (((digitList.get(4)&0x3f)<<2)+(digitList.get(3)&0x03));//分+时低二位
        }
        else if(digitList.size()==6){
            bytes[0]= (byte) ((digitList.get(0)>>4)&0xff);//年高八位
            bytes[1]= (byte) (((digitList.get(0)&0x0f)<<4)+(digitList.get(1)&0x0f));//年+月
            bytes[2]= (byte) (((digitList.get(2)&0x1f)<<3)+((digitList.get(3)&0x1f)>>2));//日+时高三位
            bytes[3]= (byte) (((digitList.get(4)&0x3f)<<2)+(digitList.get(3)&0x03));//分+时低二位
            bytes[4]= (byte) (digitList.get(5)&0xff);
        }

        return  bytes;
    }
    public List<Integer> Time2Int(String str) {
        int[] result = new int[8];
        //从字符串中取出时间，分别是年，月，日，时，分,miao
        String regex = "\\d*";
        List<Integer> digitList = new ArrayList<>();
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group())) {
                digitList.add(Integer.valueOf(m.group()));
            }
        }
        return digitList;
    }

    //计算输入扫频范围的起点、终点所对应硬件当中的段号
    public int ComputeSegNumber(double frequence){
        int segNumber=0;
        segNumber=(int) ((frequence-70)/25+1);
        return segNumber;

    }


    //计算偏移量
    public int ComputeSegOffset(double frequence){
        int segOffset=0;
        segOffset=(int) ((frequence-70)%25*(1024.0/25.0));//50MHz带宽的计算
        return segOffset;
    }
    /**
     * 中心频率的移位存储,整数14bit,小数10bit
     * @param frequency
     * @return
     */
    public byte[] ComputeFloatTobyte(double frequency){
        byte[] com=new byte[3];
        int floatzhengshu=(int) frequency;
        int floatxiaoshu=(int) ((frequency-floatzhengshu)*1024);
        com[0]=(byte) ((floatzhengshu>>6)&0xff);//整数部分高8位（总共14bit）
        com[1]=(byte) ((((floatxiaoshu>>8)&0x03)<<6)+(floatzhengshu&0x3f));
        com[2]=(byte) (floatxiaoshu&0xff);
        return com;
    }

    /**
     * 规定的业务属性查询表
     * @param number
     * @return
     */
    public   String ChartPlan(int number){
        String str=null;
        switch(number){
            case 1:
                str="固定";
                break;
            case 2:
                str="移动";
                break;
            case 3:
                str="无线电定位";
                break;
            case 4:
                str="卫星固定";
                break;
            case 5:
                str="空间研究";
                break;
            case 6:
                str="卫星地球探测";
                break;
            case 7:
                str="射电天文";
                break;
            case 8:
                str="广播";
                break;
            case 9:
                str="移动(航空移动除外)";
                break;
            case 10:
                str="无线电导航";
                break;
            case 11:
                str="航空无线电导航";
                break;
            case 12:
                str="水上移动";
                break;
            case 13:
                str="卫星移动";
                break;
            case 14:
                str="卫星间";
                break;
            case 15:
                str="卫星无线电导航";
                break;
            case 16:
                str="业余";
                break;
            case 17:
                str="卫星气象";
                break;
            case 18:
                str="标准频率和时间信号";
                break;
            case 19:
                str="空间操作";
                break;
            case 20:
                str="航空移动";
                break;
            case 21:
                str="卫星业余";
                break;
            case 22:
                str="卫星广播";
                break;
            case 23:
                str="航空移动(OR)";
                break;
            case 24:
                str="气象辅助";
                break;
            case 25:
                str="航空移动(R)";
                break;
            case 26:
                str="水上无线电导航";
                break;
            case 27:
                str="陆地移动";
                break;
            case 28:
                str="移动(航空移动(R)除外)";
                break;
            case 29:
                str="卫星无线电测定";
                break;
            case 30:
                str="卫星航空移动(R)";
                break;
            case 31:
                str="移动(航空移动(R)除外)";
                break;
            case 32:
                str="水上移动(遇险和呼叫)";
                break;
            case 33:
                str="水上移动(使用DSC的遇险和安全呼叫)";
                break;
            case 34:
                str="未划分";
                break;
            default:
                break;
        }
        return str;

    }

    /**
     * 频率截位，保留五位小数
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @return
     */
   public String Freq2float(byte b1, byte b2, byte b3, byte b4) {
        String freq = null;
        int intNum;
        float decimal;
        float de = 0;
        intNum = ((b1& 0xff) << 12)  +( (b2  & 0xff)<< 4) + ((b3& 0xff) >> 4) ;
        for (int i = 7; i > 0; i--) {
            de += ((b4 >> i) & 0x01) * Math.pow(2, i - 12);
        }
        decimal = (float) (((b3 >> 3) & 0x1) * 0.5 + ((b3 >> 2) & 0x1) * 0.25 + ((b3 >> 1) & 0x1) * 0.125 + (b3 & 0x1) * 0.0625 + de);

        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00000");
        if((intNum + decimal)!=0) {
            freq = df.format(intNum + decimal);//截小数点后5位，返回为String
        }

        return freq;
    }

    //调制方式查询表
    public String QueryModem(byte b){
        String str=null;
        switch(b){
            case 0x00:
                str="AM调制";
                break;
            case 0x01:
                str="FM调制";
                break;
            case 0x02:
                str="DSB（双边带）调制";
                break;
            case 0x03:
                str="LSB(下边带)调制";
                break;
            case 0x04:
                str="UBS(上边带)调制";
                break;
            case 0x05:
                str="VSB(残留边带)调制";
                break;
            case 0x06:
                str="AM-FM组合调制";
                break;
            case 0x07:
                str="FMCW(调频连续波)";
                break;
            case 0x08:
                str="FMICW(调频中断连续波)";
                break;
            case 0x09:
                str="PM脉冲调制";
                break;
            case 0x50:
                str="2ASK";
                break;
            case 0x51:
                str="4ASK";
                break;
            case 0x52:
                str="2FSK";
                break;
            case 0x53:
                str="4FSK";
                break;
            case 0x54:
                str="BPSK";
                break;
            case 0x55:
                str="QPSK";
                break;


        }
        return str;
    }
    //16bit的小数转换为float
    public float BitDecimal2float(byte b1,byte b2){
        float ff=0;
        float f1=0,f2=0;
        for(int i=1;i<9;i++){
            f1+=((b1>>(8-i))&0x01)*Math.pow(2,-i);
        }
        for(int i=1;i<9;i++){
            f2+=((b2>>(8-i))&0x01)*Math.pow(2,-(i+8));
        }
        ff=f1+f2;
        return ff;

    }
    //10bit的小数转换为float
    public float TenbitDecimal2float(byte b1,byte b2){
        float ff=0;
        float f1=0,f2=0;

        f1= (float) (((b1>>7)&0x01)*0.5+((b1>>6)&0x01)*0.25);
        for(int i=1;i<9;i++){
            f2+=((b2>>(8-i))&0x01)*Math.pow(2,-(i+2));
        }
        ff=f1+f2;
        return ff;

    }

    public float[] Bytes2Power(byte[] bytes){
        float[] pow=new float[1024];
        //
        for(int i=0;i<512;i++) {
//            float f1 = (float) (((((bytes[3*i] << 1) & 0xE0) + (bytes[3*i+1] >> 3)) & 0xff) + ((bytes[3*i+1] >> 2) & 0x01) * 0.5 +
//                    ((bytes[3*i+1] >> 1) & 0x01) * 0.25 + (bytes[3*i+1] & 0x01) * 0.125);
            int f1=(((bytes[3*i]>>4)&0x0f)<<8)+(bytes[3*i+1] & 0xff);
            if (((bytes[3*i]>>7)&0x01) == 0) {
                pow[2*i] = f1/8;
            } else {
                pow[2*i] = (float) ((f1-Math.pow(2,12))/8.0);
            }

//            float f2 = (float) ((((bytes[3*i] << 6) + (bytes[2+3*i] >> 3)) & 0xff) + ((bytes[2+3*i] >> 2) & 0x01) * 0.5 +
//                    ((bytes[2+3*i] >> 1) & 0x01) * 0.25 + (bytes[2+3*i] & 0x01) * 0.125);
            int f2=((bytes[3*i]&0x0f)<<8)+(bytes[3*i+2] & 0xff);
            if (((bytes[3*i] >> 3) & 0x01) == 0) {
                pow[1+2*i] = f2/8;
            } else {
                pow[1+2*i] =(float) ((f2-Math.pow(2,12))/8.0);
            }
        }
        return  pow;
    }
}
