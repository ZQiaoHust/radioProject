package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.FixCentralFreq;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class FixCentralFreqEncoder implements MessageEncoder<FixCentralFreq> {
    private ComputePara computePara=new ComputePara();
    @Override
    public void encode(IoSession ioSession, FixCentralFreq fix, ProtocolEncoderOutput out) throws Exception {

        if(fix!=null) {
            byte[] bytes = GetBytes(fix);
            IoBuffer buffer = IoBuffer.allocate(17);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
    /**
     * 定频接收中心频率设置数据帧的数据域
     *
     */
    private byte[] GetBytes(FixCentralFreq fixFreq){
        byte[] data=new byte[17];
        byte[] data1=new byte[3];
        data[0]=0x55;
        data[1]=0x02;
        data[2]= (byte) (Constants.ID&0xff);//设备ID号
        data[3]= (byte) ((Constants.ID>>8)&0xff);
        data[4]= (byte) fixFreq.getNumber();
        if(fixFreq.getFix1()!=0){
            data1=computePara.ComputeFloatTobyte(fixFreq.getFix1());
            System.arraycopy(data1, 0, data, 5, 3);
        }
        if(fixFreq.getFix2()!=0){
            data1=computePara.ComputeFloatTobyte(fixFreq.getFix2());
            System.arraycopy(data1, 0, data, 8, 3);
        }
        if(fixFreq.getFix3()!=0){
            data1=computePara.ComputeFloatTobyte(fixFreq.getFix3());
            System.arraycopy(data1, 0, data, 11, 3);
        }
        data[16]= (byte) 0xAA;
        return data;
    }
}
