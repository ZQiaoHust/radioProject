package com.hust.radiofeeler.Mina;

import com.hust.radiofeeler.Bean.ToServerIQwaveFile;
import com.hust.radiofeeler.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.hust.radiofeeler.bean2server.RequstNetwork;
import com.hust.radiofeeler.mina2FPGA.Encode.ToServerIQwaveEncoder;
import com.hust.radiofeeler.mina2FPGA.Encode.ToServerPowerSpectrumAndAbnormalPointEncoder;
import com.hust.radiofeeler.mina2server.Decoder.FileToServerReplyDecoder;
import com.hust.radiofeeler.mina2server.Encoder.RequstNetworkEncoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ToFileProtocolCodeFactory  extends DemuxingProtocolCodecFactory {

    public  ToFileProtocolCodeFactory(){

        super.addMessageDecoder(FileToServerReplyDecoder.class);//收到文件后的确认帧
        super.addMessageEncoder(RequstNetwork.class,RequstNetworkEncoder.class);
        //文件上传
        super.addMessageEncoder(ToServerIQwaveFile.class, ToServerIQwaveEncoder.class);
        super.addMessageEncoder(ToServerPowerSpectrumAndAbnormalPoint.class, ToServerPowerSpectrumAndAbnormalPointEncoder.class);
    }
}
