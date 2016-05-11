package com.hust.radiofeeler.Mina;


import com.hust.radiofeeler.Bean.Connect;
import com.hust.radiofeeler.Bean.FixCentralFreq;
import com.hust.radiofeeler.Bean.FixSetting;
import com.hust.radiofeeler.Bean.InGain;
import com.hust.radiofeeler.Bean.OutGain;
import com.hust.radiofeeler.Bean.Press;
import com.hust.radiofeeler.Bean.PressSetting;
import com.hust.radiofeeler.Bean.RequestNetworkAgain;
import com.hust.radiofeeler.Bean.StationState;
import com.hust.radiofeeler.Bean.SweepRange;
import com.hust.radiofeeler.Bean.Threshold;
import com.hust.radiofeeler.Bean.UploadData;
import com.hust.radiofeeler.HeartBeat.HEARTBEATREQUEST;
import com.hust.radiofeeler.HeartBeat.RequestEncoder;
import com.hust.radiofeeler.HeartBeat.ResponseDecoder;
import com.hust.radiofeeler.bean2server.HistoryIQRequest;
import com.hust.radiofeeler.bean2server.InteractionFixmodeRequest;
import com.hust.radiofeeler.bean2server.InteractionPressmodeRequest;
import com.hust.radiofeeler.bean2server.InteractionSweepModeRequest;
import com.hust.radiofeeler.bean2server.LocationAbnormalRequest;
import com.hust.radiofeeler.bean2server.MapInterpolation;
import com.hust.radiofeeler.bean2server.MapRadio;
import com.hust.radiofeeler.bean2server.MapRoute;
import com.hust.radiofeeler.bean2server.ModifyAntenna;
import com.hust.radiofeeler.bean2server.ModifyInGain;
import com.hust.radiofeeler.bean2server.RequstNetwork;
import com.hust.radiofeeler.bean2server.Send_ServiceRadio;
import com.hust.radiofeeler.bean2server.Station_CurrentRequst;
import com.hust.radiofeeler.bean2server.Station_RegisterRequst;
import com.hust.radiofeeler.bean2server.TerminalAttributes_All;
import com.hust.radiofeeler.bean2server.Terminal_Online;
import com.hust.radiofeeler.bean2server.Terminal_Register;
import com.hust.radiofeeler.mina2server.Decoder.MapInterpolationDecoder;
import com.hust.radiofeeler.mina2server.Encoder.HistoryIQEncoder;
import com.hust.radiofeeler.mina2server.Encoder.InteractionFixmodelEncoder;
import com.hust.radiofeeler.mina2server.Encoder.InteractionPressmodeEncoder;
import com.hust.radiofeeler.mina2server.Encoder.InteractionSweepmodeEncoder;
import com.hust.radiofeeler.mina2server.Decoder.LocationAbnormalDecode;
import com.hust.radiofeeler.mina2server.Encoder.LocationAbnormalEncoder;
import com.hust.radiofeeler.mina2server.Decoder.MapRadioDecoder;
import com.hust.radiofeeler.mina2server.Encoder.MapInterPolationEncoder;
import com.hust.radiofeeler.mina2server.Encoder.MapRadioEncoder;
import com.hust.radiofeeler.mina2server.Decoder.MapRouteDecoder;
import com.hust.radiofeeler.mina2server.Encoder.MapRouteEncoder;
import com.hust.radiofeeler.mina2server.Decoder.ModifyAntennaDecoder;
import com.hust.radiofeeler.mina2server.Encoder.ModifyAntennaEncoder;
import com.hust.radiofeeler.mina2server.Decoder.ModifyInGainDecoder;
import com.hust.radiofeeler.mina2server.Encoder.ModifyInGainEncoder;
import com.hust.radiofeeler.mina2server.Decoder.RequestWirlessPlanDecode;
import com.hust.radiofeeler.mina2server.Encoder.RequestWirlessPlanEncode;
import com.hust.radiofeeler.mina2server.Encoder.RequstNetworkAgainEncoder;
import com.hust.radiofeeler.mina2server.Decoder.RequstNetworkDecoder;
import com.hust.radiofeeler.mina2server.Encoder.RequstNetworkEncoder;
import com.hust.radiofeeler.mina2server.Decoder.StationAllAttributesDecoder;
import com.hust.radiofeeler.mina2server.Encoder.StationAllAttributesEncoder;
import com.hust.radiofeeler.mina2server.Decoder.StationCurrentDecode;
import com.hust.radiofeeler.mina2server.Encoder.StationCurrentEncoder;
import com.hust.radiofeeler.mina2server.Decoder.StationRegisterDecoder;
import com.hust.radiofeeler.mina2server.Encoder.StationRegisterEncoder;
import com.hust.radiofeeler.mina2server.Decoder.TerminalOnlineDecoder;
import com.hust.radiofeeler.mina2server.Encoder.TerminalOnlineEncoder;
import com.hust.radiofeeler.mina2server.Decoder.TerminalRegisterDecoder;
import com.hust.radiofeeler.mina2server.Encoder.TerminalRegisterEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_ConnectEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_FixCentralFreqEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_FixSettingEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_InGainEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_IsTerminalOnlineEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_OutGainEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_PressEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_PressSettingEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_SweepRangeEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_ThresholdEncoder;
import com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder.Reply_UploadDataStartEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.FileSettingFixCentralDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.FileSettingFixDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_ConnectDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_FixCentralFreqDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_FixSettingDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_InGainDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_IsTerminalOnlineDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_OutGainDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_PressDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_PressSettingDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_SweepRangeDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_ThresholdDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_UploadDataEndDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Query_UploadDataStartDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_ConnectDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_FixCentralFreqDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_FixSettingDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_InGainDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_OutGainDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_PressDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_PressSettingDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_SweepRangeDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_ThresholdDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_UploadDataEndDecoder;
import com.hust.radiofeeler.mina_transmit.server2FPGADecoder.Simple_UploadDataStartDecoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * Created by Administrator on 2015/11/18.
 */
