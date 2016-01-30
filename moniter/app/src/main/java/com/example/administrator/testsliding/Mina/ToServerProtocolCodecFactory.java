package com.example.administrator.testsliding.Mina;


import com.example.administrator.testsliding.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_Connect;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_FixCentralFreq;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_FixSetting;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_InGain;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_IsTerminalOnline;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_OutGain;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_Press;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_PressSetting;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_SweepRange;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_Threshold;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_UploadDataEnd;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_UploadDataStart;
import com.example.administrator.testsliding.bean2server.HistoryIQRequest;
import com.example.administrator.testsliding.bean2server.InteractionFixmodeRequest;
import com.example.administrator.testsliding.bean2server.InteractionPressmodeRequest;
import com.example.administrator.testsliding.bean2server.InteractionSweepModeRequest;
import com.example.administrator.testsliding.bean2server.LocationAbnormalRequest;
import com.example.administrator.testsliding.bean2server.MapRadio;
import com.example.administrator.testsliding.bean2server.MapRoute;
import com.example.administrator.testsliding.bean2server.ModifyAntenna;
import com.example.administrator.testsliding.bean2server.ModifyInGain;
import com.example.administrator.testsliding.bean2server.RequstNetwork;
import com.example.administrator.testsliding.bean2server.Send_ServiceRadio;
import com.example.administrator.testsliding.bean2server.Station_CurrentRequst;
import com.example.administrator.testsliding.bean2server.Station_RegisterRequst;
import com.example.administrator.testsliding.bean2server.TerminalAttributes_All;
import com.example.administrator.testsliding.bean2server.Terminal_Online;
import com.example.administrator.testsliding.bean2server.Terminal_Register;
import com.example.administrator.testsliding.mina2FPGA.Encode.ToServerPowerSpectrumAndAbnormalPointEncoder;
import com.example.administrator.testsliding.mina2server.HistoryIQEncoder;
import com.example.administrator.testsliding.mina2server.InteractionFixmodelEncoder;
import com.example.administrator.testsliding.mina2server.InteractionPressmodeEncoder;
import com.example.administrator.testsliding.mina2server.InteractionSweepmodeEncoder;
import com.example.administrator.testsliding.mina2server.LocationAbnormalDecode;
import com.example.administrator.testsliding.mina2server.LocationAbnormalEncoder;
import com.example.administrator.testsliding.mina2server.MapRadioDecoder;
import com.example.administrator.testsliding.mina2server.MapRadioEncoder;
import com.example.administrator.testsliding.mina2server.MapRouteDecoder;
import com.example.administrator.testsliding.mina2server.MapRouteEncoder;
import com.example.administrator.testsliding.mina2server.ModifyAntennaDecoder;
import com.example.administrator.testsliding.mina2server.ModifyAntennaEncoder;
import com.example.administrator.testsliding.mina2server.ModifyInGainDecoder;
import com.example.administrator.testsliding.mina2server.ModifyInGainEncoder;
import com.example.administrator.testsliding.mina2server.RequestWirlessPlanDecode;
import com.example.administrator.testsliding.mina2server.RequestWirlessPlanEncode;
import com.example.administrator.testsliding.mina2server.RequstNetworkDecoder;
import com.example.administrator.testsliding.mina2server.RequstNetworkEncoder;
import com.example.administrator.testsliding.mina2server.StationAllAttributesDecoder;
import com.example.administrator.testsliding.mina2server.StationAllAttributesEncoder;
import com.example.administrator.testsliding.mina2server.StationCurrentDecode;
import com.example.administrator.testsliding.mina2server.StationCurrentEncoder;
import com.example.administrator.testsliding.mina2server.StationRegisterDecoder;
import com.example.administrator.testsliding.mina2server.StationRegisterEncoder;
import com.example.administrator.testsliding.mina2server.TerminalOnlineDecoder;
import com.example.administrator.testsliding.mina2server.TerminalOnlineEncoder;
import com.example.administrator.testsliding.mina2server.TerminalRegisterDecoder;
import com.example.administrator.testsliding.mina2server.TerminalRegisterEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_ConnectEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_FixCentralFreqEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_FixSettingEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_InGainEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_IsTerminalOnlineEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_OutGainEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_PressEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_PressSettingEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_SweepRangeEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_ThresholdEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_UploadDataEndEncoder;
import com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder.Reply_UploadDataStartEncoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_ConnectDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_FixCentralFreqDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_FixSettingDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_InGainDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_IsTerminalOnlineDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_OutGainDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_PressDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_PressSettingDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_SweepRangeDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_ThresholdDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_UploadDataEndDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Query_UploadDataStartDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_ConnectDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_FixCentralFreqDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_FixSettingDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_InGainDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_OutGainDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_PressDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_PressSettingDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_SweepRangeDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_ThresholdDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_UploadDataEndDecoder;
import com.example.administrator.testsliding.mina_transmit.server2FPGADecoder.Simple_UploadDataStartDecoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * Created by Administrator on 2015/11/18.
 */
