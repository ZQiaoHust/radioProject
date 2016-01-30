package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.Press;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressEncoder implements MessageEncoder<Press> {
    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();
    @Override
    public void encode(IoSession ioSession, Press message, ProtocolEncoderOutput out) throws Exception {
        byte[] pressData=settoFPGAPacket.PressData(message.getNumber(),
                message.getFix1(),message.getFix2());
        byte[] bytes=settoFPGAPacket.SetParaPacket(0x03,0,pressData);

        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
    }
}
