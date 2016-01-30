package com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder;

import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_FixCentralFreq;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_FixCentralFreq;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_FixCentralFreqEncoder implements MessageEncoder<Reply_FixCentralFreq> {
    @Override
    public void encode(IoSession ioSession, Reply_FixCentralFreq fix,
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
