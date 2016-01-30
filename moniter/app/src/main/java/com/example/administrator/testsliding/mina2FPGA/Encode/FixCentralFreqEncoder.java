package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.FixCentralFreq;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class FixCentralFreqEncoder implements MessageEncoder<FixCentralFreq> {
    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();
    @Override
    public void encode(IoSession ioSession, FixCentralFreq message, ProtocolEncoderOutput out) throws Exception {
        byte[] FixSweepData= settoFPGAPacket.FixSweepData(message.getNumber(), message.getFix1(),
                message.getFix2(), message.getFix3());
        byte[] bytes=settoFPGAPacket.SetParaPacket(0x02,0,FixSweepData);

        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);



    }
}
