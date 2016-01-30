package com.example.administrator.testsliding.mina_transmit.server2FPGAEncoder;

import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_StationState;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_StationStateEncoder implements MessageEncoder<Simple_StationState> {
    @Override
    public void encode(IoSession ioSession, Simple_StationState state,
                       ProtocolEncoderOutput out) throws Exception {

        if(state!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=state.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
