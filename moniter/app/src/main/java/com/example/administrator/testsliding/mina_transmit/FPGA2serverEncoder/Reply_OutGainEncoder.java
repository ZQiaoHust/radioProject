package com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder;

import com.example.administrator.testsliding.Bean.OutGain;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_OutGain;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_OutGain;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_OutGainEncoder implements MessageEncoder<OutGain> {
    @Override
    public void encode(IoSession ioSession, OutGain gain,
                       ProtocolEncoderOutput out) throws Exception {

        if(gain!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=gain.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
