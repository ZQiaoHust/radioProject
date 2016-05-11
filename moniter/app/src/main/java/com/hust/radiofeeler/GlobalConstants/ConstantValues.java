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

/**
 * @ClassName: ConstantValues
 * @Description: TODO
 * 
 */
public interface ConstantValues {

	public String REQUSTNETWORK="COM.HUST.RADIOFEELER.REQUSTNETWORK";//申请入网

	public String MAPRADIO="COM.HUST.RADIOFEELER.MAPRADIO";//电磁分布态势
	public String MAPROUTE="COM.HUST.RADIOFEELER.MAPROUTE";//电磁路径分布
	public String MAPINTERPOLATION="COM.HUST.RADIOFEELER.MAPINTERPOLATION";//插值电磁态势

	public String ABNORMAL_LOCATION="COM.HUST.RADIOFEELER.ABNORMAL_LOCATION";//异常频点定位

    public String STATION_REGISTER="COM.HUST.RADIOFEELER.STATION_REGISTER";//登记台站属性
	public String STATION_CURRENT="COM.HUST.RADIOFEELER.STATION_CURRENT";//登记台站当前属性

	public String WIRLESSPLAN= "COM.HUST.RADIOFEELER.WIRLESSPLAN";//国家无线电
	public String SERVICE_IQ="COM.HUST.RADIOFEELER.SERVICE_IQ";//历史IQ波
	public String SERVICE_SPECTRUM="COM.HUST.RADIOFEELER.SERVICE_SPECTRUM";//历史功率谱
	public String TERMINAL_ALL="COM.HUST.RADIOFEELER.TERMINAL_ALL";//全部台站记录属性
	public String TERMINAL_ONLINE="COM.HUST.RADIOFEELER.TERMINAL_ONLINE";//所有在网终端
	public String TERMINAL_REGISTER="COM.HUST.RADIOFEELER.TERMINAL_REGISTER";//所有注册终端

	public String INTERACTION_WORKMODEL01="COM.HUST.RADIOFEELER.INTERACTION_WORKMODEL01";//站点互动扫频模式
	public String INTERACTION_WORKMODEL02="COM.HUST.RADIOFEELER.INTERACTION_WORKMODEL02";//站点互动定频模式
	public String INTERACTION_WORKMODEL03="COM.HUST.RADIOFEELER.INTERACTION_WORKMODEL03";//站点互动压制模式
	public String MODIFYINGAIN="COM.HUST.RADIOFEELER.MODIFYINGAIN";//接收通道增益修正表
	public String MODIFYANTENNA="COM.HUST.RADIOFEELER.MODIFYANTENNA";//天线增益修正表

	//======================================回复===================
	public String RREQUSTNETWORK="COM.HUST.RADIOFEELER.RREQUSTNETWORK";//申请入网
	public String RMAPRADIO="COM.HUST.RADIOFEELER.RMAPRADIO";//电磁分布态势
	public String RMAPROUTE="COM.HUST.RADIOFEELER.RMAPROUTE";//电磁路径分布
	public String RMAPINTERPOLATION="COM.HUST.RADIOFEELER.RMAPINTERPOLATION";//插值电磁态势
	public String RABNORMAL_LOCATION="COM.HUST.RADIOFEELER.RABNORMAL_LOCATION";//异常频点定位

	public String RSTATION_REGISTER="COM.HUST.RADIOFEELER.RSTATION_REGISTER";//登记台站属性
	public String RSTATION_CURRENT="COM.HUST.RADIOFEELER.RSTATION_CURRENT";//登记台站当前属性

	public String RWIRLESSPLAN= "COM.HUST.RADIOFEELER.RWIRLESSPLAN";//国家无线电
	public String RSERVICE_IQ="COM.HUST.RADIOFEELER.RSERVICE_IQ";//历史IQ波

