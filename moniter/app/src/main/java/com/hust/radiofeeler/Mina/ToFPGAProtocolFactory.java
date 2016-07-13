package com.hust.radiofeeler.Mina;

import com.hust.radiofeeler.Bean.Connect;
import com.hust.radiofeeler.Bean.FixCentralFreq;
import com.hust.radiofeeler.Bean.FixSetting;
import com.hust.radiofeeler.Bean.InGain;
import com.hust.radiofeeler.Bean.OutGain;
import com.hust.radiofeeler.Bean.Press;
import com.hust.radiofeeler.Bean.PressSetting;
import com.hust.radiofeeler.Bean.Query;
import com.hust.radiofeeler.Bean.ReceiveRight;
import com.hust.radiofeeler.Bean.ReceiveWrong;
import com.hust.radiofeeler.Bean.SweepRange;
import com.hust.radiofeeler.Bean.Threshold;
import com.hust.radiofeeler.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.hust.radiofeeler.Bean.UploadData;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_Connect;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_FixCentralFreq;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_FixSetting;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_InGain;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_IsTerminalOnline;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_OutGain;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_Press;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_PressSetting;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_StationState;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_SweepRange;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_Threshold;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_UploadDataEnd;
import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_UploadDataStart;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_Connect;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_FixCentralFreq;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_FixSetting;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_InGain;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_OutGain;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_Press;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_PressSetting;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_StationState;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_SweepRange;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_Threshold;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_UploadDataEnd;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_UploadDataStart;
import com.hust.radiofeeler.bean2server.File_ModifyAntenna;
import com.hust.radiofeeler.bean2server.File_ModifyIngain;
import com.hust.radiofeeler.mina2FPGA.Decode.BackgroundPowerSpectrumCoarseDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.BackgroundPowerSpectrumFineDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.CllearDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.ConnectDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.FixCentralFreqDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.FixSettingDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.IQwaveDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.InGainDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.OutGainDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.PowerSpectrumAndAbnormalPonitCoarseDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.PowerSpectrumAndAbnormalPonitFineDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.PressDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.PressSettingDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.StationStateDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.SweepRangeDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.ThresholdDecoder;
import com.hust.radiofeeler.mina2FPGA.Decode.UploadDataDecoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ConnectPCBEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.FixCentralFreqEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.FixSettingEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.InGainEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ModifyAntennaEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ModifyInGainEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.OutGainEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.PressEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.PressSettingEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.QueryEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ReceiveRightEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ReceiveWrongEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.SweepRangeEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ThresholdEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ToServerPowerSpectrumAndAbnormalPointEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.UploadDataEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_ConnectEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_FixCentralFreqEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_FixSettingEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_InGainEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_IsTerminalOnlineEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_OutGainEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_PressEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_PressSettingEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_StationStateEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_SweepRangeEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_ThresholdEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_UploadDataEndEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Query_UploadDataStartEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_ConnectEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_FixCentralFreqEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_FixSettingEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_InGainEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_OutGainEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_PressEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_PressSettingEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_StationStateEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_SweepRangeEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_ThresholdEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_UploadDataEndEncoder;
import com.hust.radiofeeler.mina_transmit.server2FPGAEncoder.Simple_UploadDataStartEncoder;


