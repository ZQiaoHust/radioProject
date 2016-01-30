package com.example.administrator.testsliding.packet;


/**
 * 与中心站服务请求数据帧
 * Created by Administrator on 2015/11/6.
 */
public class RequestToServicePacket {

    /**
     * 终端入网请求数据帧
     * @param content app向FPGA下发是否在网及类型数据帧后，FPGA应答帧的信息
     * @return
     */
    public byte[] RequestNetwork(byte[] content){
        byte[] data=new byte[17];
        data[0]=0x55;
        data[1]= (byte) 0xA1;
        //将content添加到data中
        System.arraycopy(content, 0, data, 3, 14);

        data[17]= (byte) 0xAA;//帧尾
        return data;
    }

    /**
     * 电磁分布态势图服务请求帧
     * @param equipmentID 设备ID
     * @param frequency 中心频率,只取整数部分
     * @param band 带宽
     * @param radius 地理半径
     * @param radio 经纬度分辨率
     * @param time 刷新间隔时间
     * @param Startyear
     * @param Startmonth
     * @param Startdate
     * @param Starthour
     * @param Startminute  精确到分
     * @return
     */
    public byte[] RequestRadioDiagram(int equipmentID,int frequency,int band,int radius,float radio,
                                      int time,int Startyear,int Startmonth,int Startdate, int Starthour,
                                      int Startminute,int endyear, int endmonth,int enddate,
                                      int endhour,int endminute){
        int radioZhengshu= (int) radio;
        int radioXiaoshu=(int) ((radio-radioZhengshu)*8);//小数部分占三位
        byte[] data=new byte[21];
        byte[] data1=new byte[4];

        data[0]=0x55;
        data[1]= (byte) 0xA2;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]= (byte) (frequency>>8&0xff);//整数部分高八位
        data[5]= (byte) (frequency&0xff);
        data[6]= (byte) band;
        data[7]= (byte) radius;
        data[8]= (byte) (radioZhengshu<<3+radioXiaoshu);
        data[9]= (byte) time;

        data1=TimetoCode(Startyear,Startmonth,Startdate,Starthour,Startminute);
        System.arraycopy(data1, 0, data, 10, 13);

        data1=TimetoCode(endyear,endmonth,enddate,endhour,endminute);
        System.arraycopy(data1, 0, data, 14, 17);
        data[20]= (byte) 0xAA;
        return data;
    }

    /**
     * 电磁路径分布服务请求数据帧
     * @param equipmentID  设备ID
     * @param requestType  本地还是中心站获取数据
     * @param dataType 历史路径分布还是实时路径分布
     * @param frequency 中心频率，只取整数
     * @param band 带宽
     * @param Startyear
     * @param Startmonth
     * @param Startdate
     * @param Starthour
     * @param Startminute  精确到分
     * @return
     */

    public byte[] RequestRouteDiagram(int equipmentID,int requestType,int dataType,int frequency,int band,
                                      int Startyear,int Startmonth,int Startdate, int Starthour,
                                      int Startminute,int endyear, int endmonth,int enddate,
                                      int endhour,int endminute){

        byte[] data=new byte[19];
        byte[] data1=new byte[4];
        byte[] data2=new byte[4];

        data[0]=0x55;
        data[1]= (byte) 0xA3;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]= (byte) (requestType>>4&0x0f+dataType&0x0f);
        data[5]= (byte) (frequency>>8&0xff);//整数部分高八位
        data[6]= (byte) (frequency&0xff);
        data[7]= (byte) band;

        data1=TimetoCode(Startyear,Startmonth,Startdate,Starthour,Startminute);
        System.arraycopy(data1, 0, data, 8, 11);

        data2=TimetoCode(endyear,endmonth,enddate,endhour,endminute);
        System.arraycopy(data2, 0, data, 12, 15);

        data[18]= (byte) 0xAA;

        return data;

    }

    /**
     * 异常频点定位服务请求帧
     * @param equipmentID 设备ID
     * @param locationWay 定位方法
     * @param frequency 中心频率
     * @param IQbandRadio IQ复信号带宽、数据率
     * @param blockNum 数据块个数
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param miu
     * @return
     */

    public byte[] RequestLocationAbnormal(int equipmentID,int locationWay,float frequency,byte IQbandRadio
                                          ,int blockNum,int year,int month,int date, int hour,
                                          int minute,int miu ){
        byte[] data=new byte[21];
        byte[] data1=new byte[4];
        byte[] data2=new byte[3];

        data[0]=0x55;
        data[1]= (byte) 0xA4;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位

        data[4]= (byte) locationWay;
        if(frequency!=0){
            data1=ComputeFloatTobyte(frequency);
            System.arraycopy(data1, 0, data, 5, 7);
        }
        data[8]= IQbandRadio;
        data[9]= (byte) blockNum;

        data1=TimetoCode(year,month,date,hour,minute);
        System.arraycopy(data1, 0, data, 10, 13);
        data[14]= (byte) (miu&0xff);

        data[20]= (byte) 0xAA;
        return data;
    }

    /**
     * 台站登记属性查询服务数据帧
     * @param equipmentID  设备ID
     * @param startFrequency 起始频率
     * @param endFrequency
     * @return
     */
    public byte[] RequestStationRegisterAttribute(int equipmentID,int startFrequency,int endFrequency){
        byte[] data=new byte[11];

        data[0]=0x55;
        data[1]= (byte) 0xA5;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]= (byte) (startFrequency>>8&0xff);//整数部分高八位
        data[5]= (byte) (startFrequency&0xff);
        data[6]= (byte) (endFrequency>>8&0xff);//整数部分高八位
        data[7]= (byte) (endFrequency&0xff);
        data[10]= (byte) 0xAA;
        return  data;

    }

    /**
     * 登记台站当前属性查询服务请求数据帧
     * @param equipmentID
     * @param IDcard 指定台站的识别码
     * @return
     */
    public byte[] RequestStationCurrentAttribute(int equipmentID,int IDcard ){
        byte[] data=new byte[10];

        data[0]=0x55;
        data[1]= (byte) 0xA6;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]= (byte) (IDcard>>16&0xff);//高八位
        data[5]= (byte) (IDcard>>8&0xff);//中八位
        data[6]= (byte) (IDcard&0xff);

        data[9]= (byte) 0xAA;
        return  data;
    }

