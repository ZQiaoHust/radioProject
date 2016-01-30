package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.FixSetting;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/24.
 */
public class FixSettingEncoder implements MessageEncoder<FixSetting> {
    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();
    @Override
    public void encode(IoSession ioSession, FixSetting message, ProtocolEncoderOutput out) throws Exception {
        byte[] fixrecvData=settoFPGAPacket.FixrecvData((int) message.getIQwidth(), message.getBlockNum(), message.getYear(),
                message.getMonth(), message.getDay(), message.getHour(),
                message.getMinute(), message.getSecond());
        byte[] bytes=settoFPGAPacket.SetParaPacket(0x07,0,fixrecvData);

        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
    }
}
