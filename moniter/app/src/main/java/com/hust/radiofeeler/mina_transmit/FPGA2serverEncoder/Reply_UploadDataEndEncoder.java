package com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder;

import com.hust.radiofeeler.bean2Transmit.FPGA2server.Reply_UploadDataEnd;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_UploadDataEndEncoder implements MessageEncoder<Reply_UploadDataEnd> {
    @Override
    public void encode(IoSession ioSession, Reply_UploadDataEnd data,
                       ProtocolEncoderOutput out) throws Exception {

        if(data!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=data.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
