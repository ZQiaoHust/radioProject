package com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder;

import com.hust.radiofeeler.Bean.StationState;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_IsTerminalOnlineEncoder implements MessageEncoder<StationState> {
    @Override
    public void encode(IoSession ioSession, StationState data,
                       ProtocolEncoderOutput out) throws Exception {

        if(data!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=data.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
