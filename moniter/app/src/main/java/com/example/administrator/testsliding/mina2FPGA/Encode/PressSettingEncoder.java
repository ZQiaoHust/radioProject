package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.PressSetting;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressSettingEncoder implements MessageEncoder<PressSetting> {

    SettoFPGAPacket settoFPGAPacket=new SettoFPGAPacket();
    @Override
    public void encode(IoSession ioSession, PressSetting message,
                       ProtocolEncoderOutput out) throws Exception {
        byte[] pressParaData=settoFPGAPacket.PressParaData(message.getPressMode(),message.getStyle(),
                message.getBand(),message.getT1(),message.getT2(),message.getT3(),message.getT4());
        byte[] bytes=settoFPGAPacket.SetParaPacket(0x08,0,pressParaData);

        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);


    }
}
