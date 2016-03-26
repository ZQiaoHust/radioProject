package com.example.administrator.testsliding.Mina;

import com.example.administrator.testsliding.Bean.ToServerIQwaveFile;
import com.example.administrator.testsliding.Bean.ToServerPowerSpectrumAndAbnormalPoint;
import com.example.administrator.testsliding.bean2server.RequstNetwork;
import com.example.administrator.testsliding.mina2FPGA.Encode.ToServerIQwaveEncoder;
import com.example.administrator.testsliding.mina2FPGA.Encode.ToServerPowerSpectrumAndAbnormalPointEncoder;
import com.example.administrator.testsliding.mina2server.RequstNetworkEncoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * Created by Administrator on 2016/1/27.
 */
public class ToFileProtocolCodeFactory  extends DemuxingProtocolCodecFactory {

    public  ToFileProtocolCodeFactory(){

        super.addMessageEncoder(RequstNetwork.class,RequstNetworkEncoder.class);
        //文件上传
        super.addMessageEncoder(ToServerIQwaveFile.class, ToServerIQwaveEncoder.class);
        super.addMessageEncoder(ToServerPowerSpectrumAndAbnormalPoint.class, ToServerPowerSpectrumAndAbnormalPointEncoder.class);
    }
}
