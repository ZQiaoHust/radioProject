package com.example.administrator.testsliding.packet;

/**
 * 与FPGA通信的帧结构
 */
public class SettoFPGAPacket {

	/**
	 * 下发的所有设置帧类型
	 * @param instructionType 指令类型
	 * @param equipmentID 设备ID号
	 * @param data 数据域（10byte）
	 * @return
	 */

	public byte[] SetParaPacket(int instructionType,int equipmentID,byte[] data){
		byte[] packet=new byte[17];

		packet[0]=0x55;//包头
		packet[1]= (byte) instructionType;//指令类型
		packet[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
		packet[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
		//将data添加到packet中
		System.arraycopy(data, 0, packet, 4, 10);
		//CRC校验码
		packet[14]=0;
		packet[15]=0;
		packet[16]=(byte) 0xAA;//包尾
		return packet;
	}

	/**
	 * 扫频接收频率范围设置数据帧的数据域
	 * @param aSweepMode 扫频模式
	 * @param aSendMode 功率谱数据上传模式
	 * @param aTotalOfBands 多频段扫频模式的频段总数
	 * @param aBandNumber 多频段扫频模式的频段序号
	 * @param startFrequence 起止频率
	 * @param endFrequence 终止频率
	 * @param gate 功率谱数据变化的判定门限
	 * @param aSelect 文件上传的抽取倍率
	 * @return
	 */

	public byte[] SweepData(int aSweepMode,int aSendMode,int aTotalOfBands,
							int aBandNumber,double startFrequence,double endFrequence,int gate,int aSelect){
		byte[] sweep=new byte[10];

		int startOffset=ComputeSegOffset(startFrequence);//起点段内偏移量，偏移量最多只占10位
		int endOffset=ComputeSegOffset(endFrequence);


		sweep[0]= (byte) (aSweepMode<<2+aSendMode);     //扫频模式+长传模式
		sweep[1]= (byte) aTotalOfBands;//针对离散和多频段扫频的频段总数(N)
		sweep[2]= (byte) aBandNumber;//频段序号(1~N)

		sweep[3]=ComputeSegNumber(startFrequence);
		//偏移量至多只占10bit，因此只取前两个字节即可
		sweep[4]=(byte) (startOffset >> 8 & 0xff); //偏移量高位，
		sweep[5]=(byte) (startOffset & 0xff);//偏移量低8位

		sweep[6]=ComputeSegNumber(endFrequence);
		sweep[7]=(byte) (endOffset >> 8 & 0xff); //偏移量高位，
		sweep[8]=(byte) (endOffset & 0xff);//偏移量低8位

		sweep[9]= (byte) (gate<<6+aSelect);//判定门限+抽取倍率

		return sweep;

	}
	/**
	 * 定频接收中心频率设置数据帧的数据域
	 * @param number 中心频率个数
	 * @param fix1 三个中心频率
	 * @param fix2
	 * @param fix3
	 * @return
	 */
	public byte[] FixSweepData(int number,double fix1,double fix2,double fix3){
		byte[] fixData=new byte[10];
		byte[] data1=new byte[3];
		byte[] data2=new byte[3];
		byte[] data3=new byte[3];

		fixData[0]= (byte) number;
		if(fix1!=0){
			data1=ComputeFloatTobyte(fix1);
			System.arraycopy(data1, 0, fixData, 1, 3);
		}

		if(fix2!=0){
			data2=ComputeFloatTobyte(fix2);
			System.arraycopy(data2, 0, fixData, 4, 6);
		}
		if(fix3!=0){
			data3=ComputeFloatTobyte(fix3);
			System.arraycopy(data3, 0, fixData, 7, 9);
		}
		return fixData;
	}
	/**
	 * 压制发射频率设置数据帧
	 * @param number 压制个数
	 * @param fix1 压制频点
	 * @param fix2
	 * @return
	 */

	public byte[] PressData(int number,double fix1,double fix2){
		byte[] data=new byte[10];
		byte[] data1=new byte[3];
		byte[] data2=new byte[3];

		data[0]= (byte) number;
		if(fix1!=0){
			data1=ComputeFloatTobyte(fix1);
			System.arraycopy(data1, 0, data, 1, 3);
		}

		if(fix2!=0){
			data2=ComputeFloatTobyte(fix2);
			System.arraycopy(data2, 0, data, 4, 3);
		}

		return data;
	}
	/**
	 * 接受通道增益设置数据帧
	 * @param recvGain
	 * @return
	 */
	public byte[] RecvGainData(int recvGain){
		byte[] data=new byte[10];
		data[0]=(byte) (recvGain+3);
		return data;
	}
	/**
	 * 发射通道衰减设置数据帧
	 * @param sendAttenuation
	 * @return
	 */
	public byte[] SendAttenuationData(int  sendAttenuation){
		byte[] data=new byte[10];
		data[0]=(byte) (sendAttenuation);
		return data;
	}

	/**
	 * 设置设置检测门限的包
	 * @param gateStyle 检测门限类型(0或1）
	 * @param threshold 自适应门限
	 * @param fixThreshold 固定门限
	 * @return
	 */
	public byte[] SetThreshold(int gateStyle ,int threshold,int fixThreshold){

		byte[] para=new byte[10];
		int thred=Math.abs(fixThreshold);//固定门限是有符号的数

		//gateStyle为0代表自适应门限，1代表固定门限
		para[0]= (byte) gateStyle;
		para[1]= FixThresholdChangeToCode(threshold);//门限类型和自适应门限

		//固定门限转换（固定门限的值不会有16bit）
		if(fixThreshold>0){
			para[2] = (byte) (thred>> 8 & 0xff);//固定门限高八位，最高位符号位为0
		}else {
			para[2]= (byte) (128+(thred>> 8 & 0xff));//最高位符号位为1
		}
		para[3]= (byte) (thred & 0xff);
		return para;

	}
	/**
	 * 定频接收参数设置数据帧
	 * @param index  IQ复信号带宽和数据率
	 * @param blockNum
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 * @param miu
	 * @return
	 */

	public byte[] FixrecvData(int index,int blockNum,
							  int year,int month,int date,int hour,int minute,int miu){
		byte[] data=new byte[10];
		byte[] data1=new byte[8];

		data[0]=IQtoCode(index);
		data[1]=(byte) blockNum;
		data1=TimetoCode(year, month, date, hour, minute, miu);

		System.arraycopy(data1, 0, data, 2, 8);
		return data;

	}
	/**
	 * 压制发射参数设置数据帧
	 * @param pressMode 压制发射模式对应编码
	 * @param style 压制信号类型编码
	 * @param band 压制信号带宽编码
	 * @param t1
	 * @param t2
	 * @param t3
	 * @param t4
	 * @return
	 */
	public byte[] PressParaData(int pressMode,int style,
								int band,int t1,int t2,int t3,int t4 ){
		byte[] data=new byte[10];

		data[0]= (byte) pressMode;
		data[1]=(byte) (style<<4+band);//信号类型占高四位，信号带宽占低四位
		//四个时间的转换
		data[2]=(byte) ((t1>>8)&0xff);
		data[3]=(byte) (t1&0xff);
		data[4]=(byte) ((t2>>8)&0xff);
		data[5]=(byte) (t2&0xff);
		data[6]=(byte) ((t3>>8)&0xff);
		data[7]=(byte) (t3&0xff);
		data[8]=(byte) ((t4>>8)&0xff);
		data[9]=(byte) (t4&0xff);
		return data;
	}
	/**
	 * 硬件单元本地接入方式设置数据帧
	 * @param mode 接入方式
	 * @return
	 */
	public byte[] FPGAconnectData(int mode){
		byte[] data=new byte[10];
		data[0]= (byte) mode;
		return data;
	}
	/**
	 * 功率谱和异常频点数据上传开启和关闭设置数据帧
	 * @param code 关闭 开启两种包的功能码
	 * @param equipmentID
	 * @return
	 */
	public byte[] FPGAsendToUp(byte code,int equipmentID){
		byte[] data=new byte[7];
		data[0]=0x55;//包头
		data[1]=code;//指令类型
		data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
		data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
		data[4]=0;
		data[5]=0;
		data[6]=(byte) 0xAA;//包尾

		return data;

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

	//中心频率的移位存储
	private byte[] ComputeFloatTobyte(double frequency){
		byte[] com=new byte[3];
		int floatzhengshu=(int) frequency;
		int floatxiaoshu=(int) ((frequency-floatzhengshu)*1024);
		com[0]=(byte) (floatzhengshu>>6&0xff);//整数部分高8位（总共14bit）
		com[1]=(byte) ((floatxiaoshu>>8&0x03)>>6+(floatzhengshu&0x3f));
		com[2]=(byte) (floatxiaoshu&0xff);
		return com;
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

	/**
	 *时间转换代码
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 * @param miu
	 * @return
	 */

	private byte[] TimetoCode(int year,int month,
							  int date,int hour,int minute,int miu){
		byte[] time=new byte[8];
		time[0]=(byte) ((year>>4)&0xff);//年的高8位
		time[1]=(byte) ((year&0x0f)<<4+month&0x07);//年的低四位在8bit的高位，月在低四位
		time[2]=(byte) ((date&0x1f)<<3+(hour>>2)&0x07);//日占8bit的高5位，时的高3位占低3位
		time[3]=(byte) ((minute&0x3f)<<2+hour&0x03);//分占8bit的高6位，时的低两位占低2位
		time[4]=(byte) (miu&0xff);
		return time;
	}

}
