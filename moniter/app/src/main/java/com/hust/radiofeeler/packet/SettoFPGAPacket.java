package com.hust.radiofeeler.packet;

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








}
