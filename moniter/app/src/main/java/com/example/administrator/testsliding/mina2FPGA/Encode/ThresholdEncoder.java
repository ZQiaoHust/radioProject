package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.Threshold;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class ThresholdEncoder implements MessageEncoder<Threshold> {
    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();
    @Override
    public void encode(IoSession ioSession, Threshold message, ProtocolEncoderOutput out) throws Exception {
        byte[] SetThreshold=settoFPGAPacket.SetThreshold(message.getThresholdModel(),
                message.getAutoThreshold(),message.getFixThreshold());

        byte[] bytes=settoFPGAPacket.SetParaPacket(0X06,0,SetThreshold);

        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);

        buffer.flip();
        out.write(buffer);

    }
}