	public String RTERMINAL_ALL="COM.HUST.RADIOFEELER.RTERMINAL_ALL";//全部台站记录属性
	public String RTERMINAL_ONLINE="COM.HUST.RADIOFEELER.RTERMINAL_ONLINE";//所有在网终端
	public String RTERMINAL_REGISTER="COM.HUST.RADIOFEELER.RTERMINAL_REGISTER";//所有注册终端

	public String RINTERACTION_WORKMODEL01="COM.HUST.RADIOFEELER.INTERACTION_WORKMODEL01";//站点互动扫频模式
	public String RINTERACTION_WORKMODEL02="COM.HUST.RADIOFEELER.INTERACTION_WORKMODEL02";//站点互动定频模式
	public String RINTERACTION_WORKMODEL03="COM.HUST.RADIOFEELER.INTERACTION_WORKMODEL03";//站点互动压制模式
	public String RMODIFYINGAIN="COM.HUST.RADIOFEELER.RMODIFYINGAIN";//接收通道增益修正表
	public String RMODIFYANTENNA="COM.HUST.RADIOFEELER.RMODIFYANTENNA";//天线增益修正表

	//================================转发的变量===============================
	public String CONNECT="COM.HUST.RADIOFEELER.CONNECT";
	public String FixCentralFreq="COM.HUST.RADIOFEELER.FixCentralFreq";
	public String FixSetting="COM.HUST.RADIOFEELER.FixSetting";
	public String InGain ="COM.HUST.RADIOFEELER.InGain";
	public String OutGain="COM.HUST.RADIOFEELER.OutGain";
	public String Press="COM.HUST.RADIOFEELER.Press";
	public String PressSetting="COM.HUST.RADIOFEELER.PressSetting";
	public String Query="COM.HUST.RADIOFEELER.Query";
	public String StationState="COM.HUST.RADIOFEELER.FinalStationState";
	public String SweepRange="COM.HUST.RADIOFEELER.SweepRange";
	public String Threshold="COM.HUST.RADIOFEELER.Threshold";
	public String UploadData="COM.HUST.RADIOFEELER.UploadData";


	//手机与FPGA
	public String SweepRangeQuery= "COM.EXAMPLE.SweepRange";
	public String SweepRangeSet= "COM.EXAMPLE.SweepRangeSet";

	public String InGainQuery= "COM.EXAMPLE.InGainQuery";
	public String InGainSet= "COM.EXAMPLE.InGainSet";

	public String OutGainSet= "COM.EXAMPLE.OutGainSet";
	public String OutGainQuery= "COM.EXAMPLE.OutGainQuery";

	public String ThresholdSet= "COM.EXAMPLE.ThresholdSet";
	public String ThresholdQuery= "COM.EXAMPLE.ThresholdQuery";

	public String FixCentralFreqSet= "COM.EXAMPLE.FixCentralFreqSet";
	public String FixCentralFreqQuery= "COM.EXAMPLE.FixCentralFreqQuery";

	public String FixSettingSet= "COM.EXAMPLE.FixSettingSet";
	public String FixSettingQuery= "COM.EXAMPLE.FixSettingQuery";

	public String PressSet= "COM.EXAMPLE.PressSet";
	public String PressQuery= "COM.EXAMPLE.PressQuery";

	public String PressSettingSet= "COM.EXAMPLE.PressSettingSet";
	public String PressSettingQuery= "COM.EXAMPLE.PressSettingQuery";

	public String StationStateQuery= "COM.EXAMPLE.StationStateQuery";

	public String uploadDataSet= "COM.EXAMPLE.uploadDataSet";
	public String uploadQuery= "COM.EXAMPLE.uploadQuery";

	public String ConnectPCBQuery= "COM.EXAMPLE.ConnectPCBQuery";
	public String ConnectPCB= "COM.EXAMPLE.ConnectPCB";

	public String IsOnlieQuery= "COM.EXAMPLE.IsOnlieQuery";

}
