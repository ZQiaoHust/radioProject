package com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder;

import com.hust.radiofeeler.Bean.FixSetting;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_FixSettingEncoder implements MessageEncoder<FixSetting> {
    @Override
    public void encode(IoSession ioSession, FixSetting fix,
                       ProtocolEncoderOutput out) throws Exception {

        if(fix!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=fix.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