public class ToServerProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public ToServerProtocolCodecFactory(){
        //入网申请
        super.addMessageEncoder(RequstNetwork.class,RequstNetworkEncoder.class);
        super.addMessageDecoder(RequstNetworkDecoder.class);
        //态势图
        super.addMessageEncoder(MapRadio.class,MapRadioEncoder.class);
        super.addMessageDecoder(MapRadioDecoder.class);
        //路径图
        super.addMessageEncoder(MapRoute.class,MapRouteEncoder.class);
        super.addMessageDecoder(MapRouteDecoder.class);
        //异常频点定位
        super.addMessageEncoder(LocationAbnormalRequest.class, LocationAbnormalEncoder.class);
        super.addMessageDecoder(LocationAbnormalDecode.class);
        //台站状态
        super.addMessageEncoder(Station_RegisterRequst.class,StationRegisterEncoder.class);
        super.addMessageDecoder(StationRegisterDecoder.class);
        super.addMessageEncoder(Station_CurrentRequst.class, StationCurrentEncoder.class);
        super.addMessageDecoder(StationCurrentDecode.class);

       //无线电规划
        super.addMessageEncoder(Send_ServiceRadio.class, RequestWirlessPlanEncode.class);
        super.addMessageDecoder(RequestWirlessPlanDecode.class);
        //历史IQ波
        super.addMessageEncoder(HistoryIQRequest.class,HistoryIQEncoder.class);
        //三种交互模式
        super.addMessageEncoder(InteractionSweepModeRequest.class, InteractionSweepmodeEncoder.class);
        super.addMessageEncoder(InteractionFixmodeRequest.class,InteractionFixmodelEncoder.class);
        super.addMessageEncoder(InteractionPressmodeRequest.class, InteractionPressmodeEncoder.class);

        super.addMessageEncoder(TerminalAttributes_All.class, StationAllAttributesEncoder.class);
        super.addMessageDecoder(StationAllAttributesDecoder.class);
        super.addMessageEncoder(Terminal_Online.class, TerminalOnlineEncoder.class);
        super.addMessageDecoder(TerminalOnlineDecoder.class);
        super.addMessageEncoder(Terminal_Register.class, TerminalRegisterEncoder.class);
        super.addMessageDecoder(TerminalRegisterDecoder.class);

        //增益表
        super.addMessageEncoder(ModifyInGain.class,ModifyInGainEncoder.class);
        super.addMessageDecoder(ModifyInGainDecoder.class);
        super.addMessageEncoder(ModifyAntenna.class, ModifyAntennaEncoder.class);
        super.addMessageDecoder(ModifyAntennaDecoder.class);

        //==============================数据转发========================================
        super.addMessageDecoder(Simple_ConnectDecoder.class);
        super.addMessageDecoder(Simple_FixCentralFreqDecoder.class);
        super.addMessageDecoder(Simple_FixSettingDecoder.class);
        super.addMessageDecoder(Simple_InGainDecoder.class);
        super.addMessageDecoder(Simple_OutGainDecoder.class);
        super.addMessageDecoder(Simple_PressDecoder.class);
        super.addMessageDecoder(Simple_PressSettingDecoder.class);
        super.addMessageDecoder(Simple_SweepRangeDecoder.class);
        super.addMessageDecoder(Simple_ThresholdDecoder.class);
        super.addMessageDecoder(Simple_UploadDataEndDecoder.class);
        super.addMessageDecoder(Simple_UploadDataStartDecoder.class);

        super.addMessageDecoder(Query_ConnectDecoder.class);
        super.addMessageDecoder(Query_FixCentralFreqDecoder.class);
        super.addMessageDecoder(Query_FixSettingDecoder.class);
        super.addMessageDecoder(Query_InGainDecoder.class);
        super.addMessageDecoder(Query_OutGainDecoder.class);
        super.addMessageDecoder(Query_IsTerminalOnlineDecoder.class);
        super.addMessageDecoder(Query_PressDecoder.class);
        super.addMessageDecoder(Query_PressSettingDecoder.class);
        super.addMessageDecoder(Query_SweepRangeDecoder.class);
        super.addMessageDecoder(Query_ThresholdDecoder.class);
        super.addMessageDecoder(Query_UploadDataStartDecoder.class);
        super.addMessageDecoder(Query_UploadDataEndDecoder.class);

        super.addMessageEncoder(Reply_Connect.class, Reply_ConnectEncoder.class);
        super.addMessageEncoder(Reply_FixCentralFreq.class, Reply_FixCentralFreqEncoder.class);
        super.addMessageEncoder(Reply_FixSetting.class, Reply_FixSettingEncoder.class);
        super.addMessageEncoder(Reply_InGain.class, Reply_InGainEncoder.class);
        super.addMessageEncoder(Reply_IsTerminalOnline.class, Reply_IsTerminalOnlineEncoder.class);
        super.addMessageEncoder(Reply_OutGain.class, Reply_OutGainEncoder.class);
        super.addMessageEncoder(Reply_Press.class, Reply_PressEncoder.class);
        super.addMessageEncoder(Reply_PressSetting.class, Reply_PressSettingEncoder.class);
        super.addMessageEncoder(Reply_SweepRange.class, Reply_SweepRangeEncoder.class);
        super.addMessageEncoder(Reply_Threshold.class, Reply_ThresholdEncoder.class);
        super.addMessageEncoder(Reply_UploadDataStart.class, Reply_UploadDataStartEncoder.class);
        super.addMessageEncoder(Reply_UploadDataEnd.class, Reply_UploadDataEndEncoder.class);


        //文件上传
        //super.addMessageEncoder(ToServerPowerSpectrumAndAbnormalPoint.class, ToServerPowerSpectrumAndAbnormalPointEncoder.class);
    }
}
