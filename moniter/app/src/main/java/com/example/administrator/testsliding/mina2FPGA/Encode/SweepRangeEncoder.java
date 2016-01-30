package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.SweepRange;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class SweepRangeEncoder implements MessageEncoder<SweepRange> {

    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();
    @Override
    public void encode(IoSession ioSession, SweepRange message,
                       ProtocolEncoderOutput out) throws Exception {

        byte[] sweepdata=settoFPGAPacket.SweepData(message.getaSweepMode(),message.getaSendMode(),
                message.getaTotalOfBands(),message.getaBandNumber(),message.getStartFrequence(),
                message.getEndFrequence(),message.getGate(),message.getaSelect());

        byte[] bytes=settoFPGAPacket.SetParaPacket(0x01,0,sweepdata);

        IoBuffer buffer = IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);

    }
}
