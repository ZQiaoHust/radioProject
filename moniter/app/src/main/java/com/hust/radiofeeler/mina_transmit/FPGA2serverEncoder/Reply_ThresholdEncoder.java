package com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder;

import com.hust.radiofeeler.Bean.Threshold;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_ThresholdEncoder implements MessageEncoder<Threshold> {
    @Override
    public void encode(IoSession ioSession,Threshold sweep,
                       ProtocolEncoderOutput out) throws Exception {

        if(sweep!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=sweep.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