public class ToServerProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public ToServerProtocolCodecFactory(){
        //==============================数据转发========================================
        //设置
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
        super.addMessageDecoder(FileSettingFixCentralDecoder.class);
        super.addMessageDecoder(FileSettingFixDecoder.class);
        //查询
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
        //响应
        super.addMessageEncoder(Connect.class, Reply_ConnectEncoder.class);
        super.addMessageEncoder(FixCentralFreq.class, Reply_FixCentralFreqEncoder.class);
        super.addMessageEncoder(FixSetting.class, Reply_FixSettingEncoder.class);
        super.addMessageEncoder(InGain.class, Reply_InGainEncoder.class);
        super.addMessageEncoder(StationState.class, Reply_IsTerminalOnlineEncoder.class);
        super.addMessageEncoder(OutGain.class, Reply_OutGainEncoder.class);
        super.addMessageEncoder(Press.class, Reply_PressEncoder.class);
        super.addMessageEncoder(PressSetting.class, Reply_PressSettingEncoder.class);
        super.addMessageEncoder(SweepRange.class, Reply_SweepRangeEncoder.class);
        super.addMessageEncoder(Threshold.class, Reply_ThresholdEncoder.class);
        super.addMessageEncoder(UploadData.class, Reply_UploadDataStartEncoder.class);
//        super.addMessageEncoder(UploadDataEnd.class, Reply_UploadDataEndEncoder.class);
        //入网申请
        super.addMessageEncoder(RequstNetwork.class,RequstNetworkEncoder.class);
        super.addMessageDecoder(RequstNetworkDecoder.class);
        //态势图
        super.addMessageEncoder(MapRadio.class,MapRadioEncoder.class);
        super.addMessageDecoder(MapRadioDecoder.class);
        super.addMessageEncoder(MapInterpolation.class, MapInterPolationEncoder.class);
        super.addMessageDecoder(MapInterpolationDecoder.class);//插值

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


        //文件上传
        //super.addMessageEncoder(ToServerPowerSpectrumAndAbnormalPoint.class, ToServerPowerSpectrumAndAbnormalPointEncoder.class);
       //心跳
        super.addMessageEncoder(HEARTBEATREQUEST.class, RequestEncoder.class);
        super.addMessageDecoder(ResponseDecoder.class);
        super.addMessageEncoder(RequestNetworkAgain.class, RequstNetworkAgainEncoder.class);
    }
}
