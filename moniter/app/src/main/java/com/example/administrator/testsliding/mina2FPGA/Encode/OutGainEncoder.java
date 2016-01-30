package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.OutGain;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class OutGainEncoder implements MessageEncoder<OutGain> {
    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();

    @Override
    public void encode(IoSession ioSession, OutGain message,
                       ProtocolEncoderOutput out) throws Exception {
        byte[] SendOutGain=settoFPGAPacket.SendAttenuationData(message.getOutGain());

        byte[] bytes=settoFPGAPacket.SetParaPacket(0x05,0,SendOutGain);

        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);

    }
}