//    /**
//     *国家无线电频率规划查询
//     * @return
//     */
//    public void  RequestWielessPlan(Send_ServiceRadio radio){
//         //radio=new Send_ServiceRadio();
//
//        byte[] data=new byte[11];
//        data[0]=0x55;
//        data[1]= (byte) 0xA7;
//        data[2]=(byte) ((radio.equipmentID >> 8) & 0xff);//设备ID号的高位
//        data[3]=(byte) (radio.equipmentID & 0xff);//设备ID号的低8位
//        data[4]= (byte) ((radio.startFrequency>>8)&0xff);//整数部分高八位
//        data[5]= (byte) (radio.startFrequency&0xff);
//        data[6]= (byte) ((radio.endFrequency>>8)&0xff);//整数部分高八位
//        data[7]= (byte) (radio.endFrequency&0xff);
//        data[10]= (byte) 0xAA;
//       // return  data;
//
//    }

    /**
     * 历史功率谱数据查询服务帧
     * @param equipmentID
     * @param specifyID 指定终端设备的ID号
     * @param Startyear
     * @param Startmonth
     * @param Startdate
     * @param Starthour
     * @param Startminute
     */
    public byte[] RequestHistorySpectrum(int equipmentID, int specifyID,int Startyear,int Startmonth,int Startdate,
                                         int Starthour, int Startminute,int endyear, int endmonth,
                                         int enddate, int endhour,int endminute){

        byte[] data=new byte[17];
        byte[] data1=new byte[4];

        data[0]=0x55;
        data[1]= (byte) 0xAB;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]=(byte) (specifyID >> 8 & 0xff);//指定ID号的高位
        data[5]=(byte) (specifyID & 0xff);//指定ID号的低8位
        data1=TimetoCode(Startyear,Startmonth,Startdate,Starthour,Startminute);
        System.arraycopy(data1, 0, data, 6, 9);

        data1=TimetoCode(endyear,endmonth,enddate,endhour,endminute);
        System.arraycopy(data1, 0, data, 10, 13);

        data[16]= (byte) 0xAA;

        return data;

    }
    /**
     * 历史IQ数据查询服务帧
     * @param equipmentID
     * @param specifyID 指定终端设备的ID号
     * @param Startyear
     * @param Startmonth
     * @param Startdate
     * @param Starthour
     * @param Startminute
     */
    public byte[] RequestHistoryIQ(int equipmentID, int specifyID,int Startyear,int Startmonth,int Startdate,
                                         int Starthour, int Startminute,int endyear, int endmonth,
                                         int enddate, int endhour,int endminute){

        byte[] data=new byte[17];
        byte[] data1=new byte[4];

        data[0]=0x55;
        data[1]= (byte) 0xAC;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]=(byte) (specifyID >> 8 & 0xff);//指定ID号的高位
        data[5]=(byte) (specifyID & 0xff);//指定ID号的低8位
        data1=TimetoCode(Startyear,Startmonth,Startdate,Starthour,Startminute);
        System.arraycopy(data1, 0, data, 6, 9);

        data1=TimetoCode(endyear,endmonth,enddate,endhour,endminute);
        System.arraycopy(data1, 0, data, 10, 13);

        data[16]= (byte) 0xAA;

        return data;

    }

    /**
     * 更改终端扫频接收参数的服务请求数据帧
     * @param equipmentID
     * @param specifyID 指定设备ID
     * @param aSweepMode 扫频模式
     * @param aSendMode 上传模式
     * @param aTotalOfBands 频段总数
     * @param aBandNumber 频段序号
     * @param startFrequence
     * @param endFrequence
     * @param gate  判定门限
     * @param aSelect 抽取倍率
     * @param recvGain 接收通道增益
     * @param gateStyle 检测门限类型
     * @param threshold 自适应门限
     * @param fixThreshold 固定门限
     * @return
     */
    public byte[] RequestSweepData(int equipmentID, int specifyID,int aSweepMode,int aSendMode,int aTotalOfBands,
                            int aBandNumber,double startFrequence,double endFrequence,int gate,int aSelect,
                            int recvGain,int gateStyle ,int threshold,int fixThreshold){
        byte[] data=new byte[24];
        int startOffset=ComputeSegOffset(startFrequence);//起点段内偏移量，偏移量最多只占10位
        int endOffset=ComputeSegOffset(endFrequence);
        int thred=Math.abs(fixThreshold);//固定门限是有符号的数
        data[0]=0x55;
        data[1]= (byte) 0xAD;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]=(byte) (specifyID >> 8 & 0xff);//指定ID号的高位
        data[5]=(byte) (specifyID & 0xff);//指定ID号的低8位

        data[6]= (byte) (aSweepMode<<2+aSendMode);     //扫频模式+上传模式
        data[7]= (byte) aTotalOfBands;//针对离散和多频段扫频的频段总数(N)
        data[8]= (byte) aBandNumber;//频段序号(1~N)

        data[9]=ComputeSegNumber(startFrequence);
        //偏移量至多只占10bit，因此只取前两个字节即可
        data[10]=(byte) (startOffset >> 8 & 0xff); //偏移量高位，
        data[11]=(byte) (startOffset & 0xff);//偏移量低8位

        data[12]=ComputeSegNumber(endFrequence);
        data[13]=(byte) (endOffset >> 8 & 0xff); //偏移量高位，
        data[14]=(byte) (endOffset & 0xff);//偏移量低8位

        data[15]= (byte) (gate<<6+aSelect);//判定门限+抽取倍率
        data[16]= (byte) (recvGain+3);
        //gateStyle为0代表自适应门限，1代表固定门限
        data[17]= (byte) gateStyle;
        data[18]= FixThresholdChangeToCode(threshold);//自适应门限

        //固定门限转换（固定门限的值不会有16bit）
        if(fixThreshold>0){
            data[19] = (byte) (thred>> 8 & 0xff);//固定门限高八位，最高位符号位为0
        }else {
            data[19]= (byte) (128+(thred>> 8 & 0xff));//最高位符号位为1
        }
        data[20]= (byte) (thred & 0xff);

        data[23]= (byte) 0xAA;
        return data;

    }

    /**
     * 更改终端定频接收参数的服务请求数据帧
     * @param equipmentID
     * @param specifyID 指定ID
     * @param number 频点个数
     * @param fix1
     * @param fix2
     * @param fix3
     * @param recvGain 接收通道增益
     * @param index IQ复信号带宽，数据率
     * @param blockNum 数据块个数
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param miu
     * @return
     */

    public byte[] RequestFixSweepData(int equipmentID, int specifyID,int number,double fix1,
                                      double fix2,double fix3,int recvGain,int index,int blockNum,
                                      int year,int month,int date,int hour,int minute,int miu){
        byte[] data=new byte[30];
        byte[] data1=new byte[3];
        byte[] data2=new byte[3];
        byte[] data3=new byte[3];
        byte[] data4=new byte[4];
        data[0]=0x55;
        data[1]= (byte) 0xAE;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]=(byte) (specifyID >> 8 & 0xff);//指定ID号的高位
        data[5]=(byte) (specifyID & 0xff);//指定ID号的低8位\

        data[6]= (byte) number;
        if(fix1!=0){
            data1=ComputeFloatTobyte(fix1);
            System.arraycopy(data1, 0, data, 7, 9);
        }

        if(fix2!=0){
            data2=ComputeFloatTobyte(fix2);
            System.arraycopy(data2, 0, data, 10, 12);
        }
        if(fix3!=0){
            data3=ComputeFloatTobyte(fix3);
            System.arraycopy(data3, 0, data,13, 15);
        }
        data[16]= (byte) (recvGain+3);
        data[17]=IQtoCode(index);
        data[18]=(byte) blockNum;
        data4 = TimetoCode(year, month, date, hour, minute);
        System.arraycopy(data4, 0, data,19,22 );
        data[23]= (byte) (miu&0xff);

        data[29]= (byte) 0xAA;

        return data;
    }

    /**
     * 启动终端压制发射的服务请求数据帧
     * @param equipmentID
     * @param specifyID 指定ID
     * @param number 压制品点数
     * @param fix1
     * @param fix2
     * @param sendAttenuation 发射通道衰减
     * @param pressMode 压制发射模式
     * @param style 信号类型
     * @param band 信号带宽
     * @param t1
     * @param t2
     * @param t3
     * @param t4
     * @return
     */
    public byte[] RequestPressData(int equipmentID, int specifyID,int number,double fix1,
                                   double fix2,int  sendAttenuation,int pressMode,int style,
                                int band,int t1,int t2,int t3,int t4 ){
        byte[] data=new byte[27];
        byte[] data1=new byte[3];
        byte[] data2=new byte[3];

        data[0]=0x55;
        data[1]= (byte) 0xAF;
        data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
        data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
        data[4]=(byte) (specifyID >> 8 & 0xff);//指定ID号的高位
        data[5]=(byte) (specifyID & 0xff);//指定ID号的低8位\

        data[6]= (byte) number;
        if(fix1!=0){
            data1=ComputeFloatTobyte(fix1);
            System.arraycopy(data1, 0, data, 7, 9);
        }

        if(fix2!=0){
            data2=ComputeFloatTobyte(fix2);
            System.arraycopy(data2, 0, data, 10, 12);
        }
        data[13]= (byte) sendAttenuation;

        data[14]= (byte) pressMode;
        data[15]=(byte) (style<<4+band);//信号类型占高四位，信号带宽占低四位
        //四个时间的转换
        data[16]=(byte) ((t1>>8)&0xff);
        data[17]=(byte) (t1&0xff);
        data[18]=(byte) ((t2>>8)&0xff);
        data[19]=(byte) (t2&0xff);
        data[20]=(byte) ((t3>>8)&0xff);
        data[21]=(byte) (t3&0xff);
        data[22]=(byte) ((t4>>8)&0xff);
        data[23]=(byte) (t4&0xff);
        data[26]= (byte) 0xAA;
        return data;
    }
    /**
     *时间转换代码,精确到分
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @return
     */

    private byte[] TimetoCode(int year,int month,
                              int date,int hour,int minute){
        byte[] time=new byte[4];
        time[0]=(byte) ((year>>4)&0xff);//年的高8位
        time[1]=(byte) ((year&0x0f)<<4+month&0x07);//年的低四位在8bit的高位，月在低四位
        time[2]=(byte) ((date&0x1f)<<3+(hour>>2)&0x07);//日占8bit的高5位，时的高3位占低3位
        time[3]=(byte) ((minute&0x3f)<<2+hour&0x03);//分占8bit的高6位，时的低两位占低2位
        return time;
    }
    //中心频率的移位存储,整数14bit,小数10bit
    private byte[] ComputeFloatTobyte(double frequency){
        byte[] com=new byte[3];
        int floatzhengshu=(int) frequency;
        int floatxiaoshu=(int) ((frequency-floatzhengshu)*1024);
        com[0]=(byte) (floatzhengshu>>6&0xff);//整数部分高8位（总共14bit）
        com[1]=(byte) ((floatxiaoshu>>8&0x03)>>6+(floatzhengshu&0x3f));
        com[2]=(byte) (floatxiaoshu&0xff);
        return com;
    }
    //计算输入扫频范围的起点、终点所对应硬件当中的段号
    private byte ComputeSegNumber(double frequence){
        byte segNumber=0;
        segNumber=(byte) ((frequence-70)/25+1);
        return segNumber;

    }
    //计算偏移量
    private int ComputeSegOffset(double frequence){
        int segOffset=0;
        segOffset=(int) ((frequence-70)%50*(2048.0/56.0));//50MHz带宽的计算
        return segOffset;
    }

    /**
     * 获取自适应门限的编码
     * @param threshold 自适应门限值
     * @return
     */

    private byte FixThresholdChangeToCode(int threshold){
        byte code=0;

        switch(threshold)
        {
            case 3:
                code = 0x00000000;
                break;
            case 10:
                code = 0x00000001;
                break;
            case 20:
                code = 0x00000002;
                break;
            case 25:
                code = 0x00000003;
                break;
            case 30:
                code = 0x00000004;
                break;
            case 40:
                code = 0x00000005;
                break;
            default:
                break;
        }
        return code;

    }
    /**
     * IQ复信号带宽和复信号数据率转换编码
     * @param index
     * @return
     */

    private byte IQtoCode(int index){
        byte code=0;
        switch(index){
            case 0:
                code=0x11;
                break;
            case 1:
                code=0x22;
                break;
            case 2:
                code=0x33;
                break;
            case 3:
                code=0x44;
                break;
            case 4:
                code=0x55;
                break;
            default:
                break;
        }
        return code;

    }

}
