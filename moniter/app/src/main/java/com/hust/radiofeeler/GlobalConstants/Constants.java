/**   
 * @Company: Batways
 * @Project:MINAtest 
 * @Title: ConstantValues.java
 * @Package com.example.minatest.gloable
 * @Description: TODO
 * @author victor_freedom (x_freedom_reddevil@126.com)
 * @date 2014-12-5 上午11:38:26
 * @version V1.0   
 */

package com.hust.radiofeeler.GlobalConstants;



import com.hust.radiofeeler.Bean.Press;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @ClassName: ConstantValues
 * @Description: TODO
 * 
 */
public class Constants {
	public static  int ID=11;
	public  static String IP="192.168.43.195";
	public  static String serverIP=null;
	public  static int serverPort=0;
	public  static String fileIP=null;
	public  static int filePort=0;
	public static IoSession FPGAsession=null;
	public static IoSession SERVERsession=null;
	public static IoSession FILEsession=null;
	public  static  int  isUpload=0;//功率谱和异常频点上传开启标志，0表示开启，其他表示关闭

	public static Queue<List<float[]>> Queue_DrawRealtimeSpectrum= new LinkedList<>();
	//public static Queue<List<float[]>> Queue_DrawRealtimewaterfall= new LinkedList<>();
	public static Queue<List<float[]>> Queue_BackgroundSpectrum=new LinkedList<>();//背景功率谱

	public static Queue<Map<Float,Float>> Queue_Abnormal=new LinkedList<>();//IQ波形文件


	//存储扫频范围参数及对应的起始点
	public static List<SweepRangeInfo> SweepParaList=new ArrayList<>();
	public static int spectrumCount=0;//段数计数器
	public static int IQCount=0;//段数计数器
	public static int BackgroundCount=0;//背景pinpu段数计数器

	public static boolean NotFill=false;//频谱数据没有收满
	public static boolean Backfail=false;//背景频谱数据接收失败
	public static boolean IsJump=false;//背景频谱数据接收失败h后让解码器跳转到实时频谱
	public static int positionValue=0;
	public static boolean flag=false;
	public static boolean Isstop=false;//判断是否有解码器
	public static IoBuffer buffer=IoBuffer.allocate(1024).setAutoExpand(true);
	public static Context ctx;//频谱数据
	public static ContextBackground ctxBack;//背景频谱数据
	public  static  int  sevCount;
	public static IQContext ctxIQ;//IQ波

	public static int sendMode;//功率谱文件上传模式
	public  static  int  judgePower;//功率谱变化判断门限
	public  static  int  selectRate;//抽取倍率
	public  static  boolean IsDrawWaterfall=false;//画瀑布图触发事件
	//public  static  boolean IsshowAbnormalList=false;//显示异常频点触发事件
	public  static  int serverSweepCount=0;//中心站设置扫频范围，多频段段数

	public static int  failCount;//实时频谱重传次数
	public static int  pressModel;//自动压制模式下的压制方式
	public static Press press;//自动压制模式模式保存

	public static int sequenceID;//文件服务器发过来的任务序列号

	public static byte[] requestNetcontent;//申请入网

	//偏移量
	public static double LAT_OFFSET=0.003345;
	public static double LON_OFFSET=0.01216;

	//扫频起止时间
	public static String time;
	public static int SELECT_COUNT;//抽取上传的计数器





}
