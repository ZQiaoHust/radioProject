package com.hust.radiofeeler.packet;

public class QuerytoFPGAPacket {

	/**
	 * 查询数据帧
	 * @param instructionType 查询功能码
	 * @param equipmentID
	 * @return
	 */
	public byte[] QueryParaPacket(byte instructionType,int equipmentID){
		byte[] data=new byte[7];
		data[0]=0x55;//包头
		data[1]=  instructionType;//指令类型
		data[2]=(byte) (equipmentID >> 8 & 0xff);//设备ID号的高位
		data[3]=(byte) (equipmentID & 0xff);//设备ID号的低8位
		data[4]=0;
		data[5]=0;
		data[6]=(byte) 0xAA;//包尾

		return data;

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