import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ToFPGAProtocolFactory extends DemuxingProtocolCodecFactory {
    public ToFPGAProtocolFactory() {


        super.addMessageEncoder(InGain.class, InGainEncoder.class);
        super.addMessageEncoder(OutGain.class, OutGainEncoder.class);
        super.addMessageEncoder(Query.class, QueryEncoder.class);
        super.addMessageEncoder(FixSetting.class, FixSettingEncoder.class);
        super.addMessageEncoder(FixCentralFreq.class, FixCentralFreqEncoder.class);
        super.addMessageEncoder(SweepRange.class, SweepRangeEncoder.class);
        super.addMessageEncoder(Threshold.class, ThresholdEncoder.class);
        super.addMessageEncoder(Press.class, PressEncoder.class);
        super.addMessageEncoder(PressSetting.class, PressSettingEncoder.class);
        super.addMessageEncoder(UploadData.class, UploadDataEncoder.class);
        super.addMessageEncoder(Connect.class, ConnectPCBEncoder.class);
        super.addMessageEncoder(ReceiveRight.class, ReceiveRightEncoder.class);
        super.addMessageEncoder(ReceiveWrong.class, ReceiveWrongEncoder.class);
        super.addMessageEncoder(File_ModifyIngain.class, ModifyInGainEncoder.class);
        super.addMessageEncoder(File_ModifyAntenna.class, ModifyAntennaEncoder.class);

        //上传服务器的功率谱文件编码器
        super.addMessageEncoder(ToServerPowerSpectrumAndAbnormalPoint.class, ToServerPowerSpectrumAndAbnormalPointEncoder.class);

        super.addMessageDecoder(CllearDecoder.class);//注意该解码器的位置
        super.addMessageDecoder(InGainDecoder.class);
        super.addMessageDecoder(OutGainDecoder.class);
        super.addMessageDecoder(FixSettingDecoder.class);
        super.addMessageDecoder(FixCentralFreqDecoder.class);
        super.addMessageDecoder(SweepRangeDecoder.class);
        super.addMessageDecoder(ThresholdDecoder.class);
        super.addMessageDecoder(PressDecoder.class);
        super.addMessageDecoder(PressSettingDecoder.class);
        super.addMessageDecoder(UploadDataDecoder.class);
        super.addMessageDecoder(StationStateDecoder.class);
        super.addMessageDecoder(ConnectDecoder.class);






        // ==============================数据转发=====================================================
        //查询
        super.addMessageEncoder(Query_Connect.class, Query_ConnectEncoder.class);
        super.addMessageEncoder(Query_FixCentralFreq.class, Query_FixCentralFreqEncoder.class);
        super.addMessageEncoder(Query_FixSetting.class, Query_FixSettingEncoder.class);
        super.addMessageEncoder(Query_InGain.class, Query_InGainEncoder.class);
        super.addMessageEncoder(Query_IsTerminalOnline.class, Query_IsTerminalOnlineEncoder.class);
        super.addMessageEncoder(Query_OutGain.class, Query_OutGainEncoder.class);
        super.addMessageEncoder(Query_Press.class, Query_PressEncoder.class);
        super.addMessageEncoder(Query_PressSetting.class, Query_PressSettingEncoder.class);
        super.addMessageEncoder(Query_StationState.class, Query_StationStateEncoder.class);
        super.addMessageEncoder(Query_SweepRange.class, Query_SweepRangeEncoder.class);
        super.addMessageEncoder(Query_Threshold.class, Query_ThresholdEncoder.class);
        super.addMessageEncoder(Query_UploadDataStart.class, Query_UploadDataStartEncoder.class);
        super.addMessageEncoder(Query_UploadDataEnd.class, Query_UploadDataEndEncoder.class);
        //设置
        super.addMessageEncoder(Simple_Connect.class, Simple_ConnectEncoder.class);
        super.addMessageEncoder(Simple_FixCentralFreq.class, Simple_FixCentralFreqEncoder.class);
        super.addMessageEncoder(Simple_FixSetting.class, Simple_FixSettingEncoder.class);
        super.addMessageEncoder(Simple_InGain.class, Simple_InGainEncoder.class);
        super.addMessageEncoder(Simple_OutGain.class, Simple_OutGainEncoder.class);
        super.addMessageEncoder(Simple_Press.class, Simple_PressEncoder.class);
        super.addMessageEncoder(Simple_PressSetting.class, Simple_PressSettingEncoder.class);
        super.addMessageEncoder(Simple_StationState.class, Simple_StationStateEncoder.class);
        super.addMessageEncoder(Simple_SweepRange.class, Simple_SweepRangeEncoder.class);
        super.addMessageEncoder(Simple_Threshold.class, Simple_ThresholdEncoder.class);
        super.addMessageEncoder(Simple_UploadDataStart.class, Simple_UploadDataStartEncoder.class);
        super.addMessageEncoder(Simple_UploadDataEnd.class, Simple_UploadDataEndEncoder.class);

//以下解码器与非转发部分合并解码
//        super.addMessageDecoder(Reply_ConnectDecoder.class);
//        super.addMessageDecoder(Reply_FixCentralFreqDecoder.class);
//        super.addMessageDecoder(Reply_FixSettingDecoder.class);
//        super.addMessageDecoder(Reply_InGainDecoder.class);
//        super.addMessageDecoder(Reply_IsTerminalOnlineDecoder.class);
//        super.addMessageDecoder(Reply_OutGainDecoder.class);
//        super.addMessageDecoder(Reply_PressDecoder.class);
//        super.addMessageDecoder(Reply_PressSettingDecoder.class);
//        super.addMessageDecoder(Reply_SweepRangeDecoder.class);
//        super.addMessageDecoder(Reply_ThresholdDecoder.class);
//        super.addMessageDecoder(Reply_UploadDataEndDecoder.class);
//        super.addMessageDecoder(Reply_UploadDataStartDecoder.class);
        //最优先
        super.addMessageDecoder(IQwaveDecoder.class);
        // super.addMessageDecoder(PowerSpectrumAndAbnormalPonitDecoder.class);//功率谱异常频点

        super.addMessageDecoder(BackgroundPowerSpectrumFineDecoder.class);//功率谱异常频点
        super.addMessageDecoder(PowerSpectrumAndAbnormalPonitFineDecoder.class);//功率谱异常频点
        super.addMessageDecoder(BackgroundPowerSpectrumCoarseDecoder.class);//功率谱异常频点
        super.addMessageDecoder(PowerSpectrumAndAbnormalPonitCoarseDecoder.class);//功率谱异常频点
    }
}
